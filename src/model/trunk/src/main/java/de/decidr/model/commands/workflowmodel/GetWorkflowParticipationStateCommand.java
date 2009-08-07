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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.enums.UserWorkflowParticipationState;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Fetches a map of users that can be used to tell whether the given usernames
 * or emails belong to:
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

    private List<String> usernames;

    private List<String> emails;

    private Map<User, UserWorkflowParticipationState> result;

    /**
     * Creates a new GetWorkflowParticipationStateCommand that retrieves the
     * participation state.
     * 
     * @param role
     *            user / system executing the command
     * @param workflowModelId
     *            workflow model of which a new instance will be created
     * @param usernames
     *            list of usernames to search for
     * @param emails
     *            list of email addresses to search for
     */
    public GetWorkflowParticipationStateCommand(Role role,
            Long workflowModelId, List<String> usernames, List<String> emails) {
        super(role, workflowModelId);
        this.usernames = usernames;
        this.emails = emails;
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
        String hql = "select distinct u.id, u.email "
                + "from User as u left join fetch u.userProfile "
                + "where (u.email in (:emails)) or "
                + "(u.userProfile.username in (:usernames))";

        Query q = session.createQuery(hql).setParameterList("emails", emails)
                .setParameterList("usernames", usernames);

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
        String hql = "select u.id from User u "
                + "inner join UserIsMemberOfTenant as rel "
                + "inner join WorkflowModel as w"
                + "where (u in (:knownUsers)) and (rel.user = u) and "
                + "(rel.tenant = w.tenant) and " + "w.id = :modelId";

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
     * Finds those usernames and email addresses that are unknown to the system
     * by looking at the given list of known users and the current list of
     * usernames and emails.
     * 
     * @param knownUsers list of known users to modify
     * @return all unknown users
     */
    private List<User> getUnknownUsers(List<User> knownUsers) {
        ArrayList<User> unknownUsers = new ArrayList<User>();
        /*-
         * Username is unknown <-> no known user has this username 
         * Email is unknown <-> no known user has this email
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

        return unknownUsers;
    }

    /**
     * @return the result
     */
    public Map<User, UserWorkflowParticipationState> getResult() {
        return result;
    }
}
