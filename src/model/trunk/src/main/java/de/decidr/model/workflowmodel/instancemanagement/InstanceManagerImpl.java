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

    /*
     * (non-Javadoc)
     * 
     * @seede.decidr.model.workflowmodel.instancemanagement.IInstanceManager#
     * startInstance(de.decidr.model.entities.DeployedWorkflowModel, byte[],
     * de.decidr.model.entities.ServerLoadView)
     */
    @Override
    public WorkflowInstance startInstance(DeployedWorkflowModel dwfm,
            byte[] startConfiguration, ServerLoadView serverStatistics)
            throws Exception {
        ServerSelector selector = new ServerSelector();
        long serverID = selector.selectServer(serverStatistics);
        SOAPGenerator generator = new SOAPGenerator();
        String soapMessage = generator.getSOAP(dwfm.getSoapTemplate()
                .toString(), startConfiguration);
        SOAPExecution execution = new SOAPExecution();
        execution.invoke(serverID, soapMessage);
        return null;
    }
    
    //FIXME StopWorkflowInstance implementieren

}
