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
package de.decidr.model.commands.system;

import java.util.List;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.Log;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Filters;
import de.decidr.model.filters.PaginatingCriteria;
import de.decidr.model.filters.Paginator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Returns the logs saved in the database.
 * 
 * @author Daniel Huss
 */
public class GetLogCommand extends SystemCommand {

    private List<Log> result;
    private List<Filter> filters;
    private Paginator paginator;

    /**
     * Creates a new <code>{@link GetLogCommand}</code>. The command saves the
     * logs in the variable <code>{@link #result}</code>.
     * 
     * @param role
     *            the user who executes the command
     * @param filters
     *            filters the result by the given criteria
     * @param paginator
     *            splits the document into several pages
     */
    public GetLogCommand(Role role, List<Filter> filters, Paginator paginator) {
        super(role, null);
        this.filters = filters;
        this.paginator = paginator;
    }

    /**
     * Returns the result.
     * 
     * @return List of the logs.
     */
    public List<Log> getResult() {
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        PaginatingCriteria c = new PaginatingCriteria(Log.class, evt
                .getSession());

        Filters.apply(c, filters, paginator);

        result = c.list();
    }
}
