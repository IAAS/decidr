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

import de.decidr.ui.controller.ChangePasswordAction;
import de.decidr.ui.controller.HideChangePasswordAction;

public class ChangePasswordComponent extends Window {

    private static ChangePasswordComponent changePasswordComponent = null;
    
    
    private VerticalLayout verticalLayout = null;
    private HorizontalLayout horizontalLayout = null;
    
    private Form passwordForm = null;
    
    private TextField newPasswordText = null;
    private TextField newPasswordConfirmText = null;
    private TextField oldPasswordText = null;
    
    private Button submitButton = null;
    private Button cancelButton = null;
    
    public Item getPasswords(){
        return passwordForm;
    }
    
    private ChangePasswordComponent(){
        init();
    }
    
    private void init(){
        passwordForm = new Form();
        passwordForm.setWriteThrough(false);
        
        
        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true);
        verticalLayout.setSizeUndefined();
        
        horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        horizontalLayout.setMargin(false);
        horizontalLayout.setSizeUndefined();
        
        
        newPasswordText = new TextField();
        newPasswordText.setCaption("New Password");
        newPasswordText.setSecret(true);
        newPasswordText.setColumns(21);

        newPasswordConfirmText = new TextField();
        newPasswordConfirmText.setCaption("Confirm New Password");
        newPasswordConfirmText.setSecret(true);
        newPasswordConfirmText.setColumns(21);

        oldPasswordText = new TextField();
        oldPasswordText.setCaption("Old Password");
        oldPasswordText.setSecret(true);
        oldPasswordText.setColumns(21);
        
        submitButton = new Button("Change Password", new ChangePasswordAction());
        cancelButton = new Button("Cancel", new HideChangePasswordAction());

        passwordForm.setWidth(400, Form.UNITS_PIXELS);
        passwordForm.addField("newPassword", newPasswordText);
        passwordForm.addField("newPasswordConfirm", newPasswordConfirmText);
        passwordForm.addField("oldPassword", oldPasswordText);
        
        verticalLayout.addComponent(passwordForm);
        horizontalLayout.addComponent(cancelButton);
        horizontalLayout.addComponent(submitButton);
        verticalLayout.addComponent(horizontalLayout);
        
        horizontalLayout.setComponentAlignment(cancelButton, "right bottom");
        horizontalLayout.setComponentAlignment(submitButton, "right bottom");

        verticalLayout.setComponentAlignment(horizontalLayout, "right bottom");
        
        this.setModal(true);
        this.setResizable(false);
        this.setCaption("Change Password");
        this.setContent(verticalLayout);
    }
    
    public static ChangePasswordComponent getInstance(){
        if (changePasswordComponent == null){
            changePasswordComponent = new ChangePasswordComponent();
        }
        return changePasswordComponent;
    }
}