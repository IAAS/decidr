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

import org.hibernate.Session;

import de.decidr.model.entities.SystemSettings;

/**
 * FIXME add documentation
 * FIXME add properties file with configuration for ESB
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class DecidrGlobals {

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
     * Fetches the current system settings.
     * 
     * @param session
     * @return the current system settings.
     */
    public static SystemSettings getSettings(Session session) {
        String hql = "from SystemSettings limit 1";
        return (SystemSettings) session.createQuery(hql).uniqueResult();
    }

    public static String getWebServiceUrl(String webServiceName) {
        // FIXME DH document and implement stub!
        return "https://localhost:8280/soap/" + webServiceName;
    }

    public static String getWebServiceWsdlUrl(String webServiceName) {
        // FIXME DH document and implement stub!
        return getWebServiceUrl(webServiceName) + "?wsdl";
    }

}
