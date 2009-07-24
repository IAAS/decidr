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

import de.decidr.model.notifications.NotificationEvents;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Removes a user from a tenant and notifies the user.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class RemoveFromTenantCommand extends LeaveTenantCommand {
    /**
     * Creates a new RemoveFromTenantCommand.
     * 
     * @param role
     *            user which executes the command
     * @param userId
     *            the id of the user who should be removed from tenant
     * @param tenantId
     *            id of the tenant where the user should be removed
     */
    public RemoveFromTenantCommand(Role role, Long userId, Long tenantId) {
        super(role, userId, tenantId);
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        super.transactionAllowed(evt);
        if (leftTenant) {
            leftTenant = false;
            NotificationEvents.removedFromTenant(user, tenant);
            leftTenant = true;
        }
    }
}
