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
import java.util.Date;
import java.util.List;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPMessage;

import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.workflowmodel.bpel.TProcess;
import de.decidr.model.workflowmodel.dd.TDeployment;
import de.decidr.model.workflowmodel.dwdl.translator.Translator;
import de.decidr.model.workflowmodel.dwdl.validation.IProblem;
import de.decidr.model.workflowmodel.dwdl.validation.Problem;
import de.decidr.model.workflowmodel.dwdl.validation.Validator;

/**
 * This interfaces specifies the functionality to deploy a workflow instance.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class DeployerImpl implements Deployer {

    private List<Long> serverList = null;
    private Validator validator = null;
    private List<IProblem> problems = null;
    private ODESelector selector = null;
    private Translator translator = null;
    private FileDeployer fileDeployer = null;
    private DeploymentResultImpl result = null;

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.model.workflowmodel.deployment.Deployer#deploy(byte[],
     * java.lang.String, java.util.List,
     * de.decidr.model.workflowmodel.deployment.DeploymentStrategy)
     */
    @Override
    public DeploymentResult deploy(byte[] dwdl, String tenantName,
            List<ServerLoadView> serverStatistics, DeploymentStrategy strategy)
            throws DWDLValidationException, ODESelectorException, IOException,
            JAXBException, WSDLException {

        validator = new Validator();
        problems = validator.validate(dwdl);
        if (!problems.isEmpty()) {
            throw new DWDLValidationException(problems);
        }
        selector = new ODESelector();
        serverList = selector.selectServer(serverStatistics);
        if (serverList.isEmpty()) {
            throw new ODESelectorException(serverStatistics);
        }
        translator = new Translator();
        translator.laod(dwdl);
        TProcess bpel = translator.getBPEL();
        Definition wsdl = translator.getWSDL("someloaction", "sometenantName");
        TDeployment dd = translator.getDD();
        SOAPMessage soap = translator.getSOAP();
        fileDeployer = new FileDeployer();
        fileDeployer.deploy(serverList, "", bpel, wsdl, dd);

        DeployedWorkflowModel dwfm = new DeployedWorkflowModel();
        dwfm.setDeployDate(new Date());

        result = new DeploymentResultImpl();
        result.setDeployedWorkflowModel(dwfm);
        result.setServers(serverList);

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
            throws Exception {
        // TODO Auto-generated method stub

    }

}
