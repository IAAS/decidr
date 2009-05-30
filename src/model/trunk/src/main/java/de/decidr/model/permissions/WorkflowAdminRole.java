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
package de.decidr.model.permissions;

/**
 * Represents a workflow admin.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class WorkflowAdminRole extends UserRole {

    /**
     * Constructor.
     * 
     * @param userId
     */
    public WorkflowAdminRole(Long userId) {
        super(userId);
    }

    /**
     * Creates a new WorkflowAdminRole with its actor id set to unknown;
     * 
     */
    public WorkflowAdminRole() {
        this(UNKNOWN_USER_ID);
    }
}