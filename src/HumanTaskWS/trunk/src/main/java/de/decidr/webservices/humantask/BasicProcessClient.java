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
package de.decidr.webservices.humantask;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

import org.apache.log4j.Logger;

import de.decidr.model.logging.DefaultLogger;

/**
 * A web service client for the callback method provided by the BPEL process.
 * 
 * @author Reinhold
 */
@WebServiceClient(name = "BasicProcessClient", targetNamespace = BasicProcessInterface.TARGET_NAMESPACE)
public class BasicProcessClient extends Service {

    static Logger logger = DefaultLogger.getLogger(BasicProcessClient.class);

    private Long deployedWorkflowModelId;

    public BasicProcessClient(Long deployedWorkflowModelId)
            throws MalformedURLException {
        // XXX: revert when ESB works
        // this(new URL(DecidrGlobals
        // .getWebServiceWsdlUrl(BasicProcessInterface.SERVICE_NAME)),
        // BasicProcessInterface.SERVICE);
        super(new URL(BasicProcessInterface.SERVICE_LOCATION
                + deployedWorkflowModelId + ".basicProcessSOAP?wsdl"),
                getServiceQName(deployedWorkflowModelId));

        this.deployedWorkflowModelId = deployedWorkflowModelId;
    }

    /**
     * @return The callback of the the ODE engine.
     */
    @WebEndpoint(name = "basicProcessSOAP")
    public BasicProcessInterface getBPELCallbackInterfacePort() {
        return super.getPort(getPortQName(deployedWorkflowModelId),
                BasicProcessInterface.class);
    }

    /**
     * @param features
     *            <code>{@link WebServiceFeature WebServiceFeatures}</code> to
     *            ensure features of the communication with the WS.
     * @return The callback of the the ODE engine.
     */
    @WebEndpoint(name = "basicProcessSOAP")
    public BasicProcessInterface getBPELCallbackInterfacePort(
            WebServiceFeature... features) {
        return super.getPort(getPortQName(deployedWorkflowModelId),
                BasicProcessInterface.class, features);
    }

    /**
     * TODO document
     * 
     * @param deployedWorkflowModelId
     * @return TODO document
     */
    private static QName getServiceQName(Long deployedWorkflowModelId) {
        QName result = new QName(BasicProcessInterface.TARGET_NAMESPACE,
                deployedWorkflowModelId
                        + BasicProcessInterface.SERVICE_NAME_SUFFIX);
        logger.debug("BasicProcessClient: creating SERVICE QName: "
                + result.toString());
        return result;
    }

    /**
     * TODO document
     * 
     * @param deployedWorkflowModelId
     * @return TODO document
     */
    private static QName getPortQName(Long deployedWorkflowModelId) {
        QName result = new QName(BasicProcessInterface.TARGET_NAMESPACE,
                deployedWorkflowModelId + BasicProcessInterface.PORT_SUFFIX);
        logger.debug("BasicProcessClient: creating PORT QName: "
                + result.toString());
        return result;
    }
}
