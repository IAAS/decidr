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

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.logging.DefaultLogger;

/**
 * Invokes {@link TransactionalCommand}s within a hibernate transaction. Inner
 * transactions are supported by giving up the durability property of all inner
 * transactions.
 * <p>
 * The HibernateTransactionCoordinator is implemented as a singleton. The
 * freshly initialized instance uses a default configuration which it reads from
 * the first "hibernate.cfg.xml" that it finds in the classpath. You can change
 * the configuration at any time using the <code>setConfiguration</code> method.
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
     * A list of commands whose transactionStarted() method has been called and
     * should therefore be notified of the transactionAborted() and
     * transactionCommitted() events.
     * 
     * It is intended that a single command instance can be present more than
     * once in a command chain and the expected behavior in this case is to
     * receive multiple events as well.
     */
    private ArrayList<TransactionalCommand> notifiedReceivers = null;

    /**
     * The current Hibernate configuration.
     */
    private Configuration configuration;

    /**
     * @return the singleton instance.
     */
    public static HibernateTransactionCoordinator getInstance() {
        return instance;
    }

    /**
     * Creates the HibernateTransactionCoordinator singleton instance using the
     * default configuration.
     */
    private HibernateTransactionCoordinator() {
        super();
        this.setConfiguration(new Configuration().configure());
        this.currentTransaction = null;
        this.transactionDepth = 0;
        this.session = null;
        this.notifiedReceivers = new ArrayList<TransactionalCommand>();
    }

    /**
     * Starts a new transaction. If the new transaction is an inner transaction,
     * the existing outer transaction is reused.
     */
    protected synchronized void beginTransaction() {

        if (transactionDepth == 0) {
            session = sessionFactory.openSession();
            currentTransaction = session.beginTransaction();
        }

        transactionDepth++;
    }

    /**
     * Commits the current transaction.
     */
    protected void commitCurrentTransaction() throws TransactionException {

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
    protected void rollbackCurrentTransaction() throws TransactionException {

        if (currentTransaction == null) {
            throw new TransactionException(
                    "Transaction must be started before rolling back.");
        } else {
            if (transactionDepth > 0) {
                currentTransaction.rollback();
                session.close();
                transactionDepth = 0;
            }
        }
    }

    /**
     * @return the current Hibernate session or null if no session has been
     *         opened yet. The returned session is not necessarily open.
     */
    public Session getCurrentSession() {
        return session;
    }

    /**
     * Updates the session factory using the given configuration. Currently
     * runnning transactions are not affected by the new configuration. The new
     * configuration will be applied the next time a session is opened.
     * 
     * @param config the initialized configuration
     */
    // RR new method, please add JUnit test case
    public void setConfiguration(Configuration config) {
        if (config == null) {
            throw new IllegalArgumentException(
                    "Hibernate config cannot be null");
        }
        this.sessionFactory = config.buildSessionFactory();
        configuration = config;
    }

    /**
     * Returns the currently used Hibernate configuration. Please note that
     * changes to the {@link Configuration} object do not affect this
     * transaction coordinator. To apply new settings, you'll have to use the
     * setConfiguration method.
     * 
     * @return the currently used Hibernate configuration.
     */
    // RR new method, please add JUnit test case
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * {@inheritDoc}
     */
    public void runTransaction(TransactionalCommand command)
            throws TransactionException {
        TransactionalCommand[] commands = { command };
        runTransaction(commands);
    }

    /**
     * {@inheritDoc}
     */
    public void runTransaction(
            Collection<? extends TransactionalCommand> commands)
            throws TransactionException {
        runTransaction((TransactionalCommand[]) commands.toArray());
    }

    /**
     * {@inheritDoc}
     */
    public void runTransaction(TransactionalCommand... commands)
            throws TransactionException {
        if (commands == null) {
            throw new TransactionException(new IllegalArgumentException(
                    "Null value not allowed."));
        }

        notifiedReceivers.clear();

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

                    for (TransactionalCommand c : notifiedReceivers) {
                        /*
                         * Exceptions thrown in transactionAborted must be
                         * ignored to give all commands a chance to react to the
                         * rollback, but will be logged.
                         */
                        try {
                            fireTransactionAborted(c, e);
                        } catch (Exception receiverRollbackException) {
                            logger.warn("Exception during transactionAborted",
                                    receiverRollbackException);
                        }
                    }
                    notifiedReceivers.clear();
                }
            } catch (Exception rollbackException) {
                logger.fatal("Could not roll back transaction.",
                        rollbackException);
            }

            throw new TransactionException(e);
        }
    }

    /**
     * Fires transaction started event.
     * 
     * @param receiver
     *            the receiver of the "transaction started" event.
     */
    private void fireTransactionStarted(TransactionalCommand receiver)
            throws TransactionException {
        TransactionEvent event = new TransactionEvent(session,
                transactionDepth > 1);
        receiver.transactionStarted(event);
        notifiedReceivers.add(receiver);
    }

    /**
     * Fires transaction aborted event.
     * 
     * @param receiver
     *            the receiver of the "transaction aborted" event
     * @param caughtException
     *            the exception that caused the rollback.
     */
    private void fireTransactionAborted(TransactionalCommand receiver,
            Exception caughtException) throws TransactionException {
        TransactionAbortedEvent event = new TransactionAbortedEvent(session,
                caughtException, transactionDepth > 1);
        receiver.transactionAborted(event);
    }
}