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

package de.decidr.model.notifications;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.entities.ChangeEmailRequest;
import de.decidr.model.entities.Invitation;
import de.decidr.model.entities.PasswordResetRequest;
import de.decidr.model.entities.RegistrationRequest;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.soap.types.AbstractUserList;
import de.decidr.model.soap.types.EmailUser;
import de.decidr.model.webservices.EmailInterface;

/**
 * Provides a very simple means of sending notifications to users.
 * 
 * XXX this class violates DNRY. ~dh
 * 
 * @author Daniel Huss
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public final class NotificationEvents {

    private static final String AMERICAN_DATE_FORMAT = "MM/dd/yyyy ";

    static EmailInterface client;

    /**
     * Informs the user, that he has a new Workitem.
     * 
     * @param newWorkItem
     *            the corresponding workitem which has been created
     */
    public static void createdWorkItem(WorkItem newWorkItem)
            throws TransactionException {

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (Exception e) {
            throw new TransactionException(e);
        }

        // content creation
        String username;

        if (newWorkItem.getUser().getUserProfile() == null) {
            username = newWorkItem.getUser().getEmail();
        } else {
            username = newWorkItem.getUser().getUserProfile().getUsername();
        }

        String tenantName = newWorkItem.getWorkflowInstance()
                .getDeployedWorkflowModel().getTenant().getName();
        String workflowModelName = newWorkItem.getWorkflowInstance()
                .getDeployedWorkflowModel().getName();
        String signature = "";

        String bodyTXT = NotificationText.getNewWorkItemText(username,
                tenantName, workflowModelName, signature);
        String bodyHTML = NotificationText.getNewWorkItemHTML(username,
                tenantName, workflowModelName, signature);
        String subject = NotificationText.getNewWorkItemSubject(tenantName);

        // sender and receiver data creation
        AbstractUserList to = new AbstractUserList();

        SystemSettings settings = DecidrGlobals.getSettings();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        // fill "to" list
        EmailUser user = new EmailUser();
        user.setEmail(newWorkItem.getUser().getEmail());

        List<EmailUser> users = new ArrayList<EmailUser>();
        users.add(user);

        to.getEmailUser().addAll(users);

        try {
            client.sendEmail(to, null, null, fromName, fromAddress, subject,
                    null, bodyTXT, bodyHTML, null);
        } catch (Exception e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Sends a confirmation email to the newly registered user.
     * 
     * @param request
     *            registration request that has been created
     * @throws TransactionException
     */
    public static void createdRegistrationRequest(RegistrationRequest request)
            throws TransactionException {

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (Exception e) {
            throw new TransactionException(e);
        }

        if (request.getUser() == null
                || request.getUser().getUserProfile() == null) {
            throw new IllegalArgumentException(
                    "Registration request is missing user or user profile property.");
        }

        // create email content

        UserProfile profile = request.getUser().getUserProfile();

        String confirmationUrl;
        try {
            confirmationUrl = new URLGenerator().getConfirmRegistrationURL(Long
                    .toString(request.getUser().getId()), request.getAuthKey());
        } catch (UnsupportedEncodingException e) {
            throw new TransactionException(e);
        }

        SystemSettings settings = DecidrGlobals.getSettings();

        Calendar expireDate = DecidrGlobals.getTime();
        expireDate.setTime(request.getCreationDate());
        expireDate.add(Calendar.SECOND, settings
                .getRegistrationRequestLifetimeSeconds());

        String expireDateString = new SimpleDateFormat(AMERICAN_DATE_FORMAT)
                .format(expireDate.getTime());

        String subject = NotificationText.getConfirmRegistrationSubject();
        String bodyHtml = NotificationText.getConfirmRegistrationHTML(profile
                .getUsername(), confirmationUrl, expireDateString, "");
        String bodyText = NotificationText.getConfirmRegistrationText(profile
                .getUsername(), confirmationUrl, expireDateString, "");

        AbstractUserList to = new AbstractUserList();
        EmailUser receiver = new EmailUser();
        receiver.setEmail(request.getUser().getEmail());
        to.getEmailUser().add(receiver);

        try {
            client.sendEmail(to, null, null, settings.getSystemName(), settings
                    .getSystemEmailAddress(), subject, null, bodyText,
                    bodyHtml, null);
        } catch (Exception e) {
            if (e instanceof TransactionException) {
                throw (TransactionException) e;
            } else {
                throw new TransactionException(e);
            }
        }
    }

    /**
     * Sends a confirmation email to the new email address.
     * 
     * @param request
     *            the change email request containing the new email address.
     * @throws TransactionException
     */
    public static void createdChangeEmailRequest(ChangeEmailRequest request)
            throws TransactionException {

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (Exception e) {
            throw new TransactionException(e);
        }

        // content creation
        String signature = "";

        // get userName
        String userName;

        if (request.getUser().getUserProfile() == null) {
            userName = request.getUser().getEmail();
        } else {
            userName = request.getUser().getUserProfile().getUsername();
        }

        // create url
        URLGenerator URLGenerator = new URLGenerator();
        Long userIdLong = request.getId();
        String userId = userIdLong.toString();
        String confirmationUrl;

        try {
            confirmationUrl = URLGenerator.getChangeEmailRequestURL(userId,
                    request.getAuthKey());
        } catch (UnsupportedEncodingException e1) {
            throw new TransactionException(e1);
        }

        // calculate Date
        SystemSettings settings = DecidrGlobals.getSettings();
        int requestLifeTime = settings.getChangeEmailRequestLifetimeSeconds();

        Calendar expireDate = DecidrGlobals.getTime();
        expireDate.setTime(request.getCreationDate());
        expireDate.add(Calendar.SECOND, requestLifeTime);

        SimpleDateFormat sd = new SimpleDateFormat(AMERICAN_DATE_FORMAT);
        String expireDateString = sd.format(expireDate.getTime());

        String bodyTXT = NotificationText.getChangeEmailRequestText(userName,
                confirmationUrl, expireDateString, signature);
        String bodyHTML = NotificationText.getChangeEmailRequestHTML(userName,
                confirmationUrl, expireDateString, signature);
        String subject = NotificationText.getChangeEmailRequestSubject();

        // sender and receiver data creation
        AbstractUserList to = new AbstractUserList();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        // fill "to" list
        EmailUser user = new EmailUser();
        user.setEmail(request.getUser().getEmail());

        List<EmailUser> users = new ArrayList<EmailUser>();
        users.add(user);

        to.getEmailUser().addAll(users);

        try {
            client.sendEmail(to, null, null, fromName, fromAddress, subject,
                    null, bodyTXT, bodyHTML, null);
        } catch (Exception e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Sends the user the password request mail.
     * 
     * @param request
     *            the password request which has been created
     */
    public static void createdPasswordResetRequest(PasswordResetRequest request)
            throws TransactionException {

        URLGenerator URLGenerator = new URLGenerator();

        SystemSettings settings = DecidrGlobals.getSettings();

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (Exception e) {
            throw new TransactionException(e);
        }

        // content creation
        String userName;

        if (request.getUser().getUserProfile() == null) {
            userName = request.getUser().getEmail();
        } else {
            userName = request.getUser().getUserProfile().getUsername();
        }

        String confirmationUrl;

        try {
            confirmationUrl = URLGenerator.getConfirmRegistrationURL(request
                    .getUser().getId().toString(), request.getAuthKey());
        } catch (UnsupportedEncodingException e1) {
            throw new TransactionException(e1);
        }

        // calculate Date
        int requestLifeTime = settings.getPasswordResetRequestLifetimeSeconds();

        Calendar expireDate = DecidrGlobals.getTime();
        expireDate.setTime(request.getCreationDate());
        expireDate.add(Calendar.SECOND, requestLifeTime);

        SimpleDateFormat sd = new SimpleDateFormat(AMERICAN_DATE_FORMAT);
        String expireDateString = sd.format(expireDate.getTime());

        // create body text
        String signature = "";

        String bodyTXT = NotificationText.getPasswordResetRequestText(userName,
                confirmationUrl, expireDateString, signature);
        String bodyHTML = NotificationText.getPasswordResetRequestHTML(
                userName, confirmationUrl, expireDateString, signature);
        String subject = NotificationText.getPasswordResetRequestSubject();

        // sender and receiver data creation
        AbstractUserList to = new AbstractUserList();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        // fill "to" list
        EmailUser user = new EmailUser();
        user.setEmail(request.getUser().getEmail());

        List<EmailUser> users = new ArrayList<EmailUser>();
        users.add(user);

        to.getEmailUser().addAll(users);

        try {
            client.sendEmail(to, null, null, fromName, fromAddress, subject,
                    null, bodyTXT, bodyHTML, null);
        } catch (Exception e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Informs the user that his tenant has been rejected.
     * 
     * @param user
     *            user who created the tenant
     * @param tenantName
     *            name of the tenant which has been rejected
     */
    public static void rejectedTenant(User user, String tenantName)
            throws TransactionException {

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (Exception e) {
            throw new TransactionException(e);
        }

        // create body text
        String signature = "";

        String bodyTXT = NotificationText.getRejectedTenantText(user
                .getUserProfile().getUsername(), tenantName, signature);
        String bodyHTML = NotificationText.getRejectedTenantHTML(user
                .getUserProfile().getUsername(), tenantName, signature);
        String subject = NotificationText.getRejectedTenantSubject();

        // sender and receiver data creation
        AbstractUserList to = new AbstractUserList();

        SystemSettings settings = DecidrGlobals.getSettings();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        // fill "to" list
        EmailUser eUser = new EmailUser();
        eUser.setEmail(user.getEmail());

        List<EmailUser> users = new ArrayList<EmailUser>();
        users.add(eUser);

        to.getEmailUser().addAll(users);

        try {
            client.sendEmail(to, null, null, fromName, fromAddress, subject,
                    null, bodyTXT, bodyHTML, null);
        } catch (Exception e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Informs the registered user that he has been invited as a tenant member.
     * 
     * @param invitation
     *            the corresponding invitation
     */
    public static void invitedRegisteredUserAsTenantMember(Invitation invitation)
            throws TransactionException {

        URLGenerator URLGenerator = new URLGenerator();
        SystemSettings settings = DecidrGlobals.getSettings();

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (Exception e) {
            throw new TransactionException(e);
        }

        String userName = invitation.getReceiver().getUserProfile()
                .getUsername();
        String inviterName = invitation.getSender().getUserProfile()
                .getUsername();
        String tenantName = invitation.getJoinTenant().getName();

        String tenantUrl = "http://" + settings.getDomain() + "/" + tenantName;

        String invitationUrl = "";
        try {
            invitationUrl = URLGenerator.getInvitationURL(invitation
                    .getReceiver().getId().toString(), invitation.getId()
                    .toString(), invitation.getReceiver().getAuthKey());
        } catch (UnsupportedEncodingException e1) {
            new TransactionException(e1);
        }

        // calculate Date
        int invitationLifeTime = settings.getInvitationLifetimeSeconds();

        Calendar expireDate = DecidrGlobals.getTime();
        expireDate.setTime(invitation.getCreationDate());
        expireDate.add(Calendar.SECOND, invitationLifeTime);

        SimpleDateFormat sd = new SimpleDateFormat(AMERICAN_DATE_FORMAT);
        String expireDateString = sd.format(expireDate.getTime());

        // create body text
        String signature = "";

        String bodyTXT = NotificationText
                .getInviteRegisteredUserAsTenantMemberText(userName,
                        inviterName, tenantName, tenantUrl, invitationUrl,
                        expireDateString, signature);
        String bodyHTML = NotificationText
                .getInviteRegisteredUserAsTenantMemberHTML(userName,
                        inviterName, tenantName, tenantUrl, invitationUrl,
                        expireDateString, signature);
        String subject = NotificationText
                .getInviteRegisteredUserAsTenantMemberSubject(tenantName);

        // sender and receiver data creation
        AbstractUserList to = new AbstractUserList();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        // fill "to" list
        EmailUser eUser = new EmailUser();
        eUser.setEmail(invitation.getReceiver().getEmail());

        List<EmailUser> users = new ArrayList<EmailUser>();
        users.add(eUser);

        to.getEmailUser().addAll(users);

        try {
            client.sendEmail(to, null, null, fromName, fromAddress, subject,
                    null, bodyTXT, bodyHTML, null);
        } catch (Exception e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Informs the unregistered User that he has been invited to a tenant as as
     * member.
     * 
     * @param invitation
     *            the corresponding invitation
     */
    public static void invitedUnregisteredUserAsTenantMember(
            Invitation invitation) throws TransactionException {

        URLGenerator URLGenerator = new URLGenerator();
        SystemSettings settings = DecidrGlobals.getSettings();

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (Exception e) {
            throw new TransactionException(e);
        }

        String inviterName = invitation.getSender().getUserProfile()
                .getUsername();
        String tenantName = invitation.getJoinTenant().getName();

        String tenantUrl = "http://" + settings.getDomain() + "/" + tenantName;

        String invitationUrl;
        try {
            invitationUrl = URLGenerator.getInvitationURL(invitation
                    .getReceiver().getId().toString(), invitation.getId()
                    .toString(), invitation.getReceiver().getAuthKey());
        } catch (UnsupportedEncodingException e1) {
            throw new TransactionException(e1);
        }

        // calculate Date
        int invitationLifeTime = settings.getInvitationLifetimeSeconds();

        Calendar expireDate = DecidrGlobals.getTime();
        expireDate.setTime(invitation.getCreationDate());
        expireDate.add(Calendar.SECOND, invitationLifeTime);

        SimpleDateFormat sd = new SimpleDateFormat(AMERICAN_DATE_FORMAT);
        String expireDateString = sd.format(expireDate.getTime());

        // create body text
        String signature = "";

        String bodyTXT = NotificationText
                .getInviteUnregisteredUserAsTenantMemberText(inviterName,
                        tenantName, tenantUrl, invitationUrl, expireDateString,
                        signature);
        String bodyHTML = NotificationText
                .getInviteUnregisteredUserAsTenantMemberHTML(inviterName,
                        tenantName, tenantUrl, invitationUrl, expireDateString,
                        signature);
        String subject = NotificationText
                .getInviteUnregisteredUserAsTenantMemberSubject();

        // sender and receiver data creation
        AbstractUserList to = new AbstractUserList();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        // fill "to" list
        EmailUser eUser = new EmailUser();
        eUser.setEmail(invitation.getReceiver().getEmail());

        List<EmailUser> users = new ArrayList<EmailUser>();
        users.add(eUser);

        to.getEmailUser().addAll(users);

        try {
            client.sendEmail(to, null, null, fromName, fromAddress, subject,
                    null, bodyTXT, bodyHTML, null);
        } catch (Exception e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Informs the user that he has been removed from the tenant.
     * 
     * @param user
     *            the user who has been removed
     * @param tenant
     *            the tenant from which the user has been removed
     */
    public static void removedFromTenant(User user, Tenant tenant)
            throws TransactionException {

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (Exception e) {
            throw new TransactionException(e);
        }

        String userName;

        if (user.getUserProfile() == null) {
            userName = user.getEmail();
        } else {
            userName = user.getUserProfile().getUsername();
        }

        String tenantName = tenant.getName();

        // create body text
        String signature = "";

        String bodyTXT = NotificationText.getRemovedFromTenantText(userName,
                tenantName, signature);
        String bodyHTML = NotificationText.getRemovedFromTenantHTML(userName,
                tenantName, signature);
        String subject = NotificationText.getRemovedFromTenantSubject();

        // sender and receiver data creation
        AbstractUserList to = new AbstractUserList();

        SystemSettings settings = DecidrGlobals.getSettings();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        // fill "to" list
        EmailUser eUser = new EmailUser();
        eUser.setEmail(user.getEmail());

        List<EmailUser> users = new ArrayList<EmailUser>();
        users.add(eUser);

        to.getEmailUser().addAll(users);

        try {
            client.sendEmail(to, null, null, fromName, fromAddress, subject,
                    null, bodyTXT, bodyHTML, null);
        } catch (Exception e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Informs the sender of an invitation that the invitation has been refused.
     * 
     * @param invitation
     *            corresponding invitation
     */
    public static void refusedInvitation(Invitation invitation)
            throws TransactionException {

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (Exception e) {
            throw new TransactionException(e);
        }

        String userName = invitation.getSender().getUserProfile().getUsername();
        String refusedByName = invitation.getReceiver().getUserProfile()
                .getUsername();
        String tenantName = invitation.getJoinTenant().getName();

        // create body text
        String signature = "";

        String bodyTXT = NotificationText.getRefusedInvitationText(userName,
                refusedByName, tenantName, signature);
        String bodyHTML = NotificationText.getRefusedInvitationText(userName,
                refusedByName, tenantName, signature);
        String subject = NotificationText.getRefusedInvitationSubject();

        // sender and receiver data creation
        AbstractUserList to = new AbstractUserList();

        SystemSettings settings = DecidrGlobals.getSettings();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        // fill "to" list
        EmailUser eUser = new EmailUser();
        eUser.setEmail(invitation.getSender().getEmail());

        List<EmailUser> users = new ArrayList<EmailUser>();
        users.add(eUser);

        to.getEmailUser().addAll(users);

        try {
            client.sendEmail(to, null, null, fromName, fromAddress, subject,
                    null, bodyTXT, bodyHTML, null);
        } catch (Exception e) {
            throw new TransactionException(e);
        }
    }

    /**
     * 
     * Informs the user that he has generated a new password for his account.
     * The password will be delivered too.
     * 
     * @param user
     *            the user which should be informed
     * @param newPassword
     *            the new password as string
     */
    public static void generatedNewPassword(User user, String newPassword)
            throws TransactionException {

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (Exception e) {
            throw new TransactionException(e);
        }

        String userName;

        if (user.getUserProfile() == null) {
            userName = user.getEmail();
        } else {
            userName = user.getUserProfile().getUsername();
        }

        // create body text
        String signature = "";

        String bodyTXT = NotificationText.getGeneratedNewPasswordText(userName,
                newPassword, signature);
        String bodyHTML = NotificationText.getGeneratedNewPasswordHTML(
                userName, newPassword, signature);
        String subject = NotificationText.getGeneratedNewPasswordSubject();

        // sender and receiver data creation
        AbstractUserList to = new AbstractUserList();

        SystemSettings settings = DecidrGlobals.getSettings();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        // fill "to" list
        EmailUser eUser = new EmailUser();
        eUser.setEmail(user.getEmail());

        List<EmailUser> users = new ArrayList<EmailUser>();
        users.add(eUser);

        to.getEmailUser().addAll(users);

        try {
            client.sendEmail(to, null, null, fromName, fromAddress, subject,
                    null, bodyTXT, bodyHTML, null);
        } catch (Exception e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Send an email to the super admin that he should start a new ODE instance.
     * 
     * If parameter &quot;where == null&quot; = anywhere
     */
    public static void requestNewODEInstance(String where)
            throws TransactionException {

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (Exception e) {
            throw new TransactionException(e);
        }

        // create body text
        // String signature="";

        String bodyTXT = NotificationText.getRequestNewODEInstanceText(where);
        String bodyHTML = NotificationText.getRequestNewODEInstanceHTML(where);
        String subject = NotificationText.getRequestNewODEInstanceSubject();

        // sender and receiver data creation
        AbstractUserList to = new AbstractUserList();

        SystemSettings settings = DecidrGlobals.getSettings();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        // fill "to" list
        EmailUser eUser = new EmailUser();
        eUser.setEmail(DecidrGlobals.getSettings().getSuperAdmin().getEmail());

        List<EmailUser> users = new ArrayList<EmailUser>();
        users.add(eUser);

        to.getEmailUser().addAll(users);

        try {
            client.sendEmail(to, null, null, fromName, fromAddress, subject,
                    null, bodyTXT, bodyHTML, null);
        } catch (Exception e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Sends a notification email to invitedUser that informs the user that he
     * has to register at the DecidR web site and join the tenant that owns the
     * workflow model if he wants to accept the workflow admin invitation.
     * 
     * @param invitedUser
     *            receiver of the notification
     * @param model
     *            workflow model [and tenant via getTenant()] to administrate
     */
    public static void invitedUnregisteredUserAsWorkflowAdmin(
            Invitation invitation, WorkflowModel model)
            throws TransactionException {

        URLGenerator URLGenerator = new URLGenerator();

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (Exception e) {
            throw new TransactionException(e);
        }

        // create body text
        String signature = "";

        // get expire Date
        Calendar expireDate = DecidrGlobals.getTime();
        expireDate.setTime(invitation.getCreationDate());
        expireDate.add(Calendar.SECOND, DecidrGlobals.getSettings()
                .getInvitationLifetimeSeconds());

        SimpleDateFormat sd = new SimpleDateFormat(AMERICAN_DATE_FORMAT);
        String expireDateString = sd.format(expireDate.getTime());

        String tenantName = model.getTenant().getName();
        String invitationUrl;
        try {
            invitationUrl = URLGenerator.getInvitationURL(invitation
                    .getReceiver().getId().toString(), invitation.getId()
                    .toString(), invitation.getReceiver().getAuthKey());
        } catch (UnsupportedEncodingException e1) {
            throw new TransactionException(e1);
        }

        String bodyTXT = NotificationText
                .getInvitedUnregisteredUserAsWorkflowAdminText(tenantName,
                        invitationUrl, expireDateString, signature);
        String bodyHTML = NotificationText
                .getInvitedUnregisteredUserAsWorkflowAdminHTML(tenantName,
                        invitationUrl, expireDateString, signature);
        String subject = NotificationText
                .getInvitedUnregisteredUserAsWorkflowAdminSubject();

        // sender and receiver data creation
        AbstractUserList to = new AbstractUserList();

        SystemSettings settings = DecidrGlobals.getSettings();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        // fill "to" list
        EmailUser eUser = new EmailUser();
        eUser.setEmail(DecidrGlobals.getSettings().getSuperAdmin().getEmail());

        List<EmailUser> users = new ArrayList<EmailUser>();
        users.add(eUser);

        to.getEmailUser().addAll(users);

        try {
            client.sendEmail(to, null, null, fromName, fromAddress, subject,
                    null, bodyTXT, bodyHTML, null);
        } catch (Exception e) {
            throw new TransactionException(e);
        }

    }

    /**
     * Sends a notification email to invitedUser that informs the already
     * registered user that he has to join the tenant that owns the workflow
     * model if he wants to accept the workflow admin invitation.
     * 
     * @param invitedUser
     *            receiver of the notification
     * @param model
     *            workflow model [and tenant via getTenant()] to administrate
     */
    public static void invitedRegisteredUserAsWorkflowAdmin(
            Invitation invitation, WorkflowModel model)
            throws TransactionException {

        URLGenerator URLGenerator = new URLGenerator();

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (Exception e) {
            throw new TransactionException(e);
        }

        // create body text
        String signature = "";

        // get expire Date
        Calendar expireDate = DecidrGlobals.getTime();
        expireDate.setTime(invitation.getCreationDate());
        expireDate.add(Calendar.SECOND, DecidrGlobals.getSettings()
                .getInvitationLifetimeSeconds());

        SimpleDateFormat sd = new SimpleDateFormat(AMERICAN_DATE_FORMAT);
        String expireDateString = sd.format(expireDate.getTime());

        String tenantName = model.getTenant().getName();
        String userName = invitation.getReceiver().getUserProfile()
                .getUsername();

        String invitationUrl;
        try {
            invitationUrl = URLGenerator.getInvitationURL(invitation
                    .getReceiver().getId().toString(), invitation.getId()
                    .toString(), invitation.getReceiver().getAuthKey());
        } catch (UnsupportedEncodingException e1) {
            throw new TransactionException(e1);
        }

        String bodyTXT = NotificationText
                .getInvitedRegisteredUserAsWorkflowAdminText(userName,
                        invitationUrl, expireDateString, tenantName, signature);
        String bodyHTML = NotificationText
                .getInvitedRegisteredUserAsWorkflowAdminHTML(userName,
                        invitationUrl, expireDateString, tenantName, signature);
        String subject = NotificationText
                .getInvitedRegisteredUserAsWorkflowAdminSubject();

        // sender and receiver data creation
        AbstractUserList to = new AbstractUserList();

        SystemSettings settings = DecidrGlobals.getSettings();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        // fill "to" list
        EmailUser eUser = new EmailUser();
        eUser.setEmail(DecidrGlobals.getSettings().getSuperAdmin().getEmail());

        List<EmailUser> users = new ArrayList<EmailUser>();
        users.add(eUser);

        to.getEmailUser().addAll(users);

        try {
            client.sendEmail(to, null, null, fromName, fromAddress, subject,
                    null, bodyTXT, bodyHTML, null);
        } catch (Exception e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Sends a notification email to a registered user that he was invited to
     * participate in a workflow of a tenant that he is not a member of.
     * 
     * @param invitedUser
     *            receiver of the notification
     * @param createdWorkflowInstance
     *            workflow instance in which the invited user should participate
     */
    public static void invitedRegisteredUserAsWorkflowParticipant(
            Invitation invitation, WorkflowInstance createdWorkflowInstance)
            throws TransactionException {

        URLGenerator URLGenerator = new URLGenerator();

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (Exception e) {
            throw new TransactionException(e);
        }

        // create body text
        String signature = "";

        // get expire Date
        Calendar expireDate = DecidrGlobals.getTime();
        expireDate.setTime(invitation.getCreationDate());
        expireDate.add(Calendar.SECOND, DecidrGlobals.getSettings()
                .getInvitationLifetimeSeconds());

        SimpleDateFormat sd = new SimpleDateFormat(AMERICAN_DATE_FORMAT);
        String expireDateString = sd.format(expireDate.getTime());

        String tenantName = createdWorkflowInstance.getDeployedWorkflowModel()
                .getTenant().getName();
        String workflowName = createdWorkflowInstance
                .getDeployedWorkflowModel().getName();

        String invitationUrl;
        try {
            invitationUrl = URLGenerator.getInvitationURL(invitation
                    .getReceiver().getId().toString(), invitation.getId()
                    .toString(), invitation.getReceiver().getAuthKey());
        } catch (UnsupportedEncodingException e1) {
            throw new TransactionException(e1);
        }

        String bodyTXT = NotificationText
                .getInvitedUserAsWorkflowParticipantText(tenantName,
                        invitationUrl, expireDateString, workflowName,
                        signature);
        String bodyHTML = NotificationText
                .getInvitedUserAsWorkflowParticipantHTML(tenantName,
                        invitationUrl, expireDateString, workflowName,
                        signature);
        String subject = NotificationText
                .getInvitedUserAsWorkflowParticipantSubject();

        // sender and receiver data creation
        AbstractUserList to = new AbstractUserList();

        SystemSettings settings = DecidrGlobals.getSettings();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        // fill "to" list
        EmailUser eUser = new EmailUser();
        eUser.setEmail(DecidrGlobals.getSettings().getSuperAdmin().getEmail());

        List<EmailUser> users = new ArrayList<EmailUser>();
        users.add(eUser);

        to.getEmailUser().addAll(users);

        try {
            client.sendEmail(to, null, null, fromName, fromAddress, subject,
                    null, bodyTXT, bodyHTML, null);
        } catch (Exception e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Sends a notification email to an unregistered user that he was invited to
     * participate in a workflow of a tenant that he is not a member of.
     * 
     * @param invitedUser
     *            receiver of the notification
     * @param createdWorkflowInstance
     *            workflow instance in which the invited user should participate
     */
    public static void invitedUnregisteredUserAsWorkflowParticipant(
            Invitation invitation, WorkflowInstance createdWorkflowInstance)
            throws TransactionException {

        URLGenerator URLGenerator = new URLGenerator();

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (Exception e) {
            throw new TransactionException(e);
        }

        // create body text
        String signature = "";

        // get expire Date
        Calendar expireDate = DecidrGlobals.getTime();
        expireDate.setTime(invitation.getCreationDate());
        expireDate.add(Calendar.SECOND, DecidrGlobals.getSettings()
                .getInvitationLifetimeSeconds());

        SimpleDateFormat sd = new SimpleDateFormat(AMERICAN_DATE_FORMAT);
        String expireDateString = sd.format(expireDate.getTime());

        String tenantName = createdWorkflowInstance.getDeployedWorkflowModel()
                .getTenant().getName();
        String workflowName = createdWorkflowInstance
                .getDeployedWorkflowModel().getName();

        String invitationUrl;
        try {
            invitationUrl = URLGenerator.getInvitationURL(invitation
                    .getReceiver().getId().toString(), invitation.getId()
                    .toString(), invitation.getReceiver().getAuthKey());
        } catch (UnsupportedEncodingException e1) {
            throw new TransactionException(e1);
        }

        String bodyTXT = NotificationText
                .getInvitedUserAsWorkflowParticipantText(tenantName,
                        invitationUrl, expireDateString, workflowName,
                        signature);
        String bodyHTML = NotificationText
                .getInvitedUserAsWorkflowParticipantHTML(tenantName,
                        invitationUrl, expireDateString, workflowName,
                        signature);
        String subject = NotificationText
                .getInvitedUserAsWorkflowParticipantSubject();

        // sender and receiver data creation
        AbstractUserList to = new AbstractUserList();

        SystemSettings settings = DecidrGlobals.getSettings();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        // fill "to" list
        EmailUser eUser = new EmailUser();
        eUser.setEmail(DecidrGlobals.getSettings().getSuperAdmin().getEmail());

        List<EmailUser> users = new ArrayList<EmailUser>();
        users.add(eUser);

        to.getEmailUser().addAll(users);

        try {
            client.sendEmail(to, null, null, fromName, fromAddress, subject,
                    null, bodyTXT, bodyHTML, null);
        } catch (Exception e) {
            throw new TransactionException(e);
        }
    }
}
