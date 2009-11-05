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

import com.google.gwt.core.client.GWT;
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
        GWT.log("Creating UI data for workflow", null);
        this.workflowModel = workflowModel;

        // connect workflow to model
        workflowModel.setChangeListener(Workflow.getInstance());
        GWT.log("Connecting workflow to model", null);

        // iterate over all child nodes
        for (NodeModel model : workflowModel.getChildNodeModels()) {
            if (model instanceof ContainerModel) {
                // create child nodes and connections of container
                GWT.log("Creating child nodes and connections of container",
                        null);
                cmdList.addCommand(createContainer((ContainerModel) model));

            } else if (model instanceof InvokeNodeModel) {
                // create invoke node
                GWT.log("Creating invoke node", null);
                cmdList.addCommand(new CreateInvokeNodeCommand(
                        (InvokeNodeModel) model));
            } else if (model instanceof StartNodeModel) {
                // create start node
                GWT.log("Creating start node", null);
                cmdList.addCommand(new CreateStartEndNodeCommand(
                        (StartNodeModel) model));
            } else if (model instanceof EndNodeModel) {
                // create end node
                GWT.log("Creating end node", null);
                cmdList.addCommand(new CreateStartEndNodeCommand(
                        (EndNodeModel) model));
            }
        }

        // iterate over all child connections
        for (ConnectionModel model : workflowModel.getChildConnectionModels()) {
            GWT.log("Creating child connection", null);
            cmdList.addCommand(new CreateConnectionCommand(model));
        }

        GWT.log("Finished creating UI data", null);
    }

    @Override
    public void execute() {
        // connect model to workflow
        Workflow.getInstance().setModel(workflowModel);
        // create child nodes and connections
        cmdList.execute();

        // flush command stack
        CommandStack.getInstance().flush();
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
        GWT.log("Creating container", null);
        cmdList.addCommand(new CreateContainerCommand(containerModel));

        // iterate over all child nodes
        for (NodeModel model : containerModel.getChildNodeModels()) {
            if (model instanceof ContainerModel) {
                // create container with its children
                GWT.log("Creating child container", null);
                cmdList.addCommand(createContainer((ContainerModel) model));
            } else if (model instanceof InvokeNodeModel) {
                // create invoke node
                GWT.log("Creating child invoke node", null);
                cmdList.addCommand(new CreateInvokeNodeCommand(
                        (InvokeNodeModel) model));
            }
        }

        // iterate over all child connections
        for (ConnectionModel model : containerModel.getChildConnectionModels()) {
            GWT.log("Creating child connection for container", null);
            cmdList.addCommand(new CreateConnectionCommand(model));
        }

        GWT.log("Finished creating container", null);
        return cmdList;
    }

}