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

import org.hibernate.Session;

import de.decidr.model.acl.access.TenantAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.EntityNotFoundException;

/**
 * The Abstract Tenant Command.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public abstract class TenantCommand extends AclEnabledCommand implements
        TenantAccess {

    private Long tenantId;

    /**
     * Creates a new TenantCommand
     * 
     * @param role
     *            user / system executing the command
     * @param tenantId
     *            tenant that is being accessed
     */
    public TenantCommand(Role role, Long tenantId) {
        super(role, (Permission) null);
        this.tenantId = tenantId;
    }

    /**
     * @param session
     *            the current Hibernate session
     * @return the {@link Tenant} entity that correlates with the tenant ID.
     * @throws EntityNotFoundException
     *             iff the tenant does not exist
     */
    protected Tenant fetchTenant(Session session)
            throws EntityNotFoundException {
        Tenant result = (Tenant) session.get(Tenant.class, tenantId);

        if (result == null) {
            throw new EntityNotFoundException(Tenant.class, tenantId);
        }

        return result;
    }

    /**
     * @return the ID of the tenant that is being accessed by this command.
     */
    public Long getTenantId() {
        return tenantId;
    }

    /**
     * @return the ID(s) of the tenants that are being accessed by this command.
     */
    public Long[] getTenantIds() {
        Long[] result = { tenantId };
        return result;
    }

    /**
     * Throws an {@link IllegalArgumentException} if the current tenant ID is
     * <code>null</code>.
     * 
     * @throws IllegalArgumentException
     */
    protected void requireTenantId() {
        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID must not be null.");
        }
    }
}