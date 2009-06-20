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

import org.hibernate.Session;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.entities.File;
import de.decidr.model.entities.UserHasFileAccess;
import de.decidr.model.entities.UserHasFileAccessId;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Asserter;
import de.decidr.model.permissions.FileDeletePermission;
import de.decidr.model.permissions.FilePermission;
import de.decidr.model.permissions.FileReadPermission;
import de.decidr.model.permissions.FileReplacePermission;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.UserRole;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Asserts that the given user may read, replace or delete the given file.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class UserHasAccessToFile implements Asserter, TransactionalCommand {

    private Long userId = null;
    private Long fileId = null;
    private FilePermission permission;

    private Boolean hasFileAccess = false;

    @Override
    public Boolean assertRule(Role role, Permission permission) throws TransactionException{
        Boolean result = false;

        if ((role instanceof UserRole)
                && (permission instanceof FilePermission)) {

            userId = role.getActorId();
            fileId = ((FilePermission) permission).getId();

            HibernateTransactionCoordinator.getInstance().runTransaction(this);

            result = hasFileAccess;
        }

        return result;
    }

    @Override
    public void transactionStarted(TransactionEvent evt) {
        hasFileAccess = false;
        Session session = evt.getSession();
        File file = (File) session.get(File.class, fileId);

        if (file != null) {
            // The file exists, so far so good.

            UserHasFileAccess access = (UserHasFileAccess) session.get(
                    UserHasAccessToFile.class, new UserHasFileAccessId(userId,
                            fileId));

            if (permission instanceof FileReadPermission) {
                hasFileAccess = file.isMayPublicRead();
                if ((!hasFileAccess) && (access != null)) {
                    hasFileAccess = access.isMayRead();
                }
            } else if ((permission instanceof FileReplacePermission)
                    && (access != null)) {
                hasFileAccess = access.isMayReplace();
            } else if ((permission instanceof FileDeletePermission)
                    && (access != null)) {
                hasFileAccess = access.isMayDelete();
            } else {
                hasFileAccess = false;
            }
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
