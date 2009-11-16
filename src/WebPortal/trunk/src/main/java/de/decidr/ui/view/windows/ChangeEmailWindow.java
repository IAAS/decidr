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

package de.decidr.ui.view.windows;

/**
 * In this window the user can change his email address by inserting 
 * his new email address.
 *
 * @author Geoffrey-Alexeij Heinze
 */

import com.vaadin.data.Item;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.decidr.ui.controller.HideDialogWindowAction;
import de.decidr.ui.controller.user.ChangeEmailAction;

public class ChangeEmailWindow extends Window {

    private VerticalLayout verticalLayout = null;
    private HorizontalLayout horizontalLayout = null;

    private Label infoLabel = null;

    private Form emailForm = null;

    private TextField newEmailText = null;

    private Button submitButton = null;
    private Button cancelButton = null;

    /**
     * Default constructor
     * 
     */
    public ChangeEmailWindow() {
        init();
    }

    public Item getNewEmail() {
        return emailForm;
    }

    /**
     * Initializes the components for the change email component.
     * 
     */
    private void init() {
        emailForm = new Form();
        emailForm.setWriteThrough(false);

        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true);
        verticalLayout.setSizeUndefined();

        horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        horizontalLayout.setMargin(false);
        horizontalLayout.setSizeUndefined();

        infoLabel = new Label(
                "To change your email address, insert your new email address and click Change E-mail.<br/>"
                        + "A confirmation email will be send to the new address.",
                Label.CONTENT_XHTML);
        infoLabel.setWidth(350, Sizeable.UNITS_PIXELS);

        newEmailText = new TextField();
        newEmailText.setCaption("New E-mail Address");
        newEmailText.setColumns(20);

        submitButton = new Button("Change E-mail", new ChangeEmailAction());
        cancelButton = new Button("Cancel", new HideDialogWindowAction());

        emailForm.setWidth(370, Sizeable.UNITS_PIXELS);
        // emailForm.setSizeUndefined();
        emailForm.addField("newEmail", newEmailText);
        emailForm.getField("newEmail").addValidator(
                new EmailValidator("Please enter a valid email adress"));

        verticalLayout.addComponent(infoLabel);
        verticalLayout.addComponent(emailForm);
        horizontalLayout.addComponent(submitButton);
        horizontalLayout.addComponent(cancelButton);
        verticalLayout.addComponent(horizontalLayout);

        horizontalLayout.setComponentAlignment(cancelButton, "right bottom");
        horizontalLayout.setComponentAlignment(submitButton, "right bottom");

        verticalLayout.setComponentAlignment(horizontalLayout, "right bottom");

        this.setModal(true);
        this.setResizable(false);
        this.setCaption("Change E-mail Address");
        this.setContent(verticalLayout);
    }

}