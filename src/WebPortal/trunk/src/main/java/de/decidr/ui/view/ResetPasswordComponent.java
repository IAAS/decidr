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

import java.util.Arrays;

import com.vaadin.data.Item;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.ui.FieldFactory;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;

import de.decidr.ui.controller.HideDialogWindowAction;
import de.decidr.ui.controller.ResetPasswordAction;

public class ResetPasswordComponent extends Window {
    
    
    private VerticalLayout verticalLayout = null;
    private HorizontalLayout horizontalLayout = null;
    
    private Form requestForm = null;
    
    private TextField email = null;
    
    private Button submitButton = null;
    private Button cancelButton = null;
    
    public Item getRequestForm(){
        return requestForm;
    }
    
    /**
     * Default constructor
     *
     */
    public ResetPasswordComponent(){
        init();
    }
    
    /**
     * This method initializes the components of the reset password component
     *
     */
    private void init(){
        requestForm = new Form();
        requestForm.setWriteThrough(false);
        
        
        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true);
        verticalLayout.setSizeUndefined();
        
        horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        horizontalLayout.setMargin(false);
        horizontalLayout.setSizeUndefined();
        
        
        email = new TextField();
        email.setCaption("User Name or E-Mail:");
        email.setColumns(21);
        
        submitButton = new Button("Reset Password", new ResetPasswordAction());
        cancelButton = new Button("Cancel", new HideDialogWindowAction());

        requestForm.setWidth(400, Form.UNITS_PIXELS);
        requestForm.addField("email", email);
        
        verticalLayout.addComponent(requestForm);
        horizontalLayout.addComponent(submitButton);
        horizontalLayout.addComponent(cancelButton);
        verticalLayout.addComponent(horizontalLayout);
        
        horizontalLayout.setComponentAlignment(cancelButton, "right bottom");
        horizontalLayout.setComponentAlignment(submitButton, "right bottom");

        verticalLayout.setComponentAlignment(horizontalLayout, "right bottom");
        
        this.setModal(true);
        this.setResizable(false);
        this.setCaption("Reset Password");
        this.setContent(verticalLayout);
    }
   
}
