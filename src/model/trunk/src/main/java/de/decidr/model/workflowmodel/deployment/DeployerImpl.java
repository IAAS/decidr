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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMText;
import org.apache.axiom.om.util.Base64;
import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;
import org.apache.ode.axis2.service.ServiceClientUtil;
import org.apache.ode.utils.Namespaces;
import org.xml.sax.SAXException;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.URLGenerator;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.KnownWebService;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.workflowmodel.bpel.Process;
import de.decidr.model.workflowmodel.dd.TDeployment;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;
import de.decidr.model.workflowmodel.dwdl.transformation.Translator;
import de.decidr.model.workflowmodel.dwdl.validation.IProblem;
import de.decidr.model.workflowmodel.dwdl.validation.Validator;

/**
 * This class provides an interface for other components to access the
 * functionality to deploy a workflow instance.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class DeployerImpl implements Deployer {

    private static Logger log = DefaultLogger.getLogger(DeployerImpl.class);

    private List<ServerLoadView> prefferedServers = null;
    private List<Long> serverList = new ArrayList<Long>();
    private Validator validator = null;
    private List<IProblem> problems = null;
    private Translator translator = null;
    private DeploymentResult result = null;
    private OMFactory factory = null;
    private ServiceClientUtil client = null;

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.model.workflowmodel.deployment.Deployer#deploy(byte[],
     * java.lang.String, java.util.List,
     * de.decidr.model.workflowmodel.deployment.DeploymentStrategy)
     */
    @Override
    public DeploymentResult deploy(byte[] dwdl,
            List<KnownWebService> knownWebservices, String tenantName,
            List<ServerLoadView> serverStatistics, DeploymentStrategy strategy)
            throws DWDLValidationException, ODESelectorException, IOException,
            JAXBException, WSDLException, SOAPException, SAXException {

        // validate the given DWDL workflow
        validator = new Validator();
        problems = validator.validate(dwdl);
        if (!problems.isEmpty()) {
            log.error("The DWDL file is not valid");
            throw new DWDLValidationException(problems);
        }
        log.info("DWDL validaion successful");

        // select the server according deployment strategy
        prefferedServers = strategy.selectServer(serverStatistics);
        if (prefferedServers.isEmpty()) {
            log.error("Could't find a server for deployment");
            throw new ODESelectorException(serverStatistics);
        }
        log.info("Server selection successful");

        // get BPEL, WSDL, SOAP, and DD files
        translator = new Translator();
        translator.load(dwdl, tenantName, knownWebservices);
        Process bpel = translator.getBPEL();

        PackageBuilder builder = new PackageBuilder();

        // deploy on each selected server
        for (ServerLoadView server : prefferedServers) {
            Definition wsdl = translator.getWSDL(server.getLocation());
            TDeployment dd = translator.getDD();
            byte[] zipFile = builder.getPackage(tenantName, bpel, wsdl, dd,
                    knownWebservices);
            deploy(tenantName, zipFile, server.getLocation());
            serverList.add(server.getId());
        }
        log.info("Deployment successful");

        // create a deployment result
        result = new DeploymentResultImpl();
        result.setDeploymentDate(DecidrGlobals.getTime().getTime());
        try {
            SOAPMessage soap = translator.getSOAPTemplate();
            result.setSOAPTemplate(TransformUtil.SOAPMessagetoBytes(soap));
        } catch (SOAPException e) {
            log.error("Can't transform soap to byte array", e);
            throw e;
        }
        result.setServers(serverList);

        return result;
    }

    public void deploy(String packageName, byte[] zip, String serverLocation)
            throws AxisFault {

        factory = OMAbstractFactory.getOMFactory();
        client = new ServiceClientUtil();

        // Use the factory to create three elements
        OMNamespace depns = factory.createOMNamespace(
                Namespaces.ODE_DEPLOYAPI_NS, "deployapi");
        OMElement root = factory.createOMElement("deploy", depns);
        OMElement namePart = factory.createOMElement("name", null);
        namePart.setText(packageName);
        OMElement zipPart = factory.createOMElement("package", null);
        OMElement zipElmt = factory.createOMElement("zip", depns);

        // Add the zip to deploy
        String base64Enc = Base64.encode(zip);
        OMText zipContent = factory.createOMText(base64Enc, "application/zip",
                true);
        root.addChild(namePart);
        root.addChild(zipPart);
        zipPart.addChild(zipElmt);
        zipElmt.addChild(zipContent);

        // deploy
        client.send(root, URLGenerator.getOdeDeploymentServiceUrl(
                serverLocation));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.model.workflowmodel.deployment.Deployer#undeploy(de.decidr.
     * model.entities.DeployedWorkflowModel, de.decidr.model.entities.Server)
     */
    @Override
    public void undeploy(DeployedWorkflowModel dwfm, Server server)
            throws AxisFault {

        // Prepare undeploy message
        OMNamespace depns = factory.createOMNamespace(
                Namespaces.ODE_DEPLOYAPI_NS, "deployapi");
        OMElement root = factory.createOMElement("undeploy", depns);
        OMElement part = factory.createOMElement("packageName", null);
        part.setText(dwfm.getName());
        root.addChild(part);

        // undeploy
        client.send(root, URLGenerator.getOdeDeploymentServiceUrl(
                server));
    }
}
