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

import java.io.UnsupportedEncodingException;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.User;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Saves a workflow model, incrementing its version by one.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class SaveWorkflowModelCommand extends WorkflowModelCommand {

    private String name;
    private String description;
    private String dwdl;

    /**
     * Creates a new SaveWorkflowModelCommand that saves a workflow model and
     * increments its version number by one.
     * 
     * @param role
     *            user / system executing the command
     * @param workflowModelId
     *            ID of workflow model to save
     * @param name
     *            name of workflow model
     * @param description
     *            description of workflow model
     * @param dwdl
     *            XML to save
     * @throws IllegalArgumentException
     *             if workflowModelId is <code>null</code> or if name is
     *             <code>null</code> or empty.
     */
    public SaveWorkflowModelCommand(Role role, Long workflowModelId,
            String name, String description, String dwdl) {
        super(role, workflowModelId);
        requireWorkflowModelId();
        if ((name == null) || name.isEmpty()) {
            throw new IllegalArgumentException(
                    "Name must not be null or empty.");
        }
        this.name = name;
        this.description = description;
        this.dwdl = dwdl;
    }

    @Override
    public void transactionAllowed(TransactionStartedEvent evt)
            throws TransactionException {
        WorkflowModel model = fetchWorkflowModel(evt.getSession());

        /*
         * The model to save exists. Does the actor exist?
         */
        boolean userExists = evt.getSession().createQuery(
                "select u.id from User u where u.id = :userId").setLong(
                "userId", role.getActorId()).setMaxResults(1).uniqueResult() != null;

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
            try {
                model.setDwdl(dwdl.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new TransactionException(e);
            }
            model.setModifiedByUser(user);
            model.setModifiedDate(DecidrGlobals.getTime().getTime());
            model.setName(name);
            model.setVersion(model.getVersion() + 1);

            evt.getSession().update(model);
        }
    }

}
