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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.wsdl.WSDLException;
import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;

import org.apache.axis2.AxisFault;

import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.KnownWebService;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.workflowmodel.deployment.DWDLValidationException;
import de.decidr.model.workflowmodel.deployment.Deployer;
import de.decidr.model.workflowmodel.deployment.DeploymentResult;
import de.decidr.model.workflowmodel.deployment.DeploymentResultImpl;
import de.decidr.model.workflowmodel.deployment.DeploymentStrategy;
import de.decidr.model.workflowmodel.deployment.ODESelectorException;

/**
 * Stub for the workflow model deployer.
 * 
 * FIXME DH use actual implementation instead of stub!! ~dh
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class DeployerImplStub implements Deployer {

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.model.workflowmodel.deployment.Deployer#deploy(byte[],
     * java.util.List, java.lang.String, java.util.List,
     * de.decidr.model.workflowmodel.deployment.DeploymentStrategy)
     */
    @Override
    public DeploymentResult deploy(byte[] dwdl,
            List<KnownWebService> webservices, String tenantName,
            List<ServerLoadView> serverStatistics, DeploymentStrategy strategy)
            throws DWDLValidationException, ODESelectorException, IOException,
            JAXBException, WSDLException, SOAPException {
        
        DeploymentResult result = new DeploymentResultImpl();
        
        List<Long> servers = new ArrayList<Long>();
        servers.add(serverStatistics.get(0).getId());
        
        result.setDeploymentDate(new Date());
        result.setServers(servers);
        
        return result;
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
        // do nothing
    }

}
