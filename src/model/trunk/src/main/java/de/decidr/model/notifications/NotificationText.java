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

/**
 * Provides text templates for notifications
 * 
 * 
 * @author Geoffrey-Alexeij Heinze
 */

public class NotificationText {

    /*
     * 
     * All functions require the parameter signature, which represents the last
     * sentence of the generated messages and should be sth. like
     * "--Your DecidR Team"
     */

    /**
     * 
     * Returns the email text which is sent to a user, whose account has been
     * (re-)activated by a super admin
     * 
     * @param userName
     * @param signature
     * @return complete email message
     */
    public static String getActivatedUserAccountText(String userName,
            String signature) {
        String message = null;
        message = "Dear "
                + userName
                + ",<br><br>"
                +
                // GH Bitte verwende keine Zeichen wie "“" und "”"
                "We like to inform you that your DecidR account \""
                + userName
                + "\" has been activated.<br>"
                + "You may now login and use this account.<br><br><br>"
                + signature
                + "<br>"
                + "(Please do not respond to this automatically generated mail)";

        return message;
    }

    /**
     * 
     * Returns the email text which is sent to a registered user, who is already
     * member of a tenant and has been appointed to a workflowmodel admin of
     * this tenant
     * 
     * @param userName
     * @param promoterName
     *            username of the tenant admin, who appointed this user to wfm
     *            admin
     * @param tenantName
     * @param wfmName
     *            name of the workflow model
     * @param signature
     * @return complete email message
     */
    public static String getAppointedWorkflowModelAdminText(String userName,
            String promoterName, String tenantName, String wfmName,
            String signature) {
        String message = null;
        message = "Dear "
                + userName
                + "!<br><br>"
                + promoterName
                + " from the tenant "
                + tenantName
                + " has promoted you to the role of Workflow Administrator"
                + " for the following Workflow Model:<br>"
                + wfmName
                + "<br><br><br>"
                + signature
                + "<br>"
                + "(Please do not respond to this automatically generated mail)";

        return message;
    }

    /**
     * 
     * Returns the email text which is sent to a user, whose attempt to create a
     * new tenant has been approved.
     * 
     * @param userName
     * @param tenantName
     * @param signature
     * @return complete email message
     */
    public static String getApprovedTenantText(String userName,
            String tenantName, String signature) {
        String message = null;
        message = "Dear "
                + userName
                + ",<br><br>"
                + "We are happy to inform you that your request to create the tenant "
                + tenantName
                + " has been approved."
                + "You have been set as tenant admin automatically and are now able to"
                + " customize your tenant and invite other users to join your tenant.<br><br><br>"
                + signature
                + "<br>"
                + "(Please do not respond to this automatically generated mail)";

        return message;
    }

    /**
     * 
     * Returns the email text which is sent to a user, who has requested to
     * change his accounts email address
     * 
     * @param userName
     * @param confirmationUrl
     * @param expireDate
     * @param signature
     * @return complete email message
     */
    public static String getChangeEmailRequestText(String userName,
            String confirmationUrl, String expireDate, String signature) {
        String message = null;
        message = "Dear "
                + userName
                + ",<br><br>"
                + "You have requested to change the email address of your DecidR account. "
                + "If that was not the case, please ignore this email.<br>"
                + "To verify this new email address and complete the change, visit the following link:<br>"
                + confirmationUrl
                + "<br><br>"
                + "If clicking the link in this message does not work, copy and"
                + " paste it into the address bar of your browser.<br><br>"
                + "Please note that the provided link is only valid for 3 days an will expire on "
                + expireDate
                + ".<br><br><br>"
                + signature
                + "<br>"
                + "(Please do not respond to this automatically generated mail)";

        return message;
    }

    /**
     * 
     * Returns the email text which is sent to a newly registered user to
     * confirm and activate his account.
     * 
     * @param userName
     * @param confirmationUrl
     * @param expireDate
     * @param signature
     * @return complete email message
     */
    public static String getConfirmRegistrationText(String userName,
            String confirmationUrl, String expireDate, String signature) {
        String message = null;
        message = "Dear "
                + userName
                + ",<br><br>"
                + "Thank you for registering and welcome to DecidR!<br><br>"
                + "For being able to use your newly created account, please activate it by visiting the following link:<br>"
                + confirmationUrl
                + "<br><br>"
                + "If clicking the link in this message does not work, copy and paste"
                + " it into the address bar of your browser.<br><br>"
                + "Please note that the provieded link is only valid for 3 days and will expire on "
                + expireDate
                + ".<br><br><br>"
                + signature
                + "<br>"
                + "(Please do not respond to this automatically generated mail)";

        return message;
    }

    /**
     * 
     * Returns the email text which is sent to a user, whose account has been
     * deactivated by a super admin
     * 
     * @param userName
     * @param signature
     * @return complete email message
     */
    public static String getDeactivatedUserAccountText(String userName,
            String signature) {
        String message = null;
        message = "Dear "
                + userName
                + ",<br><br>"
                + "We regret to inform you that your DecidR account \""
                + userName
                + "\" has been deactivated.<br>"
                + "You will no longer be able to login and/or fulfill any tasks using this account.<br><br><br>"
                + signature
                + "<br>"
                + "(Please do not respond to this automatically generated mail)";

        return message;
    }

    /**
     * 
     * Returns the email text which is sent to a user, whose attempt to create a
     * new tenant has been rejected.
     * 
     * @param userName
     * @param tenantName
     * @param signature
     * @return complete email message
     */
    public static String getDisapprovedTenantText(String userName,
            String tenantName, String signature) {
        String message = null;
        message = "Dear "
                + userName
                + ",<br><br>"
                + "Unfortunately your request to create the tenant "
                + tenantName
                + " has been rejected.<br><br><br>"
                + signature
                + "<br>"
                + "(Please do not respond to this automatically generated mail)";

        return message;
    }

    /**
     * 
     * Returns the email text which is sent to a user, who successfully
     * generated a new password
     * 
     * @param userName
     * @param newPassword
     * @param signature
     * @return complete email message
     */
    public static String getGeneratedNewPasswordText(String userName,
            String newPassword, String signature) {
        String message = null;
        message = "Dear "
                + userName
                + ",<br><br>"
                + "You successfully requested a password reset.<br>"
                + "Your new generated password is: "
                + newPassword
                + "<br>"
                + "Please change this password as soon as possible.<br><br><br>"
                + signature
                + "<br>"
                + "(Please do not respond to this automatically generated mail)";

        return message;
    }

    /**
     * 
     * Returns the email text which is sent to a user, who has been invited to
     * join a tenant and is already registered
     * 
     * @param userName
     * @param inviterName
     *            GH: The real name of the inviter?
     * @param tenantName
     * @param tenantUrl
     * @param invitationUrl
     * @param expireDate
     * @param signature
     * @return complete email message
     */
    public static String getInviteRegisteredUserAsTenantMemberText(
            String userName, String inviterName, String tenantName,
            String tenantUrl, String invitationUrl, String expireDate,
            String signature) {
        String message = null;
        message = "Dear "
                + userName
                + "!<br><br>"
                + inviterName
                + " invited you to join the following DecidR tenant: "
                + tenantName
                + "<br>"
                + "For further information about this tenant, please visit "
                + tenantUrl
                + "<br><br>"
                + "Please visit the following link and follow the instructions on the website to join the tenant:"
                + invitationUrl
                + "<br><br>"
                + "If clicking the link in this message does not work, copy and"
                + " paste it into the address bar of your browser.<br><br>"
                + "Please note that this invitation is only valid for 3 days and will expire on "
                + expireDate
                + ".<br><br><br>"
                + signature
                + "<br>"
                + "(Please do not respond to this automatically generated mail)";

        return message;
    }

    /**
     * 
     * Returns the email text which is sent to a user, who has been invited to
     * join a tenant and is not yet registered
     * 
     * @param inviterName
     *            GH: The real name of the inviter?
     * @param tenantName
     * @param tenantUrl
     * @param invitationUrl
     * @param expireDate
     * @param signature
     * @return complete email message
     */
    public static String getInviteUnregisteredUserAsTenantMemberText(
            String inviterName, String tenantName, String tenantUrl,
            String invitationUrl, String expireDate, String signature) {
        String message = null;
        message = "Hello!<br><br>"
                + inviterName
                + " invited you to join the following DecidR tenant: "
                + tenantName
                + "<br>"
                + "For further information about this tenant, please visit "
                + tenantUrl
                + "<br><br>"
                + "Please visit the following link and follow the instructions on the"
                + " website to create your own DecidR account and join the tenant:"
                + invitationUrl
                + "<br><br>"
                + "If clicking the link in this message does not work, copy and paste"
                + " it into the address bar of your browser.<br><br>"
                + "Please note that this invitation is only valid for 3 days and will expire on "
                + expireDate
                + ".<br><br><br>"
                + signature
                + "<br>"
                + "(Please do not respond to this automatically generated mail)";

        return message;
    }

    /**
     * 
     * Returns the email text which is sent to a user, who has received a new
     * work item
     * 
     * @param userName
     * @param tenantName
     * @param workflowModelName
     * @param signature
     * @return complete email message
     */
    public static String getNewWorkItemText(String userName, String tenantName,
            String workflowModelName, String signature) {
        String message = null;
        message = "Dear "
                + userName
                + ",<br><br>"
                + "You have received a new work item from your tenant "
                + tenantName
                + ".<br><br><br>"
                + signature
                + "<br>"
                + "(Please do not respond to this automatically generated mail)";

        return message;
    }

    /**
     * 
     * Returns the email text which is sent to a user, who has requested to
     * reset his password
     * 
     * @param userName
     * @param confirmationUrl
     * @param expireDate
     * @param signature
     * @return complete email message
     */
    public static String getPasswordResetRequestText(String userName,
            String confirmationUrl, String expireDate, String signature) {
        String message = null;
        message = "Dear "
                + userName
                + ",<br><br>"
                + "You have requested a reset of your password. If that was not"
                + " the case, please ignore this email.<br>"
                + "To reset your password, please visit the following link:<br>"
                + confirmationUrl
                + "<br><br>"
                + "A new password will be generated and sent to you.<br>"
                + "If clicking the link in this message does not work, copy and"
                + " paste it into the address bar of your browser.<br><br>"
                + "Please note that this invitation is only valid for 3 days"
                + " and will expire on "
                + expireDate
                + ".<br><br><br>"
                + signature
                + "<br>"
                + "(Please do not respond to this automatically generated mail)";

        return message;
    }

    /**
     * 
     * Returns the email text which is sent to a user, whose invitation has been
     * rejected by another user
     * 
     * @param userName
     *            user name of the inviter
     * @param refusedByName
     *            user name of the user who has been invited
     * @param tenantName
     * @param signature
     * @return complete email message
     */
    public static String getRefusedInvitationText(String userName,
            String refusedByName, String tenantName, String signature) {
        String message = null;
        message = "Dear "
                + userName
                + ",<br><br>"
                + "The user "
                + refusedByName
                + " has refused your invitation to join the tenant "
                + tenantName
                + ".<br><br><br>"
                + signature
                + "<br>"
                + "(Please do not respond to this automatically generated mail)";

        return message;
    }

    /**
     * 
     * Returns the email text which is sent to a user, who has been removed from
     * a tenant
     * 
     * @param userName
     * @param tenantName
     * @param signature
     * @return complete email message
     */
    public static String getRemovedFromTenantText(String userName,
            String tenantName, String signature) {
        String message = null;
        message = "Dear "
                + userName
                + ",<br><br>"
                + "You have been removed from the following tenant: "
                + tenantName
                + "<br>"
                + "You will no longer be able to work on tasks for this tenant.<br><br><br>"
                + signature
                + "<br>"
                + "(Please do not respond to this automatically generated mail)";

        return message;
    }

}
