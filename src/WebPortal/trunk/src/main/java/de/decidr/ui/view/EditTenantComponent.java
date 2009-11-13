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

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.controller.tenant.ApproveTenantAction;
import de.decidr.ui.controller.tenant.DeclineTenantAction;
import de.decidr.ui.controller.tenant.DeleteTenantAction;
import de.decidr.ui.data.TenantContainer;
import de.decidr.ui.view.tables.TenantTable;

/**
 * The tenants can be edited. By selecting a tenant from the table, a tenant can
 * be deleted, approved or declined.
 * 
 * @author AT
 */
public class EditTenantComponent extends CustomComponent {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 5979343483852012500L;

    private TenantContainer tenantContainer = null;

    private VerticalLayout verticalLayout = null;
    private HorizontalLayout buttonHorizontalLayout = null;

    private Label editTenantLabel = null;

    private SearchPanel searchPanel = null;
    private Panel buttonPanel = null;

    private TenantTable tenantTable = null;

    private Button deleteButton = null;
    private Button approveButton = null;
    private Button declineButton = null;

    /**
     * Default constructor.
     * 
     */
    public EditTenantComponent() {
        init();
    }

    /**
     * This method initializes the components for the edit tenant component.
     * 
     */
    private void init() {
        tenantContainer = new TenantContainer();

        verticalLayout = new VerticalLayout();
        buttonHorizontalLayout = new HorizontalLayout();

        editTenantLabel = new Label("<h2> Edit tenant </h2>");
        editTenantLabel.setContentMode(Label.CONTENT_XHTML);

        buttonPanel = new Panel();

        tenantTable = new TenantTable(tenantContainer);

        searchPanel = new SearchPanel(tenantTable);

        deleteButton = new Button("Delete", new DeleteTenantAction(tenantTable));
        approveButton = new Button("Approve", new ApproveTenantAction(
                tenantTable));
        declineButton = new Button("Decline", new DeclineTenantAction(
                tenantTable));

        setCompositionRoot(verticalLayout);

        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(editTenantLabel);
        verticalLayout.addComponent(searchPanel);
        verticalLayout.addComponent(tenantTable);
        verticalLayout.addComponent(buttonPanel);

        buttonPanel.setCaption("Selected tenants:");
        buttonPanel.addComponent(buttonHorizontalLayout);
        buttonHorizontalLayout.setSpacing(true);
        buttonHorizontalLayout.addComponent(approveButton);
        buttonHorizontalLayout.addComponent(declineButton);
        buttonHorizontalLayout.addComponent(deleteButton);

    }

}
