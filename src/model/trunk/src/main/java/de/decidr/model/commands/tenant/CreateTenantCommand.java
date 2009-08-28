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
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Creates a tenant with the given properties.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class CreateTenantCommand extends TenantCommand {

    private String name;
    private String description;
    private Long adminId;
    private Long tenantId;

    /**
     * Creates a new CreateTenantCommand. The tenant which will be created in
     * this command will have the given properties.
     * 
     * @param role
     *            user which executes the command
     * @param name
     *            tenant name
     * @param description
     *            tenant description
     * @param adminId
     *            ID of the tenant admin
     */
    public CreateTenantCommand(Role role, String name, String description,
            Long adminId) {
        super(role, null);

        this.adminId = adminId;
        this.name = name;
        this.description = description;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        User admin = (User) evt.getSession().get(User.class, adminId);

        if (admin == null) {
            throw new EntityNotFoundException(User.class, adminId);
        }

        // Does a tenant with the chosen name already exist?
        String hql = "select count(*) from Tenant t where t.name = :tenantName";
        Number numExistingTenants = (Number) evt.getSession().createQuery(hql)
                .setString("tenantName", name).uniqueResult();

        if (numExistingTenants == null) {
            throw new NullPointerException(
                    "A query that was supposed to return a number returned null.");
        }

        if (numExistingTenants.intValue() == 0) {
            // there is no existing tenant with the chosen name, so we may
            // create the new tenant
            Tenant tenant = new Tenant();
            tenant.setName(name);
            tenant.setDescription(description);
            tenant.setAdmin(admin);
            tenant.setApprovedSince(null);

            evt.getSession().save(tenant);

            tenantId = tenant.getId();
        } else {
            throw new TransactionException("Tenant name already exists.");
        }
    }

    /**
     * @return The ID of the new tenant
     */
    @Override
    public Long getTenantId() {
        return tenantId;
    }

}
