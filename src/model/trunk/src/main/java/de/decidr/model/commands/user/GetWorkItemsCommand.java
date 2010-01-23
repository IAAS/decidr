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

import org.hibernate.criterion.Restrictions;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.WorkItemSummaryView;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Filters;
import de.decidr.model.filters.PaginatingCriteria;
import de.decidr.model.filters.Paginator;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Saves the workitem of the given user in the result variable as {@link List}
 * {@code <}{@link WorkItemSummaryView}{@code >}.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class GetWorkItemsCommand extends UserCommand {

    private List<WorkItemSummaryView> result;
    private Paginator paginator;
    private List<Filter> filters;

    /**
     * Creates a new GetWorkitemsCommand. The command saves the workitem of the
     * given user in the result variable as <code>{@link List}</code>.
     * 
     * @param role
     *            the user which executes the command
     * @param userId
     *            the id of the user whose workitems should be requested
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>
     */
    public GetWorkItemsCommand(Role role, Long userId, List<Filter> filters,
            Paginator paginator) {
        super(role, userId);
        requireUserId();

        this.paginator = paginator;
        this.filters = filters;

    }

    /**
     * 
     * @return list of the workitems of the given user
     */
    public List<WorkItemSummaryView> getResult() {

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionStartedEvent evt)
            throws TransactionException {

        PaginatingCriteria c = new PaginatingCriteria(
                WorkItemSummaryView.class, evt.getSession());
        c.add(Restrictions.eq("userId", getUserId()));

        Filters.apply(c, filters, paginator);

        result = c.list();
    }
}
