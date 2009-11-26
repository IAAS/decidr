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

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

import de.decidr.model.DecidrGlobals;

/**
 * Client for the <code>{@link EmailInterface}</code>. Needs at least the WSDL's
 * location, as that has to be determined at runtime.
 * 
 * @author Reinhold
 */
@WebServiceClient(name = "Email", targetNamespace = EmailInterface.TARGET_NAMESPACE)
public class EmailClientStatic extends Service {

    public EmailClientStatic() throws MalformedURLException {
        // DH RR XXX revert once the ESB works
        this(new URL(DecidrGlobals
                .getWebServiceWsdlUrl(EmailInterface.SERVICE_NAME + "."
                        + EmailInterface.PORT_NAME)), EmailInterface.SERVICE);
    }

    public EmailClientStatic(URL wsdlLocation) {
        super(wsdlLocation, EmailInterface.SERVICE);
    }

    public EmailClientStatic(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    /**
     * @return returns an implementation using SOAP 1.1 to access the
     *         <code>{@link EmailInterface}</code>.
     */
    @WebEndpoint(name = EmailInterface.ENDPOINT_NAME)
    public EmailInterface getEmailSOAP() {
        return super.getPort(EmailInterface.ENDPOINT, EmailInterface.class);
    }

    /**
     * 
     * @param features
     *            A list of {@link javax.xml.ws.WebServiceFeature} to configure
     *            on the proxy. Supported features not in the
     *            <code>features</code> parameter will have their default
     *            values.
     * @return returns an implementation using SOAP 1.1 to access the
     *         <code>{@link EmailInterface}</code>.
     */
    @WebEndpoint(name = EmailInterface.ENDPOINT_NAME)
    public EmailInterface getEmailSOAP(WebServiceFeature... features) {
        return super.getPort(EmailInterface.ENDPOINT, EmailInterface.class,
                features);
    }
}
