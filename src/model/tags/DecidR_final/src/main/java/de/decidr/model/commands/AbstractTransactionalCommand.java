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

import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Abstract base class for commands so you don't have to implement every method
 * of {@link TransactionalCommand} in every class.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public abstract class AbstractTransactionalCommand implements
        TransactionalCommand {

    /**
     * {@inheritDoc}
     */
    public void transactionAborted(TransactionAbortedEvent evt)
            throws TransactionException {
        // intentionally left empty, the exception object in evt is just fyi.
        // The transaction coordinator will throw the exception after this
        // method has been executed.
    }

    /**
     * {@inheritDoc}
     */
    public void transactionCommitted(TransactionEvent evt)
            throws TransactionException {
        // intentionally left empty
    }

    /**
     * {@inheritDoc}
     */
    public void transactionStarted(TransactionEvent evt)
            throws TransactionException {
        // intentionally left empty
    }
}