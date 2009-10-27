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

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Filters;
import de.decidr.model.filters.PaginatingCriteria;
import de.decidr.model.filters.Paginator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Saves the result in the variable result as
 * <code>{@link List<WorkflowModel>}</code>.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class GetWorkflowModelsCommand extends TenantCommand {

    private List<Filter> filters;
    private Paginator paginator;
    private List<WorkflowModel> result;

    /**
     * Creates a new GetWorkflowModelsCommand. The command saves the result in
     * the variable result as List<WorkflowModel>.
     * 
     * @param role
     *            the user which executes the command
     * @param tenantId
     *            the ID of the tenant whose workflow models should be returned
     * @param filters
     *            {@link Filter}
     * @param paginator
     *            {@link Paginator}
     */
    public GetWorkflowModelsCommand(Role role, Long tenantId,
            List<Filter> filters, Paginator paginator) {
        super(role, tenantId);

        this.filters = filters;
        this.paginator = paginator;

    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        PaginatingCriteria c = new PaginatingCriteria(WorkflowModel.class, evt
                .getSession());

        c.add(Restrictions.eq("tenant.id", getTenantId()));

        Filters.apply(c, filters, paginator);

        result = c.setResultTransformer(Criteria.ROOT_ENTITY).list();
    }

    /**
     * 
     * @return list of all workflow models for the given tenant which survived
     *         the filters ;)
     */
    public List<WorkflowModel> getResult() {
        return result;
    }
}
