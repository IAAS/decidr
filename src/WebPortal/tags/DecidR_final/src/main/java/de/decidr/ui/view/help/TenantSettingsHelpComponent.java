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
 * related to tenant settings.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2377", currentReviewState = State.PassedWithComments)
public class TenantSettingsHelpComponent extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    private Button changeSettingsButton = null;
    private Label changeSettingsLabel = null;

    private Button changeLogoButton = null;
    private Label changeLogoLabel = null;

    private Button restoreDefaultButton = null;
    private Label restoreDefaultLabel = null;

    public TenantSettingsHelpComponent() {
        setMargin(false, true, true, true);

        changeSettingsLabel = new Label(
                "<ol>"
                        + "<li>Log into DecidR and navigate to &quot;Tenant Settings&quot; in the navigation bar.</li>"
                        + "<li>Enter new description text and change the tenant settings.</li>"
                        + "<li>Click on the &quot;Save&quot; button.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        changeSettingsLabel.setVisible(false);
        changeSettingsButton = new Button(
                "How do I change the tenant settings?", new ToggleLabelAction(
                        changeSettingsLabel));
        changeSettingsButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(changeSettingsButton);
        this.addComponent(changeSettingsLabel);

        changeLogoLabel = new Label(
                "<ol>"
                        + "<li>Log into DecidR and navigate to &quot;Tenant Settings&quot; in the navigation bar.</li>"
                        + "<li>Select a file on your system to use as the new tenant logo.</li>"
                        + "<li>After the upload click on the &quot;Save&quot; button.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        changeLogoLabel.setVisible(false);
        changeLogoButton = new Button("How do I change the tenant logo?",
                new ToggleLabelAction(changeLogoLabel));
        changeLogoButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(changeLogoButton);
        this.addComponent(changeLogoLabel);

        restoreDefaultLabel = new Label(
                "<ol>"
                        + "<li>Log into DecidR and navigate to &quot;Tenant Settings&quot; in the navigation bar.</li>"
                        + "<li>The system displays the current tenant settings.</li>"
                        + "<li>Click on the &quot;Restore default settings&quot; button.</li>"
                        + "</ol><br/>", Label.CONTENT_XHTML);
        restoreDefaultLabel.setVisible(false);
        restoreDefaultButton = new Button(
                "How do I restore the default tenant settings?",
                new ToggleLabelAction(restoreDefaultLabel));
        restoreDefaultButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(restoreDefaultButton);
        this.addComponent(restoreDefaultLabel);
    }
}
