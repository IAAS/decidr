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

package de.decidr.ui.view.windows;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.decidr.ui.data.ModelingTool;

/**
 * This class represents the client side of the modeling tool widget which is
 * integrated into the web portal. It is a window which wraps the modeling tool
 * widget.
 * 
 * @author AT
 */
public class ModelingToolWindow extends Window {

    private VerticalLayout verticalLayout = null;

    private ModelingTool modelingTool = null;

    /**
     * Default constructor which calls the init method
     * 
     */
    public ModelingToolWindow() {
        init();
    }

    /**
     * Initializes the components for the modeling tool window.
     * 
     */
    private void init() {
        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        // verticalLayout.setSizeFull();
        // verticalLayout.setSizeUndefined();
        verticalLayout.setWidth(600, Sizeable.UNITS_PIXELS);
        verticalLayout.setHeight(420, Sizeable.UNITS_PIXELS);

        modelingTool = new ModelingTool();

        verticalLayout.addComponent(modelingTool);

        this.setModal(true);
        this.setResizable(false);
        this.setContent(verticalLayout);
        this.setCaption("Modeling Tool");
    }

}
