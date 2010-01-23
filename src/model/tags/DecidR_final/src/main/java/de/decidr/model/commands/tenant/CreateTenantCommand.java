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
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Creates a new tenant with the given properties.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class CreateTenantCommand extends AclEnabledCommand {

    private String name;
    private String description;
    private Long adminId;
    private Long tenantId;

    /**
     * Creates a new CreateTenantCommand that will create a new tenant with the
     * given properties.
     * 
     * @param role
     *            user/system which executes the command
     * @param name
     *            tenant name
     * @param description
     *            tenant description
     * @param adminId
     *            ID of the tenant admin
     * @throws IllegalArgumentException
     *             if name is <code>null</code> or empty or if adminId is
     *             <code>null</code>.
     */
    public CreateTenantCommand(Role role, String name, String description,
            Long adminId) {
        super(role, (Permission) null);

        if ((name == null) || name.isEmpty()) {
            throw new IllegalArgumentException(
                    "Tenant name must not be null or empty.");
        }

        if (adminId == null) {
            throw new IllegalArgumentException("Admin ID must not be null.");
        }

        this.adminId = adminId;
        this.name = name;
        this.description = description;
    }

    /**
     * @return The ID of the new tenant
     */
    public Long getTenantId() {
        return tenantId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        tenantId = null;

        // does the tenant admin exist?
        User admin = (User) evt.getSession().get(User.class, adminId);
        if (admin == null) {
            throw new EntityNotFoundException(User.class, adminId);
        }

        // Does a tenant with the chosen name already exist?
        String hql = "select t.id from Tenant t where t.name = :tenantName";
        boolean tenantAlreadyExists = evt.getSession().createQuery(hql)
                .setString("tenantName", name).setMaxResults(1).uniqueResult() != null;

        if (!tenantAlreadyExists) {
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

}
