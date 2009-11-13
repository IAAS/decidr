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

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Upload;

import de.decidr.ui.controller.UploadFileStartConfigurationAction;
import de.decidr.ui.view.windows.StartConfigurationWindow;

/**
 * This component represents the filename and a button to delete the uploaded
 * file.
 * 
 * @author AT
 */
public class DeleteUploadComponent extends CustomComponent {

    private HorizontalLayout horizontlLayout = null;

    private Label label = null;

    private Button button = null;

    private StartConfigurationWindow sCWindow = null;

    private DeleteUploadComponent deleteUploadComponent = null;

    /**
     * Default constructor
     * 
     */
    public DeleteUploadComponent() {
        init();
    }

    /**
     * Returns the label
     * 
     * @return label
     */
    public Label getLabel() {
        return label;
    }

    /**
     * Initializes the components of the delete upload component
     * 
     */
    private void init() {
        horizontlLayout = new HorizontalLayout();
        label = new Label();
        button = new Button("Delete");
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                deleteUploadComponent = (DeleteUploadComponent) event
                        .getSource();
                sCWindow = (StartConfigurationWindow) deleteUploadComponent
                        .getWindow();
                deleteUploadComponent.setVisible(false);
                sCWindow.getAssignmentForm().getLayout().addComponent(
                        new Upload("Upload", new UploadFileStartConfigurationAction()));
            }

        });

        this.setCompositionRoot(horizontlLayout);

        horizontlLayout.setSpacing(true);

        horizontlLayout.addComponent(label);
        horizontlLayout.addComponent(button);

    }

}
