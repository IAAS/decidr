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

package de.decidr.model.commands.user;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Sets the current tenant ID of a user. This setting helps the application
 * remember which tenant to switch to upon logging in.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class SetCurrentTenantCommand extends UserCommand {

    private Long currentTenantId = null;

    /**
     * Creates a new SetCurrentTenantCommand that sets the the current tenant ID
     * of the given user.
     * 
     * @param role
     *            user / system executing the command
     * @param userId
     *            user whose current tenant ID should be set
     * @param currentTenantId
     *            current tenant ID (<code>null</code> means no choice was made
     *            and is a valid option)
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>
     */
    public SetCurrentTenantCommand(Role role, Long userId, Long currentTenantId) {
        super(role, userId);
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null.");
        }
        this.currentTenantId = currentTenantId;
    }

    @Override
    public void transactionAllowed(TransactionStartedEvent evt)
            throws TransactionException {
        User user = fetchUser(evt.getSession());

        if (currentTenantId == null) {
            user.setCurrentTenant(null);
        } else {
            Tenant t = (Tenant) evt.getSession().get(Tenant.class,
                    currentTenantId);
            user.setCurrentTenant(t);
        }

        evt.getSession().save(user);
    }
}
