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

package de.decidr.model.acl.asserters;

import org.hibernate.Session;

import de.decidr.model.acl.permissions.FilePermission;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.entities.File;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Checks the following implicit file permissions:
 * <ul>
 * <li>Workflow administrators have implicit access to files associated with
 * administered work items.</li>
 * <li>XXX DH extension case</li>
 * </ul>
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class ImplicitFileAccessAsserter extends AbstractTransactionalCommand
        implements Asserter {

    Boolean implicitAccess = false;

    Long userId = null;
    FilePermission filePermission = null;
    File file = null;

    Session session = null;

    @Override
    public Boolean assertRule(Role role, Permission permission)
            throws TransactionException {

        Boolean result = false;

        if (role instanceof UserRole && role.getActorId() != null
                && permission instanceof FilePermission) {

            userId = role.getActorId();
            filePermission = (FilePermission) permission;

            HibernateTransactionCoordinator.getInstance().runTransaction(this);
            result = implicitAccess;
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt)
            throws TransactionException {
        implicitAccess = false;

        session = evt.getSession();

        file = (File) evt.getSession().get(File.class, filePermission.getId());
        if (file == null) {
            // cannot grant access to a non-existing file.
            return;
        }

        checkWorkflowAdminAccess();
    }

    /**
     * Sets implicitAccess to true if the file has been uploaded in the context
     * of a work item that is administered by the user in the role given to
     * assertRole.
     */
    private void checkWorkflowAdminAccess() {
        // is the user a workflow admin?
        String hql = "select f.id from File "
                + "inner join f.workItemContainsFiles as containsRel "
                + "inner join containsRel.workflowInstance as wfi "
                + "inner join wfi.userAdministratesWorkflowInstance as adminRel "
                + "with (adminRel.user.id = :userId";

        Object found = session.createQuery(hql).setLong("userId", userId)
                .setMaxResults(1).uniqueResult();

        if (found == null) {
            // is the user a tenant admin?
            hql = "select f.id from File "
                    + "inner join f.workItemContainsFiles as containsRel "
                    + "inner join containsRel.workflowInstance as wfi "
                    + "inner join wfi.deployedWorkflowModel as dwm "
                    + "inner join dwm.tenant as t with t.admin.id = :userId";

            found = session.createQuery(hql).setLong("userId", userId)
                    .setMaxResults(1).uniqueResult();
        }

        if (found != null) {
            implicitAccess = true;
        }
    }
}
