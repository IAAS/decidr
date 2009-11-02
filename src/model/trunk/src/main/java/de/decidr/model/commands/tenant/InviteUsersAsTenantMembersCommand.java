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

import org.hibernate.Query;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.user.CreateNewUnregisteredUserCommand;
import de.decidr.model.entities.Invitation;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.notifications.NotificationEvents;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Sends invitation mails to the given users. An Invitation object will be
 * created at the database. If the user doesn't exist yet an user object will be
 * created too. If a username does not exist an exception will be thrown.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class InviteUsersAsTenantMembersCommand extends TenantCommand {

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
     *            the ID of the tenant to which the given users should be
     *            invited
     * @param emails
     *            list of the email addresses of unregistered user which should
     *            be invited
     * @param usernames
     *            list of usernames of registered user which should be invited
     */
    public InviteUsersAsTenantMembersCommand(Role role, Long tenantId,
            List<String> emails, List<String> usernames) {
        super(role, tenantId);

        this.emails = emails;
        this.usernames = usernames;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        UserProfile profile;
        Invitation newInvitation;

        Tenant tenant = fetchTenant(evt.getSession());

        User actor = (User) evt.getSession().get(User.class, role.getActorId());
        if (actor == null) {
            throw new EntityNotFoundException(User.class, role.getActorId());
        }

        // go through usernames
        for (String uname : usernames) {

            profile = (UserProfile) evt.getSession().createQuery(
                    "from UserProfile where username = :uname").setString(
                    "uname", uname).uniqueResult();

            if (profile == null) {
                throw new EntityNotFoundException(UserProfile.class);
            }

            // create invitation object
            newInvitation = new Invitation();
            newInvitation.setJoinTenant(tenant);
            newInvitation.setReceiver(profile.getUser());
            newInvitation.setSender(actor);
            newInvitation.setCreationDate(DecidrGlobals.getTime().getTime());
            evt.getSession().save(newInvitation);

            NotificationEvents
                    .invitedRegisteredUserAsTenantMember(newInvitation);

        }

        // go through email-addresses
        for (String email : emails) {

            // create new unregistered user
            User newUser = new User();
            newUser.setEmail(email);
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    new CreateNewUnregisteredUserCommand(role, newUser));

            // create invitation
            newInvitation = new Invitation();
            newInvitation.setJoinTenant(tenant);
            newInvitation.setReceiver(newUser);
            newInvitation.setSender(actor);
            newInvitation.setCreationDate(DecidrGlobals.getTime().getTime());
            evt.getSession().save(newInvitation);

            NotificationEvents
                    .invitedUnregisteredUserAsTenantMember(newInvitation);
        }
    }
}
