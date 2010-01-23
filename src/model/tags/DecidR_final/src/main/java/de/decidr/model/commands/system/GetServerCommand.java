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

package de.decidr.model.commands.system;

import java.util.List;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.Server;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Retrieves one or more servers from the database by ID.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class GetServerCommand extends SystemCommand {

    private Long[] serverIds;
    private List<Server> servers;

    /**
     * Creates a new GetServerCommand that retrieves the servers with the given
     * IDs from the database. If one or more servers are not found, they will
     * simply not be included in the list of found servers. No exception is
     * thrown in this case.
     * 
     * @param role
     *            user / system executing the command
     * @param serverIds
     *            servers to retrieve.
     */
    public GetServerCommand(Role role, Long... serverIds) {
        super(role, null);
        if ((serverIds == null) || (serverIds.length == 0)) {
            throw new IllegalArgumentException(
                    "serverIds must not be null or empty.");
        }
        this.serverIds = serverIds;
    }

    /**
     * @return the first found server. Returns <code>null</code> if no server
     *         has been retrieved.
     */
    public Server getServer() {
        Server result = null;
        if ((servers != null) && !servers.isEmpty()) {
            result = servers.get(0);
        }
        return result;
    }

    /**
     * @return the list of found servers. Returns <code>null</code> if no server
     *         has been retrieved.
     */
    public List<Server> getServers() {
        return servers;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        servers = null;
        if ((serverIds != null) && (serverIds.length > 0)) {
            servers = evt
                    .getSession()
                    .createQuery(
                            "select s from Server s join fetch s.serverType where s.id in (:serverIds)")
                    .setParameterList("serverIds", serverIds).list();
        }
    }
}
