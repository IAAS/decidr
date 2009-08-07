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

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.vaadin.terminal.ParameterHandler;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.view.InformationDialogComponent;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * TODO: add comment
 *
 * @author Geoffrey-Alexeij Heinze
 */
public class ConfirmationParameterHandler implements ParameterHandler {
    
	private HttpSession session = null;
	
	private UserFacade userFacade = null;
	
	private String confirmationId = null;
	private String userId = null;
	private String action = null;
	
    String key = null;
    String value = null;

    /* (non-Javadoc)
     * @see com.vaadin.terminal.ParameterHandler#handleParameters(java.util.Map)
     */
    @Override
    public void handleParameters(Map parameters) {
    	confirmationId = null;
    	userId = null;
        for (Iterator it = parameters.keySet().iterator(); it.hasNext();) {
            key   = (String) it.next();
            value = ((String[]) parameters.get(key))[0];
            if(key.equals("c")){
            	confirmationId = value;
            }else if(key.equals("u")){
            	userId = value;
            }else if(key.equals("a")){
            	action = value;
            }
        }
        
        if (confirmationId != null){
        	session = Main.getCurrent().getSession();
        	userFacade = new UserFacade(new UserRole(Long.parseLong(userId)));
        	//GH: create useful text depending on confirmation context
        	String confDescription = "Moo!";
        	
        	if(action.equals("email")){
        		try {
					userFacade.confirmChangeEmailRequest(Long.parseLong(userId), confirmationId);
					Main.getCurrent().getMainWindow().addWindow(new InformationDialogComponent("Moo! email", "Email Changed!"));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TransactionException e) {
					Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent());
				}
        	}else if(action.equals("pass")){
        		try {
					userFacade.confirmPasswordReset(Long.parseLong(userId), confirmationId);
					Main.getCurrent().getMainWindow().addWindow(new InformationDialogComponent("Moo! password", "Password Reset Confirmed!"));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TransactionException e) {
					Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent());
				}
        	}else if(action.equals("reg")){
        		try {
					userFacade.confirmRegistration(Long.parseLong(userId), confirmationId);
					Main.getCurrent().getMainWindow().addWindow(new InformationDialogComponent("Moo! registration", "Registration Complete!"));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TransactionException e) {
					Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent());
				}
        	}
        }



    }

}
