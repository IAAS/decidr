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

package de.decidr.model.commands.system;

import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.entities.File;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.StorageProvider;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Retrieves file meta-information from the database. To retrieve the actual
 * file contents, see {@link StorageProvider}.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
// DH: how does a file's meta-info get added to the DB? ~rr
public class GetFileCommand extends AbstractTransactionalCommand {

    private Long fileId;

    private File file = null;

    /**
     * Creates a new GetFileCommand
     * 
     * @param fileId
     *            the file to retrieve
     */
    public GetFileCommand(Long fileId) {
        super();
        this.fileId = fileId;
    }

    @Override
    public void transactionStarted(TransactionEvent evt)
            throws TransactionException {
        file = (File) evt.getSession().load(File.class, fileId);
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }
}