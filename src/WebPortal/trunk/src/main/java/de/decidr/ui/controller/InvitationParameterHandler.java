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

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.view.InformationDialogComponent;
import de.decidr.ui.view.InvitationDialogComponent;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.RegisterUserComponent;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * This class handles URL parameters for invitation tasks,
 *
 * @author Geoffrey-Alexeij Heinze
 */
public class InvitationParameterHandler implements ParameterHandler {
    
	private HttpSession session = null;
	private UserFacade userFacade = null;
	
	private Long invitationId = null;
	private Long userId = null;
	private boolean registrationRequired = false;
	
    String key = null;
    String value = null;

    /* (non-Javadoc)
     * @see com.vaadin.terminal.ParameterHandler#handleParameters(java.util.Map)
     */
    @Override
    public void handleParameters(Map parameters) {
    	invitationId = null;
    	userId = null;
    	registrationRequired = false;
        for (Iterator it = parameters.keySet().iterator(); it.hasNext();) {
            key   = (String) it.next();
            value = ((String[]) parameters.get(key))[0];
            try{
                if(key.equals(DecidrGlobals.URL_PARAM_INVITATION_ID)){
                	invitationId = Long.parseLong(value);
                }else if(key.equals(DecidrGlobals.URL_PARAM_USER_ID)){
                	userId = Long.parseLong(value);
                }else if(key.equals("regreq")){
                	//GH DecidrGlobals.URL_PARAM_REGISTRATION_REQUIRED
                	registrationRequired = true;
                }
                
            }catch(NumberFormatException e){
    			Main.getCurrent().getMainWindow().addWindow(new InformationDialogComponent("An error occured while handling your invitation.<br/>Please try again and do not modify the provided link!","Invitation Error"));
    		}
        }
        
        if (invitationId != null){
        	session = Main.getCurrent().getSession();
        	userFacade = new UserFacade(new UserRole());
        	
        	try{
	        	String invDescription = "mooomoomoo";
	//        	Item invitationItem = userFacade.getInvitation(invitationId);
	//        	String invDescription = "Please confirm this invitation from " + 
	//        							invitationItem.getItemProperty("senderFirstName").getValue().toString() +
	//        							" " +
	//        							invitationItem.getItemProperty("senderLastName").getValue().toString();
	        	if (registrationRequired){
	        		if(userFacade.isRegistered(userId)){
	            		//User is registered
	            		
	            		session.setAttribute("userId", userId);
	                	Main.getCurrent().getMainWindow().addWindow(new InvitationDialogComponent(invDescription,invitationId));	
	            		
	            		
	            	}else{
	            		//User not yet registered
	    				UIDirector.getInstance().getTemplateView().setContent(new RegisterUserComponent(invitationId));
	            	}
	        	}else{
	        		session.setAttribute("userId", userId);
	        		Main.getCurrent().getMainWindow().addWindow(new InvitationDialogComponent(invDescription,invitationId));
	        	}
        	
        	}catch(TransactionException exception){
        	    Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent());
        	}
        
        }



    }

}
