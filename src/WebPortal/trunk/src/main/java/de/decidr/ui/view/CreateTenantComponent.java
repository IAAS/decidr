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
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.controller.tenant.CreateTenantAction;

/**
 * A tenant will be created by entering some information about the tenant. The
 * informations are the tenant name and the tenant description.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class CreateTenantComponent extends CustomComponent {

    private VerticalLayout verticalLayout = null;

    private Label descriptionLabel = null;

    private Button createTenant = null;

    private TextField tenantName = null;
    private TextField tenantDescription = null;

    private Form createTenantForm = null;

    /**
     * Default constructor.
     * 
     */
    public CreateTenantComponent() {
        init();
    }

    /**
     * Initializes the components for the create tenent component.
     * 
     */
    private void init() {
        verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setSpacing(true);

        createTenantForm = new Form();
        createTenantForm.setWriteThrough(false);

        descriptionLabel = new Label(
                "Create a new tenant by entring a name and description:<br/>Further options can be set later via Tenant Settings.",
                Label.CONTENT_XHTML);

        tenantName = new TextField();
        tenantName.setCaption("Tenant name:");

        tenantDescription = new TextField();
        tenantDescription.setCaption("Tenant description");
        tenantDescription.setRows(5);

        createTenantForm.addField("tenantName", tenantName);
        createTenantForm.getField("tenantName").setRequired(true);
        createTenantForm.addField("tenantDescription", tenantDescription);
        createTenantForm.getField("tenantDescription").setRequired(true);

        createTenant = new Button("Create Tenant", new CreateTenantAction(
                createTenantForm));

        verticalLayout.addComponent(descriptionLabel);
        verticalLayout.addComponent(createTenantForm);
        verticalLayout.addComponent(createTenant);

        this.setCompositionRoot(verticalLayout);

    }
}
