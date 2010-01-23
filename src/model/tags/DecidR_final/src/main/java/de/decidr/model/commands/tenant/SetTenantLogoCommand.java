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
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.StorageProvider;
import de.decidr.model.storage.StorageProviderFactory;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Sets the given logo as tenant logo to the given tenant. The logo will be
 * saved on permanent storage an a file entity will be created. The id of the
 * entity and the id of the file in the storage service for will be the same.
 * The old logo is deleted automatically.
 * <p>
 * To delete a tenant logo, use <code>null</code> as the logo ID. In this case
 * the command will also remove the tenant logo from the default storage
 * provider.
 * 
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class SetTenantLogoCommand extends TenantCommand {

    Long fileId;

    /**
     * Creates a new SetTenantLogoCommand. This commands sets the given logo as
     * tenant logo to the given tenant. The logo will be saved on permanent
     * storage an a file entity will be created. The id of the entity and the id
     * of the file in the storage service for will be the same. The old logo is
     * deleted automatically.
     * <p>
     * <strong>Passing <code>null</code> as the file ID will remove the current
     * tenant logo from the database and the default storage provider.</strong>
     * 
     * @param role
     *            user which executes the command
     * @param tenantId
     *            the id of the tenant where the logo will be set
     * @param fileId
     *            the id of the file that should be used as the tenant logo. Use
     *            null to remove the tenant logo.
     * @throws IllegalArgumentException
     *             if tenantId is <code>null</code>.
     */
    public SetTenantLogoCommand(Role role, Long tenantId, Long fileId) {
        super(role, tenantId);
        requireTenantId();

        this.fileId = fileId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        Tenant tenant = fetchTenant(evt.getSession());

        /*
         * First we delete the old logo.
         */
        File currentLogo = tenant.getLogo();
        if (currentLogo != null) {
            StorageProvider storage;
            try {
                storage = StorageProviderFactory.getDefaultFactory()
                        .getStorageProvider();

                storage.removeFile(currentLogo.getId());
                tenant.setLogo(null);
                evt.getSession().update(tenant);
                evt.getSession().delete(currentLogo);
            } catch (Exception e) {
                if (e instanceof TransactionException) {
                    throw (TransactionException) e;
                } else {
                    throw new TransactionException(e);
                }
            }
        }

        /*
         * If desired, we set a new logo. Note the transaction anomaly if we
         * cannot find the logo file entity: not every StorageProvider will roll
         * back the removeFile invocation.
         */
        if (fileId != null) {
            File logo = (File) evt.getSession().get(File.class, fileId);
            if (logo == null) {
                throw new EntityNotFoundException(File.class, fileId);
            }
            logo.setTemporary(false);
            tenant.setLogo(logo);
            evt.getSession().update(tenant);
            evt.getSession().update(logo);
        }
    }
}