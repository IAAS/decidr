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

package de.decidr.model.webservices;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.ws.Service;
import javax.xml.ws.WebServiceFeature;

import de.decidr.model.URLGenerator;

/**
 * Some static methods to get web service clients dynamically. Differs from the
 * static clients in that the used endpoint isn't pre-determined. Also, the
 * static clients allow choosing the WSDL to use, allowing the user to manually
 * select which web service instance to choose.
 * 
 * @author Reinhold
 */
public class DynamicClients {

    public static EmailInterface getEmailClient() throws MalformedURLException {
        // XXX revert once the ESB works
        Service service = Service.create(new URL(URLGenerator
                .getWebServiceWsdlUrl(EmailInterface.SERVICE_NAME + "."
                        + EmailInterface.PORT_NAME)), EmailInterface.SERVICE);
        return service.getPort(EmailInterface.ENDPOINT, EmailInterface.class);
    }

    public static EmailInterface getEmailClient(WebServiceFeature... features)
            throws MalformedURLException {
        // XXX revert once the ESB works
        Service service = Service.create(new URL(URLGenerator
                .getWebServiceWsdlUrl(EmailInterface.SERVICE_NAME + "."
                        + EmailInterface.PORT_NAME)), EmailInterface.SERVICE);
        return service.getPort(EmailInterface.ENDPOINT, EmailInterface.class,
                features);
    }

    public static HumanTaskInterface getHumanTaskClient()
            throws MalformedURLException {
        // XXX revert once the ESB works
        Service service = Service.create(new URL(URLGenerator
                .getWebServiceWsdlUrl(HumanTaskInterface.SERVICE_NAME + "."
                        + HumanTaskInterface.PORT_NAME)),
                HumanTaskInterface.SERVICE);
        return service.getPort(HumanTaskInterface.ENDPOINT,
                HumanTaskInterface.class);
    }

    public static HumanTaskInterface getHumanTaskClient(
            WebServiceFeature... features) throws MalformedURLException {
        // XXX revert once the ESB works
        Service service = Service.create(new URL(URLGenerator
                .getWebServiceWsdlUrl(HumanTaskInterface.SERVICE_NAME + "."
                        + HumanTaskInterface.PORT_NAME)),
                HumanTaskInterface.SERVICE);
        return service.getPort(HumanTaskInterface.ENDPOINT,
                HumanTaskInterface.class, features);
    }
}
