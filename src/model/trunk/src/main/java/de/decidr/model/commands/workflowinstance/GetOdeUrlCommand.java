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

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Returns the Url of the server on which the given WorkflowInstance is executed
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 * @deprecated Use DecidrGlobals instead? ~dh
 */
@Deprecated
public class GetOdeUrlCommand extends WorkflowInstanceCommand {

    private String result;

    public GetOdeUrlCommand(Role role, Long workflowInstanceId) {
        super(role, null, workflowInstanceId);
    }

    @Override
    public void transactionAllowed(TransactionEvent evt) {

        WorkflowInstance instance = (WorkflowInstance) evt.getSession().load(
                WorkflowInstance.class, this.getWorkflowInstanceIds()[0]);

        result = instance.getServer().getLocation();

    }

    public String getResult() {
        return result;
    }

}
