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

import de.decidr.model.commands.TransactionalCommand;

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
     * Executes a single command within a transaction. If another transaction is
     * already running, the new transaction becomes an inner transaction.
     * 
     * @param command
     *            the command to execute.
     * @throws IllegalArgumentException
     *             if command is null.
     */
    public void runTransaction(TransactionalCommand command);

    /**
     * Executes a number of commands within a transaction. If another
     * transaction is already running, the new transaction becomes an inner
     * transaction.
     * 
     * @param commands
     *            the commands to execute
     * @throws IllegalArgumentException
     *             if command is null.
     */
    public void runTransaction(TransactionalCommand[] commands);
}