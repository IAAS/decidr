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

package de.decidr.model.workflowmodel.deployment;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axis2.Constants;
import org.apache.axis2.AxisFault;
import org.apache.axiom.om.OMNamespace;

import de.decidr.model.logging.DefaultLogger;

/**
 * This class provides the functionality to deploy the files of a workflow model
 * process on selected servers
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class FileDeployer {
    
    private static Logger logger = DefaultLogger.getLogger(FileDeployer.class);


    /**
     * TODO: add comment
     *
     * @param zipFile
     * @throws AxisFault
     */
    public void deploy(byte[] zipFile) throws AxisFault{

        
        String packageName = "somepackageName";
        if (!Base64.isArrayByteBase64(zipFile)) {
            byte[] encodedBytes = Base64.encodeBase64(zipFile);
            String encodedString = new String(encodedBytes);
            Options opts = new Options();
            opts
                    .setAction("http://www.apache.org/ode/deployapi/DeploymentPortType/deployRequest");
            opts.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            opts.setProperty(Constants.Configuration.HTTP_METHOD,
                    Constants.Configuration.HTTP_METHOD_POST);
            opts.setTo(new EndpointReference(
                    "http://localhost:8080/ode/processes/DeploymentService"));
            OMElement payload = null;
            OMFactory omFactory = OMAbstractFactory.getOMFactory();
            OMNamespace ns = omFactory.createOMNamespace(
                    "http://www.apache.org/ode/pmapi", "p");
            payload = omFactory.createOMElement("deploy", ns);
            OMElement omName = omFactory.createOMElement("name", ns);
            OMElement packageCont = omFactory.createOMElement("package", ns);
            OMElement zipEle = omFactory.createOMElement("zip", ns);
            if (packageName != null && encodedString != null) {

                packageCont.addChild(zipEle);
                payload.addChild(omName);
                payload.addChild(packageCont);

                // creating service client
                ServiceClient sc = new ServiceClient();
                sc.setOptions(opts);

                try {
                    // invoke service
                    OMElement responseMsg = sc.sendReceive(payload);
                    String body = responseMsg.toString();
                    if (body.indexOf("name") > 0) {
                        logger.info("Package deployed successfully!");
                    } else {
                        logger.info("Package deployement failed!");
                    }
                } catch (AxisFault axisFault) {
                    logger.error("Axis2 Fault Occurred while Sending the request!",axisFault);
                }
            }
        }

    }

}
