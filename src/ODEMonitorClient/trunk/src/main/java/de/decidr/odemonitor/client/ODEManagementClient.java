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

package de.decidr.odemonitor.client;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

import org.apache.ode.bpel.pmapi.InstanceManagement;
import org.apache.ode.bpel.pmapi.ProcessManagement;

/**
 * This is a WS-Client for the <code>{@link ProcessManagement}</code> and
 * <code>{@link InstanceManagement}</code> web services provided by the ODE
 * engine. Provides a simple and intuitive way of accessing the above-mentioned
 * web services from inside java using JAX-WS.
 * 
 * @author Reinhold
 */
@WebServiceClient(name = "ProcessAndInstanceManagement", targetNamespace = ODEManagementClient.TARGET_NAMESPACE, wsdlLocation = ODEManagementClient.WSDL_LOCATION)
public class ODEManagementClient extends Service {

    private static final String TARGET_NAMESPACE = "http://www.apache.org/ode/pmapi";
    private static final QName SERVICE = new QName(TARGET_NAMESPACE,
            "ProcessAndInstanceManagement");
    // RR find proper local wsdl location
    private static final String WSDL_LOCATION = "http://svn.apache.org/repos/asf/ode/trunk/axis2/src/main/wsdl/pmapi.wsdl";
    private final static QName PROCESS_ENDPOINT = new QName(TARGET_NAMESPACE,
            "ProcessManagementPort");
    private final static QName INSTANCE_ENDPOINT = new QName(TARGET_NAMESPACE,
            "InstanceManagementPort");

    public ODEManagementClient(URL wsdlDocumentLocation, QName serviceName) {
        super(wsdlDocumentLocation, serviceName);
    }

    public ODEManagementClient(URL wsdlDocumentLocation) {
        this(wsdlDocumentLocation, SERVICE);
    }

    public ODEManagementClient() throws MalformedURLException {
        this(new URL(WSDL_LOCATION));
    }

    @WebEndpoint(name = "InstanceManagementPort")
    public InstanceManagement getInstancePort() {
        return super.getPort(INSTANCE_ENDPOINT, InstanceManagement.class);
    }

    @WebEndpoint(name = "InstanceManagementPort")
    public InstanceManagement getInstancePort(WebServiceFeature... features) {
        return super.getPort(INSTANCE_ENDPOINT, InstanceManagement.class,
                features);
    }

    @WebEndpoint(name = "ProcessManagementPort")
    public ProcessManagement getProcessPort() {
        return super.getPort(PROCESS_ENDPOINT, ProcessManagement.class);
    }

    @WebEndpoint(name = "ProcessManagementPort")
    public ProcessManagement getProcessPort(WebServiceFeature... features) {
        return super.getPort(PROCESS_ENDPOINT, ProcessManagement.class,
                features);
    }
}
