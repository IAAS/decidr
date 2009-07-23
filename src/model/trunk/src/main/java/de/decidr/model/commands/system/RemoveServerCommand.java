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

import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Removes the server from the database. The corresponding real server will not
 * be closed. If the server doesn't exist the command will be ignored.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class RemoveServerCommand extends SystemCommand {

    private Long serverId = null;

    /**
     * Creates a new RemoveServerCommand. The command removes the server from
     * the database. The corresponding real server will not be closed. If the
     * server doesn't exist the command will be ignored.
     * 
     * @param role
     *            the user who wants to execute the command
     * @param serverId
     *            the id of the server to remove from the database
     */
    public RemoveServerCommand(Role role, Long serverId) {
        super(role, null);
        this.serverId = serverId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt) {
        evt.getSession().createQuery(
                "delete from Server s where s.id = :serverId").setLong(
                "serverId", serverId).executeUpdate();
    }
}
