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

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import de.decidr.model.commands.TransactionalCommand;

/**
 * Invokes {@link TransactionalCommand}s within a hibernate transaction. Inner
 * transactions are supported by giving up the durablility property of all inner
 * transactions.
 * 
 * @author Daniel Huss
 * @author Markus Fischer
 * @version 0.1
 */
public class HibernateTransactionCoordinator implements TransactionCoordinator {
    /**
     * The singleton instance.
     */
    protected static HibernateTransactionCoordinator instance = new HibernateTransactionCoordinator();

    /**
     * Hibernate session factory.
     */
    protected SessionFactory sessionFactory;

    /**
     * The current hibernate transaction. Inner transactions are executed within
     * the context of a single Hibernate transaction.
     */
    protected Transaction currentTransaction;

    /**
     * The current transaction depth.
     */
    protected Integer innerTransactionCount;

    /**
     * Constructor.
     */
    private HibernateTransactionCoordinator() {
        super();
        this.sessionFactory = new Configuration().configure()
                .buildSessionFactory();
        this.currentTransaction = null;
        this.innerTransactionCount = 0;
    }

    /**
     * Starts a new transaction. If the new transaction is an inner
     * transaction, the existing outer transaction is reused.
     */
    protected void beginTransaction() {
        if (this.innerTransactionCount > 0) {
            
        }
    }

    protected void commitCurrentTransaction() {
        throw new UnsupportedOperationException();
    }

    protected void rollbackCurrentTransaction() {
        throw new UnsupportedOperationException();
    }

    public void runTransaction(TransactionalCommand command) {
        throw new UnsupportedOperationException();
    }

    public void runTransaction(TransactionalCommand[] commands) {
        throw new UnsupportedOperationException();
    }

    public TransactionCoordinator getInstance() {
        throw new UnsupportedOperationException();
    }
}