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

import java.util.List;

import javax.servlet.http.HttpSession;
import com.vaadin.data.Item;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.SuperAdminRole;
import de.decidr.model.permissions.TenantAdminRole;
import de.decidr.model.permissions.UserRole;
import de.decidr.model.permissions.WorkflowAdminRole;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.SuperAdminViewBuilder;
import de.decidr.ui.view.TenantAdminViewBuilder;
import de.decidr.ui.view.UIBuilder;
import de.decidr.ui.view.UserViewBuilder;
import de.decidr.ui.view.WorkflowAdminViewBuilder;

/**
 * TODO: add comment
 *
 * @author AT
 */
public class Login {
    
    UIDirector uiDirector = UIDirector.getInstance();
    UIBuilder uiBuilder = null;
    
    UserFacade userFacade = new UserFacade(new UserRole());
    
    Long userId = null;
    List<Item> tenantList = null;
    Item tenantItem = null;
    Class<? extends Role> role = null;
    String username = null;
    
    public void authenticate(String username, String password) throws TransactionException{
        if(userFacade.getUserIdByLogin(username, password) != null){
            ApplicationContext ctx = Main.getCurrent().getContext();
            WebApplicationContext webCtx = (WebApplicationContext)ctx;
            HttpSession session = webCtx.getHttpSession();
            userId = userFacade.getUserIdByLogin(username, password);
            tenantList = userFacade.getJoinedTenants(userId);
            if(!tenantList.isEmpty()){
                tenantItem = tenantList.get(0);
            }
            role = userFacade.getUserRoleForTenant(userId, (Long)tenantItem.getItemProperty("id").getValue());
            username = (String)userFacade.getUserProfile(userId).getItemProperty("username").getValue();
            
            session.setAttribute("userId", userId);
            session.setAttribute("tenant", tenantItem);
            session.setAttribute("role", role);
            Main.getCurrent().setUser(username);
            
            loadProtectedResources();
        }else{
            Main.getCurrent().getMainWindow().showNotification("Login unsuccessful");
        }
    }

    /**
     * Loads the protected resources if and only if the user is logged in.
     *
     */
    private void loadProtectedResources() {
        if(role.getClass().equals(UserRole.class)){
            uiBuilder = new UserViewBuilder();
            uiDirector.setUiBuilder(uiBuilder);
            uiDirector.constructView();
        }
        else if(role.getClass().equals(WorkflowAdminRole.class)){
            uiBuilder = new WorkflowAdminViewBuilder();
            uiDirector.setUiBuilder(uiBuilder);
            uiDirector.constructView();
        }
        else if(role.getClass().equals(TenantAdminRole.class)){
            uiBuilder = new TenantAdminViewBuilder();
            uiDirector.setUiBuilder(uiBuilder);
            uiDirector.constructView();
        }
        else if(role.getClass().equals(SuperAdminRole.class)){
            uiBuilder = new SuperAdminViewBuilder();
            uiDirector.setUiBuilder(uiBuilder);
            uiDirector.constructView();
        }
        
        
    }
    

}
