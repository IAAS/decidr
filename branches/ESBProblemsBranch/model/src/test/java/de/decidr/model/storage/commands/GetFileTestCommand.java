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

import java.io.InputStream;

import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.HibernateEntityStorageProvider;
import de.decidr.model.transactions.TransactionEvent;

public class GetFileTestCommand extends AbstractTransactionalCommand {

    Long fId;
    HibernateEntityStorageProvider storageProvider;
    InputStream resultStream;

    public GetFileTestCommand(Long fileId,
            HibernateEntityStorageProvider provider) {
        fId = fileId;
        storageProvider = provider;
    }

    public InputStream getResultStrem() {
        return resultStream;
    }

    @Override
    public void transactionStarted(TransactionEvent evt)
            throws TransactionException {
        try {
            resultStream = storageProvider.getFile(fId);
        } catch (Exception e) {
            throw new TransactionException(e);
        }
    }
}
