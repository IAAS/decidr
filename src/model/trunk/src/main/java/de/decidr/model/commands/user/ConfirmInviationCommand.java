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

import org.hibernate.Query;

import de.decidr.model.LifetimeValidator;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.Invitation;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserAdministratesWorkflowModel;
import de.decidr.model.entities.UserIsMemberOfTenant;
import de.decidr.model.entities.UserParticipatesInWorkflow;
import de.decidr.model.exceptions.TransactionException;
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
 * DH review code
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class ConfirmInviationCommand extends AclEnabledCommand {

    private Long invitationId;

    /**
     * Creates a new ConfirmInviationCommand.
     * 
     * @param role
     * @param invitationId
     */
    public ConfirmInviationCommand(Role role, Long invitationId) {
        super(role, (Permission) null);
        this.invitationId = invitationId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        Invitation i = (Invitation) evt.getSession().load(Invitation.class,
                invitationId);

        LifetimeValidator.isInvitationValid(i);

        if (i.getAdministrateWorkflowModel() != null) {

            // add to tenant if user isn't tenant member
            User user = i.getReceiver();
            Tenant tenant = i.getAdministrateWorkflowModel().getTenant();

            Query q = evt
                    .getSession()
                    .createQuery(
                            "COUNT(*) from UserIsMemberOfTenant as a where (a.tenant.id = :tenantId AND a.user.id = :userId) OR a.tenant.admin.id = :userId");
            q.setLong("tenantId", tenant.getId());
            q.setLong("userId", user.getId());

            if (q.uniqueResult() == null) {

                UserIsMemberOfTenant relation = new UserIsMemberOfTenant();

                relation.setUser(user);
                relation.setTenant(tenant);

                evt.getSession().save(relation);
            }

            // Add as WorkflowAdmin
            UserAdministratesWorkflowModel relation = new UserAdministratesWorkflowModel();
            relation.setUser(user);
            relation.setWorkflowModel(i.getAdministrateWorkflowModel());
            evt.getSession().save(relation);

        } else if (i.getJoinTenant() != null) {

            // add to tenant if user isn't tenant member
            User user = i.getReceiver();
            Tenant tenant = i.getJoinTenant();

            Query q = evt
                    .getSession()
                    .createQuery(
                            "COUNT(*) from UserIsMemberOfTenant as a where (a.tenant.id = :tenantId AND a.user.id = :userId) OR a.tenant.admin.id = :userId");
            q.setLong("tenantId", tenant.getId());
            q.setLong("userId", user.getId());

            if (q.uniqueResult() == null) {

                UserIsMemberOfTenant relation = new UserIsMemberOfTenant();

                relation.setUser(user);
                relation.setTenant(tenant);

                evt.getSession().save(relation);
            }

        } else if (i.getParticipateInWorkflowInstance() != null) {

            // add to tenant if user isn't tenant member
            User user = i.getReceiver();
            Tenant tenant = i.getParticipateInWorkflowInstance()
                    .getDeployedWorkflowModel().getTenant();

            Query q = evt
                    .getSession()
                    .createQuery(
                            "COUNT(*) from UserIsMemberOfTenant as a where (a.tenant.id = :tenantId AND a.user.id = :userId) OR a.tenant.admin.id = :userId");
            q.setLong("tenantId", tenant.getId());
            q.setLong("userId", user.getId());

            if (q.uniqueResult() == null) {

                UserIsMemberOfTenant relation = new UserIsMemberOfTenant();

                relation.setUser(user);
                relation.setTenant(tenant);

                evt.getSession().save(relation);
            }

            // Add as WorkflowParticipant
            UserParticipatesInWorkflow relation = new UserParticipatesInWorkflow();
            relation.setUser(user);
            relation.setWorkflowInstance(i.getParticipateInWorkflowInstance());
            evt.getSession().save(relation);

        } else {
            // nothing to do
        }

    }
}
