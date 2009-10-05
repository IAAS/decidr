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

import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.File;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.StorageProviderFactory;
import de.decidr.model.transactions.TransactionEvent;

/**
 * @author Daniel Huss
 * @version 0.1
 */
public class CreateFileCommand extends AclEnabledCommand {

    private InputStream contents;
    private String originalFileName;
    private String mimeType;
    private Boolean temporary;

    private File newFile;

    /**
     * Creates a new CreateFileCommand that will store a new file using the
     * default storage provider.
     * 
     * @param role
     *            user / system executing the command
     * @param contents
     *            file contents
     * @param originalFileName
     *            original file name as reported by the uploader's user agent
     *            (browser).
     * @param mimeType
     *            mime type as reported by the uploader's user agent (browser).
     * @param temporary
     *            whether the file is a temporary file that is subject to
     *            deletion if it is not made permanent within a certain time
     *            limit.
     */
    public CreateFileCommand(Role role, InputStream contents,
            String originalFileName, String mimeType, Boolean temporary) {
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

        this.contents = contents;
        this.originalFileName = originalFileName;
        this.mimeType = mimeType;
        this.temporary = temporary;

        this.newFile = null;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        this.newFile = null;

    }

    /**
     * @return the newly persisted file entity or null if no file was created.
     */
    public File getFile() {
        return this.newFile;
    }

}
