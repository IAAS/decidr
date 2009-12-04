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
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.type.Type;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserAdministratesWorkflowModel;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Filters;
import de.decidr.model.filters.PaginatingCriteria;
import de.decidr.model.filters.Paginator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Retrieves all {@link WorkflowModel}s that are administrated by a given user.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class GetAdministratedWorkflowModelsCommand extends UserCommand {

    private List<WorkflowModel> result;
    private List<Filter> filters;
    private Paginator paginator;

    /**
     * Creates a new GetAdministratedWorkflowModelCommand. This command will
     * write all WorkflowModels which the user administrates as a list in the
     * result variable.
     * 
     * @param role
     *            user which executes the command
     * @param userId
     *            the ID of the user whose administrated wokflow models should
     *            be requested
     * @param filters
     *            optional (nullable) list of filters to apply to the query
     * @param paginator
     *            optional (nullable) paginator
     * @throws IllegalArgumentException
     *             if userId is <code>null</code>.
     */
    public GetAdministratedWorkflowModelsCommand(Role role, Long userId,
            List<Filter> filters, Paginator paginator) {
        super(role, userId);
        requireUserId();
        this.filters = filters;
        this.paginator = paginator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        result = null;

        // does the user exist? returning an empty list might be ambigous.
        String hql = "select u.id from User u where u.id = :userId";
        Object id = evt.getSession().createQuery(hql).setLong("userId",
                getUserId()).setMaxResults(1).uniqueResult();

        if (id == null) {
            throw new EntityNotFoundException(User.class, getUserId());
        }

        /*
         * Criteria that represent the following query:
         * 
         * "from WorkflowModel w where exists(from
         * UserAdministratesWorkflowModel rel where rel.workflowModel = w and
         * rel.user.id = :userId)"
         */
        PaginatingCriteria criteria = new PaginatingCriteria(
                WorkflowModel.class, "m", evt.getSession());

        DetachedCriteria adminCriteria = DetachedCriteria.forClass(
                UserAdministratesWorkflowModel.class, "rel");

        adminCriteria.add(Restrictions.eqProperty("rel.workflowModel", "m"))
                .add(Restrictions.eq("rel.user.id", getUserId()));

        /*
         * Workaround for Hibernate issue HHH-993: Criteria subquery without
         * projection fails throwing NullPointerException.
         * 
         * Additionally, Mysql doesn't seem to like aliases in EXISTS
         * subqueries, so we have to explicitly specify "*"
         */
        Projection existsSubqueryProjection = Projections.sqlProjection("*",
                new String[0], new Type[0]);
        adminCriteria.setProjection(existsSubqueryProjection);
        criteria.add(Subqueries.exists(adminCriteria));

        criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);

        Filters.apply(criteria, filters, paginator);

        result = criteria.list();
    }

    /**
     * @return List of workflow models which are administrated by the given user
     */
    public List<WorkflowModel> getResult() {
        return result;
    }
}
