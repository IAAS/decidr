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

package de.decidr.model.commands.file;

import java.io.InputStream;

import de.decidr.model.acl.permissions.FileReadPermission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.exceptions.StorageException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.StorageProvider;
import de.decidr.model.storage.StorageProviderFactory;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Retrieves the contents of an existing file using the default storage
 * provider.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class GetFileDataCommand extends FileCommand {

    InputStream stream = null;

    /**
     * Creates a new GetFileDataCommand that retrieves the contents of the given
     * file.
     * 
     * @param role
     *            user / system executing the command
     * @param fileId
     */
    public GetFileDataCommand(Role role, Long fileId) {
        super(role, fileId);
        this.additionalPermissions.add(new FileReadPermission(fileId));
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        stream = null;

        try {
            StorageProvider storage = StorageProviderFactory
                    .getDefaultFactory().getStorageProvider();

            stream = storage.getFile(fileId);
        } catch (StorageException e) {
            throw new TransactionException(e);
        } catch (Exception e) {
            if (e instanceof TransactionException) {
                throw (TransactionException) e;
            } else {
                throw new TransactionException(e);
            }
        }
    }

    /**
     * @return the stream containing the file data.
     */
    public InputStream getStream() {
        return stream;
    }

}
