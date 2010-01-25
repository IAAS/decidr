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

import com.google.gwt.user.client.ui.FocusPanel;

import de.decidr.modelingtool.client.exception.NoPropertyWindowException;

/**
 * End node of every workflow. This is neither deletable nor resizable.
 * 
 * @author Johannes Engelhardt
 */
public class EndNode extends Node implements ModelChangeListener {

    public EndNode(HasChildren parentPanel) {
        super(parentPanel);

        graphic = new FocusPanel();
        graphic.setStyleName("node-graphic-endnode");
        setGraphic(graphic);

        this.setInputPort(new InputPort());
    }

    @Override
    public boolean isDeletable() {
        return false;
    }

    @Override
    public boolean isResizable() {
        return false;
    }

    @Override
    public void showPropertyWindow() throws NoPropertyWindowException {
        throw new NoPropertyWindowException("This node has no properties.");
    }

}