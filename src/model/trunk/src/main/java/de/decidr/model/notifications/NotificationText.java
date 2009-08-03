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
        //GH message = message.replaceAll("<userName>", userName);
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
     *            GH document
     * @param wfmName
     *            name of the workflow model
     * @param signature
     *            GH document
     * @return complete email message
     */
    public static String getAppointedWorkflowModelAdminText(String userName,
            String promoterName, String tenantName, String wfmName,
            String signature) {
        
        String message = bundle.getString("AppointedWorkflowModelAdmin_Message");
        //GH message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

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
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message with HTML tags
     */
    public static String getApprovedTenantHTML(String userName,
            String tenantName, String signature) {
        
        String message = bundle.getString("ApprovedTenant_Message");
        //GH message = message.replaceAll("<userName>", userName);
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
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message
     */
    public static String getApprovedTenantText(String userName,
            String tenantName, String signature) {
        String message = null;
        message = "Dear "
                + userName
                + ","
                + crlf
                + crlf
                + "We are happy to inform you that your request to create the tenant "
                + tenantName
                + " has been approved."
                + "You have been set as tenant admin automatically and are now able to"
                + " customize your tenant and invite other users to join your tenant."
                + crlf
                + crlf
                + crlf
                + signature
                + crlf
                + "(Please do not respond to this automatically generated mail)";

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
     *            GH document
     * @param expireDate
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message with HTML tags
     */
    public static String getChangeEmailRequestHTML(String userName,
            String confirmationUrl, String expireDate, String signature) {
        
        String message = bundle.getString("ChangeEmailRequest_Message");
        //GH message = message.replaceAll("<userName>", userName);
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
     *            GH document
     * @param expireDate
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message
     */
    public static String getChangeEmailRequestText(String userName,
            String confirmationUrl, String expireDate, String signature) {
        String message = null;
        message = "Dear "
                + userName
                + ","
                + crlf
                + crlf
                + "You have requested to change the email address of your DecidR account. "
                + "If that was not the case, please ignore this email."
                + crlf
                + "To verify this new email address and complete the change, visit the following link:"
                + crlf
                + confirmationUrl
                + crlf
                + crlf
                + "If clicking the link in this message does not work, copy and"
                + " paste it into the address bar of your browser."
                + crlf
                + crlf
                + "Please note that the provided link is only valid for 3 days an will expire on "
                + expireDate
                + "."
                + crlf
                + crlf
                + crlf
                + signature
                + crlf
                + "(Please do not respond to this automatically generated mail)";

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
     * @param userName
     *            user name of the recipient
     * @param confirmationUrl
     *            GH document
     * @param expireDate
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message with HTML tags
     */
    public static String getConfirmRegistrationHTML(String userName,
            String confirmationUrl, String expireDate, String signature) {
       
        String message = bundle.getString("ConfirmRegistration_Message");
        //GH message = message.replaceAll("<userName>", userName);
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
     *            GH document
     * @param expireDate
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message
     */
    public static String getConfirmRegistrationText(String userName,
            String confirmationUrl, String expireDate, String signature) {
        String message = null;
        message = "Dear "
                + userName
                + ","
                + crlf
                + crlf
                + "Thank you for registering and welcome to DecidR!"
                + crlf
                + crlf
                + "For being able to use your newly created account, please activate it by visiting the following link:"
                + crlf
                + confirmationUrl
                + crlf
                + crlf
                + "If clicking the link in this message does not work, copy and paste"
                + " it into the address bar of your browser."
                + crlf
                + crlf
                + "Please note that the provieded link is only valid for 3 days and will expire on "
                + expireDate
                + "."
                + crlf
                + crlf
                + crlf
                + signature
                + crlf
                + "(Please do not respond to this automatically generated mail)";

        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return GH document
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
     *            GH document
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
     *            GH document
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
     * @return GH document
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
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message with HTML tags
     */
    public static String getDisapprovedTenantHTML(String userName,
            String tenantName, String signature) {
        
        String message = bundle.getString("DisapprovedTenant_Message");
        //GH message = message.replaceAll("<userName>", userName);
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
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message
     */
    public static String getDisapprovedTenantText(String userName,
            String tenantName, String signature) {
        String message = null;
        message = "Dear "
                + userName
                + ","
                + crlf
                + crlf
                + "Unfortunately your request to create the tenant "
                + tenantName
                + " has been rejected."
                + crlf
                + crlf
                + crlf
                + signature
                + crlf
                + "(Please do not respond to this automatically generated mail)";

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
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message with HTML tags
     */
    public static String getGeneratedNewPasswordHTML(String userName,
            String newPassword, String signature) {
        
        String message = bundle.getString("GeneratedNewPassword_Message");
        //GH message = message.replaceAll("<userName>", userName);
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
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message
     */
    public static String getGeneratedNewPasswordText(String userName,
            String newPassword, String signature) {
        String message = null;
        message = "Dear "
                + userName
                + ","
                + crlf
                + crlf
                + "You successfully requested a password reset."
                + crlf
                + "Your new generated password is: "
                + newPassword
                + crlf
                + "Please change this password as soon as possible."
                + crlf
                + crlf
                + crlf
                + signature
                + crlf
                + "(Please do not respond to this automatically generated mail)";

        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return GH document
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
     *            GH: The real name of the inviter?
     * @param tenantName
     *            GH document
     * @param tenantUrl
     *            GH document
     * @param invitationUrl
     *            GH document
     * @param expireDate
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message with HTML tags
     */
    public static String getInviteRegisteredUserAsTenantMemberHTML(
            String userName, String inviterName, String tenantName,
            String tenantUrl, String invitationUrl, String expireDate,
            String signature) {
        
        String message = bundle.getString("InvviteRegisteredUserAsTenantMember_Message");
        //GH message = message.replaceAll("<userName>", userName);
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
     *            GH: The real name of the inviter?
     * @param tenantName
     *            GH document
     * @param tenantUrl
     *            GH document
     * @param invitationUrl
     *            GH document
     * @param expireDate
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message
     */
    public static String getInviteRegisteredUserAsTenantMemberText(
            String userName, String inviterName, String tenantName,
            String tenantUrl, String invitationUrl, String expireDate,
            String signature) {
        String message = null;
        message = "Dear "
                + userName
                + "!"
                + crlf
                + crlf
                + inviterName
                + " invited you to join the following DecidR tenant: "
                + tenantName
                + crlf
                + "For further information about this tenant, please visit "
                + tenantUrl
                + crlf
                + crlf
                + "Please visit the following link and follow the instructions on the website to join the tenant:"
                + invitationUrl
                + crlf
                + crlf
                + "If clicking the link in this message does not work, copy and"
                + " paste it into the address bar of your browser."
                + crlf
                + crlf
                + "Please note that this invitation is only valid for 3 days and will expire on "
                + expireDate
                + "."
                + crlf
                + crlf
                + crlf
                + signature
                + crlf
                + "(Please do not respond to this automatically generated mail)";

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
     *            GH: The real name of the inviter?
     * @param tenantName
     *            GH document
     * @param tenantUrl
     *            GH document
     * @param invitationUrl
     *            GH document
     * @param expireDate
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message with HTML tags
     */
    public static String getInviteUnregisteredUserAsTenantMemberHTML(
            String inviterName, String tenantName, String tenantUrl,
            String invitationUrl, String expireDate, String signature) {
        
        String message = bundle.getString("InviteUnregisteredUserAsTenantMember_Message");
        //GH message = message.replaceAll("<userName>", userName);
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
     *            GH: The real name of the inviter?
     * @param tenantName
     *            GH document
     * @param tenantUrl
     *            GH document
     * @param invitationUrl
     *            GH document
     * @param expireDate
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message
     */
    public static String getInviteUnregisteredUserAsTenantMemberText(
            String inviterName, String tenantName, String tenantUrl,
            String invitationUrl, String expireDate, String signature) {
        String message = null;
        message = "Hello!"
                + crlf
                + crlf
                + inviterName
                + " invited you to join the following DecidR tenant: "
                + tenantName
                + crlf
                + "For further information about this tenant, please visit "
                + tenantUrl
                + crlf
                + crlf
                + "Please visit the following link and follow the instructions on the"
                + " website to create your own DecidR account and join the tenant:"
                + invitationUrl
                + crlf
                + crlf
                + "If clicking the link in this message does not work, copy and paste"
                + " it into the address bar of your browser."
                + crlf
                + crlf
                + "Please note that this invitation is only valid for 3 days and will expire on "
                + expireDate
                + "."
                + crlf
                + crlf
                + crlf
                + signature
                + crlf
                + "(Please do not respond to this automatically generated mail)";

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
     *            GH document
     * @param workflowModelName
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message with HTML tags
     */
    public static String getNewWorkItemHTML(String userName, String tenantName,
            String workflowModelName, String signature) {
        
        String message = bundle.getString("NewWorkItem_Message");
        //GH message = message.replaceAll("<userName>", userName);
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
     *            GH document
     * @param workflowModelName
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message
     */
    public static String getNewWorkItemText(String userName, String tenantName,
            String workflowModelName, String signature) {
        String message = null;
        message = "Dear "
                + userName
                + ","
                + crlf
                + crlf
                + "You have received a new work item from your tenant "
                + tenantName
                + "."
                + crlf
                + crlf
                + crlf
                + signature
                + crlf
                + "(Please do not respond to this automatically generated mail)";

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
     *            GH document
     * @param expireDate
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message with HTML tags
     */
    public static String getPasswordResetRequestHTML(String userName,
            String confirmationUrl, String expireDate, String signature) {
        
        String message = bundle.getString("PasswordResetRequest_Message");
        //GH message = message.replaceAll("<userName>", userName);
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
     *            GH document
     * @param expireDate
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message
     */
    public static String getPasswordResetRequestText(String userName,
            String confirmationUrl, String expireDate, String signature) {
        String message = null;
        message = "Dear "
                + userName
                + ","
                + crlf
                + crlf
                + "You have requested a reset of your password. If that was not"
                + " the case, please ignore this email."
                + crlf
                + "To reset your password, please visit the following link:"
                + crlf
                + confirmationUrl
                + crlf
                + crlf
                + "A new password will be generated and sent to you."
                + crlf
                + "If clicking the link in this message does not work, copy and"
                + " paste it into the address bar of your browser."
                + crlf
                + crlf
                + "Please note that this invitation is only valid for 3 days"
                + " and will expire on "
                + expireDate
                + "."
                + crlf
                + crlf
                + crlf
                + signature
                + crlf
                + "(Please do not respond to this automatically generated mail)";

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
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message with HTML tags
     */
    public static String getRefusedInvitationHTML(String userName,
            String refusedByName, String tenantName, String signature) {
        
        String message = bundle.getString("RefusedInvitation_Message");
        //GH message = message.replaceAll("<userName>", userName);
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
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message
     */
    public static String getRefusedInvitationText(String userName,
            String refusedByName, String tenantName, String signature) {
        String message = null;
        message = "Dear "
                + userName
                + ","
                + crlf
                + crlf
                + "The user "
                + refusedByName
                + " has refused your invitation to join the tenant "
                + tenantName
                + "."
                + crlf
                + crlf
                + crlf
                + signature
                + crlf
                + "(Please do not respond to this automatically generated mail)";

        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return GH document
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
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message with HTML tags
     */
    public static String getRemovedFromTenantHTML(String userName,
            String tenantName, String signature) {
        
        String message = bundle.getString("RemovedFromTenant_Message");
        //GH message = message.replaceAll("<userName>", userName);
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
     *            GH document
     * @param signature
     *            GH document
     * @return complete email message
     */
    public static String getRemovedFromTenantText(String userName,
            String tenantName, String signature) {
        String message = null;
        message = "Dear "
                + userName
                + ","
                + crlf
                + crlf
                + "You have been removed from the following tenant: "
                + tenantName
                + crlf
                + "You will no longer be able to work on tasks for this tenant."
                + crlf
                + crlf
                + crlf
                + signature
                + crlf
                + "(Please do not respond to this automatically generated mail)";

        return message;
    }

    /**
     * Returns the email subject of the corresponding email text.
     * 
     * @return GH document
     */
    public static String getRemovedFromTenantSubject() {
        String message = bundle.getString("RemovedFromTenant_Subject");
        return message;
    }

    /**
     * GH: Add text
     * 
     * @param location
     * @return
     */
    public static String getRequestNewODEInstanceHTML(String location) {
        
        String message = bundle.getString("RequestNewODEInstance_Message");
        //GH message = message.replaceAll("<userName>", userName);
        //GH message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * GH: Add text
     * 
     * @param location
     * @return
     */
    public static String getRequestNewODEInstanceText(String location) {
        String message = "";
        return message;
    }

    /**
     * GH: Add text
     * 
     * @param location
     * @return
     */
    public static String getRequestNewODEInstanceSubject() {
        String message = "";
        return message;
    }
    
    //#################################################################################
    

    /**
     * GH: Add text
     * 
     * @param location
     * @return
     */
    public static String getInvitedUnregisteredUserAsWorkflowAdminHTML(String tenantName, String signature) {
        String message = bundle.getString("InvitedUnregisteredUserAsWorkflowAdmin_Message");
        //GH message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * GH: Add text
     * 
     * @param location
     * @return
     */
    public static String getInvitedUnregisteredUserAsWorkflowAdminText(String tenantName, String signature) {
        String message = "";
        return message;
    }

    /**
     * GH: Add text
     * 
     * @param location
     * @return
     */
    public static String getInvitedUnregisteredUserAsWorkflowAdminSubject() {
        String message = bundle.getString("InvitedUnregisteredUserAsWorkflowAdmin_Subject");
        return message;
    }

    /**
     * GH: Add text
     * 
     * @param location
     * @return
     */
    public static String getInvitedRegisteredUserAsWorkflowAdminHTML(String userName,
                         String tenantName, String signature) {
        
        String message = bundle.getString("InvitedRegisteredUserAsWorkflowAdmin_Message");
        //GH message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * GH: Add text
     * 
     * @param location
     * @return
     */
    public static String getInvitedRegisteredUserAsWorkflowAdminText(String userName,
                         String tenantName, String signature) {
        String message = "";
        return message;
    }

    /**
     * GH: Add text
     * 
     * @param location
     * @return
     */
    public static String getInvitedRegisteredUserAsWorkflowAdminSubject() {
        String message = bundle.getString("InvitedRegisteredUserAsWorkflowAdmin_Subject");
        return message;
    }
    

    /**
     * GH: Add text
     * 
     * @param location
     * @return
     */
    public static String getInvitedUserAsWorkflowParticipantHTML(String tenantName, String signature) {
        
        String message = bundle.getString("InvitedUserAsWorkflowParticipant_Message");
        //GH message = message.replaceAll("<userName>", userName);
        message = message.replaceAll("<signature>", signature);
        message = "<html>" +
                  message +
                  "</html>";

        return message;
    }

    /**
     * GH: Add text
     * 
     * @param location
     * @return
     */
    public static String getInvitedUserAsWorkflowParticipantText(String tenantName, String signature) {
        String message = "";
        return message;
    }

    /**
     * GH: Add text
     * 
     * @param location
     * @return
     */
    public static String getInvitedUserAsWorkflowParticipantSubject() {
        String message = bundle.getString("InvitedUserAsWorkflowParticipant_Subject");
        return message;
    }
}
