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
 * @author GH
 */

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.ui.controller.ChangeEmailAction;
import de.decidr.ui.controller.HideDialogWindowAction;

public class InformationDialogComponent extends Window {

    private static InformationDialogComponent informationDialogComponent = null;
    

    private VerticalLayout verticalLayout = null;
    //private HorizontalLayout horizontalLayout = null;
    
    private Label infoLabel = null;
       
    private Button cancelButton = null;
    
    private InformationDialogComponent(String text, String caption){
        init(text, caption);
    }
    
    private void init (String text, String caption){
        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true);
        verticalLayout.setSizeUndefined();
        verticalLayout.setWidth(400, VerticalLayout.UNITS_PIXELS);
        
        //horizontalLayout = new HorizontalLayout();
        //horizontalLayout.setSpacing(true);
        //horizontalLayout.setMargin(false);
        //horizontalLayout.setSizeUndefined();
        
        infoLabel = new Label(text, Label.CONTENT_XHTML);
        
        cancelButton = new Button("OK",new HideDialogWindowAction());


        verticalLayout.addComponent(infoLabel);
        //horizontalLayout.addComponent(submitButton);
        verticalLayout.addComponent(cancelButton);
        //verticalLayout.addComponent(horizontalLayout);

        //horizontalLayout.setComponentAlignment(submitButton, "center bottom");
        //horizontalLayout.setComponentAlignment(cancelButton, "center bottom");

        //verticalLayout.setComponentAlignment(horizontalLayout, "right bottom");
        
        this.setModal(true);
        this.setResizable(false);
        this.setCaption(caption);
        this.setContent(verticalLayout);
    }
    
    public static InformationDialogComponent getInstance(String text, String caption){
        //if(confirmDialogComponent == null){
        informationDialogComponent = new InformationDialogComponent(text, caption);
        //}
        return informationDialogComponent;
    }
    
    public static InformationDialogComponent getInstance(){
        if(informationDialogComponent == null){
            //TODO: add other exception
            throw new UnsupportedOperationException();
        }
        return informationDialogComponent;
    }
}