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

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;
import org.apache.ode.axis2.service.ServiceClientUtil;

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
    public StartInstanceResult startInstance(DeployedWorkflowModel dwfm,
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
        StartInstanceResult result = new StartInstanceResultImpl();
        result.setServer(selectedServer.getId());
        result.setODEPid(getODEPid(replySOAPMessage));
        return result;
    }

    private String getODEPid(SOAPMessage replySOAPMessage) {
        // FIXME null? ~dh
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.model.workflowmodel.instancemanagement.InstanceManager#stopInstance
     * (de.decidr.model.entities.WorkflowInstance)
     */
    @Override
    public void stopInstance(WorkflowInstance instance) throws AxisFault {
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
}
