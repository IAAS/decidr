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

import de.decidr.model.annotations.Reviewed;
import de.decidr.ui.controller.AppointWorkflowAdminAction;

/**
 * In this component a user can appoint a workflow administrator for a workflow
 * instance.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = "RR", lastRevision = "2048")
public class AppointWorkflowAdminComponent extends CustomComponent {

    private Integer userCounter = 0;
    private Long wfmId = null;

    private VerticalLayout verticalLayout = null;
    private HorizontalLayout horizontalLayout = null;
    private Form appointForm = null;
    private Label descriptionLabel = null;
    private Button addField = null;
    private Button appointUsers = null;

    /**
     * Default constructor. The given workflow model ID is stored in a variable.
     * 
     * @param workflowModelId
     */
    public AppointWorkflowAdminComponent(Long workflowModelId) {
        wfmId = workflowModelId;
        init();
    }

    /**
     * Adds a user to the form.
     */
    private void addUser() {
        userCounter += 1;
        appointForm.addField("user" + userCounter.toString(), new TextField(
                "Username:"));
    }

    /**
     * Initializes the components for the {@link AppointWorkflowAdminComponent}.
     */
    // Aleks, GH: set initial focus? ~rr
    private void init() {
        TextField appointSelf = null;

        userCounter = 1;
        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        verticalLayout.setSizeFull();

        horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        horizontalLayout.setSpacing(true);

        appointForm = new Form();
        appointForm.setWriteThrough(true);

        descriptionLabel = new Label("Add new workflow admins by pressing "
                + "\"Add User\" and entering their username.<br/>"
                + "You will always be added automatically.",
                Label.CONTENT_XHTML);

        appointSelf = new TextField();
        appointSelf.setCaption("Username:");
        appointSelf.setValue(Main.getCurrent().getUser());
        appointSelf.setEnabled(false);

        appointForm.addField("user" + userCounter.toString(), appointSelf);

        addField = new Button("Add User", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                addUser();
            }
        });

        appointUsers = new Button("Appoint Users as Workflow Admins",
                new AppointWorkflowAdminAction(appointForm, wfmId));

        horizontalLayout.addComponent(addField);
        horizontalLayout.setComponentAlignment(addField, "left middle");
        horizontalLayout.addComponent(appointUsers);
        horizontalLayout.setComponentAlignment(appointUsers, "right middle");

        verticalLayout.addComponent(descriptionLabel);
        verticalLayout.addComponent(horizontalLayout);
        verticalLayout.addComponent(appointForm);

        this.setCompositionRoot(verticalLayout);
    }
}
