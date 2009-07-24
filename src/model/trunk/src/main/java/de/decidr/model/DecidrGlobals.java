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

import java.util.Calendar;
import java.util.TimeZone;

import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.SystemSettings;
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
     * @return the current system time (UTC)
     */
    public static Calendar getTime() {
        return Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    }

    /**
     * @return Returns the global DecidR version <code>{@link String}</code>.
     */
    public static String getVersion() {
        return "0.0.1";
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
                    "from SystemSettings s join fetch s.superAdmin limit 1")
                    .uniqueResult();
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
            esb = (Server) evt
                    .getSession()
                    .createQuery(
                            "from Server s join fetch s.serverType where s.serverType.name = :serverType limit 1")
                    .setString("serverType", ServerTypeEnum.Esb.toString())
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
     *            the name of the webservice of which the url should be requested
     * @return An URL which can be used to connect to the web service identified
     *         by webServiceName
     */
    public static String getWebServiceUrl(String webServiceName) {
        // This may change if we switch from Synapse to another ESB that's not
        // based on Axis2
        return getEsb().getLocation() + "/soap/" + webServiceName;
    }

    /**
     * @param webServiceName
     *            name of the web service such as "EmailWS"
     * @return An URL which can be used to retrieve the WSDL of the web service
     *         identified by webServiceName
     */
    public static String getWebServiceWsdlUrl(String webServiceName) {
        // This may change if we switch from Synapse to another ESB that's not
        // based on Axis2
        return getEsb().getLocation() + getWebServiceUrl(webServiceName)
                + "?wsdl";
    }
}
