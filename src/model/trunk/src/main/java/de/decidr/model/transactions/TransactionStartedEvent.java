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

package de.decidr.model.transactions;

import org.hibernate.Session;

/**
 * Event fired after a transaction has been started. This event provides an open
 * Hibernate session.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class TransactionStartedEvent extends TransactionEvent {
    /**
     * The current hibernate session.
     */
    protected Session session;

    /**
     * Creates a new {@link TransactionStartedEvent}.
     * 
     * @param innerTransaction
     *            whether this event is fired for an inner transaction.
     * @param source
     *            the invoker of this event.
     * @param open
     *            Hibernate session.
     */
    public TransactionStartedEvent(boolean innerTransaction,
            HibernateTransactionCoordinator source, Session session) {
        super(innerTransaction, source);
        this.session = session;
    }

    /**
     * @return the open Hibernate session.
     */
    public Session getSession() {
        return this.session;
    }
}