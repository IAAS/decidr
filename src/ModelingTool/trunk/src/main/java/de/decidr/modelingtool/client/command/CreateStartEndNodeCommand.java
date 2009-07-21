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

import de.decidr.modelingtool.client.exception.IncompleteModelDataException;
import de.decidr.modelingtool.client.model.EndNodeModel;
import de.decidr.modelingtool.client.model.NodeModel;
import de.decidr.modelingtool.client.model.StartNodeModel;
import de.decidr.modelingtool.client.ui.EndNode;
import de.decidr.modelingtool.client.ui.Node;
import de.decidr.modelingtool.client.ui.StartNode;

/**
 * This command adds a start or end node to the workflow.
 * 
 * @author Johannes Engelhardt
 */
public class CreateStartEndNodeCommand implements UndoableCommand {

    /** The graphical node. */
    Node node = null;

    /** The model of the invoke node. */
    NodeModel model = null;

    /**
     * Constructor for creating a start node from an existing and linked start
     * node model. The node is drawn from the data of the model.
     * 
     * @param model
     *            The linked node model.
     */
    public CreateStartEndNodeCommand(StartNodeModel model)
            throws IncompleteModelDataException {
        this.model = model;

        // check if model contains all needed information. if not, and
        // IncompleteModelDataException is thrown
        checkModelData();

        // create node
        node = new StartNode(model.getParentModel()
                .getHasChildrenChangeListener());

        // link node and model
        node.setModel(model);
        model.setChangeListener(node);
    }

    /**
     * Constructor for creating a end node from an existing and linked end
     * node model. The node is drawn from the data of the model.
     * 
     * @param model
     *            The linked node model.
     */
    public CreateStartEndNodeCommand(EndNodeModel model)
            throws IncompleteModelDataException {
        this.model = model;

        // check if model contains all needed information. if not, and
        // IncompleteModelDataException is thrown
        checkModelData();

        // create node
        node = new EndNode(model.getParentModel()
                .getHasChildrenChangeListener());

        // link node and model
        node.setModel(model);
        model.setChangeListener(node);
    }

    @Override
    public void undo() {
        // remove node from workflow
        node.getParentPanel().removeNode(node);

        // remove model from workflow
        model.getParentModel().removeNodeModel(model);
    }

    @Override
    public void execute() {
        // add node to parent panel
        node.getParentPanel().addNode(node, model.getChangeListenerLeft(),
                model.getChangeListenerTop());

        // add model to workflow
        model.getParentModel().addNodeModel(model);
    }

    /**
     * Checks the node model if it consists all required data for drawing the
     * container: its parent model.
     * 
     * @return True, if all required data is not null.
     * @throws IncompleteModelDataException
     *             if any relevant data is null.
     */
    private boolean checkModelData() throws IncompleteModelDataException {
        if (model.getParentModel() == null) {
            throw new IncompleteModelDataException("model.parentModel is null.");
        }

        return true;
    }

}
