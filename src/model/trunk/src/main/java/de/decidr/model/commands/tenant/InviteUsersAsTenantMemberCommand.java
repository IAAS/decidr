package de.decidr.model.commands.tenant;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.hibernate.Query;

import de.decidr.model.notifications.NotificationEvents;
import de.decidr.model.entities.Invitation;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Password;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Sends invitation mails to the given users. An Invitation object will be
 * created at the database. If the user doesn't exist yet an user object will be
 * created too. If a username does not exist an exception will be thrown.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class InviteUsersAsTenantMemberCommand extends TenantCommand {

    private List<String> emails;
    private List<String> usernames;

    /**
     * Creates a new InviteUsersAsTenantMemberCommand. This command sends
     * invitation mails to the given users. An Invitation object will be created
     * at the database. If the user doesn't exist yet an user object will be
     * created too. If a username does not exist an exception will be thrown.
     * 
     * @param role
     *            user which executes the command
     * @param tenantId
     *            the ID of the tenant to which the given users should be invited
     * @param emails
     *            list of the email addresses of unregistered user which should be invited
     * @param usernames
     *            list of usernames of registered user which should be invited
     */
    public InviteUsersAsTenantMemberCommand(Role role, Long tenantId,
            List<String> emails, List<String> usernames) {
        super(role, tenantId);

        this.emails = emails;
        this.usernames = usernames;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        UserProfile profile;
        Invitation invi;

        Tenant tenant = (Tenant) evt.getSession().load(Tenant.class,
                getTenantId());
        User actor = (User) evt.getSession()
                .load(User.class, role.getActorId());

        // go through usernames
        for (String uname : usernames) {

            Query q = evt.getSession().createQuery(
                    "from UserProfile where username= :uname");
            q.setString("uname", uname);

            profile = (UserProfile) q.uniqueResult();

            if (profile == null) {
                throw new TransactionException("User " + uname
                        + " does not exists");
            } else {

                // create invitation object
                invi = new Invitation();
                invi.setJoinTenant(tenant);
                invi.setReceiver(profile.getUser());
                invi.setSender(actor);
                evt.getSession().save(invi);

                NotificationEvents.invitedRegisteredUserAsTenantMember(invi);
            }

        }

        // go through email-addresses
        for (String email : emails) {

            // create new unregistered user
            User newUser = new User();
            newUser.setEmail(email);

            try {
                newUser.setAuthKey(Password.getRandomAuthKey());
            } catch (UnsupportedEncodingException e) {
                throw new TransactionException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new TransactionException(e);
            }

            evt.getSession().save(newUser);

            // create invitation
            invi = new Invitation();
            invi.setJoinTenant(tenant);
            invi.setReceiver(newUser);
            invi.setSender(actor);
            evt.getSession().save(invi);

            NotificationEvents.invitedUnregisteredUserAsTenantMember(invi);
        }
    }
}
