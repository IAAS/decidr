package de.decidr.model.commands.tenant;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserIsMemberOfTenant;
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

        evt.getSession().save(uimof);
    }
}
