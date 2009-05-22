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

import javassist.bytecode.ExceptionsAttribute;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;

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
    private Integer innerTransactionCount;

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
     * Starts a new transaction. If the new transaction is an inner transaction,
     * the existing outer transaction is reused.
     */
    protected void beginTransaction() {

        if (innerTransactionCount == 0) {
            session = sessionFactory.openSession();
            currentTransaction = session.beginTransaction();
        }

        innerTransactionCount++;
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
            if (innerTransactionCount == 1) {
                currentTransaction.commit();
                session.flush();
                session.close();
                innerTransactionCount = 0;
            } else if (innerTransactionCount > 0) {
                innerTransactionCount--;
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
            if (innerTransactionCount > 0) {
                currentTransaction.rollback();
                session.flush();
                session.close();
                innerTransactionCount = 0;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void runTransaction(TransactionalCommand command) {
        TransactionalCommand[] commands = {command};
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
                
            }
            
        } catch (Exception e) {
            
        }
    }

    /**
     * @return the singleton instance.
     */
    public static TransactionCoordinator getInstance() {
        return instance;
    }
}