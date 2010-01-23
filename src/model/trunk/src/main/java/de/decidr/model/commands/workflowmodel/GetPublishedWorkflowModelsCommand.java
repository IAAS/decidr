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

package de.decidr.model.commands.workflowmodel;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;

import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.EqualsFilter;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Filters;
import de.decidr.model.filters.PaginatingCriteria;
import de.decidr.model.filters.Paginator;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Retrieves all published workflow models.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class GetPublishedWorkflowModelsCommand extends AclEnabledCommand {

    private Paginator paginator;
    private List<Filter> filters;
    private List<WorkflowModel> result;

    public GetPublishedWorkflowModelsCommand(Role role, List<Filter> filters,
            Paginator paginator) {
        super(role, (Permission) null);

        this.paginator = paginator;
        this.filters = filters;
    }

    /**
     * @return the result
     */
    public List<WorkflowModel> getResult() {
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionStartedEvent evt)
            throws TransactionException {

        PaginatingCriteria crit = new PaginatingCriteria(WorkflowModel.class,
                evt.getSession());

        /*
         * We only want published workflow models.
         */
        new EqualsFilter(true, "published", true).apply(crit);

        /*
         * Make the "tenant" property available even after the session has been
         * closed.
         */
        crit.setFetchMode("tenant", FetchMode.JOIN);

        Filters.apply(crit, filters, paginator);

        result = crit.setResultTransformer(CriteriaSpecification.ROOT_ENTITY)
                .list();
    }

}
