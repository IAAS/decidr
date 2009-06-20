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

package de.decidr.model.workflowmodel.deployment;

import java.util.List;
import de.decidr.model.entities.DeployedWorkflowModel;


/**
 * An interface for the deployment component
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class DeploymentResultImpl implements DeploymentResult {

    private List<Long> servers;
    private DeployedWorkflowModel dwfm;

    @Override
    public DeployedWorkflowModel getDeployedWorkflowModel() {
        return dwfm;
    }

    @Override
    public List<Long> getServers() {
        return servers;
    }

    public void setDeployedWorkflowModel(DeployedWorkflowModel dwfm) {
        this.dwfm = dwfm;

    }

    public void setServers(List<Long> servers) {
        this.servers = servers;
    }

}
