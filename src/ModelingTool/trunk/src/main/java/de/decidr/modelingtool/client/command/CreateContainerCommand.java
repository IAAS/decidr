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
import de.decidr.modelingtool.client.exception.InvalidTypeException;
import de.decidr.modelingtool.client.model.ContainerModel;
import de.decidr.modelingtool.client.model.FlowContainerModel;
import de.decidr.modelingtool.client.model.IfContainerModel;
import de.decidr.modelingtool.client.model.foreach.ForEachContainerModel;
import de.decidr.modelingtool.client.ui.Container;
import de.decidr.modelingtool.client.ui.FlowContainer;
import de.decidr.modelingtool.client.ui.ForEachContainer;
import de.decidr.modelingtool.client.ui.IfContainer;

/**
 * TODO: add comment
 * 
 * @author JE
 */
public class CreateContainerCommand implements UndoableCommand {

    /**
     * The graphical container node.
     */
    Container node = null;

    /**
     * The model of the node.
     */
    ContainerModel model = null;

    public CreateContainerCommand(Container node) {
        this.node = node;

        try {
            // create model
            if (node instanceof FlowContainer) {
                model = new FlowContainerModel(node.getParentPanel()
                        .getHasChildModelsModel());
            } else if (node instanceof ForEachContainer) {
                model = new ForEachContainerModel(node.getParentPanel()
                        .getHasChildModelsModel());
            } else if (node instanceof IfContainer) {
                model = new IfContainerModel(node.getParentPanel()
                        .getHasChildModelsModel());
            }
        } catch (InvalidTypeException e) {
            e.printStackTrace();
        }

        model.setChangeListenerPosition(node.getLeft(), node.getTop());
        model.setChangeListenerSize(node.getOffsetWidth(), node
                .getOffsetHeight());

        // link node and model
        node.setModel(model);
        model.setChangeListener(node);

        // set parent of model
        // model.setParentModel(node.getParentPanel().getHasChildModelsModel());
    }

    public CreateContainerCommand(ContainerModel model)
            throws IncompleteModelDataException {
        this.model = model;

        // check if model contains all needed information. if not, and
        // IncompleteModelDataException is thrown
        checkModelData();

        // create node
        if (model instanceof FlowContainerModel) {
            node = new FlowContainer(model.getParentModel()
                    .getHasChildrenChangeListener());
        } else if (model instanceof ForEachContainerModel) {
            node = new ForEachContainer(model.getParentModel()
                    .getHasChildrenChangeListener());
        } else if (model instanceof IfContainerModel) {
            node = new IfContainer(model.getParentModel()
                    .getHasChildrenChangeListener());
        }

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
        // set size
        node.setGraphicPixelSize(model.getChangeListenerWidth(), model
                .getChangeListenerHeight());

        // add model to workflow
        model.getParentModel().addNodeModel(model);
    }

    /**
     * 
     * TODO: add comment
     * 
     * @return
     * @throws IncompleteModelDataException
     */
    private boolean checkModelData() throws IncompleteModelDataException {
        if (model.getParentModel() == null) {
            throw new IncompleteModelDataException("model.parentModel is null.");
        }

        return true;
    }

}
