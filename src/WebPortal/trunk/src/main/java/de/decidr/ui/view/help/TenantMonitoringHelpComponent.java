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
 * This component is a part of the integrated manual
 * and contains information related to tenant monitoring
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class TenantMonitoringHelpComponent extends VerticalLayout{

    private Button tenantStatusButton = null;
    private Label tenantStatusLabel = null;
    
    public TenantMonitoringHelpComponent(){
        setMargin(false, true, true, true);

        tenantStatusLabel = new Label("1) Login into DecidR as tenant admin and navigate to the workflow modeling section by clicking on the 'Tenant Status' navigation link.<br/>"
                +"2) The system displays the tenant status page containing:<br/>"
                +"    - Number of registered and unregistered users<br/>"
                +"    - Total number of workflow models<br/>"
                +"    - Total number of completed workflow instances<br/>"
                +"    - Total number of aborted / terminated workflow instances<br/><br/>",
                Label.CONTENT_XHTML);
        tenantStatusLabel.setVisible(false);
        tenantStatusButton = new Button("How to view tenant status?", new ToggleLabelAction(tenantStatusLabel));
        tenantStatusButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(tenantStatusButton);
        this.addComponent(tenantStatusLabel);

    }
    
}
