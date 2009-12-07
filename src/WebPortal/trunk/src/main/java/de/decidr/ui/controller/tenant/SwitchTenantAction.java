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

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Item;
import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.acl.roles.WorkflowAdminRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.controller.UIDirector;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.uibuilder.SuperAdminViewBuilder;
import de.decidr.ui.view.uibuilder.TenantAdminViewBuilder;
import de.decidr.ui.view.uibuilder.UIBuilder;
import de.decidr.ui.view.uibuilder.UserViewBuilder;
import de.decidr.ui.view.uibuilder.WorkflowAdminViewBuilder;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * If a user switches the tenant this action is called. The session attributes
 * role and tenant name will be overridden with the new tenant name and the new
 * role. The new tenant theme is set.
 * 
 * @author AT
 */
public class SwitchTenantAction implements ClickListener {

    /**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;

	private String tenantName = null;

    private HttpSession session = null;
    private Class<? extends Role> role = null;
    private Class<? extends Role> oldRole = null;

    private Long userId = null;
    private UserFacade userFacade = null;

    private Long tenantId = null;

    private Table table = null;

    private UIBuilder uiBuilder = null;
    private UIDirector uiDirector = Main.getCurrent().getUIDirector();

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
    @SuppressWarnings("unchecked")
    @Override
    public void buttonClick(ClickEvent event) {
    	Set<?> set = (Set<?>) table.getValue();
        if (table.getValue() != null && set.size() == 1) {
        	Iterator<?> iter = set.iterator();
        	while(iter.hasNext()){
        		Item item = (Item) iter.next();
        		try {
                    session = Main.getCurrent().getSession();
                    userId = (Long) session.getAttribute("userId");
                    oldRole = (Class<Role>) session.getAttribute("role");

                    userFacade = new UserFacade(new UserRole(userId));

                    tenantName = item.getItemProperty(
                            "name").getValue().toString();
                    tenantId = (Long) item
                            .getItemProperty("id").getValue();
                    role = userFacade.getUserRoleForTenant(userId, tenantId);

                    userFacade.setCurrentTenantId(userId, tenantId);

                    session.setAttribute("tenant", tenantName);
                    session.setAttribute("role", role);

                    loadProtectedResources();

                    Main.getCurrent().getMainWindow().addWindow(
                            new InformationDialogComponent("Switched to "
                                    + tenantName, "Success"));
                } catch (TransactionException exception) {
                    Main.getCurrent().getMainWindow().addWindow(
                            new TransactionErrorDialogComponent(exception));
                }
                
        	}
        	
        } else {
        	Main.getCurrent().getMainWindow().addWindow(
                    new InformationDialogComponent("Please select an item",
                            "Information"));
        }

    }

    /**
     * Loads the protected resources if and only if the user is logged in.
     * 
     */
    private void loadProtectedResources() {
        if (UserRole.class.equals(role) || role == null) {
            uiBuilder = new UserViewBuilder();
            uiDirector.setUiBuilder(uiBuilder);
        } else if (WorkflowAdminRole.class.equals(role)) {
            uiBuilder = new WorkflowAdminViewBuilder();
            uiDirector.setUiBuilder(uiBuilder);
        } else if (TenantAdminRole.class.equals(role)) {
            uiBuilder = new TenantAdminViewBuilder();
            uiDirector.setUiBuilder(uiBuilder);
        } else if (SuperAdminRole.class.equals(role)) {
            uiBuilder = new SuperAdminViewBuilder();
            uiDirector.setUiBuilder(uiBuilder);
        } else {
            Main.getCurrent().getMainWindow().showNotification(
                    "Role class not found");
        }
        if (!oldRole.equals(role)) {
            uiDirector.constructView();
        }

        Main.getCurrent().setTheme(tenantName);
    }
}
