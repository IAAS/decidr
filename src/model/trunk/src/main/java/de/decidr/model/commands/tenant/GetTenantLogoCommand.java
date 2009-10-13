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

import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.IncompleteConfigurationException;
import de.decidr.model.exceptions.StorageException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.StorageProviderFactory;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Used to load the logo from the storage.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class GetTenantLogoCommand extends TenantCommand {

    private Long tenantId;
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
        super(role, null);
        this.tenantId = tenantId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        Tenant tenant = (Tenant) evt.getSession().load(Tenant.class, tenantId);
        Long logoid = tenant.getLogo().getId();
        StorageProviderFactory factory;

        try {
            factory = StorageProviderFactory.getDefaultFactory();
        } catch (InvalidPropertiesFormatException e) {
            throw new TransactionException(e);
        } catch (IncompleteConfigurationException e) {
            throw new TransactionException(e);
        } catch (IOException e) {
            throw new TransactionException(e);
        }

        try {
            logoStream = factory.getStorageProvider().getFile(logoid);
        } catch (StorageException e) {
            throw new TransactionException(e);
        } catch (IncompleteConfigurationException e) {
            throw new TransactionException(e);
        } catch (InstantiationException e) {
            throw new TransactionException(e);
        } catch (IllegalAccessException e) {
            throw new TransactionException(e);
        }
    }

    public InputStream getLogoStream() {
        return logoStream;
    }
}
