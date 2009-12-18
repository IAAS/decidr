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
 * related to tenant monitoring.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2377", currentReviewState = State.PassedWithComments)
public class TenantMonitoringHelpComponent extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    private Button tenantStatusButton = null;
    private Label tenantStatusLabel = null;

    public TenantMonitoringHelpComponent() {
        setMargin(false, true, true, true);

        tenantStatusLabel = new Label("<ol>"
                + "<li>Log into DecidR as tenant admin and navigate to the workflow modeling section by clicking on the"
                + " &quot;Tenant Status&quot; navigation link.</li>"
                + "<li>The system displays the tenant status page containing:"
                + "<ul>"
                + "<li>The number of registered and unregistered users.</li>"
                + "<li>The total number of workflow models.</li>"
                + "<li>The total number of completed workflow instances.</li>"
                + "<li>The total number of aborted/terminated workflow instances.</li>"
                + "</ul></li>"
                + "</ol><br/>",
                Label.CONTENT_XHTML);
        tenantStatusLabel.setVisible(false);
        tenantStatusButton = new Button("How do I view the tenant status?",
                new ToggleLabelAction(tenantStatusLabel));
        tenantStatusButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(tenantStatusButton);
        this.addComponent(tenantStatusLabel);
    }
}
