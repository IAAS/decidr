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

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.ui.controller.HideHelpDialogAction;
import de.decidr.ui.controller.show.ShowHelpAction;

/**
 * The integrated help window.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2356", currentReviewState = State.PassedWithComments)
public class HelpDialogComponent extends Window {

    private static final long serialVersionUID = 1L;
    private Accordion acc = null;
    private Button closeButton = null;
    private Button showMainScreenHelp = null;
    private HorizontalLayout horLayout = null;

    public HelpDialogComponent() {
        super("DecidR Help");
        // Aleks, GH: why would you want the help to always force the decidr
        // theme? Shouldn't it have the current tenant's, just like everything
        // else? ~rr
        // RR Does your help file changes its' style if you change your desktop wall paper?
        // This way we can assure the help is always readable, no matter how the rest
        // of the web site looks
        setTheme("decidr");

        VerticalLayout layout = (VerticalLayout) getContent();
        layout.setSpacing(true);

        horLayout = new HorizontalLayout();
        horLayout.setSpacing(false);

        layout.addComponent(horLayout);
        horLayout.setSizeFull();

        closeButton = new Button("Close", new HideHelpDialogAction());
        showMainScreenHelp = new Button("Show in main window",
                new HideHelpDialogAction());
        showMainScreenHelp.addListener(new ShowHelpAction());

        acc = new Accordion();
        acc.setSizeFull();

        acc.addTab(new AccountManagementHelpComponent(), "Account Management",
                null);
        acc.addTab(new WorkflowParticipationHelpComponent(),
                "Workflow Participation", null);
        acc.addTab(new WorkflowInstanceManagementHelpComponent(),
                "Workflow Instance Management", null);
        acc.addTab(new TenantSettingsHelpComponent(), "Tenant Settings", null);
        acc.addTab(new TenantUserManagementHelpComponent(),
                "Tenant User Management", null);
        acc.addTab(new WorkflowModelingHelpComponent(), "Workflow Modeling",
                null);
        acc.addTab(new WorkflowModelEditorHelpComponent(),
                "Workflow Model Editor", null);
        acc.addTab(new TenantMonitoringHelpComponent(), "Tenant Monitoring",
                null);
        acc.addTab(new SystemSettingsHelpComponent(), "System Settings", null);
        acc.addTab(new SystemStatusHelpComponent(), "System Status", null);
        acc.addTab(new UserManagementHelpComponent(), "User Management", null);
        acc.addTab(new TenantManagementHelpComponent(), "Tenant Management",
                null);
        acc.addTab(new GlossaryHelpComponent(), "Glossary", null);

        horLayout.addComponent(showMainScreenHelp);
        horLayout.setComponentAlignment(showMainScreenHelp, Alignment.TOP_LEFT);
        horLayout.addComponent(closeButton);
        horLayout.setComponentAlignment(closeButton, Alignment.TOP_RIGHT);
        layout.addComponent(acc);
    }

    public void show() {
        setWidth(400, Sizeable.UNITS_PIXELS);
        setHeight(600, Sizeable.UNITS_PIXELS);
        center();
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
    }
}
