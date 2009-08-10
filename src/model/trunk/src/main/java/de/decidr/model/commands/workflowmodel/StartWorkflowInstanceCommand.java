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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.Session;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.user.CreateNewUnregisteredUserCommand;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.Invitation;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserParticipatesInWorkflow;
import de.decidr.model.entities.UserParticipatesInWorkflowId;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.model.enums.UserWorkflowParticipationState;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.exceptions.UserDisabledException;
import de.decidr.model.exceptions.UserUnavailableException;
import de.decidr.model.exceptions.UsernameNotFoundException;
import de.decidr.model.exceptions.WorkflowModelNotStartableException;
import de.decidr.model.notifications.NotificationEvents;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.workflowmodel.instancemanagement.InstanceManager;
import de.decidr.model.workflowmodel.instancemanagement.InstanceManagerImpl;

/**
 * Creates a new workflow instance in the database. Sends invitations to users
 * that are unknown to the system or aren't members of the tenant that owns the
 * workflow instance.
 * <p>
 * Depending on the startImmediately parameter, this command may also create a
 * new workflow instance on the Apache ODE. Otherwise the ODE workflow instance
 * will be created when the last invited user confirms his invitation.
 * 
 * 
 * @author Daniel Huss
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class StartWorkflowInstanceCommand extends WorkflowModelCommand {

    private byte[] startConfiguration;
    private WorkflowInstance createdWorkflowInstance = null;
    private Boolean startImmediately;
    private List<String> participantUsernames;
    private List<String> participantEmails;

    private List<User> usersThatNeedInvitations = null;
    private List<User> usersThatAreAlreadyTenantMembers = null;

    /**
     * Creates a new StartWorkflowInstanceCommand
     * 
     * @param role
     *            user / system that is executing the command
     * @param workflowModelId
     *            workflow model of which an instance should be started
     * @param startConfiguration
     *            start configuration to use
     * @param startImmediately
     *            If true the system will start the workflow instance even if
     *            invitations must be sent. Otherwise the system delays the
     *            start of the workflow instance until the last user confirms
     *            his invitation.
     * @param participantUsernames
     * @param participantEmails
     */
    public StartWorkflowInstanceCommand(Role role, Long workflowModelId,
            byte[] startConfiguration, Boolean startImmediately,
            List<String> participantUsernames, List<String> participantEmails) {
        super(role, workflowModelId);
        this.startConfiguration = startConfiguration;
        this.startImmediately = startImmediately;
        this.participantEmails = participantEmails;
        this.participantUsernames = participantUsernames;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException, WorkflowModelNotStartableException,
            UserDisabledException, UserUnavailableException,
            UsernameNotFoundException {

        // make sure the workflow model is actually startable by finding its
        // corresponding deployed workflow model
        WorkflowModel model = fetchWorkflowModel(evt.getSession());
        DeployedWorkflowModel deployedWorkflowModel = fetchCurrentDeployedWorkflowModel(evt
                .getSession());

        if ((!model.isExecutable()) || (deployedWorkflowModel == null)) {
            throw new WorkflowModelNotStartableException(workflowModelId);
        }

        processUserWorkflowParticipationState();
        // usersThatNeedInvitations and usersThatAreAlreadyTenantMembers are now
        // filled.

        // create the new workflow instance in the database
        createWorkflowInstance(deployedWorkflowModel, evt.getSession());

        // create invitations
        createInvitations(usersThatNeedInvitations, evt.getSession());

        // associate users with the new workflow instance
        associateWithNewWorkflowInstance(usersThatAreAlreadyTenantMembers, evt
                .getSession());
        if (startImmediately) {
            // also associate invited users
            associateWithNewWorkflowInstance(usersThatNeedInvitations, evt
                    .getSession());
        }

        // send notification emails
        for (User invitedUser : usersThatNeedInvitations) {
            NotificationEvents.invitedUserAsWorkflowParticipant(invitedUser,
                    createdWorkflowInstance);
        }
    }

    /**
     * Makes the given list of users participants of the new workflow instance.
     * 
     * @param users
     *            users to make participants of the new workflow instance
     * @param session
     *            current Hibernate session
     */
    private void associateWithNewWorkflowInstance(List<User> users,
            Session session) {
        for (User user : users) {
            UserParticipatesInWorkflow relation = new UserParticipatesInWorkflow();
            relation.setId(new UserParticipatesInWorkflowId(user.getId(),
                    createdWorkflowInstance.getId()));
            relation.setUser(user);
            relation.setWorkflowInstance(createdWorkflowInstance);
            session.save(relation);
        }
    }

    /**
     * Creates workflow participation invitations for the given users.
     * 
     * @param invitedUsers
     *            list of users to invite
     * @param session
     *            current Hibernate sesion
     */
    private void createInvitations(List<User> invitedUsers, Session session) {
        for (User invitedUser : invitedUsers) {
            Invitation invitation = new Invitation();
            invitation.setAdministrateWorkflowModel(null);
            invitation.setCreationDate(DecidrGlobals.getTime().getTime());
            invitation.setJoinTenant(null);
            invitation
                    .setParticipateInWorkflowInstance(createdWorkflowInstance);
            invitation.setReceiver(invitedUser);

            User sender;
            if (role instanceof UserRole) {
                sender = (User) session.load(User.class, role.getActorId());
            } else {
                sender = DecidrGlobals.getSettings().getSuperAdmin();
            }

            invitation.setSender(sender);

            session.save(invitation);
        }
    }

    /**
     * Creates the new workflow instance. If no users need invitations or the
     * immediate start is enforced, the workflow instance is also started on the
     * ODE.
     * 
     * @param deployedModel
     *            workflow model to start;
     * @param session
     *            current Hibernate Session
     */
    @SuppressWarnings("unchecked")
    private void createWorkflowInstance(DeployedWorkflowModel deployedModel,
            Session session) {
        if ((usersThatNeedInvitations.size() == 0) || (startImmediately)) {
            InstanceManager manager = new InstanceManagerImpl();

            Query q = session
                    .createQuery(
                            "from ServerLoadView s where s.serverType.name = :serverType")
                    .setString("serverType", ServerTypeEnum.Ode.toString());

            List<ServerLoadView> serverStatistics = q.list();

            createdWorkflowInstance = manager.startInstance(deployedModel,
                    startConfiguration, serverStatistics);
            createdWorkflowInstance.setStartedDate(DecidrGlobals.getTime()
                    .getTime());

        } else {
            createdWorkflowInstance = new WorkflowInstance();
            createdWorkflowInstance.setStartedDate(null);
        }

        createdWorkflowInstance.setCompletedDate(null);
        createdWorkflowInstance.setDeployedWorkflowModel(deployedModel);
        createdWorkflowInstance.setId(null);
        createdWorkflowInstance.setStartConfiguration(startConfiguration);
    }

    /**
     * Decides which users must be added to the system as new users, which users
     * must be sent an invitation and which users can be added as workflow
     * participants immediately.
     * 
     * @throws TransactionException
     * @throws UserDisabledException
     * @throws UserUnavailableException
     * @throws UsernameNotFoundException
     */
    private void processUserWorkflowParticipationState()
            throws TransactionException, UserDisabledException,
            UserUnavailableException, UsernameNotFoundException {

        GetWorkflowParticipationStateCommand cmd = new GetWorkflowParticipationStateCommand(
                role, workflowModelId, participantUsernames, participantEmails);

        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        Map<User, UserWorkflowParticipationState> map = cmd.getResult();

        // make sure that we know at least the email address of all "unknown"
        // users, otherwise we cannot create new user accounts for them. Also
        // make sure that there are no disabled or unavailable users in the
        // given lists.
        assertOnlyValidUsers(map);

        usersThatAreAlreadyTenantMembers = new ArrayList<User>();
        usersThatNeedInvitations = new ArrayList<User>();

        for (Entry<User, UserWorkflowParticipationState> entry : map.entrySet()) {

            switch (entry.getValue()) {
            case IsAlreadyTenantMember:
                usersThatAreAlreadyTenantMembers.add(entry.getKey());
                break;

            case IsUnknownUser:
                // create users that are unknown to the system
                HibernateTransactionCoordinator.getInstance().runTransaction(
                        new CreateNewUnregisteredUserCommand(role, entry
                                .getKey()));
                usersThatNeedInvitations.add(entry.getKey());
                break;

            case NeedsTenantMembership:
                usersThatNeedInvitations.add(entry.getKey());
                break;

            default:
                break;
            }
        }
    }

    /**
     * Makes sure that the given user state map contains no unknown usernames,
     * disabled or unavailable users by throwing the corresponding exception if
     * one of these is detected.
     * 
     * @param map
     *            user state map to search
     * @throws UserDisabledException
     *             if a disabled user is found
     * @throws UserUnavailableException
     *             if an unavailable user is found
     * @throws UsernameNotFoundException
     *             if an unknown username is detected
     */
    private void assertOnlyValidUsers(
            Map<User, UserWorkflowParticipationState> map)
            throws UserDisabledException, UserUnavailableException,
            UsernameNotFoundException {

        for (User user : map.keySet()) {
            UserWorkflowParticipationState state = map.get(user);
            if (UserWorkflowParticipationState.IsUnknownUser.equals(state)) {

                if ((user.getUserProfile() != null)
                        && (user.getUserProfile().getUsername() != null)) {
                    // the user is unknown but has a username, whoops!
                    throw new UsernameNotFoundException(user.getUserProfile()
                            .getUsername());
                } else if (user.getEmail() == null) {
                    throw new IllegalArgumentException(
                            "An unknown user is missing an email address: cannot send invitation.");
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
    }

    @Override
    public void transactionAborted(TransactionAbortedEvent evt)
            throws TransactionException {
        // an instance may have been started, try to kill it
        InstanceManagerImpl iManager = new InstanceManagerImpl();
        if ((createdWorkflowInstance != null)
                && (createdWorkflowInstance.getStartedDate() != null)) {
            iManager.stopInstance(createdWorkflowInstance);
        }
        createdWorkflowInstance = null;
    }

    /**
     * @return the new workflow instance
     */
    public WorkflowInstance getCreatedWorkflowInstance() {
        return createdWorkflowInstance;
    }

}
