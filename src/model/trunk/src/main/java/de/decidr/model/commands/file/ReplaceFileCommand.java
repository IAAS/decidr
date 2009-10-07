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

import java.io.FileInputStream;

import de.decidr.model.acl.permissions.FileReplacePermission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.File;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.StorageProvider;
import de.decidr.model.storage.StorageProviderFactory;
import de.decidr.model.transactions.TransactionEvent;

/**
 * @author Daniel Huss
 * @version 0.1
 */
public class ReplaceFileCommand extends FileCommand {

    FileInputStream newContents = null;
    String newFileName = null;
    String newMimeType = null;
    private Boolean replaced = false;

    /**
     * Creates a new ReplaceFileCommand that replaces the content, name and mime
     * type of an existing file.
     * 
     * @param role
     *            user / system executing the command
     * @param fileId
     *            ID of existing file that is being replaced.
     */
    public ReplaceFileCommand(Role role, Long fileId,
            FileInputStream newContents, String newFileName, String newMimeType) {
        super(role, fileId);

        if (newContents == null) {
            throw new IllegalArgumentException("New contents must not be null.");
        }

        if (newFileName == null) {
            throw new IllegalArgumentException(
                    "New file name must not be null.");
        }

        if (newMimeType == null) {
            throw new IllegalArgumentException(
                    "New mime type must not be null.");
        }

        this.newContents = newContents;
        this.newFileName = newFileName;
        this.newMimeType = newMimeType;

        this.additionalPermissions.add(new FileReplacePermission(fileId));
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        replaced = false;
        try {
            File existingFile = fetchFile(evt.getSession());
            existingFile.setFileName(newFileName);
            existingFile.setMimeType(newMimeType);
            existingFile.setFileSizeBytes(newContents.getChannel().size());
            evt.getSession().save(existingFile);

            StorageProvider storage = StorageProviderFactory
                    .getDefaultFactory().getStorageProvider();

            // since the changes to the file data cannot necessarily be rolled
            // back by the transaction manager, they are our last action.
            storage.putFile(newContents, existingFile.getId(), newContents
                    .getChannel().size());
        } catch (Exception e) {
            if (e instanceof TransactionException) {
                throw (TransactionException) e;
            } else {
                throw new TransactionException(e);
            }
        }
        replaced = true;
    }

    /**
     * @return whether the file has been replaced.
     */
    public Boolean isReplaced() {
        return replaced;
    }

}
