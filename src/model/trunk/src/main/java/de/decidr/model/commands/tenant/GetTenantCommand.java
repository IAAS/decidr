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

import org.hibernate.Query;

import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Retrieves the tenant for a given tenant ID or tenant name. If the tenant does
 * not exist, an {@link EntityNotFoundException} is thrown.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class GetTenantCommand extends AclEnabledCommand {

    /**
     * Tenant name to search for.
     */
    private String tenantName = null;

    /**
     * Tenant ID to seach for (takes precedence over name if not null)
     */
    private Long tenantId = null;

    /**
     * The found tenant
     */
    private Tenant result = null;

    /**
     * Creates a new GetTenantCommand which retrieves the tenant for a given
     * tenant ID or tenant name. If the tenant does not exist, an
     * {@link EntityNotFoundException} is thrown.
     * 
     * @param role
     *            user / system executing the command
     * @param tenantId
     *            tenant ID to search for.
     * @throws IllegalArgumentException
     *             if tenantId is <code>null</code> or empty.
     */
    public GetTenantCommand(Role role, Long tenantId) {
        super(role, (Permission) null);
        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID must not be null.");
        }
        this.tenantId = tenantId;
    }

    /**
     * Creates a new GetTenantCommand which retrieves the tenant for a given
     * tenant ID or tenant name. If the tenant does not exist, an
     * {@link EntityNotFoundException} is thrown.
     * 
     * 
     * @param role
     *            user / system executing the command
     * @param tenantName
     *            tenant name to search for.
     * @throws IllegalArgumentException
     *             if tenantName is <code>null</code> or empty.
     */
    public GetTenantCommand(Role role, String tenantName) {
        super(role, (Permission) null);
        if ((tenantName == null) || tenantName.isEmpty()) {
            throw new IllegalArgumentException(
                    "Tenant name must not be null or empty.");
        }
        this.tenantName = tenantName;
    }

    /**
     * @return the found tenant (<code>null</code> if none has been found)
     */
    public Tenant getResult() {
        return result;
    }

    @Override
    public void transactionAllowed(TransactionStartedEvent evt)
            throws TransactionException {

        result = null;

        Query query;
        if (tenantId != null) {
            // search for tenant by ID
            query = evt.getSession().createQuery(
                    "from Tenant where id = :tenantId").setLong("tenantId",
                    tenantId);
        } else {
            // search for tenant by name
            query = evt.getSession().createQuery(
                    "from Tenant where name = :tenantName").setString(
                    "tenantName", tenantName);
        }

        result = (Tenant) query.uniqueResult();
        if (result == null) {
            throw new EntityNotFoundException(Tenant.class, tenantName);
        }
    }
}
