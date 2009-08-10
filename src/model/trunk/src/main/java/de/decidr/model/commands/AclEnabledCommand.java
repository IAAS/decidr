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

package de.decidr.model.commands;

import java.util.ArrayList;
import java.util.Collection;

import de.decidr.model.acl.AccessControlList;
import de.decidr.model.acl.DefaultAccessControlList;
import de.decidr.model.acl.permissions.CommandPermission;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.exceptions.AccessDeniedException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Abstract base class for all commands that require permission checks in order
 * to be executed. The default ACL is used to determine whether or not the
 * command may be executed.
 * 
 * Commands derived from AclEnabledCommand must overwrite the transactionAllowed
 * Method instead of transactionStarted.
 * 
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public abstract class AclEnabledCommand extends AbstractTransactionalCommand {
    /**
     * The user/system that is requesting to execute this action.
     */
    protected Role role;

    /**
     * Permissions that are being requested besides the command itself.
     */
    protected ArrayList<Permission> additionalPermissions = new ArrayList<Permission>(
            0);

    /**
     * Creates a new AclEnabledCommand using the given role and permissions.
     * 
     * @param role
     *            user/system that is requesting access to this command and the
     *            given permissions
     * @param additionalPermissions
     *            {@link Permission Permissions} besides the command that are
     *            involved in the transaction.
     */
    public AclEnabledCommand(Role role,
            Collection<Permission> additionalPermissions) {
        super();
        if (additionalPermissions != null) {
            additionalPermissions.addAll(additionalPermissions);
        }
    }

    /**
     * Creates a new AclEnabledCommand using the given role and permission.
     * 
     * @param role
     *            user/system that is requesting access to this command and the
     *            given permissions
     * @param additionalPermission
     *            {@link Permission} besides the command that is involved in the
     *            transaction.
     */
    public AclEnabledCommand(Role role, Permission additionalPermission) {
        this(role, (Collection<Permission>) null);
        this.additionalPermissions.add(additionalPermission);
    }

    /**
     * Performs the necessary permission checks, then forwards to
     * transactionAllowed iff the ACL grants access to this command and all
     * specified additional permissions.
     */
    @Override
    public final void transactionStarted(TransactionEvent evt)
            throws TransactionException {
        AccessControlList acl = DefaultAccessControlList.getInstance();

        Boolean mayExecute = acl.isAllowed(role, new CommandPermission(this));

        for (Permission p : additionalPermissions) {
            if (mayExecute) {
                mayExecute = acl.isAllowed(role, p);
            } else {
                break;
            }
        }

        if (mayExecute) {
            this.transactionAllowed(evt);
        } else {
            String roleName = role == null ? "null" : role.getClass().getName();
            StringBuffer permissions = new StringBuffer();
            permissions.append(this.getClass().getName());

            for (Permission p : additionalPermissions) {
                permissions.append(", ");
                permissions.append(p.getName());
            }

            throw new AccessDeniedException(String.format(
                    "Permission check failed for actor %1$s, permissions %2$s",
                    roleName, permissions.toString()));
        }
    }

    /**
     * Event fired iff the transaction has been allowed by the ACL. Place your
     * database access code here.
     * 
     * @param evt
     *            forwarded transaction event
     */
    public abstract void transactionAllowed(TransactionEvent evt)
            throws TransactionException;

    /**
     * @return the additional permissions
     */
    public ArrayList<Permission> getAdditionalPermissions() {
        return additionalPermissions;
    }

    /**
     * @param additionalPermissions
     *            the additional permissions to set
     */
    public void setAdditionalPermissions(
            ArrayList<Permission> additionalPermissions) {
        this.additionalPermissions = additionalPermissions;
    }
}