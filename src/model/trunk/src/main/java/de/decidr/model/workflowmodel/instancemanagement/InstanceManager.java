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

import org.apache.axis2.AxisFault;

import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.workflowmodel.wsc.TConfiguration;

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
     * For this a SOAP Message is build and send to the process.
     * 
     * TODO the checked exceptions thrown by this method should not be deduced
     * from the (current) implementation. Same for stopInstance().
     * <ul>
     * <li>Create a new exception "InstanceManagerException?"</li>
     * <li>Wrap in RuntimeException?</li>
     * </ul>
     * ~dh
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
            TConfiguration startConfiguration, List<ServerLoadView> serverStatistics)
            throws SOAPException, IOException, JAXBException;

    /**
     * This operation first terminates a given {@link WorkflowInstance} and then
     * deletes it. For this the InstanceManagement API of Apache ODE is used.
     * 
     * The terminate operation causes the process instance to terminate
     * immediately, without a chance to perform any fault handling or
     * compensation. The process transitions to the terminated state. It only
     * affects process instances that are in the active, suspended or error
     * states.
     * 
     * The delete operation delete all, or some completed process instances.
     * 
     * The request identifies the process instances using a filter that can
     * select instances with a given name, status, property values, etc.
     * Alternatively, the instance element can be used to specify a particular
     * process instance to delete. A process instance that is in the active,
     * suspended or error state cannot be deleted. Similarly, specifying a
     * process instance has no affect if that instance is not in the completed,
     * terminated or faulted state.
     * 
     * 
     * 
     * @param instance
     *            {@link WorkflowInstance} to stop
     * @throws AxisFault
     */
    public void stopInstance(WorkflowInstance instance) throws AxisFault;

}
