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

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.entities.Invitation;
import de.decidr.model.entities.PasswordResetRequest;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.soap.types.AbstractUser;
import de.decidr.model.soap.types.AbstractUserList;
import de.decidr.model.soap.types.EmailUser;
import de.decidr.model.soap.types.IDList;
import de.decidr.model.soap.types.StringMap;
import de.decidr.model.webservices.EmailInterface;

/**
 * Provides a very simple means of sending notifications to users.
 * 
 * @author Daniel Huss
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public final class NotificationEvents {

    static EmailInterface client;

    /**
     * Informs the user, that he has a new Workitem.
     * 
     * @param newWorkItem
     *            the corresponding workitem which has been created
     */
    @SuppressWarnings("unchecked")
    public static void createdWorkItem(WorkItem newWorkItem)
            throws TransactionException {

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (MalformedURLException e) {
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
        AbstractUserList cc = new AbstractUserList();
        AbstractUserList bcc = new AbstractUserList();

        SystemSettings settings = DecidrGlobals.getSettings();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        StringMap headers = null;
        IDList attachements = null;

        // fill "to" list
        EmailUser user = new EmailUser();
        user.setUser(newWorkItem.getUser().getEmail());

        List<AbstractUser> users = new ArrayList();
        users.add(user);

        to.setAbstractUser(users);

        try {
            client.sendEmail(to, cc, bcc, fromName, fromAddress, subject,
                    headers, bodyTXT, bodyHTML, attachements);
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
    @SuppressWarnings("unchecked")
    public static void createdPasswordResetRequest(PasswordResetRequest request)
            throws TransactionException {

        SystemSettings settings = DecidrGlobals.getSettings();

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (MalformedURLException e) {
            throw new TransactionException(e);
        }

        // content creation
        String userName;

        if (request.getUser().getUserProfile() == null) {
            userName = request.getUser().getEmail();
        } else {
            userName = request.getUser().getUserProfile().getUsername();
        }

        // FIXME Where to I get this? ID Password Reset Request
        String confirmationUrl = "";

        // calculate Date
        int requestLifeTime = settings.getPasswordResetRequestLifetimeSeconds();

        Calendar creationDate = Calendar.getInstance();
        creationDate.setTime(request.getCreationDate());
        creationDate.add(Calendar.SECOND, requestLifeTime);

        SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
        String expireDate = sd.format(new Date());

        // create body text
        String signature = "";

        String bodyTXT = NotificationText.getPasswordResetRequestText(userName,
                confirmationUrl, expireDate, signature);
        String bodyHTML = NotificationText.getPasswordResetRequestHTML(
                userName, confirmationUrl, expireDate, signature);
        String subject = NotificationText.getPasswordResetRequestSubject();

        // sender and receiver data creation
        AbstractUserList to = new AbstractUserList();
        AbstractUserList cc = new AbstractUserList();
        AbstractUserList bcc = new AbstractUserList();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        StringMap headers = null;
        IDList attachements = null;

        // fill "to" list
        EmailUser user = new EmailUser();
        user.setUser(request.getUser().getEmail());

        List<AbstractUser> users = new ArrayList();
        users.add(user);

        to.setAbstractUser(users);

        try {
            client.sendEmail(to, cc, bcc, fromName, fromAddress, subject,
                    headers, bodyTXT, bodyHTML, attachements);
        } catch (Exception e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Informs the user, that is tenant has been disapproved.
     * 
     * @param user
     *            user who created the tenant
     * @param tenantName
     *            name of the tenant which has been disapproved
     */
    @SuppressWarnings("unchecked")
    public static void disapprovedTenant(User user, String tenantName)
            throws TransactionException {

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (MalformedURLException e) {
            throw new TransactionException(e);
        }

        // create body text
        String signature = "";

        String bodyTXT = NotificationText.getDisapprovedTenantText(user
                .getUserProfile().getUsername(), tenantName, signature);
        String bodyHTML = NotificationText.getDisapprovedTenantHTML(user
                .getUserProfile().getUsername(), tenantName, signature);
        String subject = NotificationText.getDisapprovedTenantSubject();

        // sender and receiver data creation
        AbstractUserList to = new AbstractUserList();
        AbstractUserList cc = new AbstractUserList();
        AbstractUserList bcc = new AbstractUserList();

        SystemSettings settings = DecidrGlobals.getSettings();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        StringMap headers = null;
        IDList attachements = null;

        // fill "to" list
        EmailUser eUser = new EmailUser();
        eUser.setUser(user.getEmail());

        List<AbstractUser> users = new ArrayList();
        users.add(eUser);

        to.setAbstractUser(users);

        try {
            client.sendEmail(to, cc, bcc, fromName, fromAddress, subject,
                    headers, bodyTXT, bodyHTML, attachements);
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
    @SuppressWarnings("unchecked")
    public static void invitedRegisteredUserAsTenantMember(Invitation invitation)
            throws TransactionException {

        SystemSettings settings = DecidrGlobals.getSettings();

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (MalformedURLException e) {
            throw new TransactionException(e);
        }

        String userName = invitation.getReceiver().getUserProfile()
                .getUsername();
        String inviterName = invitation.getSender().getUserProfile()
                .getUsername();
        String tenantName = invitation.getJoinTenant().getName();

        String tenantUrl = "http://" + settings.getDomain() + "/" + tenantName;

        // FIXME Where to I get this? ID von invitation Objekt
        String invitationUrl = "http://" + settings.getDomain() + "/";

        // calculate Date
        int invitationLifeTime = settings.getInvitationLifetimeSeconds();

        Calendar creationDate = Calendar.getInstance();
        creationDate.setTime(invitation.getCreationDate());
        creationDate.add(Calendar.SECOND, invitationLifeTime);

        SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
        String expireDate = sd.format(new Date());

        // create body text
        String signature = "";

        String bodyTXT = NotificationText
                .getInviteRegisteredUserAsTenantMemberText(userName,
                        inviterName, tenantName, tenantUrl, invitationUrl,
                        expireDate, signature);
        String bodyHTML = NotificationText
                .getInviteRegisteredUserAsTenantMemberHTML(userName,
                        inviterName, tenantName, tenantUrl, invitationUrl,
                        expireDate, signature);
        String subject = NotificationText
                .getInviteRegisteredUserAsTenantMemberSubject(tenantName);

        // sender and receiver data creation
        AbstractUserList to = new AbstractUserList();
        AbstractUserList cc = new AbstractUserList();
        AbstractUserList bcc = new AbstractUserList();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        StringMap headers = null;
        IDList attachements = null;

        // fill "to" list
        EmailUser eUser = new EmailUser();
        eUser.setUser(invitation.getReceiver().getEmail());

        List<AbstractUser> users = new ArrayList();
        users.add(eUser);

        to.setAbstractUser(users);

        try {
            client.sendEmail(to, cc, bcc, fromName, fromAddress, subject,
                    headers, bodyTXT, bodyHTML, attachements);
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
    @SuppressWarnings("unchecked")
    public static void invitedUnregisteredUserAsTenantMember(
            Invitation invitation) throws TransactionException {

        SystemSettings settings = DecidrGlobals.getSettings();

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (MalformedURLException e) {
            throw new TransactionException(e);
        }

        String inviterName = invitation.getSender().getUserProfile()
                .getUsername();
        String tenantName = invitation.getJoinTenant().getName();

        String tenantUrl = "http://" + settings.getDomain() + "/" + tenantName;

        // FIXME Where to I get this? ID von invitation Objekt
        String invitationUrl = "";

        // calculate Date
        int invitationLifeTime = settings.getInvitationLifetimeSeconds();

        Calendar creationDate = Calendar.getInstance();
        creationDate.setTime(invitation.getCreationDate());
        creationDate.add(Calendar.SECOND, invitationLifeTime);

        SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
        String expireDate = sd.format(new Date());

        // create body text
        String signature = "";

        String bodyTXT = NotificationText
                .getInviteUnregisteredUserAsTenantMemberText(inviterName,
                        tenantName, tenantUrl, invitationUrl, expireDate,
                        signature);
        String bodyHTML = NotificationText
                .getInviteUnregisteredUserAsTenantMemberHTML(inviterName,
                        tenantName, tenantUrl, invitationUrl, expireDate,
                        signature);
        String subject = NotificationText
                .getInviteUnregisteredUserAsTenantMemberSubject();

        // sender and receiver data creation
        AbstractUserList to = new AbstractUserList();
        AbstractUserList cc = new AbstractUserList();
        AbstractUserList bcc = new AbstractUserList();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        StringMap headers = null;
        IDList attachements = null;

        // fill "to" list
        EmailUser eUser = new EmailUser();
        eUser.setUser(invitation.getReceiver().getEmail());

        List<AbstractUser> users = new ArrayList();
        users.add(eUser);

        to.setAbstractUser(users);

        try {
            client.sendEmail(to, cc, bcc, fromName, fromAddress, subject,
                    headers, bodyTXT, bodyHTML, attachements);
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
    @SuppressWarnings("unchecked")
    public static void removedFromTenant(User user, Tenant tenant)
            throws TransactionException {

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (MalformedURLException e) {
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
        AbstractUserList cc = new AbstractUserList();
        AbstractUserList bcc = new AbstractUserList();

        SystemSettings settings = DecidrGlobals.getSettings();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        StringMap headers = null;
        IDList attachements = null;

        // fill "to" list
        EmailUser eUser = new EmailUser();
        eUser.setUser(user.getEmail());

        List<AbstractUser> users = new ArrayList();
        users.add(eUser);

        to.setAbstractUser(users);

        try {
            client.sendEmail(to, cc, bcc, fromName, fromAddress, subject,
                    headers, bodyTXT, bodyHTML, attachements);
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
    @SuppressWarnings("unchecked")
    public static void refusedInvitation(Invitation invitation)
            throws TransactionException {

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (MalformedURLException e) {
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
        AbstractUserList cc = new AbstractUserList();
        AbstractUserList bcc = new AbstractUserList();

        SystemSettings settings = DecidrGlobals.getSettings();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        StringMap headers = null;
        IDList attachements = null;

        // fill "to" list
        EmailUser eUser = new EmailUser();
        eUser.setUser(invitation.getSender().getEmail());

        List<AbstractUser> users = new ArrayList();
        users.add(eUser);

        to.setAbstractUser(users);

        try {
            client.sendEmail(to, cc, bcc, fromName, fromAddress, subject,
                    headers, bodyTXT, bodyHTML, attachements);
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
    @SuppressWarnings("unchecked")
    public static void generatedNewPassword(User user, String newPassword)
            throws TransactionException {

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (MalformedURLException e) {
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
        AbstractUserList cc = new AbstractUserList();
        AbstractUserList bcc = new AbstractUserList();

        SystemSettings settings = DecidrGlobals.getSettings();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        StringMap headers = null;
        IDList attachements = null;

        // fill "to" list
        EmailUser eUser = new EmailUser();
        eUser.setUser(user.getEmail());

        List<AbstractUser> users = new ArrayList();
        users.add(eUser);

        to.setAbstractUser(users);

        try {
            client.sendEmail(to, cc, bcc, fromName, fromAddress, subject,
                    headers, bodyTXT, bodyHTML, attachements);
        } catch (Exception e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Send an email to the super admin that he should start a new ODE instance.
     * 
     * If parameter &quot;where == null&quot; = anywhere
     */
    @SuppressWarnings("unchecked")
    public static void requestNewODEInstance(String where)
            throws TransactionException {

        try {
            client = de.decidr.model.webservices.DynamicClients
                    .getEmailClient();
        } catch (MalformedURLException e) {
            throw new TransactionException(e);
        }

        // create body text
        // String signature="";

        String bodyTXT = NotificationText.getRequestNewODEInstanceText(where);
        String bodyHTML = NotificationText.getRequestNewODEInstanceHTML(where);
        String subject = NotificationText.getRequestNewODEInstanceSubject();

        // sender and receiver data creation
        AbstractUserList to = new AbstractUserList();
        AbstractUserList cc = new AbstractUserList();
        AbstractUserList bcc = new AbstractUserList();

        SystemSettings settings = DecidrGlobals.getSettings();

        String fromAddress = settings.getSystemEmailAddress();
        String fromName = settings.getSystemName();

        StringMap headers = null;
        IDList attachements = null;

        // fill "to" list
        EmailUser eUser = new EmailUser();
        eUser.setUser(DecidrGlobals.getSettings().getSuperAdmin().getEmail());

        List<AbstractUser> users = new ArrayList();
        users.add(eUser);

        to.setAbstractUser(users);

        try {
            client.sendEmail(to, cc, bcc, fromName, fromAddress, subject,
                    headers, bodyTXT, bodyHTML, attachements);
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
    public static void invitedUnregisteredUserAsWorkflowAdmin(User invitedUser,
            WorkflowModel model) {
        // FIXME Auto-generated method stub
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
    public static void invitedRegisteredUserAsWorkflowAdmin(User invitedUser,
            WorkflowModel model) {
        // FIXME Auto-generated method stub

    }

    /**
     * Sends a notification email to invited user that informs the (registered or
     * unregistered) user that he was invited to participate in a workflow of a
     * tenant that he is not a member of.
     * 
     * @param invitedUser
     *            receiver of the notification
     * @param createdWorkflowInstance
     *            workflow instance in which the invited user should participate
     */
    public static void invitedUserAsWorkflowParticipant(User invitedUser,
            WorkflowInstance createdWorkflowInstance) {
        // FIXME Auto-generated method stub

    }
}
