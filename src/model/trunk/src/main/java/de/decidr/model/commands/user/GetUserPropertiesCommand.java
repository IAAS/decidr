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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;

import de.decidr.model.acl.access.UserAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Retrieves the desired properties of one or more users.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class GetUserPropertiesCommand extends AclEnabledCommand implements
        UserAccess {

    private Set<String> propertiesToGet;
    private Set<Long> userIds;
    private List<User> users = new ArrayList<User>(0);

    /**
     * Creates a new GetUserPropertiesCommand
     * 
     * @param actor
     *            the user or system invoking this command
     * @param userIds
     *            the IDs of the users whose properties are requested
     * @param propertiesToGet
     *            collection of names of the properties which should be
     *            requested
     */
    public GetUserPropertiesCommand(Role actor, Collection<Long> userIds,
            Collection<String> propertiesToGet) {
        super(actor, (Permission) null);
        this.propertiesToGet = new HashSet<String>();
        this.propertiesToGet.addAll(propertiesToGet);
        this.userIds = new HashSet<Long>();
        this.userIds.addAll(userIds);
        this.userIds.remove(null);
    }

    /**
     * Creates a new GetUserPropertiesCommand
     * 
     * @param role
     *            the user or system invoking this command
     * @param userIds
     *            the IDs of the users whose properties are requested
     * @param propertiesToGet
     *            strings of names of the properties which should be requested
     */
    public GetUserPropertiesCommand(Role role, Collection<Long> userIds,
            String... propertiesToGet) {
        this(role, userIds, Arrays.asList(propertiesToGet));
    }

    /**
     * Creates a new GetUserPropertiesCommand
     * 
     * @param role
     *            user / system executing the command
     * @param userId
     *            the ID of the user whose properties are requested
     * @param propertiesToGet
     *            strings of names of the properties which should be requested
     */
    public GetUserPropertiesCommand(Role role, Long userId,
            String... propertiesToGet) {
        this(role, new ArrayList<Long>(), propertiesToGet);
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null.");
        }
        this.userIds.add(userId);
    }

    /**
     * @return the first user
     */
    public User getFirstUser() {
        return users.isEmpty() ? null : users.get(0);
    }

    /**
     * @return the users
     */
    public List<User> getUser() {
        return users;
    }

    @Override
    public Long[] getUserIds() {
        return userIds.toArray(new Long[userIds.size()]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionStartedEvent evt)
            throws TransactionException {

        users = new ArrayList<User>(0);
        if ((userIds == null) || userIds.isEmpty()) {
            return;
        }

        Criteria crit = evt.getSession().createCriteria(User.class);

        for (String propertyToGet : propertiesToGet) {
            crit.setFetchMode(propertyToGet, FetchMode.JOIN);
        }

        crit.add(Restrictions.in("id", userIds));

        users = crit.list();
    }
}
