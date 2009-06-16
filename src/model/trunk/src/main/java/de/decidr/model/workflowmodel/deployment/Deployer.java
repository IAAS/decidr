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

import java.util.List;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.workflowmodel.dwdl.translator.Translator;
import de.decidr.model.workflowmodel.dwdl.validation.IProblem;
import de.decidr.model.workflowmodel.dwdl.validation.Validator;

/**
 * This interfaces specifies the functionality to deploy a workflow instance.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class Deployer implements IDeployer {

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.model.workflowmodel.deployment.IDeployer#deploy(de.decidr.model
     * .entities.DeployedWorkflowModel, de.decidr.model.entities.ServerLoadView)
     */
    @Override
    public List<Long> deploy(DeployedWorkflowModel dwfm,
            ServerLoadView serverStatistics) throws Exception {
       List<Long> serverList=null; 
       Validator validator = new Validator();
       IProblem[] problems = validator.validate(dwfm.getDwdl());
       if (problems.length==0){
           ODESelector selector = new ODESelector();
           serverList = selector.selectServer(serverStatistics);
           if (!serverList.isEmpty()){
               Translator translator = new Translator();
               translator.laod(dwfm.getDwdl());
               String bpel = translator.getBPEL();
               String wsdl = translator.getWSDL("someloaction", "sometenantName");
               String dd = translator.getDD();
               String soap = translator.getSOAP();
               FileDeployer fileDeployer = new FileDeployer();
               List<String> server = null;
               Long odeVersion = fileDeployer.deploy(server, dwfm.getName(), bpel, wsdl, dd);
               updateDeployedWorkflowModel(dwfm);
               dwfm.setSoapTemplate(soap.getBytes());
               
           }
           else{
               throw new Exception();
           }
       }
       else{
           throw new Exception();
       }
        return serverList;
    }

    /**
     * TODO: add comment
     *
     */
    private void updateDeployedWorkflowModel(DeployedWorkflowModel dwfm) {
        //dwfm.setSoapTemplate(soap.getBytes());   
    }
    
}
