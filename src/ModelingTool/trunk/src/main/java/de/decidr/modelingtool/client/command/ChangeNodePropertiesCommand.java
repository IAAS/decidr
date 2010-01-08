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

import de.decidr.modelingtool.client.model.nodes.EmailInvokeNodeModel;
import de.decidr.modelingtool.client.model.nodes.NodePropertyData;
import de.decidr.modelingtool.client.ui.EmailInvokeNode;
import de.decidr.modelingtool.client.ui.Node;

/**
 * This command changes the properties of a {@link Node}. The properties of a
 * node are typically the model data, for example, the properties of an
 * {@link EmailInvokeNode} are stored in {@link EmailInvokeNodeModel}.
 * 
 * @author Jonas Schlaak
 */
public class ChangeNodePropertiesCommand implements UndoableCommand {

    private Node node;
    private NodePropertyData oldProperties;
    private NodePropertyData newProperties;

    /**
     * Default constructor for the command.
     * 
     * @param node
     *            the node whose properties are to be changed
     * @param newModel
     *            the new model in which the properties are stored
     */
    public ChangeNodePropertiesCommand(Node node, NodePropertyData newModel) {
        this.node = node;
        this.oldProperties = node.getModel().getProperties();
        this.newProperties = newModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        node.getModel().setProperties(newProperties);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.command.UndoableCommand#undo()
     */
    @Override
    public void undo() {
        node.getModel().setProperties(oldProperties);
    }

}
