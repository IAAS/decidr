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
package de.decidr.model.commands.tenant;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;

import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.TenantWithAdminView;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Filters;
import de.decidr.model.filters.PaginatingCriteria;
import de.decidr.model.filters.Paginator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Save all tenants which have to be approved in the result variable.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class GetTenantsToApproveCommand extends AclEnabledCommand {

    private Paginator paginator;
    private List<Filter> filters;
    private List<TenantWithAdminView> result;

    /**
     * Creates a new GetTenantsTo ApproveCommand. This command will save all
     * Tenants which have to bee approve in the result variable.
     * 
     * @param role
     *            the user which executes the command
     * @param filters
     *            {@link Filter}
     * @param paginator
     *            {@link Paginator}
     */
    public GetTenantsToApproveCommand(Role role, List<Filter> filters,
            Paginator paginator) {
        super(role, (Permission) null);

        this.paginator = paginator;
        this.filters = filters;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        result = new ArrayList<TenantWithAdminView>();

        PaginatingCriteria c = new PaginatingCriteria(
                TenantWithAdminView.class, evt.getSession());
        c.add(Restrictions.isNull("approvedSince"));

        Filters.apply(c, filters, paginator);

        result = c.list();
    }

    /**
     * @return List of all tenants which have to been approved
     */
    public List<TenantWithAdminView> getResult() {
        return result;
    }
}
