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

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Provides text templates for notifications
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class NotificationText {

    private static String crlf = "\n";
    
    // make sure the english templates are selected,
    // since we only use one set of templates for now
    private static Locale lang = new Locale("en");
    private static ResourceBundle bundle = ResourceBundle.getBundle("notifications.EmailNotifications",
                                                                    lang);

    /**
     * Returns the email text which is sent to a user, whose account has been
     * (re-)activated by a super admin.
     * 
     * @param userName
     *            user name of the recipient
     * @param signature
     *            signature of the email
     * @return complete email message with HTML tags
     */
    public static String getActivatedUserAccountHTML(String userName,
            String signature) {
        
        String message = bundle.getString("ActivatedUserAccount_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * Returns the email text which is sent to a user, whose account has been
     * (re-)activated by a super admin.
     * 
     * @param userName
     *            user name of the recipient
     * @param signature
     *            signature of the email
     * @return complete email message
     */
    public static String getActivatedUserAccountText(String userName,
            String signature) {
        
        String message = bundle.getString("ActivatedUserAccount_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<signature>", signature);
        message = message.replaceAll("<br>", crlf);

        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return subject of the email
     */
    public static String getActivatedUserAccountSubject() {
        String message = bundle.getString("ActivatedUserAccount_Subject");
        return message;
    }

    /**
     * Returns the email text which is sent to a registered user, who is already
     * member of a tenant and has been appointed to a workflowmodel admin of
     * this tenant.
     * 
     * @param userName
     *            user name of the recipient
     * @param promoterName
     *            user name of the tenant admin, who appointed this user to wfm
     *            admin
     * @param tenantName
     *            name of the tenant of which the user has been appointed to
     *            a wfm admin
     * @param wfmName
     *            name of the workflow model
     * @param signature
     *            signature of the email
     * @return complete email message with HTML tags
     */
    public static String getAppointedWorkflowModelAdminHTML(String userName,
            String promoterName, String tenantName, String wfmName,
            String signature) {
        
        String message = bundle.getString("AppointedWorkflowModelAdmin_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<promoterName>", promoterName);
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<wfmName>", wfmName);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * Returns the email text which is sent to a registered user, who is already
     * member of a tenant and has been appointed to a workflowmodel admin of
     * this tenant.
     * 
     * @param userName
     *            user name of the recipient
     * @param promoterName
     *            user name of the tenant admin, who appointed this user to wfm
     *            admin
     * @param tenantName
     *            name of the tenant of which the user has been appointed to
     *            a wfm admin
     * @param wfmName
     *            name of the workflow model
     * @param signature
     *            signature of the email
     * @return complete email message
     */
    public static String getAppointedWorkflowModelAdminText(String userName,
            String promoterName, String tenantName, String wfmName,
            String signature) {
        
        String message = bundle.getString("AppointedWorkflowModelAdmin_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<promoterName>", promoterName);
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<wfmName>", wfmName);
        message = message.replaceAll("<signature>", signature);
        message = message.replaceAll("<br>", crlf);

        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return subject of the email
     */
    public static String getAppointedWorkflowModelAdminSubject() {
        String message = bundle.getString("AppointedWorkflowModelAdmin_Subject");
        return message;
    }

    /**
     * Returns the email text which is sent to a user, whose attempt to create a
     * new tenant has been approved.
     * 
     * @param userName
     *            user name of the recipient
     * @param tenantName
     *            name of the tenant that has been approved
     * @param signature
     *            signature of the email
     * @return complete email message with HTML tags
     */
    public static String getApprovedTenantHTML(String userName,
            String tenantName, String signature) {
        
        String message = bundle.getString("ApprovedTenant_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * Returns the email text which is sent to a user, whose attempt to create a
     * new tenant has been approved.
     * 
     * @param userName
     *            user name of the recipient
     * @param tenantName
     *            name of the tenant that has been approved
     * @param signature
     *            signature of the email
     * @return complete email message
     */
    public static String getApprovedTenantText(String userName,
            String tenantName, String signature) {

        String message = bundle.getString("ApprovedTenant_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<signature>", signature);
        message = message.replaceAll("<br>", crlf);
        
        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return subject of the email
     */
    public static String getApprovedTenantSubject() {
        String message = bundle.getString("ApprovedTenant_Subject");
        return message;
    }

    /**
     * Returns the email text which is sent to a user, who has requested to
     * change his accounts email address.
     * 
     * @param userName
     *            user name of the recipient
     * @param confirmationUrl
     *            confirmation link, generated with URLGenerator
     * @param expireDate
     *            date when the conrimationUrl expired
     * @param signature
     *            signature of the email
     * @return complete email message with HTML tags
     */
    public static String getChangeEmailRequestHTML(String userName,
            String confirmationUrl, String expireDate, String signature) {
        
        String message = bundle.getString("ChangeEmailRequest_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<confirmationUrl>", confirmationUrl);
        message = message.replaceAll("<expireDate>", expireDate);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * Returns the email text which is sent to a user, who has requested to
     * change his accounts email address.
     * 
     * @param userName
     *            user name of the recipient
     * @param confirmationUrl
     *            confirmation link, generated with URLGenerator
     * @param expireDate
     *            date when the conrimationUrl expired
     * @param signature
     *            signature of the email
     * @return complete email message
     */
    public static String getChangeEmailRequestText(String userName,
            String confirmationUrl, String expireDate, String signature) {

        String message = bundle.getString("ChangeEmailRequest_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<confirmationUrl>", confirmationUrl);
        message = message.replaceAll("<expireDate>", expireDate);
        message = message.replaceAll("<signature>", signature);
        message = message.replaceAll("<br>", crlf);

        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return subject of the email
     */
    public static String getChangeEmailRequestSubject() {
        String message = bundle.getString("ChangeEmailRequest_Subject");
        return message;
    }

    /**
     * Returns the email text which is sent to a newly registered user to
     * confirm and activate his account.
     * 
     *@param userName
     *            user name of the recipient
     * @param confirmationUrl
     *            confirmation link, generated with URLGenerator
     * @param expireDate
     *            date when the conrimationUrl expired
     * @param signature
     *            signature of the email
     * @return complete email message with HTML tags
     */
    public static String getConfirmRegistrationHTML(String userName,
            String confirmationUrl, String expireDate, String signature) {
       
        String message = bundle.getString("ConfirmRegistration_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<confirmationUrl>", confirmationUrl);
        message = message.replaceAll("<expireDate>", expireDate);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * Returns the email text which is sent to a newly registered user to
     * confirm and activate his account.
     * 
     * @param userName
     *            user name of the recipient
     * @param confirmationUrl
     *            confirmation link, generated with URLGenerator
     * @param expireDate
     *            date when the conrimationUrl expired
     * @param signature
     *            signature of the email
     * @return complete email message
     */
    public static String getConfirmRegistrationText(String userName,
            String confirmationUrl, String expireDate, String signature) {

        String message = bundle.getString("ConfirmRegistration_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<confirmationUrl>", confirmationUrl);
        message = message.replaceAll("<expireDate>", expireDate);
        message = message.replaceAll("<signature>", signature);
        message = message.replaceAll("<br>", crlf);

        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return subject of the email
     */
    public static String getConfirmRegistrationSubject() {
        String message = bundle.getString("ConfirmRegistration_Subject");
        return message;
    }

    /**
     * Returns the email text which is sent to a user, whose account has been
     * deactivated by a super admin.
     * 
     * @param userName
     *            user name of the recipient
     * @param signature
     *            signature of the email
     * @return complete email message with HTML tags
     */
    public static String getDeactivatedUserAccountHTML(String userName,
            String signature) {
        
        String message = bundle.getString("DeactivatedUserAccount_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * Returns the email text which is sent to a user, whose account has been
     * deactivated by a super admin.
     * 
     * @param userName
     *            user name of the recipient
     * @param signature
     *            signature of the email
     * @return complete email message
     */
    public static String getDeactivatedUserAccountText(String userName,
            String signature) {
        
        String message = bundle.getString("DeactivatedUserAccount_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<signature>", signature);
        message = message.replaceAll("<br>", crlf);

        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return subject of the email
     */
    public static String getDeactivatedUserAccountSubject() {
        String message = bundle.getString("DeactivatedUserAccount_Subject");
        return message;
    }

    /**
     * Returns the email text which is sent to a user, whose attempt to create a
     * new tenant has been rejected.
     * 
     * @param userName
     *            user name of the recipient
     * @param tenantName
     *            name of the tenant that has been rejected
     * @param signature
     *            signature of the email
     * @return complete email message with HTML tags
     */
    public static String getDisapprovedTenantHTML(String userName,
            String tenantName, String signature) {
        
        String message = bundle.getString("DisapprovedTenant_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * Returns the email text which is sent to a user, whose attempt to create a
     * new tenant has been rejected.
     * 
     * @param userName
     *            user name of the recipient
     * @param tenantName
     *            name of the tenant that has been rejected
     * @param signature
     *            signature of the email
     * @return complete email message
     */
    public static String getDisapprovedTenantText(String userName,
            String tenantName, String signature) {

        String message = bundle.getString("DisapprovedTenant_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<signature>", signature);
        message = message.replaceAll("<br>", crlf);

        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return subject of the email
     */
    public static String getDisapprovedTenantSubject() {
        String message = bundle.getString("DisapprovedTenant_Subject");
        return message;
    }

    /**
     * Returns the email text which is sent to a user, who successfully
     * generated a new password.
     * 
     * @param userName
     *            user name of the recipient
     * @param newPassword
     *            new, created password
     * @param signature
     *            signature of the email
     * @return complete email message with HTML tags
     */
    public static String getGeneratedNewPasswordHTML(String userName,
            String newPassword, String signature) {
        
        String message = bundle.getString("GeneratedNewPassword_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<newPassword>", newPassword);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * Returns the email text which is sent to a user, who successfully
     * generated a new password.
     * 
     * @param userName
     *            user name of the recipient
     * @param newPassword
     *            new, created password
     * @param signature
     *            signature of the email
     * @return complete email message
     */
    public static String getGeneratedNewPasswordText(String userName,
            String newPassword, String signature) {

        String message = bundle.getString("GeneratedNewPassword_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<newPassword>", newPassword);
        message = message.replaceAll("<signature>", signature);
        message = message.replaceAll("<br>", crlf);

        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return subject of the email
     */
    public static String getGeneratedNewPasswordSubject() {
        String message = bundle.getString("GeneratedNewPassword_Subject");
        return message;
    }

    /**
     * Returns the email text which is sent to a user, who has been invited to
     * join a tenant and is already registered.
     * 
     * @param userName
     *            user name of the recipient
     * @param inviterName
     *            name of the inviter
     * @param tenantName
     *            name of the tenant the user has been invited to join
     * @param tenantUrl
     *            link to the tenant welcome page
     * @param invitationUrl
     *            invitation link, generated with URLGenerator
     * @param expireDate
     *            date when the invitationUrl expired
     * @param signature
     *            signature of the email
     * @return complete email message with HTML tags
     */
    public static String getInviteRegisteredUserAsTenantMemberHTML(
            String userName, String inviterName, String tenantName,
            String tenantUrl, String invitationUrl, String expireDate,
            String signature) {
        
        String message = bundle.getString("InvviteRegisteredUserAsTenantMember_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<inviterName>", inviterName);
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<tenantUrl>", tenantUrl);
        message = message.replaceAll("<invitationUrl>", invitationUrl);
        message = message.replaceAll("<expireDate>", expireDate);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * Returns the email text which is sent to a user, who has been invited to
     * join a tenant and is already registered.
     * 
     * @param userName
     *            user name of the recipient
     * @param inviterName
     *            name of the inviter
     * @param tenantName
     *            name of the tenant the user has been invited to join
     * @param tenantUrl
     *            link to the tenant welcome page
     * @param invitationUrl
     *            invitation link, generated with URLGenerator
     * @param expireDate
     *            date when the invitationUrl expired
     * @param signature
     *            signature of the email
     * @return complete email message
     */
    public static String getInviteRegisteredUserAsTenantMemberText(
            String userName, String inviterName, String tenantName,
            String tenantUrl, String invitationUrl, String expireDate,
            String signature) {

        String message = bundle.getString("InvviteRegisteredUserAsTenantMember_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<inviterName>", inviterName);
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<tenantUrl>", tenantUrl);
        message = message.replaceAll("<invitationUrl>", invitationUrl);
        message = message.replaceAll("<expireDate>", expireDate);
        message = message.replaceAll("<signature>", signature);
        message = message.replaceAll("<br>", crlf);

        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return subject of the email
     */
    public static String getInviteRegisteredUserAsTenantMemberSubject(
            String tenantName) {
        String message = bundle.getString("InviteRegisteredUserAsTenantMember");
        message = message.replaceAll("<tenantName>", tenantName);
        return message;
    }

    /**
     * Returns the email text which is sent to a user, who has been invited to
     * join a tenant and is not yet registered.
     * 
     * @param inviterName
     *            name of the inviter
     * @param tenantName
     *            name of the tenant the user has been invited to join
     * @param tenantUrl
     *            link to the tenant welcome page
     * @param invitationUrl
     *            invitation link, generated with URLGenerator
     * @param expireDate
     *            date when the invitationUrl expired
     * @param signature
     *            signature of the email
     * @return complete email message with HTML tags
     */
    public static String getInviteUnregisteredUserAsTenantMemberHTML(
            String inviterName, String tenantName, String tenantUrl,
            String invitationUrl, String expireDate, String signature) {
        
        String message = bundle.getString("InviteUnregisteredUserAsTenantMember_Message");
        message = message.replaceAll("<inviterName>", inviterName);
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<tenantUrl>", tenantUrl);
        message = message.replaceAll("<invitationUrl>", invitationUrl);
        message = message.replaceAll("<expireDate>", expireDate);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * Returns the email text which is sent to a user, who has been invited to
     * join a tenant and is not yet registered.
     * 
     * @param inviterName
     *            name of the inviter
     * @param tenantName
     *            name of the tenant the user has been invited to join
     * @param tenantUrl
     *            link to the tenant welcome page
     * @param invitationUrl
     *            invitation link, generated with URLGenerator
     * @param expireDate
     *            date when the invitationUrl expired
     * @param signature
     *            signature of the email
     * @return complete email message
     */
    public static String getInviteUnregisteredUserAsTenantMemberText(
            String inviterName, String tenantName, String tenantUrl,
            String invitationUrl, String expireDate, String signature) {

        String message = bundle.getString("InviteUnregisteredUserAsTenantMember_Message");
        message = message.replaceAll("<inviterName>", inviterName);
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<tenantUrl>", tenantUrl);
        message = message.replaceAll("<invitationUrl>", invitationUrl);
        message = message.replaceAll("<expireDate>", expireDate);
        message = message.replaceAll("<signature>", signature);
        message = message.replaceAll("<br>", crlf);

        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return subject of the email
     */
    public static String getInviteUnregisteredUserAsTenantMemberSubject() {
        String message = bundle.getString("InviteUnregisteredUserAsTenantMember");
        return message;
    }

    /**
     * Returns the email text which is sent to a user, who has received a new
     * work item.
     * 
     * @param userName
     *            user name of the recipient
     * @param tenantName
     *            name of the tenant the work item belongs to
     * @param workflowModelName
     *            name of the workflow model, that created the work item
     * @param signature
     *            signature of the email
     * @return complete email message with HTML tags
     */
    public static String getNewWorkItemHTML(String userName, String tenantName,
            String workflowModelName, String signature) {
        
        String message = bundle.getString("NewWorkItem_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<workflowModelName>", userName);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * Returns the email text which is sent to a user, who has received a new
     * work item.
     * 
     * @param userName
     *            user name of the recipient
     * @param tenantName
     *            name of the tenant the work item belongs to
     * @param workflowModelName
     *            name of the workflow model, that created the work item
     * @param signature
     *            signature of the email
     * @return complete email message
     */
    public static String getNewWorkItemText(String userName, String tenantName,
            String workflowModelName, String signature) {

        String message = bundle.getString("NewWorkItem_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<workflowModelName>", userName);
        message = message.replaceAll("<signature>", signature);
        message = message.replaceAll("<br>", crlf);

        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return subject of the email
     */
    public static String getNewWorkItemSubject(String tenantName) {
        String message = bundle.getString("NewWorkItem_Subject");
        message = message.replaceAll("<tenantName>", tenantName);
        return message;
    }

    /**
     * Returns the email text which is sent to a user, who has requested to
     * reset his password.
     * 
     * @param userName
     *            user name of the recipient
     * @param confirmationUrl
     *            confirmation link, generated with URLGenerator
     * @param expireDate
     *            the date when the confirmationUrl expired
     * @param signature
     *            signature of the email
     * @return complete email message with HTML tags
     */
    public static String getPasswordResetRequestHTML(String userName,
            String confirmationUrl, String expireDate, String signature) {
        
        String message = bundle.getString("PasswordResetRequest_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<confirmationUrl>", confirmationUrl);
        message = message.replaceAll("<expireDate>", expireDate);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * Returns the email text which is sent to a user, who has requested to
     * reset his password.
     * 
     * @param userName
     *            user name of the recipient
     * @param confirmationUrl
     *            confirmation link, generated with URLGenerator
     * @param expireDate
     *            the date when the confirmationUrl expired
     * @param signature
     *            signature of the email
     * @return complete email message
     */
    public static String getPasswordResetRequestText(String userName,
            String confirmationUrl, String expireDate, String signature) {

        String message = bundle.getString("PasswordResetRequest_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<confirmationUrl>", confirmationUrl);
        message = message.replaceAll("<expireDate>", expireDate);
        message = message.replaceAll("<signature>", signature);
        message = message.replaceAll("<br>", crlf);


        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return subject of the email
     */
    public static String getPasswordResetRequestSubject() {
        String message = bundle.getString("PasswordResetRequest_Subject");
        return message;
    }

    /**
     * Returns the email text which is sent to a user, whose invitation has been
     * rejected by another user.
     * 
     * @param userName
     *            user name of the inviter
     * @param refusedByName
     *            user name of the user who has been invited
     * @param tenantName
     *            tenant the user has been invited to
     * @param signature
     *            signature of the email
     * @return complete email message with HTML tags
     */
    public static String getRefusedInvitationHTML(String userName,
            String refusedByName, String tenantName, String signature) {
        
        String message = bundle.getString("RefusedInvitation_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<refusedByName>", refusedByName);
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * Returns the email text which is sent to a user, whose invitation has been
     * rejected by another user.
     * 
     * @param userName
     *            user name of the inviter
     * @param refusedByName
     *            user name of the user who has been invited
     * @param tenantName
     *            tenant the user has been invited to
     * @param signature
     *            signature of the email
     * @return complete email message
     */
    public static String getRefusedInvitationText(String userName,
            String refusedByName, String tenantName, String signature) {

        String message = bundle.getString("RefusedInvitation_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<refusedByName>", refusedByName);
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<signature>", signature);
        message = message.replaceAll("<br>", crlf);

        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return subject of the email
     */
    public static String getRefusedInvitationSubject() {
        String message = bundle.getString("RefusedInvitation_Subject");
        return message;
    }

    /**
     * Returns the email text which is sent to a user, who has been removed from
     * a tenant.
     * 
     * @param userName
     *            user name of the recipient
     * @param tenantName
     *            name of the tenant the user has been removed from
     * @param signature
     *            signature of the email
     * @return complete email message with HTML tags
     */
    public static String getRemovedFromTenantHTML(String userName,
            String tenantName, String signature) {
        
        String message = bundle.getString("RemovedFromTenant_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * Returns the email text which is sent to a user, who has been removed from
     * a tenant.
     * 
     * @param userName
     *            user name of the recipient
     * @param tenantName
     *            name of the tenant the user has been removed from
     * @param signature
     *            signature of the email
     * @return complete email message
     */
    public static String getRemovedFromTenantText(String userName,
            String tenantName, String signature) {

        String message = bundle.getString("RemovedFromTenant_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<signature>", signature);
        message = message.replaceAll("<br>", crlf);

        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return subject of the email
     */
    public static String getRemovedFromTenantSubject() {
        String message = bundle.getString("RemovedFromTenant_Subject");
        return message;
    }

    /**
     * Returns the email text which is sent to the super admin if a new 
     * ODE instance is required
     * 
     * @param location
     *          location where the instance should be started
     * @return complete email text with html tags
     */
    public static String getRequestNewODEInstanceHTML(String location) {
        
        String message = bundle.getString("RequestNewODEInstance_Message");
        if (location != null){
            message = message + bundle.getString("RequestNewODEInstance_Location");
        }
        message.replaceAll("<location>", location);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * Returns the email text which is sent to the super admin if a new 
     * ODE instance is required
     * 
     * @param location
     *          location where the instance should be started
     * @return complete email text
     */
    public static String getRequestNewODEInstanceText(String location) {

        String message = bundle.getString("RequestNewODEInstance_Message");
        if (location != null){
            message = message + bundle.getString("RequestNewODEInstance_Location");
        }
        message.replaceAll("<location>", crlf);
        message = message.replaceAll("<br>", crlf);
        
        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return subject of the email
     */
    public static String getRequestNewODEInstanceSubject() {
        String message = bundle.getString("RequestNewODEInstance_Subject");
        return message;
    }
    
    
    /**
     * Returns the email text which is sent to a unregistered user, who
     * has been invited to the role of a workflow admin
     *
     * @param tenantName
     *          name of the tenant that owns the workflow
     * @param invitationUrl
     *          invitation link
     * @param expireDate
     *          date when the inviationUrl expires
     * @param signature
     *          signature of the email
     * @return complete email text with html tags
     */
    public static String getInvitedUnregisteredUserAsWorkflowAdminHTML(String tenantName,
            String invitationUrl, String expireDate, String signature) {
        String message = bundle.getString("InvitedUnregisteredUserAsWorkflowAdmin_Message");
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<invitationUrl>", invitationUrl);
        message = message.replaceAll("<expireDate>", expireDate);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * Returns the email text which is sent to a unregistered user, who
     * has been invited to the role of a workflow admin
     *
     * @param tenantName
     *          name of the tenant that owns the workflow
     * @param invitationUrl
     *          invitation link
     * @param expireDate
     *          date when the inviationUrl expires
     * @param signature
     *          signature of the email
     * @return complete email text 
     */
    public static String getInvitedUnregisteredUserAsWorkflowAdminText(String tenantName,
            String invitationUrl, String expireDate, String signature) {
        String message = bundle.getString("InvitedUnregisteredUserAsWorkflowAdmin_Message");
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<invitationUrl>", invitationUrl);
        message = message.replaceAll("<expireDate>", expireDate);
        message = message.replaceAll("<signature>", signature);
        message = message.replaceAll("<br>", crlf);

        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return subject of the email
     */
    public static String getInvitedUnregisteredUserAsWorkflowAdminSubject() {
        String message = bundle.getString("InvitedUnregisteredUserAsWorkflowAdmin_Subject");
        return message;
    }

    /**
     * Returns the email text which is sent to a registered user, who
     * has been invited to the role of a workflow admin
     *
     * @param userName
     *          username of the recipient
     * @param invitationUrl
     *          invitation link
     * @param expireDate
     *          date when the inviationUrl expires
     * @param tenantName
     *          name of the tenant that owns the workflow
     * @param signature
     *          signature of the email
     * @return complete email text with html tags
     */
    public static String getInvitedRegisteredUserAsWorkflowAdminHTML(String userName,
            String invitationUrl, String expireDate, String tenantName, String signature) {
        
        String message = bundle.getString("InvitedRegisteredUserAsWorkflowAdmin_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<invitationUrl>", invitationUrl);
        message = message.replaceAll("<expireDate>", expireDate);
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * Returns the email text which is sent to a registered user, who
     * has been invited to the role of a workflow admin
     *
     * @param userName
     *          username of the recipient
     * @param invitationUrl
     *          invitation link
     * @param expireDate
     *          date when the inviationUrl expires
     * @param tenantName
     *          name of the tenant that owns the workflow
     * @param signature
     *          signature of the email
     * @return complete email text
     */
    public static String getInvitedRegisteredUserAsWorkflowAdminText(String userName,
            String invitationUrl, String expireDate, String tenantName, String signature) {

        String message = bundle.getString("InvitedRegisteredUserAsWorkflowAdmin_Message");
        message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<invitationUrl>", invitationUrl);
        message = message.replaceAll("<expireDate>", expireDate);
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<signature>", signature);
        message = message.replaceAll("<br>", crlf);

        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return subject of the email
     */
    public static String getInvitedRegisteredUserAsWorkflowAdminSubject() {
        String message = bundle.getString("InvitedRegisteredUserAsWorkflowAdmin_Subject");
        return message;
    }
    

    /**
     * Returns the email text which is sent to a user (registered or unregistered)
     * who has been invited to participate in a workflow
     *
     * @param tenantName
     *          name of the tenant which owns the workflow
     * @param invitationUrl
     *          invitation link
     * @param expireDate
     *          date when the inviationUrl expires
     * @param workflowName
     *          name of the workflow
     * @param signature
     *          signature of the email
     * @return complete email text with html tags
     */
    public static String getInvitedUserAsWorkflowParticipantHTML(String tenantName,
            String invitationUrl, String expireDate, String workflowName, String signature) {
        
        String message = bundle.getString("InvitedUserAsWorkflowParticipant_Message");
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<invitationUrl>", invitationUrl);
        message = message.replaceAll("<expireDate>", expireDate);
        message = message.replaceAll("<workflowName>", workflowName);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * Returns the email text which is sent to a user (registered or unregistered)
     * who has been invited to participate in a workflow
     *
     * @param tenantName
     *          name of the tenant which owns the workflow
     * @param invitationUrl
     *          invitation link
     * @param expireDate
     *          date when the inviationUrl expires
     * @param workflowName
     *          name of the workflow
     * @param signature
     *          signature of the email
     * @return complete email text with html tags
     */
    public static String getInvitedUserAsWorkflowParticipantText(String tenantName,
            String invitationUrl, String expireDate, String workflowName, String signature) {
        
        String message = bundle.getString("InvitedUserAsWorkflowParticipant_Message");
        message = message.replaceAll("<tenantName>", tenantName);
        message = message.replaceAll("<invitationUrl>", invitationUrl);
        message = message.replaceAll("<expireDate>", expireDate);
        message = message.replaceAll("<workflowName>", workflowName);
        message = message.replaceAll("<signature>", signature);
        message = message.replaceAll("<br>", crlf);

        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return subject of the email
     */
    public static String getInvitedUserAsWorkflowParticipantSubject() {
        String message = bundle.getString("InvitedUserAsWorkflowParticipant_Subject");
        return message;
    }
}
