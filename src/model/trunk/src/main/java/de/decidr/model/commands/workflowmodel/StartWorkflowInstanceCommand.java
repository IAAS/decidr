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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.mail.internet.InternetAddress;
import javax.xml.bind.JAXBException;

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
import de.decidr.model.workflowmodel.dwdl.translator.TransformUtil;
import de.decidr.model.workflowmodel.instancemanagement.InstanceManager;
import de.decidr.model.workflowmodel.instancemanagement.InstanceManagerImpl;
import de.decidr.model.workflowmodel.wsc.TActor;
import de.decidr.model.workflowmodel.wsc.TConfiguration;
import de.decidr.model.workflowmodel.wsc.TRole;
import de.decidr.model.workflowmodel.wsc.TRoles;

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

    private TConfiguration startConfiguration;
    private WorkflowInstance createdWorkflowInstance = null;
    private Boolean startImmediately;

    /**
     * Lists of participants depending on which field is known: id, username or
     * email
     */
    private List<String> participantUsernames = null;
    private List<String> participantEmails = null;
    private List<Long> participantIds = null;

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
     */
    public StartWorkflowInstanceCommand(Role role, Long workflowModelId,
            TConfiguration startConfiguration, Boolean startImmediately) {
        super(role, workflowModelId);
        this.startConfiguration = startConfiguration;
        this.startImmediately = startImmediately;
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

        extractParticipantsFromStartConfiguration();
        // the lists of usernames and emails are now filled

        processUserWorkflowParticipationState();
        // usersThatNeedInvitations and usersThatAreAlreadyTenantMembers are now
        // filled.

        // create the new workflow instance in the database
        try {
            createWorkflowInstance(deployedWorkflowModel, evt.getSession());
        } catch (JAXBException e) {
            throw new TransactionException(e);
        }

        // create invitations
        Set<Invitation> invitations = createInvitations(
                usersThatNeedInvitations, evt.getSession());

        // associate users with the new workflow instance
        associateWithNewWorkflowInstance(usersThatAreAlreadyTenantMembers, evt
                .getSession());
        if (startImmediately) {
            // also associate invited users
            associateWithNewWorkflowInstance(usersThatNeedInvitations, evt
                    .getSession());
        }

        // send notification emails
        for (Invitation invitation : invitations) {
            if (invitation.getReceiver().getUserProfile() != null) {
                NotificationEvents.invitedRegisteredUserAsWorkflowParticipant(
                        invitation, createdWorkflowInstance);
            } else {
                NotificationEvents
                        .invitedUnregisteredUserAsWorkflowParticipant(
                                invitation, createdWorkflowInstance);
            }
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
     * Fills the lists <code>participantIds</code>,
     * <code>participantEmails</code> and <code>participantUsernames</code>
     * using the current start configuration.
     */
    private void extractParticipantsFromStartConfiguration() {
        participantEmails = new ArrayList<String>();
        participantUsernames = new ArrayList<String>();
        participantIds = new ArrayList<Long>();

        TRoles roles = startConfiguration.getRoles();
        for (TRole role : roles.getRole()) {
            for (TActor actor : role.getActor()) {
                Long userId = null;
                try {
                    userId = Long.parseLong(actor.getUserId());
                } catch (NumberFormatException e) {
                    // the user ID is not set
                }

                if (userId == null
                        || userId.longValue() < UserRole.MIN_VALID_USER_ID) {
                    // the user ID is not set or invalid.
                    if (actor.getEmail() != null && !actor.getEmail().isEmpty()) {
                        // we can identify the user by his email address
                        participantEmails.add(actor.getEmail());
                    } else if (actor.getName() != null
                            && !actor.getName().isEmpty()) {
                        // we can identify the user by his username (or conclude
                        // that no such user is known to the system)
                        participantUsernames.add(actor.getName());
                    } else {
                        // this is bad, we cannot identify this actor at all!
                        throw new IllegalArgumentException(
                                "The start configuration contains an actor entry which has no user ID, username or e-mail address.");
                    }
                } else {
                    // the user id has already been set - less work for us!
                    participantIds.add(userId);
                }
            }
        }
    }

    /**
     * Creates workflow participation invitations for the given users.
     * 
     * @param invitedUsers
     *            list of users to invite
     * @param session
     *            current Hibernate sesion
     * @return
     */
    @SuppressWarnings("unchecked")
    private Set<Invitation> createInvitations(List<User> invitedUsers,
            Session session) {

        Set<Invitation> invis = new HashSet();

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
            invis.add(invitation);
        }

        return invis;
    }

    /**
     * Creates and persists a new workflow instance. If no users need
     * invitations or the immediate start is enforced, the workflow instance is
     * also started on the ODE.
     * 
     * @param deployedModel
     *            workflow model to start;
     * @param session
     *            current Hibernate Session
     * @throws JAXBException
     */
    @SuppressWarnings("unchecked")
    private void createWorkflowInstance(DeployedWorkflowModel deployedModel,
            Session session) throws JAXBException {

        byte[] binaryStartConfig = TransformUtil
                .configuration2Bytes(startConfiguration);

        if ((usersThatNeedInvitations.size() == 0) || (startImmediately)) {
            InstanceManager manager = new InstanceManagerImpl();

            Query q = session
                    .createQuery(
                            "from ServerLoadView s where s.serverType.name = :serverType")
                    .setString("serverType", ServerTypeEnum.Ode.toString());

            List<ServerLoadView> serverStatistics = q.list();

            createdWorkflowInstance = manager.startInstance(deployedModel,
                    binaryStartConfig, serverStatistics);
            createdWorkflowInstance.setStartedDate(DecidrGlobals.getTime()
                    .getTime());

        } else {
            createdWorkflowInstance = new WorkflowInstance();
            createdWorkflowInstance.setStartedDate(null);
        }

        createdWorkflowInstance.setCompletedDate(null);
        createdWorkflowInstance.setDeployedWorkflowModel(deployedModel);
        createdWorkflowInstance.setId(null);
        createdWorkflowInstance.setStartConfiguration(binaryStartConfig);

        session.save(createdWorkflowInstance);
    }

    /**
     * Decides which users must be added to the system as new users, which users
     * must be sent an invitation and which users can be added as workflow
     * participants immediately. <br>
     * The start configuration is updated accordingly (the missing ids are
     * filled in).
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
                role, workflowModelId, participantIds, participantUsernames,
                participantEmails);

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
            User user = entry.getKey();

            switch (entry.getValue()) {
            case IsAlreadyTenantMember:
                usersThatAreAlreadyTenantMembers.add(user);
                break;

            case IsUnknownUser:
                // create users that are unknown to the system
                HibernateTransactionCoordinator.getInstance().runTransaction(
                        new CreateNewUnregisteredUserCommand(role, user));
                usersThatNeedInvitations.add(user);
                break;

            case NeedsTenantMembership:
                usersThatNeedInvitations.add(user);
                break;

            default:
                break;
            }

            // now the user object is always a *persisted* User instance, so we
            // may update the start configuration accordingly
            completeStartConfigEntry(user);
        }
    }

    /**
     * Finds all actor entries in the start configuration that can be completed
     * using the given user object.
     * <ul>
     * <li>If a start config entry has a user id, the rest is deduced from the
     * user object if the two IDs match</li>
     * <li>If a start config entry has no user id, but the email or username
     * field matches the one of the user object, the id , email and username
     * will be deduced from the user obect as well.</li>
     * </ul>
     * 
     * @param user
     */
    private void completeStartConfigEntry(User user) {
        for (TRole role : startConfiguration.getRoles().getRole()) {
            for (TActor actor : role.getActor()) {
                Long userId = null;
                try {
                    userId = Long.parseLong(actor.getUserId());
                } catch (NumberFormatException e) {
                    // cannot parse to long - nothing needs to be done
                }

                // comparing just using string equality is insufficient for
                // email addresses.
                InternetAddress userEmail = new InternetAddress();
                InternetAddress actorEmail = new InternetAddress();

                try {
                    userEmail.setAddress(user.getEmail());
                    userEmail.setPersonal("");
                    actorEmail.setAddress(actor.getEmail());
                    actorEmail.setPersonal("");
                } catch (UnsupportedEncodingException e) {
                    // this exception will not be thrown as long as the empty
                    // string is passed to setPersonal()
                }
                
                //both usernames are converted to lowercase for comparison
                String actorUsername = actor.getName();
                if (actorUsername != null) {
                    actorUsername = actorUsername.toLowerCase();
                }
                String username = user.getUserProfile() != null ? user.getUserProfile().getUsername();
                if (username != null) {
                    username = username.toLowerCase();
                }
                
                if (userId != null && userId.equals(user.getId())) {
                    // the user id of the actor has been set and matches.
                        copyUserToActor(user, actor);
                } else if (actor.getEmail() != null
                        && !actor.getEmail().isEmpty()
                        && actorEmail.equals(userEmail)) {
                    //the email has been set and matches
                    copyUserToActor(user, actor);
                } else if (actorUsername != null && actorUsername.equals(username)) {
                    // the username has been set and matches
                    copyUserToActor(user, actor);
                }
            }
        }
    }

    /**
     * Copies the given user's properties to the given actor
     * 
     * @param user
     *            source user (must not be null)
     * @param actor
     *            target actor (must not be null)
     */
    private void copyUserToActor(User user, TActor actor) {
        actor.setUserId(user.getId().toString());
        actor.setName(user.getUserProfile() != null ? user.getUserProfile()
                .getUsername() : "");
        actor.setEmail(user.getEmail());
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
