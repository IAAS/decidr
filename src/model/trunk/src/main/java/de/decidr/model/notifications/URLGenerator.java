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
import java.net.URLEncoder;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.entities.SystemSettings;

/**
 * Provides generators for URLs like confirmation links
 * 
 * GH: static -> instance
 * GH: registered vs. unregistered user: unreg. gets authkey, reg. has
 *      to login, authkey is empty -> portal
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class URLGenerator {

    private static SystemSettings settings = DecidrGlobals.getSettings();
    private static String encoding = "UTF-8";

    /**
     * Returns the URL for invitations, where it is not required that the user
     * is registered
     * 
     * @param userId
     *          ID of the recipient of the invitation
     * @param invitationId
     *          ID of the invitation
     * @return complete url with required parameters
     * @throws UnsupportedEncodingException
     */
    public static String getInvitationURL(String userId, String invitationId)
            throws UnsupportedEncodingException {
        String url = "http://" + settings.getDomain() + "/";
        url += "?" + DecidrGlobals.URL_PARAM_USER_ID + "="
                + URLEncoder.encode(userId, encoding);
        url += "&" + DecidrGlobals.URL_PARAM_INVITATION_ID + "="
                + URLEncoder.encode(invitationId, encoding);
        return url;
    }

    /**
     * Returns the URL for invitations, where it is required that the user
     * is registered
     * 
     * @param userId
     *          ID of the recipient of the invitation
     * @param invitationId
     *          ID of the invitation
     * @return complete url with required parameters
     * @throws UnsupportedEncodingException
     */
    public static String getInvitationRegistrationRequiredURL(String userId, String invitationId)
            throws UnsupportedEncodingException {
        String url = "http://" + settings.getDomain() + "/";
        url += "?" + DecidrGlobals.URL_PARAM_USER_ID + "="
                + URLEncoder.encode(userId, encoding);
        url += "&" + DecidrGlobals.URL_PARAM_INVITATION_ID + "="
                + URLEncoder.encode(invitationId, encoding);
        url += "&" + DecidrGlobals.URL_PARAM_REGISTRATION_REQUIRED;
        return url;
    }

    /**
     * Returns the URL to confirm a change email request
     * 
     * @param userId
     *          ID of the user who wants to change his email address
     * @param authKey
     *          authentication key that allows the user to change his email
     * @return complete url with required parameters
     * @throws UnsupportedEncodingException
     */
    public static String getChangeEmailRequestURL(String userId, String authKey)
            throws UnsupportedEncodingException {
        String url = "http://" + settings.getDomain() + "/";
        url += "?" + DecidrGlobals.URL_PARAM_USER_ID + "="
                + URLEncoder.encode(userId, encoding);
        url += "&" + DecidrGlobals.URL_PARAM_CHANGE_EMAIL_REQUEST_ID + "="
                + URLEncoder.encode(authKey, encoding);
        return url;
    }

    /**
     * Returns the URL to confirm a registration
     * 
     * @param userId
     *          ID of the user to be confirmed
     * @param authKey
     *          authentication key that allows the confirmation
     * @return complete url with required parameters
     * @throws UnsupportedEncodingException
     */
    public static String getConfirmRegistrationURL(String userId, String authKey)
            throws UnsupportedEncodingException {
        String url = "http://" + settings.getDomain() + "/";
        url += "?" + DecidrGlobals.URL_PARAM_USER_ID + "="
                + URLEncoder.encode(userId, encoding);
        url += "&" + DecidrGlobals.URL_PARAM_CONFIRM_REGISTRATION_ID + "="
                + URLEncoder.encode(authKey, encoding);
        return url;
    }

    /**
     * Returns the URL for a password reset request
     * 
     * @param userId
     *          ID of the user who requested the reset
     * @param authKey
     *          authentication key that allows the user to
     *          reset the password 
     * @return complete url with required parameters
     * @throws UnsupportedEncodingException
     */
    public static String getPasswordResetURL(String userId, String authKey)
            throws UnsupportedEncodingException {
        String url = "http://" + settings.getDomain() + "/";
        url += "?" + DecidrGlobals.URL_PARAM_USER_ID + "="
                + URLEncoder.encode(userId, encoding);
        url += "&" + DecidrGlobals.URL_PARAM_PASSWORD_RESET_REQUEST_ID + "="
                + URLEncoder.encode(authKey, encoding);
        return url;
    }
    
    /**
     * Returns the URL to a tenant welcome page
     * 
     * @param tenantName
     *          name of the tenant
     * @return complete url
     * @throws UnsupportedEncodingException
     */
    public static String getTenantURL(String tenantName)
           throws UnsupportedEncodingException {
        String url = "http://" + settings.getDomain() + "/";
        url += URLEncoder.encode(tenantName, encoding) + "/";
        return url;
    }
}
