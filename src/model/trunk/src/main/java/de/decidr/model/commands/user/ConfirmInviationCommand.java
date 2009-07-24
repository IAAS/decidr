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

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import de.decidr.model.LifetimeValidator;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.Invitation;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserAdministratesWorkflowModel;
import de.decidr.model.entities.UserAdministratesWorkflowModelId;
import de.decidr.model.entities.UserIsMemberOfTenant;
import de.decidr.model.entities.UserParticipatesInWorkflow;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

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
public class ConfirmInviationCommand extends AclEnabledCommand {

    private static Logger logger = DefaultLogger
            .getLogger(ConfirmInviationCommand.class);

    private Long invitationId;

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
     * @param tenant
     * @param session
     *            Hibernate session to use for database queries
     */
    private void makeMemberOfTenant(User user, Tenant tenant, Session session) {
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

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        Invitation i = (Invitation) evt.getSession().load(Invitation.class,
                invitationId);

        LifetimeValidator.isInvitationValid(i);

        if (i.getAdministrateWorkflowModel() != null) {
            // Invitation tye: administrate workflow model
            User user = i.getReceiver();
            Tenant tenant = i.getAdministrateWorkflowModel().getTenant();
            makeMemberOfTenant(user, tenant, evt.getSession());

            // add as WorkflowAdmin only if not already a workflow admin
            Query q = evt
                    .getSession()
                    .createQuery(
                            "select count(*) from UserAdministratesWorkflowModel rel "
                                    + "where rel.user = :user and rel.workflowModel = :workflowModel");
            q.setEntity("user", user).setEntity("workflowModel",
                    i.getAdministrateWorkflowModel());

            if (((Number) q.uniqueResult()).intValue() < 1) {
                // the user is currently not administrating the workflow model
                UserAdministratesWorkflowModel relation = new UserAdministratesWorkflowModel();
                relation.setUser(user);
                relation.setWorkflowModel(i.getAdministrateWorkflowModel());
                evt.getSession().save(relation);
            }

        } else if (i.getJoinTenant() != null) {
            // Invitation tye: simply join tenant as member
            makeMemberOfTenant(i.getReceiver(), i.getJoinTenant(), evt
                    .getSession());
        } else if (i.getParticipateInWorkflowInstance() != null) {
            // add to tenant if user isn't tenant member
            User user = i.getReceiver();
            Tenant tenant = i.getParticipateInWorkflowInstance()
                    .getDeployedWorkflowModel().getTenant();

            makeMemberOfTenant(user, tenant, evt.getSession());

            // Add as WorkflowParticipant if not already a participant
            Query q = evt
                    .getSession()
                    .createQuery(
                            "select count(*) from UserParticipatesInWorkflow rel "
                                    + "where rel.user = :user and rel.workflowInstance = :workflowInstance");
            q.setEntity("user", user).setEntity("workflowInstance",
                    i.getParticipateInWorkflowInstance());

            if (((Number) q.uniqueResult()).intValue() < 1) {
                // the user is currently not participating in the workflow
                UserParticipatesInWorkflow relation = new UserParticipatesInWorkflow();
                relation.setUser(user);
                relation.setWorkflowInstance(i
                        .getParticipateInWorkflowInstance());
                evt.getSession().save(relation);
            }
        } else {
            // not a valid invitation type
            String warnMessage = String
                    .format(
                            "An invitation (id: %1$s) was confirmed but had no type "
                                    + "[workflowInstance = null, tenant = null, workflowModel = null]",
                            i.getId().toString());
            logger.warn(warnMessage);
        }
    }
}
