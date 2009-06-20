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

import de.decidr.model.entities.ServerLoadView;

/**
 * This class provides the possibility to select servers on which a workflow
 * model could be deployed on.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class ODESelector {

    /**
     * This function expects a ServerLoadView, containing a list of all
     * available servers as well as their IDs, paths, server load and deployed
     * workflow instances. Using this information, the function chooses the 50%
     * of these servers with the least load and returns a list of their IDs.
     * 
     * @param serverStatistics
     * @return List of server IDs with least load.
     */
    public List<Long> selectServer(List<ServerLoadView> serverStatistics) {
        return null;
    }

}
