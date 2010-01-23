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

package de.decidr.model.commands;

import java.awt.event.ActionListener;

import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * The TransactionalCommand is very similar to the AWT {@link ActionListener}.
 * It acts as a receiver for events that signal
 * <ul>
 * <li>the beginning of a transaction</li>
 * <li>the (successful) end of a transaction</li>
 * <li>the rollback of a transaction</li>.
 * </ul>
 * <p>
 * All database access should take place within commands.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public interface TransactionalCommand {
    /**
     * Called after the transaction has been rolled back, the command may use
     * this method to react to a rollback (e.g. by logging the exception that
     * occurred).
     * 
     * @param evt
     *            abort event data
     * 
     */
    public void transactionAborted(TransactionAbortedEvent evt)
            throws TransactionException;

    /**
     * Called after the transaction has been successfully committed and the
     * changes to the database have been made durable / visible to other
     * transactions.
     * 
     * @param evt
     *            commit event data
     */
    public void transactionCommitted(TransactionEvent evt)
            throws TransactionException;

    /**
     * Called right after the transaction has been started by the transaction
     * coordinator, this is where the command may manipulate the database. To do
     * so, the event object provides the current Hibernate Session object. If
     * this method throws an exception, the transaction is rolled back.
     * 
     * @param evt
     *            started event data
     * 
     */
    public void transactionStarted(TransactionStartedEvent evt)
            throws TransactionException;
}