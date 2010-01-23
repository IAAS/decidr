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
 * Sets the current color scheme of the given tenant.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class SetCurrentColorSchemeCommand extends TenantCommand {

    Boolean useAdvancedColorScheme;

    /**
     * Creates a new SetCurrentColorSchemeCommand. This command sets the current
     * color scheme of the given tenant.
     * 
     * @param role
     *            user which executes the command
     * @param tenantId
     *            the id of the tenant where the current color scheme should be
     *            set
     * @param useAdvancedColorScheme
     *            whether the advanced color scheme will become the current
     *            color scheme, otherwise the simple color scheme is used.
     */
    public SetCurrentColorSchemeCommand(Role role, Long tenantId,
            Boolean useAdvancedColorScheme) {
        super(role, tenantId);
        requireTenantId();
        this.useAdvancedColorScheme = useAdvancedColorScheme;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        Tenant tenant = fetchTenant(evt.getSession());

        tenant.setCurrentColorScheme(useAdvancedColorScheme ? tenant
                .getAdvancedColorScheme() : tenant.getSimpleColorScheme());
        evt.getSession().save(tenant);
    }
}
