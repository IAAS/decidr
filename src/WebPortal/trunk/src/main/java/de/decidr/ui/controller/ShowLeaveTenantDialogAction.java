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

/**
 * This action displays the ConfirmDialogComponent
 *
 * @author Geoffrey-Alexeij Heinze
 */

import javax.servlet.http.HttpSession;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.ui.view.ConfirmDialogComponent;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;

public class ShowLeaveTenantDialogAction implements ClickListener {

    private HttpSession session = null;
    private Long userId = null;
    private TenantFacade tenantFacade = null;
    private Long tenantId = null;

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
            tenantId = tenantFacade.getTenantId((String) session
                    .getAttribute("tenant"));

            Main
                    .getCurrent()
                    .getMainWindow()
                    .addWindow(
                            new ConfirmDialogComponent(
                                    "Please confirm that you want to leave your tenant.",
                                    new LeaveTenantAction(tenantId)));
        } catch (TransactionException e) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(e));
        }
    }
}
