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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.hibernate.Session;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.commands.user.CreateNewUnregisteredUserCommand;
import de.decidr.model.entities.Invitation;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserAdministratesWorkflowModel;
import de.decidr.model.entities.UserAdministratesWorkflowModelId;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.enums.UserWorkflowAdminState;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.exceptions.UserDisabledException;
import de.decidr.model.exceptions.UserUnavailableException;
import de.decidr.model.exceptions.UsernameNotFoundException;
import de.decidr.model.notifications.NotificationEvents;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Makes the given list of users administrators of the given workflow model.
 * Invitations are automatically sent to unregistered users and users who are
 * not members of the tenant that owns the workflow model.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class SetWorkflowAdministratorsCommand extends WorkflowModelCommand
        implements TransactionalCommand {

    private List<String> newAdminUsernames;
    private List<String> newAdminEmails;

    private List<User> addImmediately = null;
    private List<User> registerInvitation = null;
    private List<User> joinTenantInvitation = null;

    /**
     * Createa a new SetWorkflowAdministratorsCommand.<br>
     * The command will fail with an exception if the lists of new worklow
     * administrators contains:
     * <ul>
     * <li>a username that is unknown to the system</li>
     * <li>a username that belongs to an inactive, disabled or unavailable user
     * account</li>
     * <li>an email address that belongs to an inactive, disabled or unavailable
     * user account</li>
     * </ul>
     * 
     * @param role
     *            the user or system that is executing this command.
     * @param workflowModelId
     *            the workflow model that gets new administrators
     * @param newAdminUsernames
     *            usernames of the new workflow administrators.
     * @param newAdminEmails
     *            email addresses of the new workflow administrators.
     * @throws IllegalArgumentException
     *             if workflowModelId is <code>null</code>.
     */
    public SetWorkflowAdministratorsCommand(Role role, Long workflowModelId,
            List<String> newAdminUsernames, List<String> newAdminEmails) {
        super(role, workflowModelId);
        requireWorkflowModelId();
        this.newAdminEmails = newAdminEmails;
        this.newAdminUsernames = newAdminUsernames;
    }

    /**
     * Searches the given map for users that have an unknown username, are
     * unavailable or have been disabled by the superadmin. If found, this
     * method will throw an appropriate exception.
     * 
     * @param map
     *            map to search
     */
    private void assertOnlyValidUsers(Map<User, UserWorkflowAdminState> map)
            throws UsernameNotFoundException, UserDisabledException,
            UserUnavailableException {
        for (User user : map.keySet()) {
            if (UserWorkflowAdminState.IsUnknownUser.equals(map.get(user))) {
                if ((user.getUserProfile() != null)
                        && (user.getUserProfile().getUsername() != null)) {
                    // unknown but has a username -> system does not recognize
                    // the username
                    throw new UsernameNotFoundException(user.getUserProfile()
                            .getUsername());
                } else if (user.getEmail() == null) {
                    throw new IllegalArgumentException(
                            "An unknown user is missing an email address: cannot send invitation.");
                }
            }
            if (user.getId() != null) {
                // user is known to the system
                if (user.getDisabledSince() != null) {
                    throw new UserDisabledException(user);
                }
                if (user.getUnavailableSince() != null) {
                    throw new UserUnavailableException(user);
                }
            }
        }
    }

    /**
     * Creates new workflow admin invitations in the database.
     * 
     * @param users
     *            users to invite as workflow admin
     * @param model
     *            workflow model to administrate
     * @param session
     *            current Hibernate sesion
     */
    private Set<Invitation> inviteAsWorkflowAdmin(List<User> users,
            WorkflowModel model, Session session) {

        Set<Invitation> invis = new HashSet<Invitation>();

        for (User invitedUser : users) {
            Invitation invitation = new Invitation();
            invitation.setCreationDate(DecidrGlobals.getTime().getTime());
            invitation.setAdministrateWorkflowModel(model);
            invitation.setJoinTenant(null);
            invitation.setParticipateInWorkflowInstance(null);
            invitation.setReceiver(invitedUser);

            User sender;
            if (role instanceof UserRole) {
                sender = (User) session.get(User.class, role.getActorId());
            } else {
                sender = DecidrGlobals.getSettings().getSuperAdmin();
            }

            invitation.setSender(sender);
            session.save(invitation);
            invis.add(invitation);
        }

        return invis;
    }

    /**
     * Makes the given users workflow administrators of the given workflow model
     * 
     * @param users
     *            users to make workflow admin
     * @param model
     *            workflow model to update
     * @param session
     *            current hibernate session
     */
    private void makeWorkflowAdmin(List<User> users, WorkflowModel model,
            Session session) {
        for (User user : users) {
            UserAdministratesWorkflowModel relation = new UserAdministratesWorkflowModel();
            relation.setId(new UserAdministratesWorkflowModelId(user.getId(),
                    model.getId()));
            relation.setUser(user);
            relation.setWorkflowModel(model);
            session.save(relation);
        }
    }

    @Override
    public void transactionAllowed(TransactionStartedEvent evt)
            throws TransactionException, UserDisabledException,
            UsernameNotFoundException, UserUnavailableException {
        /*
         * First we get an overview of the workflow administration state. Based
         * on that information we will send invitations or abort the
         * transaction.
         */
        GetWorkflowAdministrationStateCommand cmd = new GetWorkflowAdministrationStateCommand(
                role, workflowModelId, newAdminUsernames, newAdminEmails);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        Map<User, UserWorkflowAdminState> map = cmd.getResult();

        // checking for unknown, disabled or unavailable users
        assertOnlyValidUsers(map);

        // clear the current workflow admin list
        String hql = "delete from UserAdministratesWorkflowModel rel "
                + "where rel.workflowModel.id = :workflowModelId";
        evt.getSession().createQuery(hql).setLong("workflowModelId",
                workflowModelId).executeUpdate();

        // tenant members are immediately given the state of workflow admin
        addImmediately = new ArrayList<User>();

        // users that have to register first must confirm an invitation to
        // become workflow admin
        registerInvitation = new ArrayList<User>();

        // users that are registered but need to join the tenant must confirm a
        // slightly different invitation
        joinTenantInvitation = new ArrayList<User>();

        for (Entry<User, UserWorkflowAdminState> entry : map.entrySet()) {
            switch (entry.getValue()) {
            case IsAlreadyWorkflowAdmin:
            case IsTenantMember:
                addImmediately.add(entry.getKey());
                break;

            case IsUnknownUser:
                // since the user is unknown, we must add him to the database
                // now
                HibernateTransactionCoordinator.getInstance().runTransaction(
                        new CreateNewUnregisteredUserCommand(role, entry
                                .getKey()));
                registerInvitation.add(entry.getKey());
                break;

            case NeedsTenantMembership:
                joinTenantInvitation.add(entry.getKey());
                break;

            default:
                break;
            }
        }

        WorkflowModel model = fetchWorkflowModel(evt.getSession());
        // preload tenant for notification email contents
        model.getTenant();

        // we are separating creating the invitations from sending the
        // notification emails because we cannot roll back the latter.
        makeWorkflowAdmin(addImmediately, model, evt.getSession());

        Set<Invitation> registeredInvitations = inviteAsWorkflowAdmin(
                registerInvitation, model, evt.getSession());
        Set<Invitation> unregistered = inviteAsWorkflowAdmin(
                joinTenantInvitation, model, evt.getSession());

        for (Invitation invi : registeredInvitations) {
            NotificationEvents.invitedUnregisteredUserAsWorkflowAdmin(invi,
                    model);
        }

        for (Invitation invi : unregistered) {
            NotificationEvents
                    .invitedRegisteredUserAsWorkflowAdmin(invi, model);
        }
    }
}
