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

import de.decidr.modelingtool.client.model.ConnectionModel;
import de.decidr.modelingtool.client.ui.selection.ConnectionDragBox;

/**
 * TODO: add comment
 * 
 * @author engelhjs
 */
public abstract class Connection implements Selectable, ModelChangeListener {

    protected HasChildren parentPanel = null;

    public Connection(HasChildren parentPanel) {
        this.parentPanel = parentPanel;
    }

    private boolean selected;

    public HasChildren getParentPanel() {
        return parentPanel;
    }

    public void setParentPanel(HasChildren parentPanel) {
        this.parentPanel = parentPanel;
    }

    public ConnectionModel getModel() {
        return model;
    }

    public void setModel(ConnectionModel model) {
        this.model = model;
    }

    private ConnectionModel model;

    protected ConnectionDragBox startDragBox;

    protected ConnectionDragBox endDragBox;

    // private Port sourcePort = null;
    //
    // private Port targetPort = null;

    public abstract void remove();

    public abstract void draw();

    // public Port getSourcePort() {
    // return sourcePort;
    // }
    //    
    // public Port getTargetPort() {
    // return targetPort;
    // }
    //    
    public boolean isSelected() {
        return selected;
    }

    public void onPanelAdd(HasChildren parentPanel) {
        this.parentPanel = parentPanel;
        draw();
    }

    public void setEndDragBox(ConnectionDragBox endDragBox) {
        this.endDragBox = endDragBox;
        // FIXME: compiler error in this line
        // endDragBox.setConnection(this);
    }

    public ConnectionDragBox getStartDragBox() {
        return startDragBox;
    }

    public ConnectionDragBox getEndDragBox() {
        return endDragBox;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;

        // bring start drag box to front
        startDragBox.getGluedPort().add(startDragBox);
        startDragBox.setVisibleStyle(selected);

        // bring end drag box to front
        endDragBox.getGluedPort().add(endDragBox);
        endDragBox.setVisibleStyle(selected);
    }

    // public void setSourcePort(Port sourcePort) {
    // this.sourcePort = sourcePort;
    // }

    public void setStartDragBox(ConnectionDragBox startDragBox) {
        this.startDragBox = startDragBox;
        // FIXME: compiler error in this line
        // startDragBox.setConnection(this);
    }

    // public void setTargetPort(Port targetPort) {
    // this.targetPort = targetPort;
    // }

}
