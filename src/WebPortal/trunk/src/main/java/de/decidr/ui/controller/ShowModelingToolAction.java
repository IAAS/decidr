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

package de.decidr.ui.controller;

import java.util.Set;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.ui.view.CurrentTenantModelTable;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.ModelingToolWindow;
import de.decidr.ui.view.SiteFrame;
import de.decidr.ui.view.WorkflowModelsComponent;

/**
 * TODO: add comment
 *
 * @author AT
 */
public class ShowModelingToolAction implements ClickListener {
    
    private ModelingToolWindow modelingToolWindow = new ModelingToolWindow();
    
    private UIDirector uiDirector = UIDirector.getInstance();
    private SiteFrame siteFrame = uiDirector.getTemplateView();
    
    private WorkflowModelsComponent component = null;
    private CurrentTenantModelTable table = null;
    
    /* (non-Javadoc)
     * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        component = (WorkflowModelsComponent)siteFrame.getContent();
        table = component.getCurrentTenantTable();
        Set<?> set = (Set<?>)table.getValue();
        
            //sMain.getCurrent().getMainWindow().showNotification("Bitte wählen sie nur ein Element aus");
       
            Main.getCurrent().getMainWindow().addWindow(modelingToolWindow);
        
       
       

    }

}
