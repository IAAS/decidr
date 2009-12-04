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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.TimeZone;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;

import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.entities.Tenant;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Contains utility methods that can be used by all system components.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class DecidrGlobals {

    /**
     * URL parameter name that contains the user id.
     */
    public static final String URL_PARAM_USER_ID = "user";
    /**
     * URL parameter name that contains the invitation id.
     */
    public static final String URL_PARAM_INVITATION_ID = "invitation";
    /**
     * URL parameter name that contains the id of the password reset request.
     */
    public static final String URL_PARAM_PASSWORD_RESET_REQUEST_ID = "passwordreset";
    /**
     * URL parameter name that contains the id of the change email request.
     */
    public static final String URL_PARAM_CHANGE_EMAIL_REQUEST_ID = "changeemail";
    /**
     * URL parameter name that contains the id of the registration request.
     */
    public static final String URL_PARAM_CONFIRM_REGISTRATION_ID = "registration";
    /**
     * URL parameter name that contains the id of the user who is allowed to login with this link.
     */
    public static final String URL_PARAM_LOGIN_ID = "login";
    /**
     * URL parameter name that contains the file id.
     */
    public static final String URL_PARAM_FILE_ID = "file";
    /**
     * URL parameter name that contains the name of the tenant.
     */
    public static final String URL_PARAM_TENANT_NAME = "tenant";
    /**
     * URL parameter name that contains the authentication key.
     */
    public static final String URL_PARAM_AUTHENTICATION_KEY = "authkey";

    /**
     * The official DecidR disclaimer
     */
    public static String DISCLAIMER = "The DecidR Development Team licenses "
            + "this file to you under the Apache License, Version 2.0 (the "
            + "\"License\"); you may not use this file except in compliance "
            + "with the License. You may obtain a copy of the License at "
            + "http://www.apache.org/licenses/LICENSE-2.0. Unless required "
            + "by applicable law or agreed to in writing, software "
            + "distributed under the License is distributed on an \"AS IS\" "
            + "BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either "
            + "express or implied. See the License for the specific language"
            + " governing permissions and limitations under the License.";

    /**
     * URL parameter name that indicates that the user has to be registered to
     * follow this url.
     */
    public static final String URL_PARAM_REGISTRATION_REQUIRED = "regreq";

    /**
     * By convention, the default tenant has the ID 1.
     */
    public static final long DEFAULT_TENANT_ID = 1L;

    /**
     * The last time when the ESB url was updated
     */
    private static Calendar lastEsbUrlFetch = null;

    /**
     * The last cached ESB.
     */
    private static Server esb = null;

    /**
     * For scalability, the entire application should use the same time zone
     * (UTC).
     * 
     * @param removeMilliseconds
     *            whether the milliseconds field should be set to zero (useful
     *            when working with Hibernate dates where milliseconds get
     *            truncated anyway)
     * @return the current system time (UTC)
     */
    public static Calendar getTime(boolean removeMilliseconds) {
        Calendar result = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        if (removeMilliseconds) {
            result.set(Calendar.MILLISECOND, 0);
        }
        return result;
    }

    /**
     * For scalability, the entire application should use the same time zone
     * (UTC).
     * 
     * @return the current system time (UTC)
     */
    public static Calendar getTime() {
        return getTime(false);
    }

    /**
     * @return Returns the global DecidR version <code>{@link String}</code>.
     */
    public static String getVersion() {
        return "0.0.1";
    }

    /**
     * @return the default tenant entity. Properties that are lazily loaded are
     *         not available outside a Hibernate {@link Session}. This includes
     *         all properties that are enties themselves.
     */
    public static Tenant getDefaultTenant() {
        FetchDefaultTenantCommand cmd = new FetchDefaultTenantCommand();
        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        } catch (TransactionException e) {
            throw new RuntimeException(e);
        }
        return cmd.defaultTenant;
    }

    /**
     * Internal command that fetches the default tenant from the database.
     */
    static class FetchDefaultTenantCommand extends AbstractTransactionalCommand {

        Tenant defaultTenant;

        @Override
        public void transactionStarted(TransactionEvent evt)
                throws TransactionException {
            defaultTenant = (Tenant) evt.getSession().get(Tenant.class,
                    DEFAULT_TENANT_ID);
            if (defaultTenant == null) {
                throw new EntityNotFoundException(Tenant.class,
                        DEFAULT_TENANT_ID);
            }
        }
    }

    /**
     * Fetches the current system settings from the database.
     * 
     * @return the current system settings.
     */
    public static SystemSettings getSettings() {
        /**
         * Inline command that fetches the settings from the DB.
         */
        FetchSettingsCommand cmd = new FetchSettingsCommand();
        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        } catch (TransactionException e) {
            throw new RuntimeException(e);
        }
        return cmd.settings;
    }

    /**
     * Internal command that fetches the settings from the database.
     */
    static class FetchSettingsCommand extends AbstractTransactionalCommand {
        public SystemSettings settings = null;

        @Override
        public void transactionStarted(TransactionEvent evt)
                throws TransactionException {
            settings = (SystemSettings) evt.getSession().createQuery(
                    "select s from SystemSettings s join fetch s.superAdmin")
                    .setMaxResults(1).uniqueResult();

            if (settings == null) {
                throw new EntityNotFoundException(SystemSettings.class);
            }
        }
    }

    /**
     * Internal command that fetches the ESB data from the database.
     */
    static class FetchEsbCommand extends AbstractTransactionalCommand {
        public Server esb = null;

        @Override
        public void transactionStarted(TransactionEvent evt)
                throws TransactionException {
            esb = (Server) evt.getSession().createQuery(
                    "select s from Server s join fetch s.serverType where "
                            + "s.serverType.name = :serverType").setMaxResults(
                    1).setString("serverType", ServerTypeEnum.Esb.toString())
                    .uniqueResult();
            if (esb == null) {
                throw new EntityNotFoundException(Server.class);
            }
        }
    }

    /**
     * Fetches the details of the Enterprise Service Bus from the database. The
     * result will be cached for one minute before another database query is
     * made.
     * 
     * An open session is required to communicate with the database
     * 
     * @return the esb server
     */
    public static Server getEsb() {
        // primitive caching
        Calendar now = getTime();
        Calendar expireTime = getTime();

        if ((lastEsbUrlFetch == null) || (esb == null)) {
            // esb url hasn't been fetched yet
            expireTime.setTime(now.getTime());
        } else {
            expireTime.setTime(lastEsbUrlFetch.getTime());
            // expire after one minute
            expireTime.add(Calendar.MINUTE, 1);
        }

        if (now.compareTo(expireTime) >= 0) {
            // now is greater or equal to expireTime -> cache miss, refresh the
            // cache
            FetchEsbCommand cmd = new FetchEsbCommand();
            try {
                HibernateTransactionCoordinator.getInstance().runTransaction(
                        cmd);
            } catch (TransactionException e) {
                throw new RuntimeException(e);
            }
            esb = cmd.esb;
        } else {
            // cache hit, nothing needs to be done
        }

        return esb;
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
        // DH RR XXX revert "/axis2/services/" to "/soap/" once the ESB works
        return "http://" + getEsb().getLocation() + "/axis2/services/"
                + webServiceName;
    }

    /**
     * @param webServiceName
     *            name of the web service such as "EmailWS"
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
     * Retrieves the WSDL for the given known Web service by performing a HTTP
     * request to the URL returned by {@link #getWebServiceWsdlUrl()}
     * 
     * @param webServiceName
     *            name of the Web service whose WSDL should be retrieved.
     * @return a byte array containing the raw XML of the WSDL file.
     * @throws RuntimeException
     *             if retrieving the WSDL fails
     */
    public static byte[] getWebServiceWsdl(String webServiceName) {
        try {
            URL url = new URL(getWebServiceWsdlUrl(webServiceName));
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            InputStream urlStream = url.openStream();
            try {
                IOUtils.copy(urlStream, result);
            } finally {
                urlStream.close();
            }
            return result.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
