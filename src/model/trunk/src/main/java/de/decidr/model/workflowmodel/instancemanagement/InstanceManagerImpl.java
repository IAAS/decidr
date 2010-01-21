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

package de.decidr.model.workflowmodel.instancemanagement;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.TransformerException;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;
import org.apache.ode.axis2.service.ServiceClientUtil;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Node;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.URLGenerator;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;
import de.decidr.model.workflowmodel.wsc.TConfiguration;

/**
 * This class provides the functionality to start and stop instances of deployed
 * workflow models on Apache ODE. It also offers an interface to allow other
 * components to access this functionality.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class InstanceManagerImpl implements InstanceManager {

    private static Logger log = DefaultLogger
            .getLogger(InstanceManagerImpl.class);

    private ServiceClientUtil client;

    /*
     * (non-Javadoc)
     * 
     * @seede.decidr.model.workflowmodel.instancemanagement.InstanceManager#
     * startInstance(de.decidr.model.entities.DeployedWorkflowModel, byte[],
     * java.util.List)
     */
    @Override
    public PrepareInstanceResult prepareInstance(DeployedWorkflowModel dwfm,
            TConfiguration startConfiguration,
            List<ServerLoadView> serverStatistics) throws SOAPException,
            IOException, JAXBException {
        ServerSelector selector = new ServerSelector();
        ServerLoadView selectedServer = selector.selectServer(serverStatistics);
        SOAPGenerator generator = new SOAPGenerator();
        SOAPMessage soapMessage = generator
                .getSOAP(TransformUtil.bytesToSOAPMessage(dwfm
                        .getSoapTemplate()), startConfiguration);
        SOAPExecution execution = new SOAPExecution();
        SOAPMessage replySOAPMessage = execution.invoke(selectedServer,
                soapMessage, dwfm);

        if (log.isDebugEnabled()) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            replySOAPMessage.writeTo(out);
            log.debug("Reponse to 'start instance' SOAP message:");
            log.debug(new String(out.toByteArray(), "UTF-8"));
        }

        PrepareInstanceResult result = new StartInstanceResultImpl();
        result.setServer(selectedServer.getId());
        result.setODEPid(getODEPid(replySOAPMessage, dwfm));
        return result;
    }

    private String getODEPid(SOAPMessage replySOAPMessage,
            DeployedWorkflowModel dwfm) throws SOAPException {

        String workflowNamespace = DecidrGlobals.getWorkflowTargetNamespace(
                dwfm.getId(), dwfm.getTenant().getName());
        replySOAPMessage.getSOAPBody().addNamespaceDeclaration("xyz",
                workflowNamespace);
        log.debug("Added namepsace declaration for xyz: " + workflowNamespace);
        try {
            Node resultNode = XPathAPI
                    .selectSingleNode(replySOAPMessage.getSOAPBody(),
                            "//xyz:pid", replySOAPMessage.getSOAPBody());
            log.debug("pid:" + resultNode.getTextContent());
            return resultNode.getTextContent();

        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void stopInstance(WorkflowInstance instance) throws AxisFault {
        // FIXME never tested ~dh
        client = new ServiceClientUtil();

        // create the terminate message
        OMElement terminateMsg = client.buildMessage("terminate",
                new String[] { "filter" }, new String[] { "iid="
                        + instance.getOdePid() });

        // send the message
        // returns a OMElement that is not used
        client.send(terminateMsg, URLGenerator
                .getOdeInstanceManangementUrl(instance.getServer()));
        log.info("Pid " + instance.getOdePid() + "stopped");

        // create the delete message
        OMElement deleteMsg = client.buildMessage("delete",
                new String[] { "filter" }, new String[] { "iid="
                        + instance.getOdePid() });

        // send the message
        // returns a OMElement that is not used
        client.send(deleteMsg, URLGenerator
                .getOdeInstanceManangementUrl(instance.getServer()));
        log.info("Pid " + instance.getOdePid() + " terminated");
    }

    @Override
    public void startInstance(WorkflowInstance preparedInstance) {
        try {
            SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory
                    .newInstance();
            SOAPConnection connection = soapConnFactory.createConnection();

            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage message = messageFactory.createMessage();

            SOAPPart soapPart = message.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            SOAPBody body = envelope.getBody();

            String namespace = DecidrGlobals.getWorkflowTargetNamespace(
                    preparedInstance.getDeployedWorkflowModel().getId(),
                    preparedInstance.getDeployedWorkflowModel().getTenant()
                            .getName());

            message.getMimeHeaders().setHeader("SOAPAction",
                    namespace + "/runProcess");

            SOAPElement runProcessMessage = body.addChildElement(new QName(
                    namespace, "runProcess"));
            runProcessMessage.addChildElement("pid").setTextContent(
                    preparedInstance.getOdePid());

            // FIXME use url generator? ~dh
            connection.call(message, "http://"
                    + preparedInstance.getServer().getLocation()
                    + "/ode/processes/"
                    + preparedInstance.getDeployedWorkflowModel().getId()
                    + ".DecidrProcess");

            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
