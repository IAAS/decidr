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
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.view.LoginComponent;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.RegisterUserComponent;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.InvitationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This class handles URL parameters for invitation tasks.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2404", currentReviewState = State.PassedWithComments)
public class InvitationParameterHandler implements ParameterHandler {

    private static final String ERROR_AUTHENTICATING = "An error occured handling your invitation.<br/>"
            + "Please try again and do not modify the provided link!";

    private static final long serialVersionUID = 1L;
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
                    if (invitationId != null) {
                        Main.getCurrent().getMainWindow().addWindow(
                                new InformationDialogComponent(
                                        "Your invitation link contained more "+
                                        "parameters than expected and might be "+
                                        "invalid.", "Error"));
                    }
                    invitationId = Long.parseLong(value);
                } else if (key.equals(DecidrGlobals.URL_PARAM_USER_ID)) {
                    if (userId != null) {
                        Main.getCurrent().getMainWindow().addWindow(
                                new InformationDialogComponent(
                                        "Your invitation link contained more "+
                                        "parameters than expected and might be "+
                                        "invalid.", "Error"));
                    }
                    userId = Long.parseLong(value);
                } else if (key
                        .equals(DecidrGlobals.URL_PARAM_AUTHENTICATION_KEY)) {
                    if (! authKey.equals("")) {
                        Main.getCurrent().getMainWindow().addWindow(
                                new InformationDialogComponent(
                                        "Your invitation link contained more "+
                                        "parameters than expected and might be "+
                                        "invalid.", "Error"));
                    }
                    authKey = value;
                } else if (key
                        .equals(DecidrGlobals.URL_PARAM_REGISTRATION_REQUIRED)) {
                    registrationRequired = true;
                } else {
                    //Other parameters are handled by other handlers
                }
            } catch (NumberFormatException e) {
                Main.getCurrent().getMainWindow().addWindow(
                        new InformationDialogComponent(ERROR_AUTHENTICATING,
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
                        concern = " and admininstrate a workflow: "
                                + invitationItem.getItemProperty(
                                        "administratedWorkflowModelName")
                                        .getValue().toString();
                    }
                }

                if (!isNullOrEmpty(invitationItem.getItemProperty(
                        "workflowInstanceId").getValue().toString())) {
                    if (concern == null) {
                        // aleks, gh: which one ~rr
                        // rr This information is not available from the
                        // invitation
                        // item we get from the facade. Tell me where it can be
                        // retrieved
                        // and i'll add it. ~gh
                        // GH, Aleks: you have a workflow instance ID - there
                        // has to be some method in a facade (ask DH) ~rr
                        concern = "Participate in a workflow";
                    } else {
                        // Aleks, GH: which one ~rr
                        concern = " and participate in a workflow";
                    }
                }

                if (concern == null) {
                    concern = "No reason specified.";
                }

                String invDescription = "You have received an invitation.<br/>"
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
                            Main
                                    .getCurrent()
                                    .getUIDirector()
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
                                                    "Please log in to accept this invitation",
                                                    "Login required"));
                        }

                    } else {
                        // User not yet registered
                        Main
                                .getCurrent()
                                .getUIDirector()
                                .getTemplateView()
                                .setContent(
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
                                Main
                                        .getCurrent()
                                        .getUIDirector()
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
                                                        "Please log in to accept this invitation",
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
                                                    "Your authentication key can not be verified!",
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
     * Returns <code>true</code> if the given String is <code>null</code> or
     * empty.
     * 
     * @param t
     *            The string to be checked
     * @return <code>true</code> if the passed string is <code>null</code> or
     *         empty, <code>false</code> if not.
     */
    private boolean isNullOrEmpty(String t) {
        if ((t == null) || t.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
