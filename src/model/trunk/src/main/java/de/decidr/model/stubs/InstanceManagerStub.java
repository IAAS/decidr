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

package de.decidr.model.stubs;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;

import org.apache.axis2.AxisFault;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.workflowmodel.instancemanagement.InstanceManager;
import de.decidr.model.workflowmodel.instancemanagement.StartInstanceResult;
import de.decidr.model.workflowmodel.instancemanagement.StartInstanceResultImpl;

/**
 * Stub for the ODE Instance Manager. Does not communicate with the ODE.
 * 
 * DH FIXME replace stub with actual implementation asap ~dh.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class InstanceManagerStub implements InstanceManager {

    @Override
    public StartInstanceResult startInstance(DeployedWorkflowModel dwfm,
            byte[] startConfiguration, List<ServerLoadView> serverStatistics)
            throws SOAPException, IOException, JAXBException {
        StartInstanceResult result = new StartInstanceResultImpl();
        result.setODEPid("stubby");
        result.setServer(serverStatistics.get(0).getId());
        result.setStartDate(DecidrGlobals.getTime().getTime());
        return result;
    }

    @Override
    public void stopInstance(WorkflowInstance instance) throws AxisFault {
        // nothing needs to be done
    }

}
