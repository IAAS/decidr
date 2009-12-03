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

import de.decidr.model.acl.permissions.FileDeletePermission;
import de.decidr.model.acl.permissions.FilePermission;
import de.decidr.model.acl.permissions.FileReadPermission;
import de.decidr.model.acl.permissions.FileReplacePermission;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.entities.File;
import de.decidr.model.entities.UserHasFileAccess;
import de.decidr.model.entities.UserHasFileAccessId;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Asserts that a given user or subsystem may read, replace or delete a given
 * file.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class FileAccessAsserter implements Asserter, TransactionalCommand {

    private FilePermission permission = null;
    private Role role = null;
    private File file = null;

    private Session session = null;

    private Boolean hasFileAccess = false;

    @Override
    public Boolean assertRule(Role role, Permission permission)
            throws TransactionException {
        Boolean result = false;
        this.role = role;

        if (permission instanceof FilePermission) {
            this.permission = (FilePermission) permission;
            HibernateTransactionCoordinator.getInstance().runTransaction(this);
            result = hasFileAccess;
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        session = evt.getSession();
        hasFileAccess = false;

        file = (File) session.get(File.class, permission.getId());
        if (file == null || file.getId() == null) {
            // Nobody has access to non-existing files.
            return;
        }

        if (role instanceof UserRole) {
            assertUserHasAccess();
        } else {
            assertAnyoneHasAccess();
        }
    }

    /**
     * Checks whether the file is publicy accessible.
     */
    protected void assertAnyoneHasAccess() {
        if (permission instanceof FileReadPermission) {
            hasFileAccess = file.isMayPublicRead();
        } else if (permission instanceof FileReplacePermission) {
            hasFileAccess = file.isMayPublicReplace();
        } else if (permission instanceof FileDeletePermission) {
            hasFileAccess = file.isMayPublicDelete();
        } else {
            hasFileAccess = false;
        }
    }

    /**
     * Checks whether a user has access to the file, assuming that this.role is
     * a {@link UserRole}.
     * <p>
     * This succeeds if the file is publicly accessible or if a
     * {@link UserHasFileAccess} entity specifiying access exists and grants
     * access.
     */
    protected void assertUserHasAccess() {
        UserHasFileAccess access = (UserHasFileAccess) session.get(
                UserHasFileAccess.class, new UserHasFileAccessId(role
                        .getActorId(), file.getId()));

        if (permission instanceof FileReadPermission) {
            hasFileAccess = file.isMayPublicRead();
            if (!hasFileAccess && access != null) {
                hasFileAccess = access.isMayRead();
            }
        } else if (permission instanceof FileReplacePermission) {
            hasFileAccess = file.isMayPublicReplace();
            if (!hasFileAccess && access != null) {
                hasFileAccess = access.isMayReplace();
            }
        } else if (permission instanceof FileDeletePermission) {
            hasFileAccess = file.isMayPublicDelete();
            if (!hasFileAccess && access != null) {
                hasFileAccess = access.isMayDelete();
            }
        } else {
            hasFileAccess = false;
        }
    }

    @Override
    public void transactionAborted(TransactionAbortedEvent evt) {
        // nothing to do
    }

    @Override
    public void transactionCommitted(TransactionEvent evt) {
        // nothing to do
    }

}
