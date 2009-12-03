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
import de.decidr.model.entities.File;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.StorageProvider;
import de.decidr.model.storage.StorageProviderFactory;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Returns the current color scheme as input stream using the default storage
 * provider to fetch the file.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class GetCurrentColorSchemeCommand extends TenantCommand {

    private InputStream currentColorScheme;

    /**
     * Creates a new GetCurrentColorSchemeCommand. This command load the current
     * color scheme using the default storage provider.
     * 
     * @param role
     *            the user which executes the command
     * @param tenantId
     *            the ID of the tenant whose color scheme should be returned
     * @throws IllegalArgumentException
     *             if tenantId is <code>null</code>
     */
    public GetCurrentColorSchemeCommand(Role role, Long tenantId) {
        super(role, tenantId);
        requireTenantId();
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        currentColorScheme = null;
        
        Tenant tenant = fetchTenant(evt.getSession());
        File current = tenant.getCurrentColorScheme();

        try {
            if (current == null) {
                currentColorScheme = null;
            } else {
                StorageProvider storage = StorageProviderFactory
                        .getDefaultFactory().getStorageProvider();
                currentColorScheme = storage.getFile(current.getId());
            }
        } catch (Exception e) {
            if (e instanceof TransactionException) {
                throw (TransactionException) e;
            } else {
                throw new TransactionException(e);
            }
        }
    }

    /**
     * @return The current color scheme. You must close this stream once you're
     *         done reading from it.
     */
    public InputStream getCurrentColorScheme() {
        return currentColorScheme;
    }
}
