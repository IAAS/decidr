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
package de.decidr.model.commands.workflowinstance;

import java.util.HashSet;
import java.util.Set;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserParticipatesInWorkflow;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Saves all participants of the given workflow instance in the result variable.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class GetParticipatingUsersCommand extends WorkflowInstanceCommand {

    private Set<User> result;

    /**
     * Creates a new GetParticipatingUsersCommand
     * 
     * @param role
     *            user / system executing the command
     * @param workflowInstanceId
     *            ID of workflow instance whose participating users should be
     *            fetched.
     * @throws IllegalArgumentException
     *             if workflowInstanceId is <code>null</code>
     */
    public GetParticipatingUsersCommand(Role role, Long workflowInstanceId) {
        super(role, null, workflowInstanceId);
        requireWorkflowInstance();
    }

    /**
     * @return the result
     */
    public Set<User> getResult() {
        return result;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws EntityNotFoundException {

        Set<UserParticipatesInWorkflow> partUsers = new HashSet<UserParticipatesInWorkflow>();
        result = new HashSet<User>();

        WorkflowInstance instance = fetchWorkflowInstance(evt.getSession());
        partUsers = instance.getUserParticipatesInWorkflows();

        for (UserParticipatesInWorkflow partuser : partUsers) {
            result.add(partuser.getUser());
        }
    }

}
