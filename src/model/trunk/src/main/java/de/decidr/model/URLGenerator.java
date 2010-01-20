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

package de.decidr.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.entities.SystemSettings;

/**
 * Provides generators for URLs like confirmation links, Web Services, Apache
 * ODE, ...
 * 
 * @author Geoffrey-Alexeij Heinze
 * @author Daniel Huss
 */
public class URLGenerator {

    /**
     * Character set to be used when encoding URLS (recommended by the World
     * Wide Web Consortium).
     */
    private static final String encoding = "UTF-8";

    /**
     * Regexp pattern used to trim whitespaces and slashes from URLs
     */
    private static final Pattern trimmer = Pattern.compile("^[/ ]*(.*?)[/ ]*$",
            Pattern.DOTALL);

    /**
     * URL parameter name that contains the authentication key.
     */
    public static final String URL_PARAM_AUTHENTICATION_KEY = "authkey";
    /**
     * URL parameter name that contains the name of the tenant.
     */
    public static final String URL_PARAM_TENANT_NAME = "tenant";
    /**
     * URL parameter name that contains the file id.
     */
    public static final String URL_PARAM_FILE_ID = "file";
    /**
     * URL parameter name that contains the id of the user who is allowed to
     * login with this link.
     */
    public static final String URL_PARAM_LOGIN_ID = "login";
    /**
     * URL parameter name that contains the id of the registration request.
     */
    public static final String URL_PARAM_CONFIRM_REGISTRATION_ID = "registration";
    /**
     * URL parameter name that contains the id of the change email request.
     */
    public static final String URL_PARAM_CHANGE_EMAIL_REQUEST_ID = "changeemail";
    /**
     * URL parameter name that contains the id of the password reset request.
     */
    public static final String URL_PARAM_PASSWORD_RESET_REQUEST_ID = "passwordreset";
    /**
     * URL parameter name that contains the invitation id.
     */
    public static final String URL_PARAM_INVITATION_ID = "invitation";
    /**
     * URL parameter name that contains the user id.
     */
    public static final String URL_PARAM_USER_ID = "user";

    /**
     * May be used for caching the system settings in the future.
     * 
     * @return the system settings
     */
    private static SystemSettings getSettings() {
        return DecidrGlobals.getSettings();
    }

    /**
     * Trims whitespaces and slashes from the given {@link String}.
     * 
     * @param s
     *            string to trim
     * @return empty string if s is <code>null</code>, otherwise returns the
     *         trimmed string
     */
    private static String trim(String s) {
        if (s == null) {
            return "";
        }

        Matcher m = trimmer.matcher(s);
        if (!m.matches()) {
            return "";
        }

        String result = m.group(1);

        return result == null ? "" : result;
    }

    /**
     * Builds an URL string using http as the proctocol, a base location/domain,
     * an optional path and a number of optional GET parameters.
     * 
     * @param location
     *            base location/domain (does not get URL encoded, must contain
     *            hostname and optionally a port number)
     * @param path
     *            gets appended to the base location/domain. Slashes at the
     *            beginning and the end of this string will be trimmed. Does not
     *            get URL encoded and may contain slashes. Must not contain GET
     *            params, use the getParams parameter instead! Can be
     *            <code>null</code>.
     * @param getParams
     *            HTTP GET parameters to add to the url (will be url-encoded).
     *            Can be <code>null</code>.
     * @return base url to the decidr domain including protocol and a trailing
     *         slash, if params is not null the urlencoded parameters are added.
     * @throws IllegalArgumentException
     *             if location is <code>null</code> or empty
     */
    public static String getHttpUrl(String location, String path,
            Map<String, String> getParams) {
        if (location == null || location.isEmpty()) {
            throw new IllegalArgumentException(
                    "Location cannot be null or empty");
        }

        try {
            StringBuilder result = new StringBuilder();
            result.append("http://");
            // trim all slashes and whitespaces from the given path and location
            result.append(trim(location));
            // make sure the string ends with a slash
            if (result.lastIndexOf("/") != result.length() - 1) {
                result.append("/");
            }
            result.append(trim(path));
            // add HTTP GET params as key=value pairs
            if (getParams != null && !getParams.isEmpty()) {
                result.append("?");
                int entryCount = 0;
                for (Entry<String, String> entry : getParams.entrySet()) {
                    entryCount++;
                    result.append(URLEncoder.encode(entry.getKey(), encoding));
                    if (!entry.getValue().isEmpty()) {
                        result.append("=");
                        result.append(URLEncoder.encode(entry.getValue(),
                                encoding));
                    }
                    if (entryCount < getParams.size()) {
                        result.append("&");
                    }
                }
            }
            return result.toString();
        } catch (UnsupportedEncodingException e) {
            // we expect UTF-8 to be supported on all platforms
            throw new RuntimeException(e);
        }
    }

    /**
     * Builds an URL string using http as the proctocol, the base url provided
     * by the system settings, an optional path and a number of optional GET
     * parameters.
     * 
     * @param path
     *            gets appended to the base location/domain. Slashes at the
     *            beginning and the end of this string will be trimmed. Can be
     *            <code>null</code>.
     * @param getParams
     *            HTTP GET parameters to add to the url (will be url-encoded).
     *            Can be <code>null</code>.
     * @return base url to the decidr domain including protocol and a trailing
     *         slash, if params is not null the urlencoded parameters are added.
     */
    public static String getHttpUrl(String path, Map<String, String> getParams) {
        return getHttpUrl(getSettings().getBaseUrl(), path, getParams);
    }

    /**
     * Returns the URL for invitations, where it is not required that the user
     * is registered
     * 
     * @param userId
     *            ID of the recipient of the invitation
     * @param invitationId
     *            ID of the invitation
     * @param authKey
     *            authentication Key. If the user is registered, the key is
     *            empty, for unregistered users the key allows confirm the
     *            invitation.
     * @return complete url with required parameters
     */
    public static String getInvitationUrl(String userId, String invitationId,
            String authKey) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(URLGenerator.URL_PARAM_USER_ID, userId);
        params.put(URLGenerator.URL_PARAM_INVITATION_ID, invitationId);
        if (authKey != null) {
            params.put(URLGenerator.URL_PARAM_AUTHENTICATION_KEY, authKey);
        }

        return getHttpUrl("", params);
    }

    /**
     * Returns the URL for invitations, where it is required that the user is
     * registered
     * 
     * @param userId
     *            ID of the recipient of the invitation
     * @param invitationId
     *            ID of the invitation
     * @return complete url with required parameters
     */
    public static String getInvitationRegistrationRequiredUrl(String userId,
            String invitationId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(URLGenerator.URL_PARAM_USER_ID, userId);
        params.put(URLGenerator.URL_PARAM_INVITATION_ID, invitationId);
        params.put(DecidrGlobals.URL_PARAM_REGISTRATION_REQUIRED, "");

        return getHttpUrl("", params);
    }

    /**
     * Returns the URL to confirm a change email request
     * 
     * @param userId
     *            ID of the user who wants to change his email address
     * @param authKey
     *            authentication key that allows the user to change his email
     * @return complete url with required parameters
     */
    public static String getChangeEmailRequestUrl(String userId, String authKey) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(URLGenerator.URL_PARAM_USER_ID, userId);
        params.put(URLGenerator.URL_PARAM_CHANGE_EMAIL_REQUEST_ID, authKey);
        return getHttpUrl("", params);
    }

    /**
     * Returns the URL to confirm a registration
     * 
     * @param userId
     *            ID of the user to be confirmed
     * @param authKey
     *            authentication key that allows the confirmation
     * @return complete url with required parameters
     */
    public static String getConfirmRegistrationUrl(String userId, String authKey) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(URLGenerator.URL_PARAM_USER_ID, userId);
        params.put(URLGenerator.URL_PARAM_CONFIRM_REGISTRATION_ID, authKey);

        return getHttpUrl("", params);
    }

    /**
     * Returns the URL for a password reset request
     * 
     * @param userId
     *            ID of the user who requested the reset
     * @param authKey
     *            authentication key that allows the user to reset the password
     * @return complete url with required parameters
     */
    public static String getPasswordResetUrl(String userId, String authKey) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(URLGenerator.URL_PARAM_USER_ID, userId);
        params.put(URLGenerator.URL_PARAM_PASSWORD_RESET_REQUEST_ID, authKey);

        return getHttpUrl("", params);
    }

    /**
     * Returns the URL to a tenant welcome page
     * 
     * @param tenantName
     *            name of the tenant
     * @return complete url
     */
    public static String getTenantUrl(String tenantName) {
        return getHttpUrl(tenantName, null);
    }

    /**
     * @param webServiceName
     *            the name of the webservice of which the url should be
     *            requested
     * @return An URL which can be used to connect to the web service identified
     *         by webServiceName
     */
    public static String getWebServiceUrl(String webServiceName) {
        if ((webServiceName == null) || (webServiceName == "")) {
            throw new IllegalArgumentException(
                    "Web service name must not be null or empty");
        }

        // This may change if we switch from Synapse to another ESB that's not
        // based on Axis2
        /*
         * XXX workaround until the ESB is re-integrated: use Axis2 server
         * location as the ESB location (by changing the corresponding database
         * entry) and append "axis2/services/" instead of "/soap/".
         * 
         * Should be changed back to "soap/" once the WS client interface is
         * decoupled from Axis2.
         */
        return getHttpUrl(DecidrGlobals.getEsb().getLocation(),
                "axis2/services/" + webServiceName, null);
    }

    /**
     * @param webServiceName
     *            name of the web service such as "Email"
     * @return An URL which can be used to retrieve the WSDL of the web service
     *         identified by webServiceName
     * @throws IllegalArgumentException
     *             if webServiceName is <code>null</code> or empty.
     */
    public static String getWebServiceWsdlUrl(String webServiceName) {
        // This may change if we switch from Synapse to another ESB that's not
        // based on Axis2
        if (webServiceName == null || webServiceName.isEmpty()) {
            throw new IllegalArgumentException(
                    "Web service name must not be null or empty.");
        }
        return getWebServiceUrl(webServiceName) + "?wsdl";
    }

    /**
     * Returns the URL to the Apache ODE deployment service for the given Apache
     * ODE server location.
     * 
     * @param serverLocation
     *            hostname and port of the server on which the ODE process is
     *            running.
     * @return URL to Apache ODE deployment service
     * @throws IllegalArgumentException
     *             if serverLocation is <code>null</code> or empty
     */
    public static String getOdeDeploymentServiceUrl(String serverLocation) {
        if (serverLocation == null || serverLocation.isEmpty()) {
            throw new IllegalArgumentException(
                    "Server location must not be null or empty.");
        }
        return getHttpUrl(serverLocation, "ode/processes/DeploymentService",
                null);
    }

    /**
     * Returns the URL to the Apache ODE deployment service for the given Apache
     * ODE server location.
     * 
     * @param server
     *            server on which the Apache ODE is running.
     * @return URL to Apache ODE deployment service
     * @throws IllegalArgumentException
     *             if server is <code>null</code>
     */
    public static String getOdeDeploymentServiceUrl(Server server) {
        if (server == null) {
            throw new IllegalArgumentException("Server must not be null.");
        }
        return getOdeDeploymentServiceUrl(server.getLocation());
    }

    /**
     * Returns the URL to the Apache ODE deployment service for the given Apache
     * ODE server location.
     * 
     * @param server
     *            server on which the Apache ODE is running.
     * @return URL to Apache ODE deployment service
     * @throws IllegalArgumentException
     *             if server is <code>null</code>
     */
    public static String getOdeDeploymentServiceUrl(ServerLoadView server) {
        if (server == null) {
            throw new IllegalArgumentException("Server must not be null.");
        }
        return getOdeDeploymentServiceUrl(server.getLocation());
    }

    /**
     * Returns the URL to the Apache ODE instance management service for the
     * given Apache ODE server location.
     * 
     * @param serverLocation
     *            server on which the Apache ODE is running.
     * @return URL to Apache ODE instance management service
     * @throws IllegalArgumentException
     *             if serverLocation is <code>null</code> or empty
     */
    public static String getOdeInstanceManangementUrl(String serverLocation) {
        if (serverLocation == null) {
            throw new IllegalArgumentException(
                    "Server location must not be null or empty.");
        }
        return getHttpUrl(serverLocation, "ode/processes/InstanceManagement",
                null);
    }

    /**
     * Returns the URL to the Apache ODE instance management service for the
     * given Apache ODE server location.
     * 
     * @param server
     *            server on which the Apache ODE is running.
     * @return URL to Apache ODE instance management service
     * @throws IllegalArgumentException
     *             if server is <code>null</code>
     */
    public static String getOdeInstanceManangementUrl(Server server) {
        if (server == null) {
            throw new IllegalArgumentException("Server must not be null.");
        }
        return getOdeInstanceManangementUrl(server.getLocation());
    }

    /**
     * Returns the URL to the Apache ODE instance management service for the
     * given Apache ODE server location.
     * 
     * @param server
     *            server on which the Apache ODE is running.
     * @return URL to Apache ODE instance management service
     * @throws IllegalArgumentException
     *             if server is <code>null</code>
     */
    public static String getOdeInstanceManangementUrl(ServerLoadView server) {
        if (server == null) {
            throw new IllegalArgumentException("Server must not be null.");
        }
        return getOdeInstanceManangementUrl(server.getLocation());
    }
}