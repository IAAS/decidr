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

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;

import de.decidr.modelingtool.client.model.ConnectionModel;
import de.decidr.modelingtool.client.ui.selection.ConnectionDragBox;

/**
 * An abstract connection without graphical representation. Subclasses have to
 * implement the draw() and delete() operations.
 * 
 * @author Johannes Engelhardt
 */
public abstract class Connection implements Selectable, ModelChangeListener {

    /** The parent panel of the connection. */
    protected HasChildren parentPanel = null;

    /**
     * The connection label. Subclasses have to implement this in their draw()
     * operation to make it visible.
     */
    protected Label label = new Label();

    /** The selected state of the connection. */
    private boolean selected;

    /** The model of the connection. */
    private ConnectionModel model;

    /** The start drag box of the connection. */
    protected ConnectionDragBox startDragBox;

    /** The end drag box of the connection. */
    protected ConnectionDragBox endDragBox;

    /**
     * The constructor.
     * 
     * @param parentPanel
     *            The parent panel of the connection
     */
    public Connection(HasChildren parentPanel) {
        this.parentPanel = parentPanel;

        // debug
        // label.setText("ConnectionLabel");
    }

    /**
     * Draws the connection. Subclasses have to care how the connection is
     * drawn.
     */
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

    public boolean isSelected() {
        return selected;
    }

    @Override
    public void onModelChange() {
        label.setText(model.getName());
        draw();
    }

    /**
     * Callback method for the parent panel. This is called after the connection
     * has been added.
     * 
     * @param parentPanel
     *            The parent panel the connection has been added to.
     */
    public void onPanelAdd(HasChildren parentPanel) {
        this.parentPanel = parentPanel;
        draw();
    }

    /**
     * Removes the connection. Subclasses have to care that all drawn elements
     * are removed.
     */
    public abstract void remove();

    public void setEndDragBox(ConnectionDragBox endDragBox) {
        this.endDragBox = endDragBox;
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
            // bring drag boxes to front
            startDragBox.getGluedPort().add(startDragBox);
            endDragBox.getGluedPort().add(endDragBox);
        } else {
            // bring single drag box to front
            startDragBox.getGluedPort().bringSingleDragBoxToFront();
            endDragBox.getGluedPort().bringSingleDragBoxToFront();
        }

        // set visible style
        startDragBox.setVisibleStyle(selected);
        endDragBox.setVisibleStyle(selected);
    }

    public void setStartDragBox(ConnectionDragBox startDragBox) {
        this.startDragBox = startDragBox;
    }

    @Override
    public void showPropertyWindow() {
        Window.alert("This connection has no properties.");
    }

}
