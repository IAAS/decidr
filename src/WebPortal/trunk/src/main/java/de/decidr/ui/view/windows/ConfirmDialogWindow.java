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
 * The user can confirm his invitation to the decidr application via
 * this dialog.
 *
 * @author Geoffrey-Alexeij Heinze
 */

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.ui.controller.HideDialogWindowAction;

public class ConfirmDialogWindow extends Window {

    /**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;
	private VerticalLayout verticalLayout = null;
    private HorizontalLayout horizontalLayout = null;

    private Label infoLabel = null;

    private Button submitButton = null;
    private Button cancelButton = null;

    /**
     * Default constructor.
     * 
     * @param text
     * @param listener
     */
    public ConfirmDialogWindow(String text, ClickListener listener) {
        init(text, listener);
    }

    /**
     * Initializes the components for the confirm dialog component.
     * 
     * @param text
     * @param listener
     */
    private void init(String text, ClickListener listener) {
        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true);
        verticalLayout.setSizeUndefined();
        verticalLayout.setWidth(400, Sizeable.UNITS_PIXELS);

        horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        horizontalLayout.setMargin(false);
        horizontalLayout.setSizeUndefined();

        infoLabel = new Label(text, Label.CONTENT_XHTML);

        submitButton = new Button("Confirm");
        submitButton.addListener(listener);
        cancelButton = new Button("Cancel", new HideDialogWindowAction());

        verticalLayout.addComponent(infoLabel);
        horizontalLayout.addComponent(submitButton);
        horizontalLayout.addComponent(cancelButton);
        verticalLayout.addComponent(horizontalLayout);

        horizontalLayout.setComponentAlignment(submitButton, "center bottom");
        horizontalLayout.setComponentAlignment(cancelButton, "center bottom");

        verticalLayout.setComponentAlignment(horizontalLayout, "right bottom");

        this.setModal(true);
        this.setResizable(false);
        this.setCaption("Confirmation Required");
        this.setContent(verticalLayout);
    }

}
