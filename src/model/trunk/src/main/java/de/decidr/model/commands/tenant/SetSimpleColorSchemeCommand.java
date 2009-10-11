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
 * Sets the simple color scheme of the given tenant.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
// XXX no null checking ~rr
public class SetSimpleColorSchemeCommand extends TenantCommand {

    Long fileId;

    /**
     * Creates a new SetSimpleColorSchemeCommand. This command sets the simple
     * color scheme of the given tenant.
     * 
     * @param role
     *            user which executes the command
     * @param tenantId
     *            the id of the tenant where the simple color scheme should be
     *            set
     * @param fileId
     *            the id of the file that should be used as the default color
     *            scheme
     */
    public SetSimpleColorSchemeCommand(Role role, Long tenantId, Long fileId) {

        super(role, tenantId);
        this.fileId = fileId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        Tenant tenant = fetchTenant(evt.getSession());

        File simpleColorScheme = (File) evt.getSession()
                .get(File.class, fileId);
        tenant.setSimpleColorScheme(simpleColorScheme);
        evt.getSession().save(tenant);
    }
}
