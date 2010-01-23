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

import de.decidr.model.acl.roles.Role;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Sets the server of the given server to locked or unlocked. If the server does
 * not exist, no exception is thrown.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class LockServerCommand extends SystemCommand {

    private boolean lock = false;
    private Long serverId = null;

    /**
     * Creates a new UnLockServerCommand. The command unlocks the given server.
     * If server does not exist, no exception is thrown.
     * 
     * @param role
     *            user who wants to execute the command
     * @param serverId
     *            ID of server to unlock
     * @param lock
     *            whether the server should be locked. If false, the server is
     *            unlocked.
     */
    public LockServerCommand(Role role, Long serverId, boolean lock) {
        super(role, null);
        if (serverId == null) {
            throw new IllegalArgumentException("Server ID must not be null.");
        }
        this.serverId = serverId;
        this.lock = lock;
    }

    @Override
    public void transactionAllowed(TransactionStartedEvent evt) {
        evt.getSession().createQuery(
                "update Server set locked = :newLock where id = :serverId")
                .setBoolean("newLock", lock).setLong("serverId", serverId)
                .executeUpdate();
    }
}
