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
 * Updates the description of a tenant.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class SetTenantDescriptionCommand extends TenantCommand {

    private String description;

    /**
     * Creates a new Command which updates the description of a tenant.
     * 
     * @param role
     *            user / system which executes the command
     * @param tenantId
     *            the id of the tenant which should be updated
     * @param description
     *            the new description
     * @throws IllegalArgumentException
     *             if tenantId is <code>null</code>
     */
    public SetTenantDescriptionCommand(Role role, Long tenantId,
            String description) {
        super(role, tenantId);
        requireTenantId();
        this.description = description;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        Tenant tenant = fetchTenant(evt.getSession());
        tenant.setDescription(description);
        evt.getSession().update(tenant);
    }
}
