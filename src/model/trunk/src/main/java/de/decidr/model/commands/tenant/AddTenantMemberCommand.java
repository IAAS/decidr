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
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Create the relation is member of between the given tenant and the given user.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class AddTenantMemberCommand extends TenantCommand {

    private Long memberId;

    /**
     * Creates a new AddTenantMemberCommand. The command create the relation is
     * member of between the given tenant and the given user.
     * 
     * @param role
     *            user which executes the command
     * @param tenantId
     *            id of the tenant
     * @param memberId
     *            id of the user
     */
    public AddTenantMemberCommand(Role role, Long tenantId, Long memberId) {
        super(role, tenantId);

        if (tenantId == null) {
            throw new IllegalArgumentException("tenantId cannot not be null");
        }

        if (memberId == null) {
            throw new IllegalArgumentException("memberId cannot not be null");
        }

        this.memberId = memberId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        User member = (User) evt.getSession().load(User.class, memberId);
        Tenant tenant = (Tenant) evt.getSession().load(Tenant.class,
                getTenantId());

        UserIsMemberOfTenant uimof = new UserIsMemberOfTenant();
        uimof.setTenant(tenant);
        uimof.setUser(member);
        uimof.setId(new UserIsMemberOfTenantId(member.getId(), tenant.getId()));

        evt.getSession().save(uimof);
    }
}
