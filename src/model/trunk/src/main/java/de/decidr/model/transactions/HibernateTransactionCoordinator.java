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
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.logging.LogQueue;

/**
 * Invokes {@link TransactionalCommand}s within a Hibernate transaction. Inner
 * transactions are supported by giving up the durability property of all inner
 * transactions.
 * <p>
 * The HibernateTransactionCoordinator is implemented as a thread-local
 * "pseudo singleton" (see {@link ThreadLocal}. The freshly initialized instance
 * uses a default configuration which it reads from the first
 * "hibernate.cfg.xml" that it finds in the classpath. You can change the
 * configuration at any time using the <code>setConfiguration</code> method.
 * 
 * <p>
 * FIXME the {@link ThreadLocal} pattern seems to be the cause for memory leaks,
 * see http://blog.codecentric.de/en/2008/09/threadlocal-memoryleak/
 * 
 * This behaviour is unacceptable, the HTC getInstance method should be changed
 * to allow for a more robust "one-instance-per-request" mechanism (dependency
 * injection? request scope beans?). ~dh
 * 
 * <p>
 * Usage example:
 * 
 * <pre>
 * public static void main() {
 *     TransactionalCommand killCommand = new KillCommand();
 *     killCommand.setVictim(&quot;James Bond&quot;);
 *     HibernateTransactionCoordinator.getInstance().run(killCommand);
 *     if (!killCommand.victimHasEscaped()) {
 *         // world domination!
 *     }
 * }
 * </pre>
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
     * A list of commands that have received the transactionStarted event. These
     * events need to be notified of transactionCommitted iff
     * <code>transactionDepth == 0</code>
     */
    List<TransactionalCommand> notifiedReceivers = null;

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
        this.notifiedReceivers = Collections
                .synchronizedList(new ArrayList<TransactionalCommand>());
        logger.log(Level.DEBUG,
                "Initial Hibernate configuration successfully applied.");
        this.currentTransaction = null;
        this.transactionDepth = 0;
        this.session = null;
        logger.makeReady();
    }

    /**
     * Starts a new transaction. If the new transaction is an inner transaction,
     * the session of the existing outer transaction is reused.
     */
    protected void beginTransaction() {
        logger.log(Level.DEBUG,
                "Beginning transaction. New transaction depth: "
                        + (transactionDepth + 1));
        if (transactionDepth == 0) {
            notifiedReceivers.clear();
            logger
                    .log(Level.DEBUG,
                            "HibernateTransactionCoordinator: starting outmost transaction");
            session = sessionFactory.openSession();
            currentTransaction = session.beginTransaction();
        }

        transactionDepth++;
    }

    /**
     * Commits the current transaction. The current session is closed if the
     * outmost transaction is committed.
     * 
     * @throws TransactionException
     *             if no transaction has been started, yet.
     * @return result of the commit
     */
    protected CommitResult commitCurrentTransaction()
            throws TransactionException {
        String logMessage = transactionDepth == 1 ? "Committing outmost transaction."
                : "Delaying commit until the outmost transaction commits.";

        logger.log(Level.DEBUG, logMessage + " Current transaction depth: "
                + transactionDepth);

        Exception resultException = null;
        if (currentTransaction == null) {
            throw new TransactionException(
                    "Transaction must be started before commit.");
        } else {
            if (transactionDepth == 1) {
                currentTransaction.commit();
                session.flush();
                session.close();
                transactionDepth = 0;
                /*
                 * Not using foreach due to list write access in other methods.
                 */
                int size = notifiedReceivers.size();
                for (int i = 0; i < size; i++) {
                    try {
                        /*
                         * note: the chain is broken if one of the commands
                         * throws an exception in its transactionCommitted()
                         * method.
                         */
                        fireTransactionCommitted(notifiedReceivers.get(i));
                    } catch (Exception e) {
                        resultException = e;
                        break;
                    }
                }
                notifiedReceivers.clear();
            } else if (transactionDepth > 0) {
                transactionDepth--;
            }
        }
        return new CommitResult(transactionDepth == 0, resultException);
    }

    /**
     * Performs a rollback for the current transaction. All enclosing outer
     * transactions are rolled back as well.
     * 
     * @param e
     *            Exception that caused the rollback
     * @throws TransactionException
     *             if no transaction has been started, yet.
     */
    protected void rollbackCurrentTransaction(Exception e)
            throws TransactionException {
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

        /*
         * Not using foreach due to list write access in other methods.
         */
        int size = notifiedReceivers.size();
        for (int i = 0; i < size; i++) {
            /*
             * Exceptions thrown in transactionAborted must be ignored to give
             * all commands a chance to react to the rollback.
             */
            try {
                fireTransactionAborted(notifiedReceivers.get(i), e);
            } catch (Exception receiverRollbackException) {
                logger.log(Level.WARN, "Exception during transactionAborted",
                        receiverRollbackException);
            }
        }
        notifiedReceivers.clear();
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
    public CommitResult runTransaction(TransactionalCommand... commands)
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

        beginTransaction();
        for (TransactionalCommand c : commands) {
            try {
                notifiedReceivers.add(c);
                String className = c.getClass().getSimpleName();
                if (className.isEmpty()) {
                    className = "<anonymous inner class>";
                }
                logger.log(Level.DEBUG, "Attempting to execute " + className);
                fireTransactionStarted(c);
            } catch (Exception e) {
                try {
                    logger.log(Level.INFO, "Exception in transactionStarted: ",
                            e);

                    if (currentTransaction != null) {
                        rollbackCurrentTransaction(e);
                    }
                } catch (Exception rollbackEventException) {
                    logger.log(Level.FATAL, "Could not roll back transaction.",
                            rollbackEventException);
                }

                if (e instanceof TransactionException) {
                    throw (TransactionException) e;
                } else {
                    throw new TransactionException(e);
                }
            }
        }

        CommitResult commitResult = commitCurrentTransaction();

        if (commitResult.isFinalCommit()
                && commitResult.getCommittedEventException() == null) {
            logger.log(Level.DEBUG,
                    "Everything OK, no exceptions in transactionCommitted.");
        } else if (commitResult.isFinalCommit()) {
            /*
             * There was an exception in a "transaction committed" event.
             */
            logger.log(Level.DEBUG,
                    "Exception thrown in transactionCommitted.", commitResult
                            .getCommittedEventException());
        }

        return commitResult;
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