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

package de.decidr.ui.controller.show;

import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.ui.controller.UIDirector;
import de.decidr.ui.data.ModelingTool;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.SiteFrame;

/**
 * Opens the modeling tool.
 * 
 * @author AT
 */
public class ShowModelingToolAction implements ClickListener {

    UIDirector uiDirector = Main.getCurrent().getUIDirector();
    SiteFrame siteFrame = uiDirector.getTemplateView();
    
    private Table table = null;
    
    /**
	 * TODO: add comment
	 *
	 */
	public ShowModelingToolAction(Table table) {
		this.table = table;
	}

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        
    	siteFrame.setContent(new ModelingTool(table));
    }

}
