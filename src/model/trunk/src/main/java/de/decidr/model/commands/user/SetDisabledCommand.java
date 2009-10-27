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

package de.decidr.model.commands.user;

import java.util.Date;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Enables or disables a user account by setting the disabled date. Disabling
 * the super admin account has no effect. Setting the disabled date of a
 * non-existing user has no effect and does not cause an exception.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class SetDisabledCommand extends UserCommand {

    private Date disabledSince;

    /**
     * Creates a new SetDisabledCommand that disables or enables the user
     * account identified by userId. Disabling the super admin account has no
     * effect.
     * 
     * @param role
     *            user / system executing the command.
     * @param userId
     *            ID of user account to disable / enable.
     * @param date
     *            set to any non-null value to disable the user account, null to
     *            enable the user account.
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>.
     */
    public SetDisabledCommand(Role role, Long userId, Date disabledSince) {
        super(role, userId);
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null.");
        }
        this.disabledSince = disabledSince;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        String hql = "update User set disabledSince = :disabledSince "
                + "where id = :userId";

        if (disabledSince != null) {
            hql += " and not exists(from SystemSettings s where s.superAdmin.id = :userId)";
        }

        evt.getSession().createQuery(hql).setDate("disabledSince",
                disabledSince).setLong("userId", getUserId()).executeUpdate();
    }
}
