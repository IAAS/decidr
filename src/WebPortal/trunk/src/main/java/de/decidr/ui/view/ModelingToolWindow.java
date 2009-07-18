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

package de.decidr.ui.view;


import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * TODO: add comment
 *
 * @author AT
 */
public class ModelingToolWindow extends Window {
    
    private VerticalLayout verticalLayout = null;
    
    private ModelingTool modelingTool = null;
    
    /**
     * TODO: add comment
     *
     */
    public ModelingToolWindow() {
        init();
    }

    /**
     * TODO: add comment
     *
     */
    private void init() {
        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        //verticalLayout.setSizeFull();
        //verticalLayout.setSizeUndefined();
        verticalLayout.setWidth(600, VerticalLayout.UNITS_PIXELS);
        verticalLayout.setHeight(420, VerticalLayout.UNITS_PIXELS);
        
        modelingTool = new ModelingTool();
        
        verticalLayout.addComponent(modelingTool);
        
        this.setModal(true);
        this.setResizable(false);
        this.setContent(verticalLayout);
        this.setCaption("Modeling Tool");
    }

    
}