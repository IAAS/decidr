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

import de.decidr.model.acl.permissions.FileReplacePermission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.File;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.StorageProvider;
import de.decidr.model.storage.StorageProviderFactory;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Replaces an existing file using the default storage provider.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class ReplaceFileCommand extends FileCommand {

    InputStream newContents = null;
    String newFileName = null;
    String newMimeType = null;
    Long fileSize = null;

    /**
     * Creates a new ReplaceFileCommand that replaces the content, name and mime
     * type of an existing file.
     * 
     * @param role
     *            user / system executing the command
     * @param fileId
     *            ID of existing file that is being replaced
     * @param newContents
     *            new file contents.
     * @param fileSize
     *            number of bytes to read from the given stream.
     * @param newFileName
     *            new file name.
     * @param newMimeType
     *            new mime type.
     * @throws IllegalArgumentException
     *             if one one of the following parameters is <code>null</code>:
     *             <ul>
     *             <li>fileId</li>
     *             <li>newContents</li>
     *             <li>fileSize</li>
     *             <li>newFileName</li>
     *             <li>newMimeType</li>
     *             </ul>
     */
    public ReplaceFileCommand(Role role, Long fileId, InputStream newContents,
            Long fileSize, String newFileName, String newMimeType) {
        super(role, fileId);

        if (fileId == null) {
            throw new IllegalArgumentException("File ID must not be null.");
        }
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
        if (fileSize == null) {
            throw new IllegalArgumentException("File size must not be null.");
        }
        if (fileSize < 0) {
            throw new IllegalArgumentException("File size must be positive.");
        }

        this.newContents = newContents;
        this.newFileName = newFileName;
        this.newMimeType = newMimeType;
        this.fileSize = fileSize;

        this.additionalPermissions.add(new FileReplacePermission(fileId));
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        try {
            File existingFile = fetchFile(evt.getSession());
            existingFile.setFileName(newFileName);
            existingFile.setMimeType(newMimeType);
            existingFile.setFileSizeBytes(fileSize);
            evt.getSession().save(existingFile);

            StorageProvider storage = StorageProviderFactory
                    .getDefaultFactory().getStorageProvider();

            // since the changes to the file data cannot necessarily be rolled
            // back by the transaction manager, they are our last action.
            storage.putFile(newContents, existingFile.getId(), fileSize);
        } catch (Exception e) {
            if (e instanceof TransactionException) {
                throw (TransactionException) e;
            } else {
                throw new TransactionException(e);
            }
        }
    }
}
