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

package de.decidr.odemonitor.service;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

import de.decidr.model.URLGenerator;

/**
 * A Client for the <code>{@link ODEMonitorService}</code>.
 */
@WebServiceClient(name = ODEMonitorService.SERVICE_NAME, targetNamespace = ODEMonitorService.TARGET_NAMESPACE)
public class ODEMonitor extends Service {
    public static ODEMonitorService getODEMonitorClient()
            throws MalformedURLException {
        // XXX revert once the ESB works
        Service service = Service.create(new URL(URLGenerator
                .getWebServiceWsdlUrl(ODEMonitorService.SERVICE_NAME + "."
                        + ODEMonitorService.PORT_NAME)),
                ODEMonitorService.SERVICE);
        return service.getPort(ODEMonitorService.ENDPOINT,
                ODEMonitorService.class);
    }

    public static ODEMonitorService getODEMonitorClient(
            WebServiceFeature... features) throws MalformedURLException {
        // XXX revert once the ESB works
        Service service = Service.create(new URL(URLGenerator
                .getWebServiceWsdlUrl(ODEMonitorService.SERVICE_NAME + "."
                        + ODEMonitorService.PORT_NAME)),
                ODEMonitorService.SERVICE);
        return service.getPort(ODEMonitorService.ENDPOINT,
                ODEMonitorService.class, features);
    }

    public ODEMonitor() throws MalformedURLException {
        // XXX revert once the ESB works
        this(new URL(URLGenerator
                .getWebServiceWsdlUrl(ODEMonitorService.SERVICE_NAME + "."
                        + ODEMonitorService.PORT_NAME)),
                ODEMonitorService.SERVICE);
    }

    public ODEMonitor(URL wsdlLocation) {
        this(wsdlLocation, ODEMonitorService.SERVICE);
    }

    public ODEMonitor(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    @WebEndpoint(name = ODEMonitorService.ENDPOINT_NAME)
    public ODEMonitorService getODEMonitorSOAP() {
        return super.getPort(ODEMonitorService.ENDPOINT,
                ODEMonitorService.class);
    }

    /**
     * @param features
     *            A list of {@link javax.xml.ws.WebServiceFeature} to configure
     *            on the proxy. Supported features not in the
     *            <code>features</code> parameter will have their default
     *            values.
     */
    @WebEndpoint(name = ODEMonitorService.ENDPOINT_NAME)
    public ODEMonitorService getODEMonitorSOAP(WebServiceFeature... features) {
        return super.getPort(ODEMonitorService.ENDPOINT,
                ODEMonitorService.class, features);
    }
}