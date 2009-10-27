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

import java.io.InputStream;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.StorageProvider;
import de.decidr.model.storage.StorageProviderFactory;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Used to load the logo from the storage.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class GetTenantLogoCommand extends TenantCommand {

    private InputStream logoStream;

    /**
     * Creates a new GetTenantLogoCommand. This command load the logo from the
     * storage and saves it in result variable.
     * 
     * @param role
     *            the user which executes the command
     * @param tenantId
     *            the id of the tenant
     */
    public GetTenantLogoCommand(Role role, Long tenantId) {
        super(role, tenantId);
        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID must not be null.");
        }
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        logoStream = null;

        Tenant tenant = fetchTenant(evt.getSession());
        if (tenant.getLogo() == null) {
            return;
        }

        try {
            StorageProvider storage = StorageProviderFactory
                    .getDefaultFactory().getStorageProvider();

            logoStream = storage.getFile(tenant.getLogo().getId());
        } catch (Exception e) {
            if (e instanceof TransactionException) {
                throw (TransactionException) e;
            } else {
                throw new TransactionException(e);
            }
        }
    }

    /**
     * @return the logo image data as an {@link InputStream} or null if the
     *         tenant hasn't set a logo.
     */
    public InputStream getLogoStream() {
        return logoStream;
    }
}
