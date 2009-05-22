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

import org.hibernate.Session;

/**
 * Base class for all events fired by the transaction coordinator.
 * 
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class TransactionEvent {
    /**
     * The current hibernate session.
     */
    protected Session session;
    
    /**
     * Is the current transaction an inner transaction?
     */
    protected Boolean innerTransaction;

    /**
     * Constructor.
     *
     * @param session the current hibernate session.
     */
    public TransactionEvent(Session session, Boolean innertransaction) {
        this.session = session;
        this.innerTransaction = innertransaction;
    }

    /**
     * @return the current hibernate session.
     */
    public Session getSession() {
        return this.session;
    }

    /**
     * @return true iff the current transaction is an inner transaction.
     */
    public Boolean isInnerTransaction() {
        return this.innerTransaction;
    }
}