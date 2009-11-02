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

import org.hibernate.Session;

import de.decidr.model.acl.access.WorkflowInstanceAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.exceptions.EntityNotFoundException;

/**
 * Abstract base class for commands that deal with workflow instances.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public abstract class WorkflowInstanceCommand extends AclEnabledCommand
        implements WorkflowInstanceAccess {

    private Long workflowInstanceId;

    /**
     * Create a new WorkflowInstanceCommand
     * 
     * @param role
     *            user / system executing the command
     * @param permission
     *            additional permissions that must be granted to this command
     *            for successful execution.
     * @param workflowInstanceId
     *            ID of workflow instance that is bein accessed.
     */
    public WorkflowInstanceCommand(Role role,
            Collection<Permission> permission, Long workflowInstanceId) {
        super(role, permission);
        this.workflowInstanceId = workflowInstanceId;

    }

    /**
     * Fetches the workflow instance that is accessed by this command from the
     * database.
     * 
     * @param session
     *            current Hibernate session
     * @return the workflow instance accesed by this command
     * @throws EntityNotFoundException
     *             if the workflow instance does not exist.
     */
    protected WorkflowInstance fetchWorkflowInstance(Session session)
            throws EntityNotFoundException {

        WorkflowInstance result = (WorkflowInstance) session.get(
                WorkflowInstance.class, workflowInstanceId);

        if (result == null) {
            throw new EntityNotFoundException(WorkflowInstance.class,
                    workflowInstanceId);
        }

        return result;
    }

    /**
     * @see WorkflowInstanceAccess#getWorkflowInstanceIds()
     */
    public Long[] getWorkflowInstanceIds() {
        Long[] result = { workflowInstanceId };
        return result;
    }
}
