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
import com.google.gwt.user.client.ui.FocusPanel;

import de.decidr.modelingtool.client.model.HasChildModels;

/**
 * TODO: add comment
 * 
 * @author engelhjs
 */
public class Container extends Node implements HasChildren {

    @Override
    public void addConnection(Connection connection) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addNode(Node node) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Collection<Connection> getConnections() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Node> getNodes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removeConnection(Connection connection) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeNode(Node node) {
        // TODO Auto-generated method stub
        
    }

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

    public Collection<Node> getChildNodes() {
        return childNodes;
    }

    public Collection<Connection> getChildConnections() {
        return childConnections;
    }

    @Override
    public void addNode(Node node, int left, int top) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public HasChildModels getHasChildModelsModel() {
        // TODO Auto-generated method stub
        return null;
    }

    public DropController getDropController() {
        return dropController;
    }

}
