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
import org.apache.ode.axis2.service.ServiceClientUtil;

import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.entities.WorkflowInstance;

/**
 * This class provides the functionality to start and stop instances of deployed
 * workflow models on Apache ODE. It also offers an interface to allow other
 * components to access this functionality.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class InstanceManagerImpl implements InstanceManager {

    private ServiceClientUtil client;
    private final String AMAZON_EC2 = "http://ec2-174-129-24-232.compute-1.amazonaws.com:8080";

    /*
     * (non-Javadoc)
     * 
     * @seede.decidr.model.workflowmodel.instancemanagement.InstanceManager#
     * startInstance(de.decidr.model.entities.DeployedWorkflowModel, byte[],
     * java.util.List)
     */
    @Override
    public StartInstanceResult startInstance(DeployedWorkflowModel dwfm,
            byte[] startConfiguration, List<ServerLoadView> serverStatistics)
            throws SOAPException, IOException, JAXBException {
        ServerSelector selector = new ServerSelector();
        ServerLoadView selectedServer = selector.selectServer(serverStatistics);
        SOAPGenerator generator = new SOAPGenerator();
// MA commented out to allow compilation
//        SOAPMessage soapMessage = generator.getSOAP(dwfm.getSoapTemplate(),
//                startConfiguration);
        SOAPExecution execution = new SOAPExecution();
//        SOAPMessage replySOAPMessage = execution.invoke(selectedServer,
//                soapMessage);
        StartInstanceResult result = new StartInstanceResultImpl();
        result.setServer(selectedServer.getId());
//        result.setODEPid(getODEPid(replySOAPMessage));
        return result;
    }

    private String getODEPid(SOAPMessage replySOAPMessage) {
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
        OMElement msg = client.buildMessage("terminate",
                new String[] { "iid" }, new String[] { instance.getOdePid() });
        OMElement result = client.send(msg, instance.getServer().getLocation()
                + "/ode/processes/InstanceManagement");
    }
}
