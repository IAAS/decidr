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

import com.google.gwt.user.client.Window;

import de.decidr.modelingtool.client.exception.IncompleteModelDataException;
import de.decidr.modelingtool.client.exception.InvalidTypeException;
import de.decidr.modelingtool.client.model.EmailInvokeNodeModel;
import de.decidr.modelingtool.client.model.NodeModel;
import de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel;
import de.decidr.modelingtool.client.ui.EmailInvokeNode;
import de.decidr.modelingtool.client.ui.HumanTaskInvokeNode;
import de.decidr.modelingtool.client.ui.InvokeNode;

/**
 * Command for creating an invoke node.
 * 
 * @author JE
 */
public class CreateInvokeNodeCommand implements UndoableCommand {

    /**
     * The graphical node.
     */
    InvokeNode node = null;

    /**
     * The model of the node.
     */
    NodeModel model = null;

    public CreateInvokeNodeCommand(InvokeNode node) {
        this.node = node;

        try {
            // create model
            if (node instanceof EmailInvokeNode) {
                model = new EmailInvokeNodeModel(node.getParentPanel()
                        .getHasChildModelsModel());
            } else if (node instanceof HumanTaskInvokeNode) {
                model = new HumanTaskInvokeNodeModel(node.getParentPanel()
                        .getHasChildModelsModel());
            }
        } catch (InvalidTypeException e) {
            e.printStackTrace();
        }
        
        // set position in model
        model.setChangeListenerPosition(node.getLeft(), node.getTop());

        // link node and model
        node.setModel(model);
        model.setChangeListener(node);

        // set parent of model
        // model.setParentModel(node.getParentPanel().getHasChildModelsModel());
    }

    public CreateInvokeNodeCommand(NodeModel model)
            throws IncompleteModelDataException {
        this.model = model;

        // check if model contains all needed information. if not, and
        // IncompleteModelDataException is thrown
        checkModelData();

        // create node
        if (model instanceof EmailInvokeNodeModel) {
            node = new EmailInvokeNode(model.getParentModel()
                    .getHasChildrenChangeListener());
        } else if (model instanceof HumanTaskInvokeNodeModel) {
            node = new HumanTaskInvokeNode(model.getParentModel()
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
