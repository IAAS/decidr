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

package de.decidr.model.commands.tenant;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Retrievs the current settings of a single tenant from the database.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class GetTenantSettingsCommand extends TenantCommand {

    private Tenant tenantSettings;

    /**
     * Creates a new GetTenantSettingsCommand that retrievs the current settings
     * of a single tenant from the database.
     * 
     * @param role
     *            user /system executing the command.
     * @param tenantId
     *            id of tenant whose settings should be retrieved.
     */
    public GetTenantSettingsCommand(Role role, Long tenantId) {
        super(role, tenantId);
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        tenantSettings = null;
        tenantSettings = fetchTenant(evt.getSession());
        // preload color schemes
        tenantSettings.getCurrentColorScheme();
        tenantSettings.getAdvancedColorScheme();
        tenantSettings.getSimpleColorScheme();
    }

    /**
     * @return the tenantSettings
     */
    public Tenant getTenantSettings() {
        return tenantSettings;
    }

}
