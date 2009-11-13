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

/**
 * This action removes a user from a tenant
 *
 * @author Geoffrey-Alexeij Heinze
 */

import javax.servlet.http.HttpSession;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

public class LeaveTenantAction implements ClickListener {

    private HttpSession session = Main.getCurrent().getSession();

    private Long userId = (Long) session.getAttribute("userId");
    private UserFacade userFacade = new UserFacade(new UserRole(userId));

    private Long tenantId = null;

    /**
     * Constructor, requires id of the tenant to be left
     * 
     * @param id
     *            : Id of the tenant to be left
     */
    public LeaveTenantAction(Long id) {
        tenantId = id;
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
            userFacade.leaveTenant(userId, tenantId);
        } catch (TransactionException e) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(e));
        }
        Main.getCurrent().getMainWindow().removeWindow(
                event.getButton().getWindow());

    }
}
