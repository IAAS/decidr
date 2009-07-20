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
import de.decidr.modelingtool.client.model.ContainerStartConnectionModel;
import de.decidr.modelingtool.client.model.EmailInvokeNodeModel;
import de.decidr.modelingtool.client.model.EndNodeModel;
import de.decidr.modelingtool.client.model.FlowContainerModel;
import de.decidr.modelingtool.client.model.StartNodeModel;
import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.model.foreach.ForEachContainerModel;
import de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel;
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
        StartNodeModel start = new StartNodeModel(workflowModel);
        start.setChangeListenerPosition(20, 20);
        workflowModel.addNodeModel(start);
        
        EndNodeModel end = new EndNodeModel(workflowModel);
        end.setChangeListenerPosition(490, 320);
        workflowModel.addNodeModel(end);

        EmailInvokeNodeModel model2 = new EmailInvokeNodeModel(workflowModel);
        model2.setChangeListenerPosition(150, 150);
        workflowModel.addNodeModel(model2);

        ConnectionModel conModel = new ConnectionModel();
        conModel.setSource(start);
        conModel.setTarget(model2);
        conModel.setParentModel(workflowModel);
        workflowModel.addConnectionModel(conModel);

        FlowContainerModel flowModel = new FlowContainerModel(workflowModel);
        flowModel.setChangeListenerPosition(250, 50);
        flowModel.setChangeListenerSize(150, 120);
        workflowModel.addNodeModel(flowModel);
        
        HumanTaskInvokeNodeModel model1 = new HumanTaskInvokeNodeModel(flowModel);
        model1.setChangeListenerPosition(10, 30);
        flowModel.addNodeModel(model1);
        
        ConnectionModel con2Model = new ContainerStartConnectionModel();
        con2Model.setSource(flowModel);
        con2Model.setTarget(model1);
        con2Model.setParentModel(flowModel);
        flowModel.addConnectionModel(con2Model);

        ForEachContainerModel forEachModel = new ForEachContainerModel(
                workflowModel);
        forEachModel.setChangeListenerPosition(50, 250);
        forEachModel.setChangeListenerSize(150, 130);
        workflowModel.addNodeModel(forEachModel);
        
        IfContainerModel ifModel = new IfContainerModel(workflowModel);
        ifModel.setChangeListenerPosition(230, 250);
        ifModel.setChangeListenerSize(150, 130);
        workflowModel.addNodeModel(ifModel);
        
        return workflowModel;
    }

    @Override
    public void saveWorkflow(WorkflowModel model) {
        // TODO Auto-generated method stub

    }

}
