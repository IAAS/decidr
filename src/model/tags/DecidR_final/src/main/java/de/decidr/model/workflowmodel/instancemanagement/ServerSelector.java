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

package de.decidr.model.workflowmodel.instancemanagement;

import java.util.List;

import org.apache.log4j.Logger;

import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.logging.DefaultLogger;

/**
 * This class uses a passed list of servers, on which a specific workflow model
 * is deployed, to select on which server a new instance of this process should
 * be started.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class ServerSelector {

    private static Logger log = DefaultLogger.getLogger(ServerSelector.class);

    /**
     * The function expects a ServerLoadView and selects the server with the
     * least load of the given servers and returns its ServerLoadView object.
     * 
     * @param serverStatistics
     * @return {@link ServerLoadView} The selected server with the least load.
     */
    public ServerLoadView selectServer(List<ServerLoadView> serverStatistics) {

        if (serverStatistics.isEmpty()) {
            log.warn("Server list for process invocation is empty");
            throw new IllegalArgumentException(
                    "Server list for process invocation is empty");
        }

        ServerLoadView minServer = serverStatistics.get(0);
        // find the server with the least load
        for (ServerLoadView server : serverStatistics) {
            if (server.getLoad() < minServer.getLoad()) {
                minServer = server;
            }
        }
        return minServer;
    }

}