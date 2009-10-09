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

import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Retrieves the ID of a tenant by name.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class GetTenantIdCommand extends AclEnabledCommand {

    private String tenantName;
    private Long tenantId = null;

    /**
     * Creates a new GetTenantIdCommand. This command writes the ID of the given
     * tenant in the variable result. If the tenantName doesn't exists, an
     * exception will be thrown.
     * 
     * @param role
     *            user which executes the command
     * @param tenantName
     *            the name of the tenant whose ID should be requested
     */
    public GetTenantIdCommand(Role role, String tenantName) {
        super(role, (Permission) null);
        this.tenantName = tenantName;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        tenantId = null;

        Number result = (Number) evt.getSession().createQuery(
                "select t.id from Tenant t where t.name = :tenantName")
                .setString("tenantName", tenantName).uniqueResult();

        if (result == null) {
            throw new EntityNotFoundException(Tenant.class);
        } else {
            tenantId = result.longValue();
        }
    }

    /**
     * @return The ID of the found tenant
     */
    public Long getTenantId() {
        return tenantId;
    }
}
