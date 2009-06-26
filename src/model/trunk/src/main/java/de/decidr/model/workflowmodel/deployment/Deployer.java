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
     * TODO: add comment
     *
     * @param dwdl DWDL file of the workflow
     * @param tenantName The tenant name or process owner
     * @param serverStatistics An list of servers containing server load information
     * @param strategy The deployment strategy defines for example how to choose servers
     * @param webservices A list of known web services
     * @return The deployment result contains information about the deployment 
     * @throws DWDLValidationException
     * @throws ODESelectorException
     * @throws IOException
     * @throws JAXBException
     * @throws WSDLException
     */
 public DeploymentResult deploy(byte[] dwdl, List<KnownWebService> webservices, String tenantName,
            List<ServerLoadView> serverStatistics, DeploymentStrategy strategy)
            throws DWDLValidationException, ODESelectorException, IOException,
            JAXBException, WSDLException;

    /**
     * TODO: add comment
     *
     * @param dwfm
     * @param server
     * @throws Exception
     */
    public void undeploy(DeployedWorkflowModel dwfm, Server server)
            throws Exception;

}
