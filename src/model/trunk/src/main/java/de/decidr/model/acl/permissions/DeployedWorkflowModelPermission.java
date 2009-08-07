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
package de.decidr.model.acl.permissions;

import de.decidr.model.entities.DeployedWorkflowModel;

/**
 * Represents the permission to read from or write to a deployed workflow model.
 * The deployed workflow model is internally represented by its id.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class DeployedWorkflowModelPermission extends
        de.decidr.model.acl.permissions.Permission {

    private static final long serialVersionUID = 1L;

    /**
     * The id of the deployed workflow model that this permission represents or
     * null if this permission represents all deployed workflow models.
     */
    protected Long deployedWorkflowModelId;

    /**
     * Constructor.
     * 
     * @param deployedWorkflowModelId
     *            if this parameter is not null, the permission represents only
     *            this particular deployed workflow model, otherwise it
     *            represents all deployed workflow model.
     */
    public DeployedWorkflowModelPermission(Long deployedWorkflowModelId) {
        super(DeployedWorkflowModel.class.getCanonicalName() + '.'
                + deployedWorkflowModelId == null ? "*"
                : deployedWorkflowModelId.toString());
        
        this.deployedWorkflowModelId = deployedWorkflowModelId;
    }

    /**
     * @return the deployedWorkflowModelId
     */
    public Long getDeployedWorkflowModelId() {
        return deployedWorkflowModelId;
    }
}