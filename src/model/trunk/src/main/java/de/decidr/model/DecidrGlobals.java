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
     * (UTC). Please use this method instead of
     * <code>Calendar.getInstance()</code>.
     * 
     * @param removeMilliseconds
     *            whether the milliseconds field should be set to zero (useful
     *            when working with Hibernate dates where milliseconds get
     *            truncated anyway)
     * @return the current system time and date (UTC)
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
     * (UTC). Please use this method instead of
     * <code>Calendar.getInstance()</code>.
     * 
     * @return the current system time and date (UTC)
     */
    public static Calendar getTime() {
        return getTime(false);
    }

    /**
     * @return the global DecidR version <code>{@link String}</code>.
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
        SystemSettings settings = null;

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
     * XXX feels kind of weird to have this in DecidrGlobals. Where could we
     * move this to? ~dh
     * 
     * Retrieves the WSDL for the given known Web service by performing a HTTP
     * request to the URL returned by
     * {@link URLGenerator#getWebServiceWsdlUrl(String)}
     * 
     * @param webServiceName
     *            name of the Web service whose WSDL should be retrieved.
     * @return a byte array containing the raw XML of the WSDL file.
     * @throws RuntimeException
     *             if retrieving the WSDL fails
     */
    public static byte[] getWebServiceWsdl(String webServiceName) {
        try {
            URL url = new URL(URLGenerator.getWebServiceWsdlUrl(webServiceName));
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
