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
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Saves all participants of the given WorkflowInstance at the result variable.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class GetParticipatingUsersCommand extends WorkflowInstanceCommand {

    private Long workflowInstanceId;
    private Set<User> result;

    public GetParticipatingUsersCommand(Role role, Long WorkflowInstanceId) {
        super(role, null, WorkflowInstanceId);
        this.workflowInstanceId = WorkflowInstanceId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt) {

        Set<UserParticipatesInWorkflow> partUsers = new HashSet<UserParticipatesInWorkflow>();
        result = new HashSet<User>();

        WorkflowInstance instance = (WorkflowInstance) evt.getSession().load(
                WorkflowInstance.class, workflowInstanceId);

        partUsers = instance.getUserParticipatesInWorkflows();

        for (UserParticipatesInWorkflow partuser : partUsers) {
            result.add(partuser.getUser());
        }
    }

    /**
     * @return the result
     */
    public Set<User> getResult() {
        return result;
    }

}
