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

package de.decidr.modelingtool.client.ui;

import java.util.Collection;
import java.util.HashSet;

import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

import de.decidr.modelingtool.client.model.HasChildModels;

/**
 * TODO: add comment
 * 
 * @author engelhjs
 */
public class Container extends Node implements HasChildren {

    private Collection<Node> childNodes = new HashSet<Node>();

    private Collection<Connection> childConnections = new HashSet<Connection>();

    private DropController dropController = new AbsolutePositionDropController(
            this);

    public Container(HasChildren parentPanel) {
        super(parentPanel);

        // set container graphic properties
        FocusPanel graphic = new FocusPanel();
        graphic.addStyleName("container-std");
        setGraphic(graphic);

        setInputPort(new InputPort());
        setOutputPort(new OutputPort());
    }

    @Override
    public void addConnection(Connection connection) {
        // add connection to the connections vector
        childConnections.add(connection);
        // callback to connection
        connection.onPanelAdd(this);

    }

    @Override
    public void addNode(Node node) {
        addNode(node, 0, 0);
    }

    /**
     * Adds a node to the container in the specified position.
     * 
     * @param node
     * @param x
     * @param y
     */
    public void addNode(Node node, int left, int top) {
        super.add(node, left, top);

        // add node to the nodes collection
        childNodes.add(node);

        // callback to node after add
        node.onPanelAdd(this);
    }

    @Override
    public Collection<Connection> getConnections() {
        return childConnections;
    }

    public DropController getDropController() {
        return dropController;
    }

    @Override
    public HasChildModels getHasChildModelsModel() {
        if (model instanceof HasChildModels) {
            return (HasChildModels) model;
        } else {
            return null;
        }
    }

    @Override
    public Collection<Node> getNodes() {
        return childNodes;
    }

    @Override
    public void removeConnection(Connection connection) {
        childConnections.remove(connection);
        connection.remove();
    }

    @Override
    public void removeNode(Node node) {
        childNodes.remove(node);

        // callback to node before remove
        node.onPanelRemove();

        super.remove(node);
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void add(Widget w) {
        super.add(w);

        if (w instanceof Node) {
            Node node = (Node) w;
            // add node to the nodes collection
            childNodes.add(node);

            // callback to node after add
            node.onPanelAdd(this);
        }
    }

    @Override
    public boolean remove(Widget w) {
        if (w instanceof Node) {
            Node node = (Node) w;
            // remove node from the nodes collection
            childNodes.remove(node);

            // callback to node before remove
            node.onPanelRemove();
        }

        return super.remove(w);
    }

}
