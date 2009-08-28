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
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.PaginatingCriteria;
import de.decidr.model.filters.Paginator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Saves all WorkflowInstances for the given tenant in the result variable.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class GetWorkflowInstancesCommand extends TenantCommand {

    private Paginator paginator;
    private List<WorkflowInstance> result;

    /**
     * Creates a new GetWorkflowInstanceCommand. The command will save all
     * WorkflowInstances for the given tenant in the result variable.
     * 
     * @param role
     *            the user which executes the command
     * @param tenantId
     *            the ID of the tenant for which the workflow instances should be requested
     * @param paginator
     *            {@link Paginator}
     */
    public GetWorkflowInstancesCommand(Role role, Long tenantId,
            Paginator paginator) {
        super(role, tenantId);

        this.paginator = paginator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        PaginatingCriteria c = new PaginatingCriteria(WorkflowInstance.class,
                evt.getSession());

        Criteria c2;

        // create criteria 2nd level and project to name
        c2 = c.createCriteria("deployedWorkflowModel",
                CriteriaSpecification.INNER_JOIN).setProjection(
                Projections.property("name"));

        // create criteria 3rd level and project to name
        c2.createCriteria("tenant", CriteriaSpecification.INNER_JOIN)
                .setProjection(Projections.property("name")).add(
                        Restrictions.idEq(getTenantId()));

        if (paginator != null) {
            paginator.apply(c);
        }

        result = c.list();

    }

    /**
     * @return List of WorkflowInstances which are part of the given tenant
     */
    public List<WorkflowInstance> getResult() {
        return result;
    }
}
