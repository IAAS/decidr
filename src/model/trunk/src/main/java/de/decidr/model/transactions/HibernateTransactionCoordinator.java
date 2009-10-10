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
        if (instance == null) {
            instance = new HibernateTransactionCoordinator();
        }
        
        return instance;
    }

    /**
     * Creates the HibernateTransactionCoordinator singleton instance using the
     * default configuration.
     */
    private HibernateTransactionCoordinator() {
        logger
                .debug("Creating HibernateTransactionCoordinator singleton instance.");
        this.setConfiguration(new Configuration().configure());
        logger.debug("Initial Hibernate configuration successfully applied.");
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
        logger.debug("Beginning transaction. Current transaction depth: "
                + transactionDepth);
        if (transactionDepth == 0) {
            session = sessionFactory.openSession();
            currentTransaction = session.beginTransaction();
        }

        transactionDepth++;
    }

    /**
     * Commits the current transaction.
     */
    protected synchronized void commitCurrentTransaction()
            throws TransactionException {

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
    protected synchronized void rollbackCurrentTransaction()
            throws TransactionException {

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
     * runnning transactions are not affected by the new configuration. The new
     * configuration will be applied the next time a session is opened. A new
     * session is opened every time a top-level transaction is started.
     * 
     * @param config
     *            the initialized configuration
     */
    public void setConfiguration(Configuration config) {
        logger.debug("Setting new Hibernate configuration.");
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

        notifiedReceivers.clear();

        try {
            beginTransaction();

            for (TransactionalCommand c : commands) {
                notifiedReceivers.add(c);
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
     */
    private void fireTransactionCommitted(TransactionalCommand receiver)
            throws TransactionException {
        TransactionEvent event = new TransactionEvent(session,
                transactionDepth > 1);
        receiver.transactionCommitted(event);
    }

    /**
     * The singleton instance. This has been moved to the bottom to make sure it
     * is the last static part of the class during initialization. Otherwise
     * accessing static parts in the contructor would be impossible.
     */
    private static HibernateTransactionCoordinator instance;
}