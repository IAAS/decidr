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

import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * This action creates a new, empty workflow model
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class CreateWorkflowModelAction implements ClickListener {

    private HttpSession session = Main.getCurrent().getSession();

    private Long userId = (Long) session.getAttribute("userId");
    private TenantFacade tenantFacade = new TenantFacade(new UserRole(userId));

    private String tenant = null;

    private Long tenantId = null;

    private String name = null;
    
    private Table table = null;

    /**
     * Constructor with a given workflow model name.
     * 
     */
    public CreateWorkflowModelAction(String name, Table table) {
    	this.table = table;
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        tenant = (String) session.getAttribute("tenant");

        try {
            tenantId = tenantFacade.getTenantId(tenant);
            tenantFacade.createWorkflowModel(tenantId, name);
            table.removeItem(table.getValue());
            new ShowModelingToolAction();
        } catch (TransactionException exception) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent());
        }

    }
}