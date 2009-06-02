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

package main.java.de.decidr.modelingtool.client.ui;

import java.util.List;
import java.util.Vector;

import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * TODO: add comment
 * 
 * @author engelhjs
 */
public class Container extends Node {

    private List<Node> childNodes = new Vector<Node>();

    private List<Connection> childConnections = new Vector<Connection>();

    private DropController dropController = new AbsolutePositionDropController(
            this);

    public Container() {
        super();

        // set container graphic properties
        Widget graphic = new FocusPanel();
        graphic.addStyleName("container-std");
        this.setGraphic(graphic);
        
        this.addPort(new InputPort());
        this.addPort(new OutputPort());
    }

    public List<Node> getChildNodes() {
        return childNodes;
    }

    public List<Connection> getChildConnections() {
        return childConnections;
    }

    public DropController getDropController() {
        return dropController;
    }

}
