/*
 * The DecidR Development Team licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.decidr.model.commands.user;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.LifetimeValidator;
import de.decidr.model.acl.access.InvitationAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.Invitation;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserAdministratesWorkflowModel;
import de.decidr.model.entities.UserIsMemberOfTenant;
import de.decidr.model.entities.UserParticipatesInWorkflow;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.model.exceptions.AccessDeniedException;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.RequestExpiredException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.workflowmodel.instancemanagement.InstanceManager;
import de.decidr.model.workflowmodel.instancemanagement.InstanceManagerImpl;
import de.decidr.model.workflowmodel.instancemanagement.StartInstanceResult;

/**
 * Confirms a single invitation by associating the user with the corresponding
 * tenant (if necessary).
 * 
 * Workflow instances that are waiting for this invitation to be confirmed will
 * be started.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class ConfirmInviationCommand extends AclEnabledCommand implements
        InvitationAccess {

    private static Logger logger = DefaultLogger
            .getLogger(ConfirmInviationCommand.class);

    private Long invitationId;

    private Invitation invitation = null;

    /**
     * Current Hibernate Session
     */
    private Session session = null;

    /**
     * Creates a new ConfirmInviationCommand.
     * 
     * @param role
     *            the user which executes the command
     * @param invitationId
     *            the id of the invitation which should be confirmed
     */
    public ConfirmInviationCommand(Role role, Long invitationId) {
        super(role, (Permission) null);
        this.invitationId = invitationId;
    }

    /**
     * Makes the given user a member of the given tenant. This method has no
     * effect if the user is already a tenant member or the tenant admin.
     * 
     * @param user
     *            user who should become member of the given tenant
     * @param tenant
     *            tenant to which the given user should be added
     */
    private void makeMemberOfTenant(User user, Tenant tenant) {
        if (tenant == null || user == null) {
            throw new IllegalArgumentException(
                    "User and tenant must not be null");
        }

        Query q = session
                .createQuery("select count(*) from UserIsMemberOfTenant a "
                        + "where (a.tenant = :tenant AND a.user = :user) or a.tenant.admin = :user");
        q.setEntity("tenant", tenant);
        q.setEntity("user", user);

        if (((Number) q.uniqueResult()).intValue() < 1) {
            // user isn't a tenant member yet, make him a tenant member!
            UserIsMemberOfTenant relation = new UserIsMemberOfTenant();

            relation.setUser(user);
            relation.setTenant(tenant);

            session.save(relation);
        }
    }

    /**
     * Makes the receiver of the invitation a member of the tenant and allows
     * him to administrate the workflow model that is specified by the
     * invitation.
     * 
     * @throws AccessDeniedException
     */
    private void processWorkflowModelInvitation() throws AccessDeniedException {
        User user = invitation.getReceiver();
        Tenant tenant = invitation.getAdministrateWorkflowModel().getTenant();
        makeMemberOfTenant(user, tenant);

        // only registered users can do this
        if (user.getUserProfile() == null) {
            throw new AccessDeniedException(
                    "You must be registered to become workflow administrator");
        }

        // add as WorkflowAdmin only if not already a workflow admin
        Query q = session
                .createQuery("select count(*) from UserAdministratesWorkflowModel rel "
                        + "where rel.user = :user and rel.workflowModel = :workflowModel");
        q.setEntity("user", user).setEntity("workflowModel",
                invitation.getAdministrateWorkflowModel());

        if (((Number) q.uniqueResult()).intValue() < 1) {
            // the user is currently not administrating the workflow model
            UserAdministratesWorkflowModel relation = new UserAdministratesWorkflowModel();
            relation.setUser(user);
            relation
                    .setWorkflowModel(invitation.getAdministrateWorkflowModel());
            session.save(relation);
        }
    }

    /**
     * Makes the receiver of the invitation a member of the tenant that is
     * specified by the invitation.
     */
    private void processJoinTenantInvitation() {
        makeMemberOfTenant(invitation.getReceiver(), invitation.getJoinTenant());
    }

    /**
     * Makes the receiver of the invitation a member of the tenant that owns the
     * workflow instance to participate in. Associates the receiver with the
     * workflow instance and starts the instance if the receiver was the last
     * user who had to confirm his invitation.
     * 
     * @throws EntityNotFoundException
     *             if no invitation could be found
     * @throws JAXBException
     * @throws IOException
     * @throws SOAPException
     */
    @SuppressWarnings("unchecked")
    private void processWorkflowInstanceInvitation()
            throws EntityNotFoundException, SOAPException, IOException,
            JAXBException {
        User user = invitation.getReceiver();
        Tenant tenant = invitation.getParticipateInWorkflowInstance()
                .getDeployedWorkflowModel().getTenant();

        makeMemberOfTenant(user, tenant);

        // Add as WorkflowParticipant if not already a participant
        Query q = session
                .createQuery("select count(*) from UserParticipatesInWorkflow rel "
                        + "where rel.user = :user and rel.workflowInstance = :workflowInstance");
        q.setEntity("user", user).setEntity("workflowInstance",
                invitation.getParticipateInWorkflowInstance());

        if (((Number) q.uniqueResult()).intValue() < 1) {
            // the user is currently not participating in the workflow
            UserParticipatesInWorkflow relation = new UserParticipatesInWorkflow();
            relation.setUser(user);
            relation.setWorkflowInstance(invitation
                    .getParticipateInWorkflowInstance());
            session.save(relation);
        }

        // start the (delayed) workflow instance if this is the last invitation.
        WorkflowInstance instance = invitation
                .getParticipateInWorkflowInstance();

        if (instance == null) {
            throw new EntityNotFoundException(WorkflowInstance.class);
        }

        String hql = "select count(*) from Invitation i where "
                + "i.participateInWorkflowInstance = :instance and "
                + "i.participateInWorkflowInstance is not null and "
                + "i.participateInWorkflowInstance.startedDate is null";

        Number remainingInvitations = (Number) session.createQuery(hql)
                .setEntity("instance", instance).uniqueResult();

        if (remainingInvitations.intValue() == 1) {
            // this is the last invitation
            InstanceManager manager = new InstanceManagerImpl();
            q = session
                    .createQuery("from ServerLoadView s where s.serverType.name = :serverType");
            List<ServerLoadView> serverStatistics = q.setString("serverType",
                    ServerTypeEnum.Ode.toString()).list();

            // instance only needed to get the OdePid and Server from the
            // instance manager
            StartInstanceResult startInstanceResult = manager.startInstance(
                    instance.getDeployedWorkflowModel(), instance
                            .getStartConfiguration(), serverStatistics);

            instance.setOdePid(startInstanceResult.getODEPid());
            instance.setStartedDate(DecidrGlobals.getTime().getTime());
            instance.setServer((Server) session.get(Server.class,
                    startInstanceResult.getServer()));
            session.save(instance);
        }
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        session = evt.getSession();

        invitation = (Invitation) evt.getSession().get(Invitation.class,
                invitationId);

        // does the invitation exist?
        if (invitation == null) {
            throw new EntityNotFoundException(Invitation.class, invitationId);
        }

        // is the invitation still valid?
        if (!LifetimeValidator.isInvitationValid(invitation)) {
            throw new RequestExpiredException("The invitation has expired.");
        }

        if (invitation.getAdministrateWorkflowModel() != null) {
            // Invitation tye: administrate workflow model
            processWorkflowModelInvitation();
        } else if (invitation.getJoinTenant() != null) {
            // Invitation tye: simply join tenant as member
            processJoinTenantInvitation();
        } else if (invitation.getParticipateInWorkflowInstance() != null) {
            // add to tenant if user isn't tenant member and start delayed
            // workflow instances
            try {
                processWorkflowInstanceInvitation();
            } catch (Exception e) {
                if (e instanceof TransactionException) {
                    throw (TransactionException) e;
                } else {
                    throw new TransactionException(e);
                }
            }
        } else {
            // not a valid invitation type
            String warnMessage = String
                    .format(
                            "An invitation (id: %1$s) was confirmed but had no type "
                                    + "[workflowInstance = null, tenant = null, workflowModel = null]",
                            invitation.getId().toString());
            logger.warn(warnMessage);
        }

        session.delete(invitation);
    }

    @Override
    public Long[] getInvitationIds() {
        Long[] result = { invitationId };
        return result;
    }
}
