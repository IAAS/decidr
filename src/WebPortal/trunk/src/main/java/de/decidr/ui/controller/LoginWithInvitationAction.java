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

package de.decidr.ui.controller;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.exceptions.TransactionException;
import de.decidr.ui.view.InvitationDialogComponent;
import de.decidr.ui.view.LoginComponent;
import de.decidr.ui.view.Main;

/**
 * This action implements the login. It calls the authenticate method and 
 * logs the user into the application and shows
 *
 * @author Geoffrey-Alexeij Heinze
 */
public class LoginWithInvitationAction implements ClickListener {
    
    private Login login = new Login();
    UIDirector uiDirector = UIDirector.getInstance();
    private InvitationDialogComponent invitationDialog = null;
    
    /**
     * Constructor
     *
     * @param invD
     * 		InvitationDialogComponent that is shown after successfull login
     */
    public LoginWithInvitationAction (InvitationDialogComponent invD)
    {
    	invitationDialog = invD;
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        try{
            login.authenticate(((LoginComponent)uiDirector.getTemplateView().getContent()).getUsernameTextField().getValue().toString(), ((LoginComponent)uiDirector.getTemplateView().getContent()).getPasswordTextField().getValue().toString());
            Main.getCurrent().getMainWindow().addWindow(invitationDialog);
        }catch(TransactionException exception){
            Main.getCurrent().getMainWindow().showNotification("Login unsuccessful");
        }
        System.out.println(Main.getCurrent().getUser());
        

    }

}
