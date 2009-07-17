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

package de.decidr.modelingtool.client.io;

import de.decidr.modelingtool.client.exception.LoadDWDLException;
import de.decidr.modelingtool.client.model.ConnectionModel;
import de.decidr.modelingtool.client.model.EmailInvokeNodeModel;
import de.decidr.modelingtool.client.model.FlowContainerModel;
import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.model.foreach.ForEachContainerModel;
import de.decidr.modelingtool.client.model.ifcondition.IfContainerModel;

/**
 * This is a stub to simulate loading and saving a workflow model.
 * 
 * @author JE
 */
public class WorkflowIOStub implements WorkflowIO {

    @Override
    public WorkflowModel loadWorkflow(long workflowModelId)
            throws LoadDWDLException {
        // create workflow model
        WorkflowModel workflowModel = new WorkflowModel();

        // create test elements
        EmailInvokeNodeModel model1 = new EmailInvokeNodeModel(workflowModel);
        model1.setChangeListenerPosition(50, 50);
        workflowModel.addNodeModel(model1);

        EmailInvokeNodeModel model2 = new EmailInvokeNodeModel(workflowModel);
        model2.setChangeListenerPosition(150, 150);
        workflowModel.addNodeModel(model2);

        ConnectionModel conModel = new ConnectionModel();
        conModel.setSource(model1);
        conModel.setTarget(model2);
        conModel.setParentModel(workflowModel);
        workflowModel.addConnectionModel(conModel);

        FlowContainerModel flowModel = new FlowContainerModel(workflowModel);
        flowModel.setChangeListenerPosition(250, 50);
        flowModel.setChangeListenerSize(300, 200);
        workflowModel.addNodeModel(flowModel);

        ForEachContainerModel forEachModel = new ForEachContainerModel(
                workflowModel);
        forEachModel.setChangeListenerPosition(50, 250);
        forEachModel.setChangeListenerSize(150, 130);
        workflowModel.addNodeModel(forEachModel);
        
        IfContainerModel ifModel = new IfContainerModel(workflowModel
                );
        workflowModel.addNodeModel(ifModel);
        
        return workflowModel;
    }

    @Override
    public void saveWorkflow(WorkflowModel model) {
        // TODO Auto-generated method stub

    }

}
