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

import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.beans.UserBean;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * TODO: add comment
 * 
 * @author AT
 */
public class AddMemberToTenantAction implements ClickListener {

    private Role role = (Role) Main.getCurrent().getSession().getAttribute(
            "role");
    private Long tenantId = (Long) Main.getCurrent().getSession().getAttribute(
            "tenantId");
    private TenantFacade tenantFacade = new TenantFacade(role);
    private UserFacade userFacade = new UserFacade(role);

    private Table table;

    /**
     * Constructor with a given table where the memebers are selected
     * 
     */
    public AddMemberToTenantAction(Table table) {
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

        if (table.getValue() != null) {
            UserBean userBean = (UserBean) table.getValue();

            try {
                tenantFacade.addTenantMember(tenantId, userBean.getId());
                userFacade.setCurrentTenantId(userBean.getId(), tenantId);
                Main.getCurrent().getMainWindow().addWindow(
                        new InformationDialogComponent(userBean.getUsername()
                                + " successfully add to the current tenant",
                                "Success"));
            } catch (TransactionException e) {
                Main.getCurrent().getMainWindow().addWindow(
                        new TransactionErrorDialogComponent(e));
            }
        } else {
            Main
                    .getCurrent()
                    .getMainWindow()
                    .addWindow(
                            new InformationDialogComponent(
                                    "Please select a member from the table to add him to the tenant",
                                    "Information"));
        }

    }
}
