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

package de.decidr.model.storage.commands;

import java.io.File;
import java.io.FileInputStream;

import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.HibernateEntityStorageProvider;
import de.decidr.model.transactions.TransactionEvent;

public class PutFileTestCommand extends AbstractTransactionalCommand {

    java.io.File basicFile;
    Long fId;
    HibernateEntityStorageProvider storageProvider;

    /**
     * Creates a new {@link PutFileTestCommand}
     */
    public PutFileTestCommand(Long fileId, File file,
            HibernateEntityStorageProvider provider) {

        basicFile = file;
        fId = fileId;
        storageProvider = provider;
    }

    @Override
    public void transactionStarted(TransactionEvent evt)
            throws TransactionException {

        try {
            storageProvider.putFile(new FileInputStream(basicFile
                    .getAbsolutePath()), fId, basicFile.length());
        } catch (Exception e) {
            throw new TransactionException(e);
        }
    }
}
