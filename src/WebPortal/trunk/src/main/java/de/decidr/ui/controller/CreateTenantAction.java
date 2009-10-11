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

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.Form;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * This action creates a new tenant
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class CreateTenantAction implements ClickListener {

    private HttpSession session = Main.getCurrent().getSession();

    private Long userId = (Long) session.getAttribute("userId");
    private TenantFacade tenantFacade = new TenantFacade(new UserRole(userId));

    private Form createForm = null;

    public CreateTenantAction(Form form) {
        createForm = form;
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        createForm
                .getField("tenantName")
                .addValidator(
                        new RegexpValidator(
                                "\\w{2,50}",
                                "Der Tenant-Name darf 2-50 Zeichen lang sein und darf keine Sonderzeichen enthalten"));
        createForm.commit();

        Long tId = null;
        try {
            tId = tenantFacade.getTenantId(createForm.getItemProperty(
                    "tenantName").getValue().toString());
            
            if (tId != null) {
                createForm.getField("tenantName").setValue((String)Main.getCurrent().getSession().getAttribute("tenant"));
                createForm.getField("tenantDescription").setValue(tenantFacade.getTenantSettings(tId).getItemProperty("description").getValue());
            } else {
                try {
                    tenantFacade.createTenant(createForm.getItemProperty(
                            "tenantName").getValue().toString(), createForm
                            .getItemProperty("tenantDescription").getValue()
                            .toString(), userId);
                } catch (TransactionException e) {
                    Main.getCurrent().getMainWindow().addWindow(
                            new TransactionErrorDialogComponent());
                }
            }
        } catch (TransactionException e) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent());
        }
    }
}
