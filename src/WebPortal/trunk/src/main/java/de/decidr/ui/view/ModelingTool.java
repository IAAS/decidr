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

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Item;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

import de.decidr.modelingtool.server.DWDLIOServiceAsyncImpl;
import de.decidr.ui.controller.UIDirector;

/**
 * TODO: add comment
 *
 * @author AT
 */
public class ModelingTool extends AbstractComponent {
    
    HttpSession session = null;
    Long id = null;
    
    private UIDirector uiDirector = UIDirector.getInstance();
    private SiteFrame siteFrame = uiDirector.getTemplateView();
      
    private WorkflowModelsComponent component = null;
    private CurrentTenantModelTable table = null;
    
    @Override
    public String getTag() {
        return "modelingtool";
    }
    
    /* (non-Javadoc)
     * @see com.vaadin.ui.AbstractComponent#paintContent(com.vaadin.terminal.PaintTarget)
     */
    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        // TODO Auto-generated method stub
        super.paintContent(target);
        
        component = (WorkflowModelsComponent)siteFrame.getContent();
        table = component.getCurrentTenantTable();
       
        //Item item = table.getItem(table.getValue());
        //Long workflowModelId = (Long)item.getItemProperty("id").getValue();
        
        target.addAttribute("workflowModelId", 10L);
        
        session = Main.getCurrent().getSession();
        
        id = (Long)session.getAttribute("userId");
        
        DWDLIOServiceAsyncImpl.setUserId(id);
        
        //target.addAttribute("userId", id);
        
    }
    
    /* (non-Javadoc)
     * @see com.vaadin.ui.AbstractComponent#changeVariables(java.lang.Object, java.util.Map)
     */
    @Override
    public void changeVariables(Object source, Map variables) {
        // TODO Auto-generated method stub
        super.changeVariables(source, variables);
    }

}
