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

import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.user.client.ui.HTML;

/**
 * TODO: add comment
 * 
 * @author engelhjs
 */
public abstract class Connection extends HTML implements Selectable {

    private boolean selected;

    // TODO
    // private ConnectionModel model;

    private Point startEndPoint;

    private Point endEndPoint;

    private DropController dragController;

    public abstract void draw();

    public Port getSourcePort() {
        // TODO Auto-generated method stub
        return null;
    }

    public Port getTargetPort() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isSelected() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setSelected(boolean selected) {
        // TODO Auto-generated method stub

    }

}
