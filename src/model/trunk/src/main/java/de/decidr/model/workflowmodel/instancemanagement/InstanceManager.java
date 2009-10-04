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

import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.entities.WorkflowInstance;

/**
 * This interface specifies the functionality to start and stop instances of
 * deployed workflow models on Apache ODE.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public interface InstanceManager {

    /**
     * The function expects a deployed workflow model, which contains the
     * required SOAP Template and the start configuration, and a server load
     * view, containing a list of servers on which the workflow model is
     * deployed, their ID, load, running instances and a byte-array, containing
     * the start configuration. If successful the instance returns the newly
     * created WorkflowInstance, containing all relevant data.
     * 
     * @param dwfm
     *            The deployed workflow model
     * @param startConfiguration
     *            The byte-array containing the start configuration
     * @param serverStatistics
     *            A list of servers on which the workflow model is deployed
     * @return A result type holding all relevant data
     */
    public StartInstanceResult startInstance(DeployedWorkflowModel dwfm,
            byte[] startConfiguration, List<ServerLoadView> serverStatistics)
            throws SOAPException, IOException, JAXBException;

    
    public void stopInstance(WorkflowInstance instance);

}
