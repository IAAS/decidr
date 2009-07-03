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

/**
 * TODO: add comment
 *
 * @author Geoffrey-Alexeij Heinze
 */

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.decidr.ui.controller.ConfirmInvitationAction;
import de.decidr.ui.controller.RefuseInvitationAction;

public class InvitationDialogComponent extends Window {

    private static InvitationDialogComponent invitationDialogComponent = null;
    
    private Long invitationId = null;
    
    private VerticalLayout verticalLayout = null;
    private HorizontalLayout horizontalLayout = null;
    
    private Label infoLabel = null;
        
    private Button submitButton = null;
    private Button cancelButton = null;
        
    public InvitationDialogComponent(String description, Long invId){
        invitationId = invId;
        init(description);
    }
    
    private void init(String description){
        
        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true);
        verticalLayout.setSizeUndefined();
        
        horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        horizontalLayout.setMargin(false);
        horizontalLayout.setSizeUndefined();
        
        infoLabel = new Label(description, Label.CONTENT_XHTML);
        infoLabel.setWidth(350,Label.UNITS_PIXELS);
        

        
        submitButton = new Button("Accept Invitation", new ConfirmInvitationAction(invitationId));
        cancelButton = new Button("Refuse Invitation", new RefuseInvitationAction(invitationId));


        verticalLayout.addComponent(infoLabel);
        horizontalLayout.addComponent(submitButton);
        horizontalLayout.addComponent(cancelButton);
        verticalLayout.addComponent(horizontalLayout);
        
        horizontalLayout.setComponentAlignment(cancelButton, "right bottom");
        horizontalLayout.setComponentAlignment(submitButton, "right bottom");

        verticalLayout.setComponentAlignment(horizontalLayout, "right bottom");
        
        this.setModal(true);
        this.setName("InvitationDialog");
        this.setResizable(false);
        this.setCaption("Invitation");
        this.setContent(verticalLayout);
    }
    
}
