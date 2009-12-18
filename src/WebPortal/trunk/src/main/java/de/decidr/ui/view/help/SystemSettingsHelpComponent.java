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

package de.decidr.ui.view.help;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;

/**
 * This component is part of the integrated manual and contains information
 * related to the system settings.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2384", currentReviewState = State.PassedWithComments)
public class SystemSettingsHelpComponent extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    private Button editSettingsButton = null;
    private Label editSettingsLabel = null;

    public SystemSettingsHelpComponent() {
        setMargin(false, true, true, true);

        editSettingsLabel = new Label("<ol>"
                +"<li>Log into DecidR as tenant admin and navigate to the workflow modeling section by clicking on the &quot;System Settings&quot; navigation link.</li>"
                + "<li>The system displays the following information:"
                + "<ul>"
                + "<li>Whether the super admin must confirm all new tenants or they are accepted automatically.</li>"
                + "<li>Amount of logic to perform.</li>"
                //Aleks, GH, TK: complete this list
                + "</ul></li>"
                + "</ol><br/>",
                Label.CONTENT_XHTML);
        editSettingsLabel.setVisible(false);
        editSettingsButton = new Button("How to edit system settings?",
                new ToggleLabelAction(editSettingsLabel));
        editSettingsButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(editSettingsButton);
        this.addComponent(editSettingsLabel);
    }
}
