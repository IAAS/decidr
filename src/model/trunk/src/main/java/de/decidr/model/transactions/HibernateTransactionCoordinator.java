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

import de.decidr.model.commands.TransactionalCommand;

/**
 * Invokes {@link TransactionalCommand}s within a transaction. Inner
 * transactions are supported.
 * 
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class HibernateTransactionCoordinator implements TransactionCoordinator {
    protected static TransactionCoordinator instance;
    protected SessionFactory sessionFactory;
    protected Transaction currentTransaction;
    protected Integer innerTransactionCount;

    private HibernateTransactionCoordinator() {
        throw new UnsupportedOperationException();
    }

    protected void beginTransaction() {
        
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