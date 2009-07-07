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
 * @author Geoffrey-Alexeij Heinze
 */

public class NotificationText {

    /**
     * 
     * Returns the email text which is sent to a user, who has 
     * requested to change his accounts email address
     * 
     * @param userName
     * @param confirmationUrl
     * @return complete email message
     */
    public static String getChangeEmailRequestText(String userName,
            String confirmationUrl) {
        throw new UnsupportedOperationException();
    }

    /**
     * 
     * Returns the email text which is sent to a user, whose 
     * attempt to create a new tenant has been rejected.
     * 
     * @param userName
     * @param tenantName
     * @return complete email message
     */
    public static String getDisaprovedTenantText(String userName,
            String tenantName) {
        throw new UnsupportedOperationException();
    }

    /**
     * 
     * Returns the email text which is sent to a user, who 
     * successfully generated a new password
     * 
     * @param userName
     * @param newPassword
     * @return complete email message
     */
    public static String getGeneratedNewPasswordText(String userName,
            String newPassword) {
        throw new UnsupportedOperationException();
    }

    /**
     * 
     * Returns the email text which is sent to a user, who has 
     * been invited to join a tenant and is already registered
     * 
     * @param userName
     * @param inviterName
     *            GH: The real name of the inviter?
     * @param tenantName
     * @param invitationUrl
     * @return complete email message
     */
    public static String getInviteRegisteredUserAsTenantMemberText(
            String userName, String inviterName, String tenantName,
            String invitationUrl) {
        throw new UnsupportedOperationException();
    }

    /**
     * 
     * Returns the email text which is sent to a user, who has 
     * been invited to join a tenant and is not yet registered
     * 
     * @param inviterName
     *            GH: The real name of the inviter?
     * @param tenantName
     * @param invitationUrl
     * @return complete email message
     */
    public static String getInviteUnregisteredUserAsTenantMemberText(
            String inviterName, String tenantName, String invitationUrl) {
        throw new UnsupportedOperationException();
    }

    /**
     * 
     * Returns the email text which is sent to a user, who has
     * received a new work item
     * 
     * @param userName
     * @param tenantName
     * @param workflowModelName
     * @return complete email message
     */
    public static String getNewWorkItemText(String userName, String tenantName,
            String workflowModelName) {
        throw new UnsupportedOperationException();
    }

    /**
     * 
     * Returns the email text which is sent to a user, who has 
     * requested to reset his password
     * 
     * @param userName
     * @param confirmationUrl
     * @return
     */
    public static String getPasswordResetRequestText(String userName,
            String confirmationUrl) {
        throw new UnsupportedOperationException();
    }

    /**
     * 
     * Returns the email text which is sent to a user, whose invitation 
     * has been rejected by another user
     * 
     * @param userName
     *            user name of the inviter
     * @param refusedByName
     *            user name of the user who has been invited
     * @param tenantName
     * @return complete email message
     */
    public static String getRefusedInvitationText(String userName,
            String refusedByName, String tenantName) {
        throw new UnsupportedOperationException();
    }

    /**
     * 
     * Returns the email text which is sent to a user, who has been 
     * removed from a tenant
     * 
     * @param userName
     * @param tenantName
     * @return complete email message
     */
    public static String getRemovedFromTenantText(String userName,
            String tenantName) {
        throw new UnsupportedOperationException();
    }

}
