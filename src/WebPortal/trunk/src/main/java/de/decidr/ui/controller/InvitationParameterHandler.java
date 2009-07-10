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

import com.vaadin.data.Item;
import com.vaadin.terminal.ParameterHandler;

import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.permissions.UserRole;
import de.decidr.ui.view.InformationDialogComponent;
import de.decidr.ui.view.InvitationDialogComponent;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.RegisterUserComponent;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * TODO: add comment
 *
 * @author Geoffrey-Alexeij Heinze
 */
public class InvitationParameterHandler implements ParameterHandler {
    
	private HttpSession session = null;
	private UserFacade userFacade = null;
	
	private Long invitationId = null;
	private Long userId = null;
	
    String key = null;
    String value = null;

    /* (non-Javadoc)
     * @see com.vaadin.terminal.ParameterHandler#handleParameters(java.util.Map)
     */
    @Override
    public void handleParameters(Map parameters) {
    	invitationId = null;
    	userId = null;
        for (Iterator it = parameters.keySet().iterator(); it.hasNext();) {
            key   = (String) it.next();
            value = ((String[]) parameters.get(key))[0];
            try{
                if(key.equals("i")){
                	invitationId = Long.parseLong(value);
                }else if(key.equals("u")){
                	userId = Long.parseLong(value);
                }	
            }catch(NumberFormatException e){
    			Main.getCurrent().getMainWindow().addWindow(new InformationDialogComponent("An error occured while handling your invitation.<br/>Please try again and do not modify the provided link!","Invitation Error"));
    		}
        }
        
        if (invitationId != null){
        	session = Main.getCurrent().getSession();
        	userFacade = new UserFacade(new UserRole(userId));
        	//GH: create useful text
        	try{
        	Item invitationItem = userFacade.getInvitation(invitationId);
        	String invDescription = "Ha, you've been invited!<br/>Click here to join the World Of DecidRaft!";
        	//GH: check if user is already registered
        	if(userFacade.isRegistered(userId)){
        		//User is registered
        		
        		session.setAttribute("userId", userId);
            	Main.getCurrent().getMainWindow().addWindow(new InvitationDialogComponent(invDescription,invitationId));	
        		
        		
        	}else{
        		//User not yet registered
				UIDirector.getInstance().getTemplateView().setContent(new RegisterUserComponent(invitationId));
        	}
        	}catch(TransactionException exception){
        	    Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent());
        	}
        
        }



    }

}
