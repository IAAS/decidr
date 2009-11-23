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

package de.decidr.modelingtool.client.model;

import de.decidr.modelingtool.client.ui.Node;

/**
 * This is the base class for all nodes and container models of a workflow. It
 * has an input and output connection as well as a {@link NodePropertyData}
 * container.
 * 
 * @author Johannes Engelhardt, Jonas Schlaak
 */
public class NodeModel extends AbstractModel {

    private HasChildModels parentModel = null;
    private ConnectionModel input = null;
    private ConnectionModel output = null;
    private Node node = null;

    /**
     * The property container for the model data.
     */
    protected NodePropertyData properties;

    /**
     * The x coordinate of the assigned change listener
     */
    protected int changeListenerLeft = 0;

    /**
     * The y coordinate of the assigned change listener
     */
    protected int changeListenerTop = 0;

    /** The height of the assigned changelistener */
    protected int changeListenerWidth = 0;

    /** The width of the assigned changelistener */
    protected int changeListenerHeight = 0;

    public NodeModel() {
        super();
        properties = new NodePropertyData();
    }

    public NodeModel(HasChildModels parentModel) {
        super();
        this.parentModel = parentModel;
        properties = new NodePropertyData();
    }

    public int getChangeListenerLeft() {
        return changeListenerLeft;
    }

    public int getChangeListenerTop() {
        return changeListenerTop;
    }

    public ConnectionModel getInput() {
        return input;
    }

    public Node getNode() {
        return node;
    }

    public ConnectionModel getOutput() {
        return output;
    }

    public HasChildModels getParentModel() {
        return parentModel;
    }

    public NodePropertyData getProperties() {
        return this.properties;
    }

    public void setChangeListenerPosition(int left, int top) {
        this.changeListenerLeft = left;
        this.changeListenerTop = top;
    }

    /**
     * Sets the size properties of the change listener. After changing this
     * data, fireModelChanged has to be called.
     * 
     * @param width
     *            The (desired) width of the change listener.
     * @param height
     *            The (desired) height of the change listener.
     */
    public void setChangeListenerSize(int width, int height) {
        this.changeListenerWidth = width;
        this.changeListenerHeight = height;
    }

    public void setInput(ConnectionModel input) {
        this.input = input;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setOutput(ConnectionModel output) {
        this.output = output;
    }

    public void setParentModel(HasChildModels parentModel) {
        this.parentModel = parentModel;
    }

    public void setProperties(NodePropertyData properties) {
        this.properties = properties;
    }

}
