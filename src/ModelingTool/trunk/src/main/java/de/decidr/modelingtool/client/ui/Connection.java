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

import com.allen_sauer.gwt.dnd.client.DragController;
import com.google.gwt.user.client.ui.AbsolutePanel;

/**
 * TODO: add comment
 * 
 * @author engelhjs
 */
public abstract class Connection implements Selectable {
    
    protected AbsolutePanel parentPanel = null;

    private boolean selected;

    // TODO
    // private ConnectionModel model;

    protected Point startEndPoint = new Point();

    protected Point endEndPoint = new Point();

    private DragController dragController;

    private Port sourcePort = null;

    private Port targetPort = null;

    public abstract void draw();
    
    public abstract void delete();
    
    public void onWorkflowAdd(AbsolutePanel parentPanel) {
        this.parentPanel = parentPanel;
        draw();
    }

    public Point getStartEndPoint() {
        return startEndPoint;
    }

    public void setStartEndPoint(Point startEndPoint) {
        this.startEndPoint = startEndPoint;
    }

    public Point getEndEndPoint() {
        return endEndPoint;
    }

    public void setEndEndPoint(Point endEndPoint) {
        this.endEndPoint = endEndPoint;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Port getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(Port sourcePort) {
        this.sourcePort = sourcePort;
    }

    public Port getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(Port targetPort) {
        this.targetPort = targetPort;
    }

}
