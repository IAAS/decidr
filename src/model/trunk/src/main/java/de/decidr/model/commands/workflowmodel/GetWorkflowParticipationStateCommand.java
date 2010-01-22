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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.enums.UserWorkflowParticipationState;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Fetches a map of users that can be used to tell whether the given user IDs,
 * usernames or emails belong to:
 * 
 * <ul>
 * <li>users that are unknown to the system. (invitations type A must be sent)</li>
 * <li>users that are known to the system, but are not a member of the tenant
 * that owns the given workflow. (invitations type B must be sent)</li>
 * <li>users that are members of the tenant that owns the workflow model and do
 * not need to be invited to participate in a workflow instance</li>
 * </ul>
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class GetWorkflowParticipationStateCommand extends WorkflowModelCommand {

    private static final Logger logger = DefaultLogger
            .getLogger(GetWorkflowParticipationStateCommand.class);

    private List<String> usernames;
    private List<String> emails;
    private List<Long> userIds;

    private Map<User, UserWorkflowParticipationState> result;

    /**
     * Creates a new GetWorkflowParticipationStateCommand that retrieves the
     * participation state.
     * 
     * @param role
     *            user / system executing the command
     * @param workflowModelId
     *            workflow model of which a new instance will be created
     * @param userIds
     *            list of user IDs to check
     * @param usernames
     *            list of usernames to search for
     * @param emails
     *            list of email addresses to search for
     */
    public GetWorkflowParticipationStateCommand(Role role,
            Long workflowModelId, List<Long> userIds, List<String> usernames,
            List<String> emails) {
        super(role, workflowModelId);
        this.usernames = usernames;
        this.emails = emails;
        this.userIds = userIds;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        result = new HashMap<User, UserWorkflowParticipationState>();
        WorkflowModel model = fetchWorkflowModel(evt.getSession());
        /*
         * First get a list of all known users so we can sort out those that are
         * unknown.
         */
        List<User> knownUsers = getKnownUsers(evt.getSession());
        List<User> unknownUsers = getUnknownUsers(knownUsers);
        /*
         * The found tenant members are removed from the list of known users as
         * a side-effect.
         */
        List<User> members = getMembers(knownUsers, model, evt.getSession());
        /*
         * The remaining users are assumed to be known to the system, but not
         * members of the tenant that owns the workflow model.
         */
        List<User> nonMembers = knownUsers;

        putResults(unknownUsers, UserWorkflowParticipationState.IsUnknownUser);
        putResults(nonMembers,
                UserWorkflowParticipationState.NeedsTenantMembership);
        putResults(members,
                UserWorkflowParticipationState.IsAlreadyTenantMember);

        logResult();
    }

    /**
     * Logs the result map in a human readable format.
     */
    private void logResult() {

        if (result == null || !logger.isDebugEnabled()) {
            return;
        }

        StringBuilder str = new StringBuilder();
        str.append("User workflow participation state:\n");

        for (Entry<User, UserWorkflowParticipationState> entry : result
                .entrySet()) {
            User user = entry.getKey();
            UserWorkflowParticipationState state = entry.getValue();

            str.append("(id: ");
            str.append(user.getId());
            str.append(" email: ");
            str.append(user.getEmail());
            if (user.getUserProfile() != null) {
                str.append(" username: ");
                str.append(user.getUserProfile().getUsername());
            }
            str.append(")  ===> ");
            str.append(state.toString());
            str.append("\n");
        }

        logger.debug(str.toString());
    }

    /**
     * Puts all given users in the result map.
     * 
     * @param users
     *            keys in the result map
     * @param state
     *            value in the result map
     */
    private void putResults(List<User> users,
            UserWorkflowParticipationState state) {
        for (User key : users) {
            result.put(key, state);
        }
    }

    /**
     * Returns all users that are known to the system using the current set of
     * usernames and emails.
     * 
     * @param session
     *            current Hibernate session
     * @return list of known users
     */
    @SuppressWarnings("unchecked")
    private List<User> getKnownUsers(Session session) {
        /*
         * Workaround for "empty parameter list bug":
         * 
         * using setParameterList with a null value or an empty list results in
         * a QuerySyntaxException.
         */
        List<String> nonEmptyEmails;
        if (emails == null || emails.isEmpty()) {
            nonEmptyEmails = new LinkedList<String>();
            // this must not be a valid email address
            nonEmptyEmails.add("");
        } else {
            nonEmptyEmails = emails;
        }

        List<String> nonEmptyUsernames;
        if (usernames == null || usernames.isEmpty()) {
            nonEmptyUsernames = new LinkedList<String>();
            // this must not be a valid username
            nonEmptyUsernames.add("");
        } else {
            nonEmptyUsernames = usernames;
        }

        List<Long> nonEmptyUserIds;
        if (userIds == null || userIds.isEmpty()) {
            nonEmptyUserIds = new LinkedList<Long>();
            // this must not be a valid user ID
            nonEmptyUserIds.add(UserRole.UNKNOWN_USER_ID);
        } else {
            nonEmptyUserIds = userIds;
        }

        String hql = "select distinct u "
                + "from User as u left join fetch u.userProfile "
                + "where (u.email in (:emails)) or "
                + "(u.userProfile.username in (:usernames)) or "
                + "(u.id in (:userIds))";

        Query q = session.createQuery(hql);

        q.setParameterList("emails", nonEmptyEmails);
        q.setParameterList("usernames", nonEmptyUsernames);
        q.setParameterList("userIds", nonEmptyUserIds);

        return q.list();
    }

    /**
     * Returns a list of those users in knownUsers that are members of the
     * tenant that owns the given workflow model. The found users are removed
     * from knownUsers.
     * 
     * @param knownUsers
     *            list of known users to modify
     * @param model
     *            workflow model to check against
     * @param session
     *            current Hibernate session
     * @return see above
     */
    @SuppressWarnings("unchecked")
    private List<User> getMembers(List<User> knownUsers, WorkflowModel model,
            Session session) {

        if (knownUsers == null || knownUsers.isEmpty()) {
            return new ArrayList<User>();
        }

        /*
         * "Give me all users who are members or admins of the tenant that owns
         * the given model and are also present in the list of known users". If
         * you have a more elegant query that returns the same result (such a
         * query probably exists), please go ahead and put it here ;-)
         */
        String hql = "select distinct u from User u "
                + "where u in (:knownUsers) and "
                + "(exists(from Tenant t where t.admin = u "
                + "and exists(from WorkflowModel m "
                + "where m.tenant = t and m.id = :modelId))) or "
                + "exists(from UserIsMemberOfTenant rel "
                + "where rel.user = u and " + "exists(from WorkflowModel m "
                + "where m.tenant = rel.tenant and m.id = :modelId))) or "
                + "(exists(from SystemSettings s where s.superAdmin = u) )";

        Query q = session.createQuery(hql).setLong("modelId", model.getId())
                .setParameterList("knownUsers", knownUsers);

        List<User> members = q.list();

        /*
         * Since our entities use the default equals() implementation, two
         * entities with the same id are not necessarily considered equal.
         * 
         * Implementing equals() based on the entity id can lead to several
         * problems, therefore we iterate manually through the lists.
         */
        List<User> knownUsersToRemove = new ArrayList<User>();
        for (User member : members) {
            for (User knownUser : knownUsers) {
                if (knownUser.getId().equals(member.getId())) {
                    knownUsersToRemove.add(knownUser);
                }
            }
        }
        // now knownUsersToRemove contains all members including their profiles
        knownUsers.removeAll(knownUsersToRemove);

        return knownUsersToRemove;
    }

    /**
     * Finds those user IDs,s usernames and email addresses that are unknown to
     * the system by looking at the given list of known users and the current
     * list of usernames and emails.
     * 
     * @param knownUsers
     *            list of known users to modify
     * @return all unknown users
     */
    private List<User> getUnknownUsers(List<User> knownUsers) {
        ArrayList<User> unknownUsers = new ArrayList<User>();
        /*-
         * Username is unknown <-> no known user has this username 
         * Email is unknown <-> no known user has this email
         * Id is unknown <-> no known user has this id
         * :-)
         */
        for (String email : emails) {
            Boolean emailIsUnknown = true;

            for (User u : knownUsers) {
                if (email.equals(u.getEmail())) {
                    emailIsUnknown = false;
                    break;
                }
            }

            if (emailIsUnknown) {
                User unknownUser = new User();
                unknownUser.setEmail(email);
                unknownUsers.add(unknownUser);
            }
        }

        for (String username : usernames) {
            Boolean usernameIsUnknown = true;

            username = username.toLowerCase(Locale.US);

            for (User u : knownUsers) {

                UserProfile profile = u.getUserProfile();
                if (profile != null) {

                    String knownUsername = profile.getUsername();
                    if (knownUsername != null) {
                        knownUsername = knownUsername.toLowerCase(Locale.US);

                        if (knownUsername.equals(username)) {
                            usernameIsUnknown = false;
                            break;
                        }
                    }
                }
            }

            if (usernameIsUnknown) {
                User unknownUser = new User();

                UserProfile profile = new UserProfile();
                profile.setUsername(username);

                unknownUser.setUserProfile(profile);
                unknownUsers.add(unknownUser);
            }
        }

        for (Long userId : userIds) {
            Boolean idIsUnknown = true;

            for (User u : knownUsers) {

                if (u.getId().equals(userId)) {
                    idIsUnknown = false;
                    break;
                }
            }

            if (idIsUnknown) {
                User unknownUser = new User();
                unknownUser.setId(userId);
                unknownUsers.add(unknownUser);
            }
        }

        return unknownUsers;
    }

    /**
     * @return the result
     */
    public Map<User, UserWorkflowParticipationState> getResult() {
        return result;
    }
}
