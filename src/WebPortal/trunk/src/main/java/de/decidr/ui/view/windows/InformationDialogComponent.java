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

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.decidr.ui.controller.HideDialogWindowAction;

/**
 * Some information are shown in a subwindow to inform the user about certain
 * processes.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class InformationDialogComponent extends Window {

    private static final long serialVersionUID = 1L;

    private VerticalLayout verticalLayout = null;

    private Label infoLabel = null;

    private Button cancelButton = null;

    /**
     * Default constructor. The given text and caption are shown in the
     * subwindow.
     * 
     * @param text
     * @param caption
     */
    public InformationDialogComponent(String text, String caption) {
        init(text, caption);
    }

    /**
     * Initializes the components of the information dialog with the given text
     * and the given caption
     * 
     * @param text
     * @param caption
     */
    private void init(String text, String caption) {
        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true);
        verticalLayout.setSizeUndefined();
        verticalLayout.setWidth(400, Sizeable.UNITS_PIXELS);

        infoLabel = new Label(text, Label.CONTENT_XHTML);

        cancelButton = new Button("OK", new HideDialogWindowAction());
        cancelButton.setWidth(75, Sizeable.UNITS_PIXELS);

        verticalLayout.addComponent(infoLabel);
        verticalLayout.addComponent(cancelButton);
        verticalLayout.setComponentAlignment(cancelButton, "center bottom");

        this.setModal(true);
        this.setResizable(false);
        this.setCaption(caption);
        this.setContent(verticalLayout);
    }

}
