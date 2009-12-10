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

/**
 * This component is a part of the integrated manual and contains information
 * related to system settings.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class SystemSettingsHelpComponent extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    private Button editSettingsButton = null;
    private Label editSettingsLabel = null;

    public SystemSettingsHelpComponent() {
        setMargin(false, true, true, true);

        editSettingsLabel = new Label(
                "1) Log into DecidR as tenant admin and navigate to the workflow modeling section by clicking on the 'System Settings' navigation link.<br/>"
                        + "2) The system displays the following information:<br/>"
                        + "    - Whether the super admin must confirm all new tenants or they are accepted automatically<br/>"
                        + "    - Amount of logic to perform<br/><br/>",
                Label.CONTENT_XHTML);
        editSettingsLabel.setVisible(false);
        editSettingsButton = new Button("How to edit system settings?",
                new ToggleLabelAction(editSettingsLabel));
        editSettingsButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(editSettingsButton);
        this.addComponent(editSettingsLabel);

    }

}
