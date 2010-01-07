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

import org.apache.log4j.Level;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.logging.LogQueue;

/**
 * Invokes {@link TransactionalCommand}s within a Hibernate transaction. Closed
 * nested transactions are supported by giving up the durability property of all
 * inner transactions.
 * <p>
 * The HibernateTransactionCoordinator is implemented as a thread-local
 * "pseudo singleton" (see {@link ThreadLocal}. The freshly initialized instance
 * uses a default configuration which it reads from the first
 * "hibernate.cfg.xml" that it finds in the classpath. You can change the
 * configuration at any time using the <code>setConfiguration</code> method.
 * 
 * @author Daniel Huss
 * @author Markus Fischer
 * @version 0.1
 */
public class HibernateTransactionCoordinator implements TransactionCoordinator {

    /**
     * The thread-local instances.
     */
    private static ThreadLocal<HibernateTransactionCoordinator> instance;

    /**
     * The current Hibernate session.
     */
    private Session session = null;

    /**
     * Hibernate session factory.
     */
    private SessionFactory sessionFactory;

    /**
     * Due to a cyclic dependency on DefaultLogger, we have to use the
     * {@link LogQueue}.
     */
    private LogQueue logger = null;

    /**
     * The current Hibernate transaction. Inner transactions are executed within
     * the context of a single Hibernate transaction.
     */
    private Transaction currentTransaction = null;

    /**
     * The current transaction depth.
     */
    private Integer transactionDepth = 0;

    /**
     * The current Hibernate configuration.
     */
    private Configuration configuration;

    /**
     * @return the thread-local instance.
     */
    public static HibernateTransactionCoordinator getInstance() {
        if (instance == null) {
            instance = new ThreadLocal<HibernateTransactionCoordinator>() {
                @Override
                protected HibernateTransactionCoordinator initialValue() {
                    return new HibernateTransactionCoordinator();
                }
            };
        }

        return instance.get();
    }

    /**
     * Creates the HibernateTransactionCoordinator thread-local instance using
     * the default configuration.
     */
    private HibernateTransactionCoordinator() {
        logger = new LogQueue(HibernateTransactionCoordinator.class);
        logger.log(Level.DEBUG, "Creating HibernateTransactionCoordinator"
                + " thread-local instance.");
        this.setConfiguration(new Configuration().configure());
        logger.log(Level.DEBUG,
                "Initial Hibernate configuration successfully applied.");
        this.currentTransaction = null;
        this.transactionDepth = 0;
        this.session = null;
        logger.makeReady();
    }

    /**
     * Starts a new transaction. If the new transaction is an inner transaction,
     * the existing outer transaction is reused.
     */
    protected void beginTransaction() {
        logger.log(Level.DEBUG,
                "Beginning transaction. New transaction depth: "
                        + (transactionDepth + 1));
        if (transactionDepth == 0) {
            session = sessionFactory.openSession();
            currentTransaction = session.beginTransaction();
        }

        transactionDepth++;
    }

    /**
     * Commits the current transaction.
     * 
     * @throws TransactionException
     *             if no transaction has been started, yet.
     */
    protected void commitCurrentTransaction() throws TransactionException {
        String logMessage = transactionDepth == 1 ? "Committing transaction."
                : "Delaying commit until the outmost transaction commits.";

        logger.log(Level.DEBUG, logMessage + " Current transaction depth: "
                + transactionDepth);

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
     * Performs a rollback for the current transaction.
     * 
     * @throws TransactionException
     *             if no transaction has been started, yet.
     */
    protected void rollbackCurrentTransaction() throws TransactionException {
        logger.log(Level.DEBUG,
                "Aborting transaction. Current transaction depth: "
                        + transactionDepth);

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
     * @return the current Hibernate session or <code>null</code> if no session
     *         has been opened yet. The returned session may have been closed
     *         using the close() method, and therefore is not necessarily open.<br>
     */
    public Session getCurrentSession() {
        return session;
    }

    /**
     * Updates the session factory using the given configuration. Currently
     * running transactions are not affected by the new configuration. The new
     * configuration will be applied the next time a session is opened. A new
     * session is opened every time a top-level transaction is started.
     * 
     * @param config
     *            the initialized configuration
     */
    public void setConfiguration(Configuration config) {
        logger.log(Level.DEBUG, "Setting new Hibernate configuration.");
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
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * {@inheritDoc}
     */
    public void runTransaction(
            Collection<? extends TransactionalCommand> commands)
            throws TransactionException {
        if (commands == null) {
            throw new TransactionException(new IllegalArgumentException(
                    "Commands collection must not be null."));
        }

        TransactionalCommand[] emptyArray = new TransactionalCommand[0];
        commands.toArray(emptyArray);

        runTransaction(commands.toArray(emptyArray));
    }

    /**
     * {@inheritDoc}
     */
    public void runTransaction(TransactionalCommand... commands)
            throws TransactionException {

        // check parameters
        if (commands == null) {
            throw new TransactionException(new IllegalArgumentException(
                    "Command(s) must not be null."));
        }

        if (commands.length == 0) {
            throw new TransactionException(new IllegalArgumentException(
                    "Must supply at least one command to execute."));
        }

        for (TransactionalCommand c : commands) {
            if (c == null) {
                throw new TransactionException(new IllegalArgumentException(
                        "Command(s) must not be null."));
            }
        }

        ArrayList<TransactionalCommand> notifiedReceivers = new ArrayList<TransactionalCommand>();

        try {
            beginTransaction();

            for (TransactionalCommand c : commands) {
                notifiedReceivers.add(c);
                logger.log(Level.DEBUG, "Attempting to execute "
                        + c.getClass().getSimpleName());
                fireTransactionStarted(c);
            }

            commitCurrentTransaction();

        } catch (Exception e) {
            try {
                logger.log(Level.INFO, "Exception in transactionStarted: ", e);

                if (currentTransaction != null) {
                    rollbackCurrentTransaction();

                    for (TransactionalCommand c : notifiedReceivers) {
                        /*
                         * Exceptions thrown in transactionAborted must be
                         * ignored to give all commands a chance to react to the
                         * rollback.
                         */
                        try {
                            fireTransactionAborted(c, e);
                        } catch (Exception receiverRollbackException) {
                            logger.log(Level.WARN,
                                    "Exception during transactionAborted",
                                    receiverRollbackException);
                        }
                    }
                    notifiedReceivers.clear();
                }
            } catch (Exception rollbackException) {
                logger.log(Level.FATAL, "Could not roll back transaction.",
                        rollbackException);
            }

            if (e instanceof TransactionException) {
                throw (TransactionException) e;
            } else {
                throw new TransactionException(e);
            }
        }

        // this code can only be reached if no exception occurred during the
        // transaction and thus the transaction succeeded.
        for (TransactionalCommand command : notifiedReceivers) {
            // note: the chain is broken if one of the commands throws an
            // exception in its transactionCommitted() method.
            fireTransactionCommitted(command);
        }
        notifiedReceivers.clear();
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

    /**
     * Fires transaction committed event.
     * 
     * @param receiver
     *            the receiver of the "transaction committed" event
     * @throws TransactionException
     *             the only checked exception that is allowed to occur within
     *             the "transaction committed" event
     */
    private void fireTransactionCommitted(TransactionalCommand receiver)
            throws TransactionException {
        TransactionEvent event = new TransactionEvent(session,
                transactionDepth > 1);
        receiver.transactionCommitted(event);
    }
}