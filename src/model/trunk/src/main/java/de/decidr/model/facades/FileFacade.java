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

package de.decidr.model.facades;

import java.io.InputStream;
import java.util.Set;

import de.decidr.model.acl.permissions.FileDeletePermission;
import de.decidr.model.acl.permissions.FilePermission;
import de.decidr.model.acl.permissions.FileReadPermission;
import de.decidr.model.acl.permissions.FileReplacePermission;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.AllowedRole;
import de.decidr.model.commands.file.CreateFileCommand;
import de.decidr.model.commands.file.DeleteFileCommand;
import de.decidr.model.commands.file.GetFileCommand;
import de.decidr.model.commands.file.GetFileDataCommand;
import de.decidr.model.commands.file.ReplaceFileCommand;
import de.decidr.model.entities.File;
import de.decidr.model.exceptions.AccessDeniedException;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;

/**
 * Provides methods for dealing with user uploaded files. You can create,
 * retrieve, replace and delete files using this facade.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class FileFacade extends AbstractFacade {

    /**
     * Creates a new FileFacade for dealing with uploaded files.
     * 
     * @param actor
     *            user / system executing the commands.
     */
    public FileFacade(Role actor) {
        super(actor);
    }

    /***
     * Stores a new file and returns the ID of the new file. It is the caller's
     * responsibility to close the given input stream. The actor invoking this
     * action is automatically given all file rights.
     * 
     * @param contents
     *            the file contents to store. This method will read fileSize
     *            byte from this stream.
     * @param fileSize
     *            the number of bytes to read from the stream.
     * @param originalFileName
     *            the original name of the file as reported by the uploader's
     *            user agent (browser).
     * @param mimeType
     *            the mime type of the file as reported by the uploader's user
     *            agent (browser).
     * @param temporary
     *            whether this is a temporary upload that is subject to deletion
     *            after a certain time limit.
     * @param publicPermissions
     *            the inital public permissions for this file. This is is a set
     *            of {@link FileReadPermission}, {@link FileReplacePermission}
     *            and {@link FileDeletePermission}.
     * @return the ID of the new file.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws IllegalArgumentException
     *             if the file size is negative of if one of the following
     *             parameters is <code>null</code>:
     *             <ul>
     *             <li>contents</li>
     *             <li>fileSize</li>
     *             <li>originalFileName</li>
     *             <li>mimeType</li>
     *             </ul>
     */
    @AllowedRole(BasicRole.class)
    public Long createFile(InputStream contents, Long fileSize,
            String originalFileName, String mimeType, boolean temporary,
            Set<Class<? extends FilePermission>> publicPermissions)
            throws TransactionException {
        CreateFileCommand cmd = new CreateFileCommand(actor, contents,
                fileSize, originalFileName, mimeType, temporary,
                publicPermissions);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        return cmd.getFile().getId();
    }

    /**
     * Replaces an existing file with new contents, filename and mime type.
     * 
     * @param fileId
     *            the file to replace
     * @param newContents
     *            new contents of the file
     * @param fileSize
     *            number of bytes to read from the given input stream.
     * @param newFileName
     *            new file name as reported by the user agent (browser)
     * @param newMimeType
     *            new mime type as reported by the user agent (browser)
     * @throws TransactionException
     *             iff the transaction is aborted for any reason
     * @throws AccessDeniedException
     *             iff the current actor does not have permission to replace the
     *             given file.
     * @throws EntityNotFoundException
     *             iff the given file ID is unknown to the system.
     * @throws IllegalArgumentException
     *             if the file size is negative of if one one of the following
     *             parameters is <code>null</code>:
     *             <ul>
     *             <li>fileId</li>
     *             <li>newContents</li>
     *             <li>fileSize</li>
     *             <li>newFileName</li>
     *             <li>newMimeType</li>
     *             </ul>
     */
    @AllowedRole(BasicRole.class)
    public void replaceFile(Long fileId, InputStream newContents,
            Long fileSize, String newFileName, String newMimeType)
            throws TransactionException {
        ReplaceFileCommand cmd = new ReplaceFileCommand(actor, fileId,
                newContents, fileSize, newFileName, newMimeType);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
    }

    /**
     * Deletes a file and its contents. If the file does not exist, no exception
     * is thrown.
     * 
     * @param fileId
     *            the ID of the file that should be deleted.
     * @return true iff a file was deleted (false means the file didn't exist).
     * @throws TransactionException
     *             iff the transaction is aborted for any reason. Depending on
     *             the storage provider, this may lead to inconsistencies such
     *             as non-existing file contents but intact file
     *             metainformation.
     * @throws IllegalArgumentException
     *             if fileId is <code>null</code>
     */
    @AllowedRole(BasicRole.class)
    public boolean deleteFile(Long fileId) throws TransactionException {
        DeleteFileCommand cmd = new DeleteFileCommand(actor, fileId);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        return cmd.getDeletedEntities() > 0;
    }

    /**
     * Uses the default storage provider to retrieve the contents of the given
     * file. It is the caller's responsibility to close the returned input
     * stream.
     * 
     * @param fileId
     *            ID of file whose contents should be retrieved.
     * @return file contents. You must close this stream once you're done
     *         reading from it.
     * @throws TransactionException
     *             iff the file contents cannot be retrieved.
     * @throws IllegalArgumentException
     *             if fileId is <code>null</code>.
     */
    @AllowedRole(BasicRole.class)
    public InputStream getFileData(Long fileId) throws TransactionException {
        /*
         * although not every storage provider provides transactional integrity,
         * those that do must be invoked from within a TransactionalCommand.
         */
        GetFileDataCommand cmd = new GetFileDataCommand(actor, fileId);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        return cmd.getStream();
    }

    /**
     * Retrieves file metainformation for the file identified by
     * <code>fileId</code>.
     * 
     * @param fileId
     *            ID of file whose metainformation should be retrieved
     * @return file entity containing metainformation.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws EntityNotFoundException
     *             iff the file does not exist.
     * @throws IllegalArgumentException
     *             if fileId is <code>null</code>
     */
    @AllowedRole(BasicRole.class)
    public File getFileInfo(Long fileId) throws TransactionException {
        GetFileCommand cmd = new GetFileCommand(actor, fileId);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        return cmd.getFile();
    }
}
