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

package de.decidr.modelingtool.client.command;

import com.google.gwt.user.client.Command;

import de.decidr.modelingtool.client.exception.IncompleteModelDataException;
import de.decidr.modelingtool.client.model.ConnectionModel;
import de.decidr.modelingtool.client.model.ContainerModel;
import de.decidr.modelingtool.client.model.EndNodeModel;
import de.decidr.modelingtool.client.model.InvokeNodeModel;
import de.decidr.modelingtool.client.model.NodeModel;
import de.decidr.modelingtool.client.model.StartNodeModel;
import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.ui.Workflow;

/**
 * This command creates a complete workflow from a workflow model. All add
 * operations are realized throw its create commands.
 * 
 * @author Johannes Engelhardt
 */
public class CreateWorkflowCommand implements Command {

    /** The workflow model from which the workflow is created. */
    WorkflowModel workflowModel;

    /** All create commands are added to this command list to be executed. */
    CommandList cmdList = new CommandList();

    /**
     * Constructor for creating the create command list from the workflow model.
     * 
     * @param workflowModel
     *            The workflow model from which the workflow is created.
     * 
     */
    public CreateWorkflowCommand(WorkflowModel workflowModel)
            throws IncompleteModelDataException {
        this.workflowModel = workflowModel;

        // connect workflow to model
        workflowModel.setChangeListener(Workflow.getInstance());

        // iterate over all child nodes
        for (NodeModel model : workflowModel.getChildNodeModels()) {
            if (model instanceof ContainerModel) {
                // create child nodes and connections of container
                cmdList.addCommand(createContainer((ContainerModel) model));

            } else if (model instanceof InvokeNodeModel) {
                // create invoke node
                cmdList.addCommand(new CreateInvokeNodeCommand(
                        (InvokeNodeModel) model));
            } else if (model instanceof StartNodeModel) {
                // create start node
                cmdList.addCommand(new CreateStartEndNodeCommand(
                        (StartNodeModel) model));
            } else if (model instanceof EndNodeModel) {
                // create end node
                cmdList.addCommand(new CreateStartEndNodeCommand(
                        (EndNodeModel) model));
            }
        }

        // iterate over all child connections
        for (ConnectionModel model : workflowModel.getChildConnectionModels()) {
            cmdList.addCommand(new CreateConnectionCommand(model));
        }

    }

    @Override
    public void execute() {
        // connect model to workflow
        Workflow.getInstance().setModel(workflowModel);
        // create child nodes and connections
        cmdList.execute();
    }

    /**
     * Creates a command list for creating a container with all its children.
     * 
     * @param containerModel
     *            The model of the container.
     * @return A command list with the create commands of the container and its
     *         children.
     */
    private UndoableCommand createContainer(ContainerModel containerModel)
            throws IncompleteModelDataException {
        CommandList cmdList = new CommandList();

        // create container
        cmdList.addCommand(new CreateContainerCommand(containerModel));

        // iterate over all child nodes
        for (NodeModel model : containerModel.getChildNodeModels()) {
            if (model instanceof ContainerModel) {
                // create container with its children
                cmdList.addCommand(createContainer((ContainerModel) model));
            } else if (model instanceof InvokeNodeModel) {
                // create invoke node
                cmdList.addCommand(new CreateInvokeNodeCommand(
                        (InvokeNodeModel) model));
            }
        }

        // iterate over all child connections
        for (ConnectionModel model : containerModel.getChildConnectionModels()) {
            cmdList.addCommand(new CreateConnectionCommand(model));
        }

        return cmdList;
    }

}