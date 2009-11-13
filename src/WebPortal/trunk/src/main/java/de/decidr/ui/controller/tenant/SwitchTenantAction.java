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

package de.decidr.ui.controller.tenant;

import javax.servlet.http.HttpSession;

import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * If a user switches the tenant this action is called. The session attributes
 * role and tenant name will be overridden with the new tenant name and the new
 * role. The new tenant theme is set.
 * 
 * @author AT
 */
public class SwitchTenantAction implements ClickListener {

	private String tenantName = null;
	
    private HttpSession session = null;
    private Class<? extends Role> role = null;

    private Long userId = null;
    private TenantFacade tenantFacade = null;
    private UserFacade userFacade = null;

    private Long tenantId = null;
    
    private Table table = null;

    /**
     * Constructor which is given a tenant name to determine to which tenant the
     * user wants to switch
     * 
     */
    public SwitchTenantAction(Table table) {
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
        try {
            session = Main.getCurrent().getSession();
            userId = (Long) session.getAttribute("userId");
            
            tenantFacade = new TenantFacade(new UserRole(userId));
            userFacade = new UserFacade(new UserRole(userId));
            
            tenantName = table.getItem(table.getValue()).getItemProperty("name").getValue().toString();
            tenantId = tenantFacade.getTenantId(tenantName);
            role = userFacade.getUserRoleForTenant(userId, tenantId);
            Main.getCurrent().setTheme(tenantName);
            userFacade.setCurrentTenantId(userId, tenantId);
            session.setAttribute("tenant", tenantName);
            session.setAttribute("role", role);
        } catch (TransactionException exception) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(exception));
        }
    }
}
