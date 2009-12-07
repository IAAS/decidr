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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.controller.user.InviteUserToTenantAction;

/**
 * In this component a user can be invited to a tenant. The tenant has to insert
 * his email address or his username to be invited.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class InviteUserToTenantComponent extends CustomComponent {

    /**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;

	private Integer userCounter = 0;

    private VerticalLayout verticalLayout = null;
    private HorizontalLayout horizontalLayout = null;
    private Form inviteForm = null;
    private Label descriptionLabel = null;
    private TextField inviteUser = null;
    private Button addField = null;
    private Button appointUsers = null;

    /**
     * Default constructor.
     * 
     */
    public InviteUserToTenantComponent() {
        init();
    }

    /**
     * Adds a new field to the form of the component where the tenant can insert
     * his username or his email address.
     * 
     */
    private void addUser() {
        userCounter += 1;
        inviteForm.addField("user" + userCounter.toString(), new TextField(
                "Username/E-Mail address:"));
        inviteForm.getField("user" + userCounter.toString()).setRequired(true);
    }

    /**
     * Initializes the components for the invite user to tenant component.
     * 
     */
    private void init() {
        userCounter = 1;
        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        verticalLayout.setSizeFull();

        horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        horizontalLayout.setSpacing(true);

        inviteForm = new Form();
        inviteForm.setWriteThrough(true);

        descriptionLabel = new Label(
                "Invite new users by pressing Add User and entering either their username or email address.",
                Label.CONTENT_XHTML);

        inviteUser = new TextField();
        inviteUser.setCaption("Username/E-Mail address:");

        inviteForm.addField("user" + userCounter.toString(), inviteUser);
        inviteForm.getField("user" + userCounter.toString()).setRequired(true);

        addField = new Button("Add User", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                addUser();
            }
        });

        appointUsers = new Button("Invite Users to Tenant",
                new InviteUserToTenantAction(inviteForm));

        horizontalLayout.addComponent(addField);
        horizontalLayout.setComponentAlignment(addField, "left middle");
        horizontalLayout.addComponent(appointUsers);
        horizontalLayout.setComponentAlignment(appointUsers, "right middle");

        verticalLayout.addComponent(descriptionLabel);
        verticalLayout.addComponent(horizontalLayout);
        verticalLayout.addComponent(inviteForm);

        this.setCompositionRoot(verticalLayout);
    }
}
