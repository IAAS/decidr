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

import java.util.List;

import org.hibernate.criterion.CriteriaSpecification;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Filters;
import de.decidr.model.filters.PaginatingCriteria;
import de.decidr.model.filters.Paginator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Saves all system users in the result variable.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class GetAllUsersCommand extends UserCommand {

    private List<Filter> filters;
    private Paginator paginator;
    private List<User> result;

    /**
     * Creates a new GetAllUsersCommand. This command saves all system users in
     * the result variable.
     * 
     * @param role
     *            the user which executes the command
     * @param filters
     *            {@link Filter}
     * @param paginator
     *            {@link Paginator}
     */
    public GetAllUsersCommand(Role role, List<Filter> filters,
            Paginator paginator) {
        super(role, null);

        this.filters = filters;
        this.paginator = paginator;
    }

    /**
     * @return List of all users of the system expect of them which has been
     *         rejected
     */
    public List<User> getResult() {
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        PaginatingCriteria c = new PaginatingCriteria(User.class, evt
                .getSession());

        c.createCriteria("userProfile", CriteriaSpecification.LEFT_JOIN);

        Filters.apply(c, filters, paginator);

        result = c.list();
    }
}
