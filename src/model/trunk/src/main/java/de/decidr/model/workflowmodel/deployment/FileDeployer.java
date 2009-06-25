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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import com.ibm.wsdl.xml.WSDLWriterImpl;
import de.decidr.model.workflowmodel.bpel.TProcess;
import de.decidr.model.workflowmodel.dd.TDeployment;
import org.apache.commons.codec.binary.Base64;
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

/**
 * This class provides the functionality to deploy the files of a workflow model
 * process on selected servers
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class FileDeployer {

    /**
     * This function expects a list of the server addresses on which the files
     * should be deployed, the name of the process which represents the name of
     * the folder in the ODE and has to be unique for each workflow model, as
     * well as the BPEL, WSDL and Deployment Descriptor as strings. If a process
     * is deployed in Apache ODE, it automatically receives a version number,
     * which is returned by the function deploy. If the function is called, a
     * folder with the passed name is created locally on the server. The passed
     * BPEL, WSDL and Deployment Descriptor are written to files in this folder.
     * This folder is compressed as a zip file. By invoking the Deployment Web
     * service of the Apache ODEs on the given servers the zip file is deployed
     * on all these servers. The returned ODE versions of the processes are
     * collected, after all servers are handled the highest one is returned.
     * After deploying the files on all servers, all files that have been
     * created locally are deleted.
     * 
     * 
     * @param serverList
     *            List of server addresses
     * @param name
     *            Name of the process
     * @param bpel
     *            BPEL representation of the process
     * @param wsdl
     *            WSDL representation of the process
     * @param dd
     *            The Deployment Descriptor
     * @throws JAXBException
     * @throws WSDLException
     * @throws IOException
     * @throws IOException
     */
    public void deploy(byte[] zipFile) throws JAXBException,
            WSDLException, IOException {

        
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
                        System.out.println("Package deployed successfully!");
                    } else {
                        System.out.println("Package deployement failed!");
                    }
                } catch (AxisFault axisFault) {
                    System.out
                            .println("Axis2 Fault Occurred while Sending the request!");
                }
            }
        }

    }

}
