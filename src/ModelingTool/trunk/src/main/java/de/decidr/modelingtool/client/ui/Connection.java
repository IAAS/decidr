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

import com.google.gwt.user.client.ui.Label;

import de.decidr.modelingtool.client.model.ConnectionModel;
import de.decidr.modelingtool.client.ui.selection.ConnectionDragBox;

/**
 * TODO: add comment
 * 
 * @author engelhjs
 */
public abstract class Connection implements Selectable, ModelChangeListener {

    protected HasChildren parentPanel = null;
    
    // subclasses have to implement this in draw() operation
    protected Label label = new Label();

    private boolean selected;

    private ConnectionModel model;

    protected ConnectionDragBox startDragBox;

    protected ConnectionDragBox endDragBox;

    public Connection(HasChildren parentPanel) {
        this.parentPanel = parentPanel;
        
        // TODO: debug
        label.setText("ConnectionLabel");
    }

    public abstract void draw();

    public ConnectionDragBox getEndDragBox() {
        return endDragBox;
    }

    public ConnectionModel getModel() {
        return model;
    }

    public HasChildren getParentPanel() {
        return parentPanel;
    }

    public ConnectionDragBox getStartDragBox() {
        return startDragBox;
    }

    // private Port sourcePort = null;
    //
    // private Port targetPort = null;

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

    @Override
    public void onModelChange() {
        label.setText(model.getName()); 
        draw();
    }

    public void onPanelAdd(HasChildren parentPanel) {
        this.parentPanel = parentPanel;
        draw();
    }

    public abstract void remove();

    public void setEndDragBox(ConnectionDragBox endDragBox) {
        this.endDragBox = endDragBox;
        // FIXME: compiler error in this line
        // endDragBox.setConnection(this);
    }

    public void setModel(ConnectionModel model) {
        this.model = model;
    }

    public void setParentPanel(HasChildren parentPanel) {
        this.parentPanel = parentPanel;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;

        if (selected) {
            // bring start drag box to front
            startDragBox.getGluedPort().add(startDragBox);
            startDragBox.setVisibleStyle(selected);
    
            // bring end drag box to front
            endDragBox.getGluedPort().add(endDragBox);
            endDragBox.setVisibleStyle(selected);
        }
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
