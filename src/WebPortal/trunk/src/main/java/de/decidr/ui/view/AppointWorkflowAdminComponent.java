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

import com.vaadin.data.Property.ConversionException;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.controller.AppointWorkflowAdminAction;

/**
 * In this component a user can appoint a workflow administrator for a workflow
 * instance.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = "RR", lastRevision = "2048", currentReviewState = State.PassedWithComments)
public class AppointWorkflowAdminComponent extends CustomComponent {

    private static final long serialVersionUID = 1L;
    private Integer userCounter = 0;
    private Long wfmId = null;

    private VerticalLayout verticalLayout = null;
    private HorizontalLayout horizontalLayout = null;
    private Form appointForm = null;
    private Label descriptionLabel = null;
    private Button addField = null;
    private Button appointUsers = null;
    
    private UserFacade userFacade = null;

    /**
     * The given workflow model ID is stored in a variable.
     * 
     * @param workflowModelId
     *            TODO document
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
    
    private void init() {
        TextField appointSelf = null;
        Long userId = (Long) Main.getCurrent().getSession().getAttribute("userId");
        userFacade = new UserFacade (new TenantAdminRole(userId));

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
        try {
            appointSelf.setValue(userFacade.getUserProfile(userId).getItemProperty("username").getValue().toString());
        } catch (ReadOnlyException e) {
            appointSelf.setValue("");
        } catch (ConversionException e) {
            appointSelf.setValue("");
        } catch (TransactionException e) {
            appointSelf.setValue("");
        }
        appointSelf.setEnabled(false);

        appointForm.addField("user" + userCounter.toString(), appointSelf);

        addField = new Button("Add User", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

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
