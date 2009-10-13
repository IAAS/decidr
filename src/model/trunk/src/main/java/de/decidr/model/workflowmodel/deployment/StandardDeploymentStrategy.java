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

import java.util.ArrayList;
import java.util.List;

import de.decidr.model.entities.ServerLoadView;

/**
 * This is the standard deployment strategy. The server with min. load is selected.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class StandardDeploymentStrategy implements DeploymentStrategy {

    /* (non-Javadoc)
     * @see de.decidr.model.workflowmodel.deployment.DeploymentStrategy#selectServer(java.util.List)
     */
    @Override
    public List<ServerLoadView> selectServer(List<ServerLoadView> serverStatistics) {
        if (serverStatistics.isEmpty()){
            throw new IllegalArgumentException("Server list for process deployment is empty");
        }
        List<ServerLoadView> resultList = new ArrayList<ServerLoadView>();
        ServerLoadView minServer = serverStatistics.get(0);
        for (ServerLoadView serverView : serverStatistics){
            if (!serverView.isLocked()){
                if(serverView.getLoad() < minServer.getId()){
                    minServer = serverView;
                }
            }
        }
        resultList.add(minServer);
        return resultList;
    }
}
