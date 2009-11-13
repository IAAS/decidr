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

import javax.servlet.http.HttpSession;

import com.vaadin.data.Item;
import com.vaadin.terminal.ParameterHandler;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.controller.UIDirector;
import de.decidr.ui.view.LoginComponent;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.RegisterUserComponent;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.InvitationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

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
    private String authKey = null;
    private boolean registrationRequired = false;

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
        invitationId = null;
        userId = null;
        authKey = "";
        registrationRequired = false;
        for (Iterator<String> it = parameters.keySet().iterator(); it.hasNext();) {
            key = it.next();
            value = ((String[]) parameters.get(key))[0];
            try {
                if (key.equals(DecidrGlobals.URL_PARAM_INVITATION_ID)) {
                    invitationId = Long.parseLong(value);
                } else if (key.equals(DecidrGlobals.URL_PARAM_USER_ID)) {
                    userId = Long.parseLong(value);
                } else if (key
                        .equals(DecidrGlobals.URL_PARAM_AUTHENTICATION_KEY)) {
                    authKey = value;
                } else if (key
                        .equals(DecidrGlobals.URL_PARAM_REGISTRATION_REQUIRED)) {
                    registrationRequired = true;
                }

            } catch (NumberFormatException e) {
                Main
                        .getCurrent()
                        .getMainWindow()
                        .addWindow(
                                new InformationDialogComponent(
                                        "An error occured while handling your invitation.<br/>Please try again and do not modify the provided link!",
                                        "Invitation Error"));
            }
        }

        if (invitationId != null) {
            session = Main.getCurrent().getSession();
            userFacade = new UserFacade(new UserRole());

            try {
                Item invitationItem = userFacade.getInvitation(invitationId);
                String concern = null;
                if (!isNullOrEmpty(invitationItem.getItemProperty(
                        "joinTenantName").getValue().toString())) {
                    concern = "Join this tenant: "
                            + invitationItem.getItemProperty("joinTenantName")
                                    .getValue().toString();

                }

                if (!isNullOrEmpty(invitationItem.getItemProperty(
                        "administratedWorkflowModelName").getValue().toString())) {
                    if (concern == null) {
                        concern = "Admininstrate a workflow: "
                                + invitationItem.getItemProperty(
                                        "administratedWorkflowModelName")
                                        .getValue().toString();
                    } else {
                        concern = ", admininstrate a workflow: "
                                + invitationItem.getItemProperty(
                                        "administratedWorkflowModelName")
                                        .getValue().toString();
                    }
                }

                if (!isNullOrEmpty(invitationItem.getItemProperty(
                        "workflowInstanceId").getValue().toString())) {
                    if (concern == null) {
                        concern = "Participate in a workflow";
                    } else {
                        concern = ", participate in a workflow";
                    }
                }

                if (concern == null) {
                    concern = "No reason specified.";
                }

                String invDescription = "You received an invitation.<br/>"
                        + "Sender: "
                        + invitationItem.getItemProperty("senderFirstName")
                                .getValue().toString()
                        + " "
                        + invitationItem.getItemProperty("senderLastName")
                                .getValue().toString() + "<br/><br/>"
                        + "You have been invited to: " + concern + "<br/><br/>"
                        + "Confirm this invitation?";

                if (registrationRequired) {
                    if (userFacade.isRegistered(userId)) {
                        // User is registered
                        // check if user is logged in
                        if ((Long) session.getAttribute("userId") == userId) {
                            // logged in
                            Main.getCurrent().getMainWindow().addWindow(
                                    new InvitationDialogComponent(
                                            invDescription, invitationId,
                                            userId));
                        } else {
                            // not logged in
                            UIDirector
                                    .getInstance()
                                    .getTemplateView()
                                    .setVerticalNavigation(
                                            new LoginComponent(
                                                    new InvitationDialogComponent(
                                                            invDescription,
                                                            invitationId,
                                                            userId)));
                            Main
                                    .getCurrent()
                                    .getMainWindow()
                                    .addWindow(
                                            new InformationDialogComponent(
                                                    "Please login to accept this invitation",
                                                    "Login required"));
                        }

                    } else {
                        // User not yet registered
                        UIDirector.getInstance().getTemplateView().setContent(
                                new RegisterUserComponent(invitationId));
                    }
                } else {
                    if (authKey.equals("")) {
                        // check if user is logged in
                        if ((Long) session.getAttribute("userId") == userId) {
                            // logged in
                            Main.getCurrent().getMainWindow().addWindow(
                                    new InvitationDialogComponent(
                                            invDescription, invitationId,
                                            userId));
                        } else {
                            // not logged in
                            // check if user is even registered
                            if (userFacade.isRegistered(userId)) {
                                UIDirector
                                        .getInstance()
                                        .getTemplateView()
                                        .setVerticalNavigation(
                                                new LoginComponent(
                                                        new InvitationDialogComponent(
                                                                invDescription,
                                                                invitationId,
                                                                userId)));
                                Main
                                        .getCurrent()
                                        .getMainWindow()
                                        .addWindow(
                                                new InformationDialogComponent(
                                                        "Please login to accept this invitation",
                                                        "Login required"));

                            } else {
                                // not registered
                                Main
                                        .getCurrent()
                                        .getMainWindow()
                                        .addWindow(
                                                new InformationDialogComponent(
                                                        "Authentication failed: Unknown User.",
                                                        "Authentication failed"));
                            }

                        }
                    } else {
                        if (userFacade.authKeyMatches(userId, authKey)) {
                            Main.getCurrent().getMainWindow().addWindow(
                                    new InvitationDialogComponent(
                                            invDescription, invitationId,
                                            userId));
                        } else {
                            Main
                                    .getCurrent()
                                    .getMainWindow()
                                    .addWindow(
                                            new InformationDialogComponent(
                                                    "Your authentication key does not match!",
                                                    "Authentication failed!"));
                        }
                    }
                }

            } catch (TransactionException exception) {
                Main.getCurrent().getMainWindow().addWindow(
                        new TransactionErrorDialogComponent(exception));
            }

        }

    }

    /**
     * Returns true if the given String is null or empty
     * 
     * @param t
     *            The string to be checked
     * @return True if the given string is null or empty, False if not.
     */
    private boolean isNullOrEmpty(String t) {
        if ((t == null) || t.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

}
