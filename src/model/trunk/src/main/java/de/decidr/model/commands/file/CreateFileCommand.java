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
import java.util.HashSet;
import java.util.Set;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.permissions.FileDeletePermission;
import de.decidr.model.acl.permissions.FilePermission;
import de.decidr.model.acl.permissions.FileReadPermission;
import de.decidr.model.acl.permissions.FileReplacePermission;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.File;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.StorageProvider;
import de.decidr.model.storage.StorageProviderFactory;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Creates a new file using the default storage provider.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class CreateFileCommand extends AclEnabledCommand {

    private InputStream contents;
    private String originalFileName;
    private String mimeType;
    private Boolean temporary;
    private Long fileSize;
    private Set<Class<? extends FilePermission>> publicPermissions;

    private File newFile;

    /**
     * Creates a new CreateFileCommand that will store a new file using the
     * default storage provider.
     * 
     * @param role
     *            user / system executing the command
     * @param contents
     *            file contents
     * @param fileSize
     *            number of bytes to read from the given stream.
     * @param originalFileName
     *            original file name as reported by the uploader's user agent
     *            (browser).
     * @param mimeType
     *            mime type as reported by the uploader's user agent (browser).
     * @param temporary
     *            whether the file is a temporary file that is subject to
     *            deletion if it is not made permanent within a certain time
     *            limit.
     * @param publicPermissions
     *            the initial global permissions of the file. If set to null,
     *            the file will have no global permissions, i.e. it will not be
     *            publicly readable/writeable.
     * @throws IllegalArgumentException
     *             if the file size is negative of if one of the following
     *             parameters is <code>null</code>:
     *             <ul>
     *             <li>contents</li>
     *             <li>fileSize</li>
     *             <li>originalFileName</li>
     *             <li>mimeType</li>
     *             <li>temporary</li>
     *             </ul>
     */
    public CreateFileCommand(Role role, InputStream contents, Long fileSize,
            String originalFileName, String mimeType, Boolean temporary,
            Set<Class<? extends FilePermission>> publicPermissions) {
        super(role, (Permission) null);

        if (contents == null) {
            throw new IllegalArgumentException(
                    "File contents must not be null.");
        }
        if (originalFileName == null) {
            throw new IllegalArgumentException("File name must not be null.");
        }
        if (mimeType == null) {
            throw new IllegalArgumentException("Mime type must not be null.");
        }
        if (fileSize == null) {
            throw new IllegalArgumentException("File size must not be null.");
        }
        if (fileSize < 0) {
            throw new IllegalArgumentException("File size must be positive.");
        }

        if (temporary == null) {
            throw new IllegalArgumentException(
                    "Temporary flag must not be null.");
        }

        if (publicPermissions == null) {
            publicPermissions = new HashSet<Class<? extends FilePermission>>(0);
        }

        this.contents = contents;
        this.originalFileName = originalFileName;
        this.mimeType = mimeType;
        this.temporary = temporary;
        this.fileSize = fileSize;
        this.publicPermissions = publicPermissions;

        this.newFile = null;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        this.newFile = null;

        try {
            /*
             * The file metainformation is stored in a Hibernate entity, the
             * actual file contents is handled by StorageProvider.
             */
            newFile = new File();
            newFile.setCreationDate(DecidrGlobals.getTime().getTime());
            newFile.setFileName(originalFileName);
            newFile.setFileSizeBytes(fileSize);
            newFile.setMayPublicDelete(publicPermissions
                    .contains(FileDeletePermission.class));
            newFile.setMayPublicRead(publicPermissions
                    .contains(FileReadPermission.class));
            newFile.setMayPublicReplace(publicPermissions
                    .contains(FileReplacePermission.class));
            newFile.setMimeType(mimeType);
            newFile.setIsTemporary(temporary);

            evt.getSession().save(newFile);

            StorageProvider storage = StorageProviderFactory
                    .getDefaultFactory().getStorageProvider();

            storage.putFile(contents, newFile.getId(), fileSize);
        } catch (Exception e) {
            newFile = null;
            if (e instanceof TransactionException) {
                throw (TransactionException) e;
            } else {
                throw new TransactionException(e);
            }
        }
    }

    /**
     * @return the newly persisted file entity or null if no file was created.
     */
    public File getFile() {
        return this.newFile;
    }

}
