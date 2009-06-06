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

import java.util.Date;

import org.hibernate.Query;

import de.decidr.model.entities.User;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Saves a workflow model, incrementing its version by one.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class SaveWorkflowModelCommand extends WorkflowModelCommand {

    private String name;
    private String description;
    private byte[] dwdl;

    /**
     * Constructor
     * 
     * @param role
     * @param workflowModelId
     */
    public SaveWorkflowModelCommand(Role role, Long workflowModelId,
            String name, String description, byte[] dwdl) {
        super(role, workflowModelId);
        this.name = name;
        this.description = description;
        this.dwdl = dwdl;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        WorkflowModel model = fetchWorkflowModel(evt.getSession());

        /*
         * The model to save exists. Does the actor exist?
         */

        Query q = evt.getSession().createQuery(
                "select count(*) from User u where u.id = :userId");
        q.setLong("userId", role.getActorId());

        Boolean userExists = Long.valueOf(1).equals(q.uniqueResult());

        if (!userExists) {
            throw new EntityNotFoundException(User.class, role.getActorId());
        } else {

            /*
             * There is a user with the given actor id, we're going to use him
             * to fill out the "modified by" field of the saved workflow model.
             */
            User user = new User();
            user.setId(role.getActorId());

            model.setDescription(description);
            model.setDwdl(dwdl);
            model.setModifiedByUser(user);
            model.setModifiedDate(new Date());
            model.setName(name);
            model.setVersion(model.getVersion() + 1);

            evt.getSession().update(model);
        }
    }

}
