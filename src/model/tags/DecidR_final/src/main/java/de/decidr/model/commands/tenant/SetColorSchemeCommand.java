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
import de.decidr.model.entities.File;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Sets the advanced color scheme of the given tenant.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @author Reinhold
 * @version 0.1
 */
public class SetColorSchemeCommand extends TenantCommand {

    Long fileId;
    boolean advanced;

    /**
     * Creates a new SetAdvancedColorSchemeCommand. This command sets the
     * advanced color scheme of the given tenant.
     * 
     * @param role
     *            user / system executing the command
     * @param tenantId
     *            id of the tenant to modify
     * @param fileId
     *            id of color scheme file to use as the advanced color scheme
     *            (can be null if the default theme should be used).
     * @param advanced
     *            whether to set the advanced or the simple color scheme
     * @throws IllegalArgumentException
     *             if tenantId is <code>null</code>
     */
    public SetColorSchemeCommand(Role role, Long tenantId, Long fileId,
            boolean advanced) {
        super(role, tenantId);
        requireTenantId();
        this.fileId = fileId;
        this.advanced = advanced;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        Tenant tenant = fetchTenant(evt.getSession());
        File colorScheme = (File) evt.getSession().get(File.class, fileId);
        if (advanced) {
            tenant.setAdvancedColorScheme(colorScheme);
        } else {
            tenant.setSimpleColorScheme(colorScheme);
        }
        evt.getSession().save(tenant);
    }
}