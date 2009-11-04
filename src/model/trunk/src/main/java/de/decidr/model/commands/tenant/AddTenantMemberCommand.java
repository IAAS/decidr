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

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserIsMemberOfTenant;
import de.decidr.model.entities.UserIsMemberOfTenantId;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Create the relation is member of between the given tenant and the given user.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class AddTenantMemberCommand extends TenantCommand {

    private Long memberId;

    /**
     * Creates a new AddTenantMemberCommand. The command create the relation is
     * member of between the given tenant and the given user.
     * 
     * @param role
     *            user/system that which executes the command
     * @param tenantId
     *            id of the tenant
     * @param memberId
     *            id of the user
     * @throws IllegalArgumentException
     *             if tenant ID or member ID is <code>null</code>.
     */
    public AddTenantMemberCommand(Role role, Long tenantId, Long memberId) {
        super(role, tenantId);
        requireTenantId();

        if (memberId == null) {
            throw new IllegalArgumentException("memberId cannot not be null");
        }

        this.memberId = memberId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        User member = (User) evt.getSession().get(User.class, memberId);
        if (member == null) {
            throw new EntityNotFoundException(User.class, memberId);
        }
        Tenant tenant = fetchTenant(evt.getSession());

        UserIsMemberOfTenantId relationId = new UserIsMemberOfTenantId(member
                .getId(), tenant.getId());
        Object existingRelation = evt.getSession().get(
                UserIsMemberOfTenant.class, relationId);
        if (existingRelation != null) {
            // do not add same relation twice (would violate unique indices)
            return;
        }

        UserIsMemberOfTenant uimof = new UserIsMemberOfTenant();
        uimof.setTenant(tenant);
        uimof.setUser(member);
        uimof.setId(relationId);

        evt.getSession().save(uimof);
    }
}
