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
 * and contains information related to workflow model editor
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class WorkflowModelEditorHelpComponent extends VerticalLayout {

    /**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;
	private Button openModelButton = null;
    private Label openModelLabel = null;

    private Button clearModelButton = null;
    private Label clearModelLabel = null;

    private Button addActivityButton = null;
    private Label addActivityLabel = null;

    private Button addContainerButton = null;
    private Label addContainerLabel = null;

    private Button connectionButton = null;
    private Label connectionLabel = null;

    public WorkflowModelEditorHelpComponent(){
        setMargin(false, true, true, true);
        
        openModelLabel = new Label("1) Login into DecidR as tenant admin and navigate to the workflow modeling section by clicking on the 'create/edit workflow models' navigation link.<br/>"
                +"2) Click on the 'Edit' link next to the workflow model you want to edit.<br/><br/>",
                Label.CONTENT_XHTML);
        openModelLabel.setVisible(false);
        openModelButton = new Button("How to open existing model for editing?", new ToggleLabelAction(openModelLabel));
        openModelButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(openModelButton);
        this.addComponent(openModelLabel);

        clearModelLabel = new Label("1) Login into DecidR as tenant admin and navigate to the workflow modeling section by clicking on the 'create/edit workflow models' navigation link.<br/>"
                +"2) Click on the 'Edit' link next to workflow model you want to clear.<br/>"
                +"3) The system shows the workflow in modeling tool editor.<br/>"
                +"4) Press the clear button and confirm it.<br/><br/>",
                Label.CONTENT_XHTML);
        clearModelLabel.setVisible(false);
        clearModelButton = new Button("How to remove all activities, containers, connections and variables from the workflow model?", new ToggleLabelAction(clearModelLabel));
        clearModelButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(clearModelButton);
        this.addComponent(clearModelLabel);

        addActivityLabel = new Label("1) You are a tenant admin.  You  have a workflow model and want to insert one of the available activities.<br/>"
                +"2) Drag an activity icon from the palette on the canvas.<br/>"
                +"3) The editor adds the new activity to the workflow model and opens its properties for editing.<br/><br/>",
                Label.CONTENT_XHTML);
        addActivityLabel.setVisible(false);
        addActivityButton = new Button("How to insert a new activity?", new ToggleLabelAction(addActivityLabel));
        addActivityButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(addActivityButton);
        this.addComponent(addActivityLabel);

        addContainerLabel = new Label("1) You are a tenant admin.  You  have a workflow model and want to insert one of the available activities.<br/>"
                +"2) Drag an activity icon from the palette on the canvas.<br/>"
                +"3) The editor adds the new activity to the workflow model and opens its properties for editing.<br/><br/>",
                Label.CONTENT_XHTML);
        addContainerLabel.setVisible(false);
        addContainerButton = new Button("How to insert a new container?", new ToggleLabelAction(addContainerLabel));
        addContainerButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(addContainerButton);
        this.addComponent(addContainerLabel);

        connectionLabel = new Label("1) You are a tenant admin and you opened an existing workflow for editing.<br/>"
                +"2) Click on the first port and the second port.<br/>"
                +"3) The editor creates a connection between the selected ports.<br/><br/>",
                Label.CONTENT_XHTML);
        connectionLabel.setVisible(false);
        connectionButton = new Button("How to connect activites and containers?", new ToggleLabelAction(connectionLabel));
        connectionButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(connectionButton);
        this.addComponent(connectionLabel);
        
    }
    
}
