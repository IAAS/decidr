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
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

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
     */
    public GetUsersOfTenantCommand(Role role, Long tenantId, Paginator paginator) {
        super(role, tenantId);

        this.paginator = paginator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        PaginatingCriteria c = new PaginatingCriteria(User.class, evt
                .getSession());

        ProjectionList plist = Projections.projectionList();
        plist.add(Projections.property("username"));
        plist.add(Projections.property("firstName"));
        plist.add(Projections.property("lastName"));

        // Create criterion "user is a member of the tenant"
        DetachedCriteria isMemberCriteria = DetachedCriteria
                .forClass(UserIsMemberOfTenant.class);
        isMemberCriteria.add(Restrictions.eq("tenant.id", getTenantId()));
        Criterion isMemberRestriction = Subqueries.exists(isMemberCriteria);

        // Create criterion "user is the administrator of the tenant"
        DetachedCriteria isAdminCriteria = DetachedCriteria
                .forClass(Tenant.class);
        isAdminCriteria.add(Restrictions.eq("tenant.id", getTenantId()));
        isAdminCriteria.add(Restrictions.eq("admin.id", c.getAlias() + ".id"));
        Criterion isAdminRestriction = Subqueries.exists(isAdminCriteria);

        c.add(Restrictions.or(isMemberRestriction, isAdminRestriction));

        // preload user profiles - no lazy loading desired
        c.createCriteria("userProfile", CriteriaSpecification.LEFT_JOIN)
                .setProjection(plist);

        if (paginator != null) {
            paginator.apply(c);
        }
        result = c.list();
    }

    /**
     * @return the tenant members including the tenant admin
     */
    public List<User> getResult() {
        return result;
    }
}
