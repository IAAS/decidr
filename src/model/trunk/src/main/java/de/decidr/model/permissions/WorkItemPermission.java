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

import de.decidr.model.entities.WorkItem;

/**
 * Represents the permission to a work item.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 *
 * @version 0.1
 */
public class WorkItemPermission extends EntityPermission {
    
	private static final long serialVersionUID = 1L;

	String WorkItemId = null;
		
	/**
	 * Constructor.
	 * 
	 * @param workItemId
	 */
	public WorkItemPermission(Long workItemId) {
		super(WorkItem.class.getCanonicalName(), workItemId);
	}

    /**
     * @return the workItemId
     */
    public String getWorkItemId() {
        return WorkItemId;
    }

    /**
     * @param workItemId the workItemId to set
     */
    public void setWorkItemId(String workItemId) {
        WorkItemId = workItemId;
    }
}