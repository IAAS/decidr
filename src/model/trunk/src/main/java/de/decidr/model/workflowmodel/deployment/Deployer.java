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
import java.util.List;

import javax.wsdl.WSDLException;
import javax.xml.bind.JAXBException;

import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.KnownWebService;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerLoadView;

/**
 * An interface for the deployment component
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public interface Deployer {

    /**
     * This interface provides functionality for other components to deploy a
     * workflow instance. The interface contains the function {@code
     * DeploymentResult deploy()}. This function expects the workflow model is
     * to be deployed and a list server load views, which lists all available
     * servers and details of these servers, like server load, running
     * instances, deployed models. The function returns a list of Long values,
     * the IDs of the servers, on which the workflow model has been deployed on,
     * changes to the deployed workflow model don’t have to be returned to the
     * calling component, since the Decidr Workflow Model (dwfm) is passed by
     * reference.
     * 
     * 
     * @param dwdl
     *            DWDL file of the workflow
     * @param tenantName
     *            The tenant name or process owner
     * @param serverStatistics
     *            An list of servers containing server load information
     * @param strategy
     *            The deployment strategy defines for example how to choose
     *            servers
     * @param webservices
     *            A list of known web services
     * @return The deployment result contains information about the deployment
     * @throws DWDLValidationException
     * @throws ODESelectorException
     * @throws IOException
     * @throws JAXBException
     * @throws WSDLException
     */
    public DeploymentResult deploy(byte[] dwdl,
            List<KnownWebService> webservices, String tenantName,
            List<ServerLoadView> serverStatistics, DeploymentStrategy strategy)
            throws DWDLValidationException, ODESelectorException, IOException,
            JAXBException, WSDLException;

    /**
     * This method undeploys a given workflow model. The files in the ODE
     * directory will be deleted.
     * 
     * @param dwfm The workflow model to undeploy
     * @param server The server on which the undeployment should be done
     * @throws Exception Exception thrown after failed undeployment
     */
    public void undeploy(DeployedWorkflowModel dwfm, Server server)
            throws AxisFault;

}
