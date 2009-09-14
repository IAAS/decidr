/*
 * The DecidR Development Team licenses this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.decidr.model.transactions;

import java.util.Collection;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;

/**
 * Invokes {@link TransactionalCommand}s within a transaction. Inner
 * transactions are supported.
 * 
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public interface TransactionCoordinator {

    /**
     * Executes a number of commands within a transaction. If another
     * transaction is already running, the new transactions become inner
     * transactions.
     * 
     * @param commands
     *            the commands to execute - must not be null or empty.
     * @throws TransactionException
     *             if transaction is not successful or commands are null or
     *             empty.
     */
    public void runTransaction(TransactionalCommand... commands)
            throws TransactionException;

    /**
     * Executes a number of commands within a transaction. If another
     * transaction is already running, the new transactions become inner
     * transactions.
     * 
     * @param commands
     *            the commands to execute - must not be null or empty.
     * @throws TransactionException
     *             if transaction is not successful or commands are null or
     *             empty.
     */
    public void runTransaction(
            Collection<? extends TransactionalCommand> commands)
            throws TransactionException;
}