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

package de.decidr.ui.controller.parameterhandler;

import java.util.Iterator;
import java.util.Map;

import com.vaadin.terminal.ParameterHandler;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.controller.authentication.Login;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This parameter handler allows automated logins with a given link.
 *
 * @author Geoffrey-Alexeij Heinze
 */
public class LoginParameterHandler implements ParameterHandler{

    private UserFacade userFacade = null;
    
    /* (non-Javadoc)
     * @see com.vaadin.terminal.ParameterHandler#handleParameters(java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void handleParameters(Map parameters) {
        Long userId = null;
        String authKey = null;
        String key = null;
        String value = null;
        
        for (Iterator<String> it = parameters.keySet().iterator(); it.hasNext();) {
                key = it.next();
                value = ((String[]) parameters.get(key))[0];
                try {
                        if (key.equals(DecidrGlobals.URL_PARAM_LOGIN_ID)) {
                                userId = Long.getLong(value);
                        }else if (key.equals(DecidrGlobals.URL_PARAM_AUTHENTICATION_KEY)) {
                                authKey = value;
                        }

                } catch (NumberFormatException e) {
                        Main
                                        .getCurrent()
                                        .getMainWindow()
                                        .addWindow(
                                                        new InformationDialogComponent(
                                                                        "An error occured while handling your login.<br/>Please try again and do not modify the provided link!",
                                                                        "Invitation Error"));
                }
        }
        
        if (userId != null && authKey != null){
            // try to login
            userFacade = new UserFacade(new UserRole(userId)); 
            try {
                if (userFacade.authKeyMatches(userId, authKey)){
                    //auth key is valid
                    Login loginAssistant = new Login();
                    loginAssistant.loginById(userId, authKey);
                }else{
                    Main
                    .getCurrent()
                    .getMainWindow()
                    .addWindow(
                                    new InformationDialogComponent(
                                                    "An error occured while handling your login.<br/>Please try again and do not modify the provided link!",
                                                    "Invitation Error"));
                }
            } catch (EntityNotFoundException e) {
                Main
                .getCurrent()
                .getMainWindow()
                .addWindow(
                                new InformationDialogComponent(
                                                "An error occured while handling your login.<br/>Please try again and do not modify the provided link!",
                                                "Invitation Error"));
            } catch (TransactionException e) {
                Main
                .getCurrent()
                .getMainWindow()
                .addWindow(
                                new TransactionErrorDialogComponent(e));
            }
        }

        
    }

}
