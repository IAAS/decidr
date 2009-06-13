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
package de.decidr.webservices.email;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = "Email", 
                  wsdlLocation = "file:Email.wsdl",
                  targetNamespace = "http://decidr.de/webservices/Email") 
public class EmailClient extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("http://decidr.de/webservices/Email", "Email");
    public final static QName EmailSOAP = new QName("http://decidr.de/webservices/Email", "EmailSOAP");
    static {
        URL url = null;
        try {
            url = new URL("file:Email.wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from file:Email.wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public EmailClient(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public EmailClient(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public EmailClient() {
        super(WSDL_LOCATION, SERVICE);
    }

    /**
     * 
     * @return
     *     returns EmailPT
     */
    @WebEndpoint(name = "EmailSOAP")
    public EmailInterface getEmailSOAP() {
        return super.getPort(EmailSOAP, EmailInterface.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns EmailPT
     */
    @WebEndpoint(name = "EmailSOAP")
    public EmailInterface getEmailSOAP(WebServiceFeature... features) {
        return super.getPort(EmailSOAP, EmailInterface.class, features);
    }
}
