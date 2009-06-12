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

package de.decidr.model.commands.workflowmodel;

import java.util.ArrayList;
import java.util.List;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserAdministratesWorkflowModel;
import de.decidr.model.entities.UserAdministratesWorkflowModelId;
import de.decidr.model.entities.UserIsMemberOfTenant;
import de.decidr.model.entities.UserIsMemberOfTenantId;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Makes the given list of users administrators of the given workflow model.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class SetWorkflowAdministratorsCommand extends WorkflowModelCommand
        implements TransactionalCommand {

    private List<Long> userIds;

    /**
     * Constructor
     * 
     * @param role
     * @param workflowModelId
     * @param userIds
     *            user ids of the new administrators. Mustn't be null.
     */
    public SetWorkflowAdministratorsCommand(Role role, Long workflowModelId,
            List<Long> userIds) {
        super(role, workflowModelId);
        /*
         * We must create a copy of the list because we might remove elements
         * from the list in transactionAllowed.
         */
        this.userIds = new ArrayList<Long>();
        this.userIds.addAll(userIds);
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        // remove the tenant admin from the list if present.
        WorkflowModel model = fetchWorkflowModel(evt.getSession());

        model.getUserAdministratesWorkflowModels().clear();

        userIds.remove(model.getTenant().getAdmin().getId());

        for (Long userId : userIds) {
            User admin = (User) evt.getSession().get(User.class, userId);

            // does the user exist?
            if (admin == null) {
                throw new EntityNotFoundException(User.class, userId);
            }

            // is he a registered user?
            if (admin.getUserProfile() == null) {
                throw new EntityNotFoundException(UserProfile.class, admin
                        .getId());
            }

            // is the user a member of the tenant that owns the workflow model?
            Object memberRelation = evt.getSession().get(
                    UserIsMemberOfTenant.class,
                    new UserIsMemberOfTenantId(admin.getId(), model.getTenant()
                            .getId()));

            if (memberRelation == null) {
                throw new EntityNotFoundException(UserIsMemberOfTenant.class,
                        admin.getId());
            }

            // yay, the user is a registered tenant member
            UserAdministratesWorkflowModel adminRelation = new UserAdministratesWorkflowModel();
            adminRelation.setId(new UserAdministratesWorkflowModelId(admin
                    .getId(), model.getId()));
            model.getUserAdministratesWorkflowModels().add(adminRelation);
        }

        evt.getSession().update(model);
    }
}
