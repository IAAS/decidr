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

package de.decidr.model.permissions.asserters;

import org.hibernate.Query;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Asserter;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.SuperAdminRole;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Asserts that the given super admin is actually the super admin.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class IsSuperAdmin implements Asserter, TransactionalCommand {

    private Long userid = null;

    Boolean isSuperAdmin = false;

    @Override
    public Boolean assertRule(Role role, Permission permission)
            throws TransactionException {
        Boolean result = false;

        if (role instanceof SuperAdminRole) {
            userid = role.getActorId();
            HibernateTransactionCoordinator.getInstance().runTransaction(this);
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt)
            throws TransactionException {
        isSuperAdmin = false;

        Query q = evt.getSession().createQuery("from SystemSettings");
        SystemSettings settings = (SystemSettings) q.uniqueResult();

        if (settings == null) {
            throw new EntityNotFoundException(SystemSettings.class);
        }

        User superAdmin = settings.getSuperAdmin();

        isSuperAdmin = (superAdmin != null) && (superAdmin.getId() == userid);
    }

    @Override
    public void transactionAborted(TransactionAbortedEvent evt)
            throws TransactionException {
        // TODO
    }

    @Override
    public void transactionCommitted(TransactionEvent evt)
            throws TransactionException {
        // TODO
    }

}