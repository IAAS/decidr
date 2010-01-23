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

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.type.Type;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserIsMemberOfTenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.PaginatingCriteria;
import de.decidr.model.filters.Paginator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Saves the members of the given tenant including the tenant admin in the
 * result variable.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class GetUsersOfTenantCommand extends TenantCommand {

    private Paginator paginator;
    private List<User> result;

    /**
     * Creates a new GetUsersOfTenantCommand. The Command saves the members of
     * the given tenant including the tenant admin in the result variable.
     * 
     * @param role
     *            the user which executes the command
     * @param tenantId
     *            the ID of the tenant whose users should be requested
     * @param paginator
     *            {@link Paginator}
     * @throws IllegalArgumentException
     *             if the given tenant ID is <code>null</code>
     */
    public GetUsersOfTenantCommand(Role role, Long tenantId, Paginator paginator) {
        super(role, tenantId);
        requireTenantId();

        this.paginator = paginator;
    }

    /**
     * @return the tenant members including the tenant admin
     */
    public List<User> getResult() {
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        PaginatingCriteria c = new PaginatingCriteria(User.class, "u", evt
                .getSession());

        String rootAlias = c.getAlias();

        // Create criterion "user is a member of the tenant"
        DetachedCriteria memberRel = DetachedCriteria.forClass(
                UserIsMemberOfTenant.class, "memberRel");
        memberRel.add(
                Property.forName("memberRel.user.id").eqProperty(
                        rootAlias + ".id")).add(
                Property.forName("memberRel.tenant.id").eq(getTenantId()));

        // Create criterion "user is the administrator of the tenant"
        DetachedCriteria adminRel = DetachedCriteria.forClass(Tenant.class,
                "adminTenant");
        adminRel.add(Property.forName("adminTenant.id").eq(getTenantId())).add(
                Property.forName("adminTenant.admin.id").eqProperty(
                        rootAlias + ".id"));

        /*
         * Workaround for Hibernate issue HHH-993: Criteria subquery without
         * projection fails throwing NullPointerException.
         * 
         * Additionally, Mysql doesn't seem to like aliases in EXISTS
         * subqueries, so we have to explicitly specify "*"
         */
        Projection existsSubqueryProjection = Projections.sqlProjection("*",
                new String[0], new Type[0]);
        memberRel.setProjection(existsSubqueryProjection);
        adminRel.setProjection(existsSubqueryProjection);

        c.add(Restrictions.or(Subqueries.exists(memberRel), Subqueries
                .exists(adminRel)));

        // preload user profiles - no lazy loading desired
        c.createCriteria("userProfile", CriteriaSpecification.LEFT_JOIN);
        c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);

        if (paginator != null) {
            paginator.apply(c);
        }
        result = c.list();
    }
}
