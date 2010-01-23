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

import org.apache.ode.bpel.pmapi.ProcessManagement;

/**
 * This is a WS-Client for the <code>{@link ProcessManagement}</code> web
 * service provided by the ODE engine. Provides a simple and intuitive way of
 * accessing the above-mentioned web services from inside java using JAX-WS.
 * 
 * @author Reinhold
 * @deprecated ODE doesn't support JAX-WS
 */
@WebServiceClient(name = "ProcessManagement", targetNamespace = ODEProcessClient.TARGET_NAMESPACE, wsdlLocation = ODEProcessClient.WSDL_LOCATION)
@Deprecated
public class ODEProcessClient extends Service {

    private static final String ENDPOINT_NAME = "ProcessManagementSOAP11port_http";
    public static final String TARGET_NAMESPACE = "http://www.apache.org/ode/pmapi";
    public static final QName SERVICE = new QName(TARGET_NAMESPACE,
            "ProcessManagement");
    public static final String WSDL_LOCATION = "http://127.0.0.1:8080/ode/processes/ProcessManagement?wsdl";
    public final static QName PROCESS_ENDPOINT = new QName(TARGET_NAMESPACE,
            ENDPOINT_NAME);

    public ODEProcessClient() throws MalformedURLException {
        this(new URL(WSDL_LOCATION));
    }

    public ODEProcessClient(URL wsdlDocumentLocation) {
        this(wsdlDocumentLocation, SERVICE);
    }

    public ODEProcessClient(URL wsdlDocumentLocation, QName serviceName) {
        super(wsdlDocumentLocation, serviceName);
    }

    @WebEndpoint(name = ENDPOINT_NAME)
    public ProcessManagement getProcessPort() {
        return super.getPort(PROCESS_ENDPOINT, ProcessManagement.class);
    }

    @WebEndpoint(name = ENDPOINT_NAME)
    public ProcessManagement getProcessPort(WebServiceFeature... features) {
        return super.getPort(PROCESS_ENDPOINT, ProcessManagement.class,
                features);
    }
}
