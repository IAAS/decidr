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

import javax.servlet.http.HttpSession;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.UserRole;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * If a user switches the tenant this action is called. The session attributes
 * role and tenant name will be overridden with the new tenant name and the
 * new role. The new tenant theme is set.
 *
 * @author AT
 */
public class SwitchTenantAction implements ClickListener {
    
    private String tenant = null;
    
    private HttpSession session = Main.getCurrent().getSession();
    private Class<? extends Role> role = null;
    
    private Long userId = (Long)session.getAttribute("userId");
    private TenantFacade tenantFacade = new TenantFacade(new UserRole(userId));
    private UserFacade userFacade = new UserFacade(new UserRole(userId));
    
    private Long tenantId = null;

    /* (non-Javadoc)
     * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        try{
            tenant = Main.getCurrent().getParameterHandler().getKey();
            tenantId = tenantFacade.getTenantId(tenant);
            role = userFacade.getUserRoleForTenant(userId, tenantId);
            Main.getCurrent().setTheme(tenant);
            session.setAttribute("tenant", tenant);
            session.setAttribute("role", role);
        }catch(TransactionException exception){
            Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent());
        }

    }

}
