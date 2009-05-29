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

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.logging.DefaultLogger;

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
    private static final HibernateTransactionCoordinator instance = new HibernateTransactionCoordinator();

    private static Logger logger = DefaultLogger
            .getLogger(HibernateTransactionCoordinator.class);

    /**
     * The current Hibernate session.
     */
    private Session session = null;

    /**
     * Hibernate session factory.
     */
    private SessionFactory sessionFactory;

    /**
     * The current Hibernate transaction. Inner transactions are executed within
     * the context of a single Hibernate transaction.
     */
    private Transaction currentTransaction;

    /**
     * The current transaction depth.
     */
    private Integer transactionDepth;

    /**
     * Constructor.
     */
    private HibernateTransactionCoordinator() {
        super();
        this.sessionFactory = new Configuration().configure()
                .buildSessionFactory();
        this.currentTransaction = null;
        this.transactionDepth = 0;
    }

    /**
     * Starts a new transaction. If the new transaction is an inner transaction,
     * the existing outer transaction is reused.
     */
    protected void beginTransaction() {

        if (transactionDepth == 0) {
            session = sessionFactory.openSession();
            currentTransaction = session.beginTransaction();
        }

        transactionDepth++;
    }

    /**
     * 
     * Commits the current transaction.
     * 
     */
    protected void commitCurrentTransaction() {

        if (currentTransaction == null) {
            throw new TransactionException(
                    "Transaction must be started before commit.");
        } else {
            if (transactionDepth == 1) {
                currentTransaction.commit();
                session.flush();
                session.close();
                transactionDepth = 0;
            } else if (transactionDepth > 0) {
                transactionDepth--;
            }
        }
    }

    /**
     * Rolls the current transaction back.
     */
    protected void rollbackCurrentTransaction() {

        if (currentTransaction == null) {
            throw new TransactionException(
                    "Transaction must be started before rolling back.");
        } else {
            if (transactionDepth > 0) {
                currentTransaction.rollback();
                session.flush();
                session.close();
                transactionDepth = 0;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void runTransaction(TransactionalCommand command) {
        TransactionalCommand[] commands = { command };
        runTransaction(commands);
    }

    /**
     * {@inheritDoc}
     */
    public void runTransaction(TransactionalCommand[] commands) {
        if (commands == null) {
            throw new IllegalArgumentException("Null value not allowed.");
        }

        try {
            beginTransaction();

            for (TransactionalCommand c : commands) {
                fireTransactionStarted(c);
            }

            commitCurrentTransaction();

        } catch (Exception e) {

            try {
                if (currentTransaction != null) {
                    rollbackCurrentTransaction();

                    for (TransactionalCommand c : commands) {
                        fireTransactionAborted(c, e);
                    }
                }
            } catch (Exception e2) {
                logger.fatal("Rollback failed", e2);
            }

            throw new TransactionException(e);
        }
    }

    /**
     * 
     * Fires transaction started event.
     * 
     * @param receiver
     * 
     */
    private void fireTransactionStarted(TransactionalCommand receiver) {
        TransactionEvent event = new TransactionEvent(session,
                transactionDepth > 1);
        receiver.transactionStarted(event);
    }

    /**
     * 
     * Fires transaction aborted event.
     * 
     * @param receiver
     * @param caughtException
     * 
     */
    private void fireTransactionAborted(TransactionalCommand receiver,
            Exception caughtException) {
        TransactionAbortedEvent event = new TransactionAbortedEvent(session,
                caughtException, transactionDepth > 1);
        receiver.transactionAborted(event);
    }

    /**
     * @return the singleton instance.
     */
    public static TransactionCoordinator getInstance() {
        return instance;
    }
}