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
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This parameter handler handles the URL for confirming user actions. If a
 * users has to confirm his registration he gets a special link. This link
 * conforms to a special syntax. This parameter handler is used to extract the
 * information from the URL.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2346", currentReviewState = State.Rejected)
public class ConfirmationParameterHandler implements ParameterHandler {

    private static final long serialVersionUID = 1L;

    private UserFacade userFacade = null;

    private String confirmationId = null;
    private String userId = null;
    private String action = null;

    String key = null;
    String value = null;

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.terminal.ParameterHandler#handleParameters(java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void handleParameters(Map parameters) {
        confirmationId = null;
        userId = null;
        // Aleks, GH: you should display some error message if a parameter is
        // specified twice or two conflicting parameters are passed ~rr
        for (Iterator<String> it = parameters.keySet().iterator(); it.hasNext();) {
            key = it.next();
            value = ((String[]) parameters.get(key))[0];
            if (key.equals(DecidrGlobals.URL_PARAM_USER_ID)) {
                userId = value;
            } else if (key
                    .equals(DecidrGlobals.URL_PARAM_CHANGE_EMAIL_REQUEST_ID)) {
                confirmationId = value;
                action = "email";
            } else if (key
                    .equals(DecidrGlobals.URL_PARAM_CONFIRM_REGISTRATION_ID)) {
                confirmationId = value;
                action = "reg";
            } else if (key
                    .equals(DecidrGlobals.URL_PARAM_PASSWORD_RESET_REQUEST_ID)) {
                confirmationId = value;
                action = "pass";
            } else {

            }
        }

        if (confirmationId != null) {
            userFacade = new UserFacade(new UserRole(Long.parseLong(userId)));

            if (action.equals("email")) {
                try {
                    userFacade.confirmChangeEmailRequest(
                            Long.parseLong(userId), confirmationId);
                    Main
                            .getCurrent()
                            .getMainWindow()
                            .addWindow(
                                    new InformationDialogComponent(
                                            "Your email address has been successfully changed!",
                                            "Email Changed!"));
                } catch (NumberFormatException e) {
                    Main.getCurrent().getMainWindow().addWindow(
                            new TransactionErrorDialogComponent(e));
                } catch (TransactionException e) {
                    Main.getCurrent().getMainWindow().addWindow(
                            new TransactionErrorDialogComponent(e));
                }
            } else if (action.equals("pass")) {
                try {
                    userFacade.confirmPasswordReset(Long.parseLong(userId),
                            confirmationId);
                    Main
                            .getCurrent()
                            .getMainWindow()
                            .addWindow(
                                    new InformationDialogComponent(
                                            "A new password has been created and sent "
                                                    + "to your email address.<br>You should change "
                                                    + "it immediately to avoid a third party from "
                                                    + "using an intercepted password!",
                                            "Password Reset Confirmed!"));
                } catch (NumberFormatException e) {
                    Main.getCurrent().getMainWindow().addWindow(
                            new TransactionErrorDialogComponent(e));
                } catch (TransactionException e) {
                    Main.getCurrent().getMainWindow().addWindow(
                            new TransactionErrorDialogComponent(e));
                }
            } else if (action.equals("reg")) {
                try {
                    userFacade.confirmRegistration(Long.parseLong(userId),
                            confirmationId);
                    Main
                            .getCurrent()
                            .getMainWindow()
                            .addWindow(
                                    new InformationDialogComponent(
                                            "You successfully completed the registration!<br>"
                                                    + "You can now log into your account.",
                                            "Registration Complete!"));
                } catch (NumberFormatException e) {
                    Main.getCurrent().getMainWindow().addWindow(
                            new TransactionErrorDialogComponent(e));
                } catch (TransactionException e) {
                    Main.getCurrent().getMainWindow().addWindow(
                            new TransactionErrorDialogComponent(e));
                }
            }
        } else {
            // GH, Aleks: show some error message ~rr
        }
    }
}
