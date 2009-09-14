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

import java.util.Collection;

import de.decidr.model.acl.access.WorkflowInstanceAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;

/**
 * Abstract base class for commands that deal with workflow instances.
 * 
 * @author Markus Fischer
 * @version 0.1
 */
public abstract class WorkflowInstanceCommand extends AclEnabledCommand
        implements WorkflowInstanceAccess {

    private Long WorkflowInstanceId;

    public WorkflowInstanceCommand(Role role,
            Collection<Permission> permission, Long workflowInstanceId) {
        super(role, permission);
        this.WorkflowInstanceId = workflowInstanceId;

    }

    public Long[] getWorkflowInstanceIds() {
        Long[] result = { WorkflowInstanceId };
        return result;
    }
}
