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

package de.decidr.ui.controller.user;

import java.util.Date;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.ProfileSettingsComponent;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This action changes the status of a user to either available or unavailable.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2351", currentReviewState = State.Passed)
public class ChangeStatusAction implements ValueChangeListener {

    private static final long serialVersionUID = 1L;

    private HttpSession session = Main.getCurrent().getSession();

    private Long userId = null;
    private Role role = (Role) session.getAttribute("role");
    private UserFacade userFacade = new UserFacade(role);

    private ProfileSettingsComponent content = null;

    /**
     * TODO: add comment
     */
    public ChangeStatusAction(Long userId) {
        this.userId = userId;
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void valueChange(ValueChangeEvent event) {
        content = (ProfileSettingsComponent) Main.getCurrent().getUIDirector()
                .getTemplateView().getContent();

        if (content.getStatus().booleanValue()) {
            try {
                userFacade.setUnavailableSince(userId, new Date());
                Main.getCurrent().getMainWindow().addWindow(
                        new InformationDialogComponent(
                                "Status successfully changed to unavailable",
                                "Information"));
            } catch (TransactionException e) {
                Main.getCurrent().getMainWindow().addWindow(
                        new TransactionErrorDialogComponent(e));
            }
        } else {
            try {
                userFacade.setUnavailableSince(userId, null);
                Main.getCurrent().getMainWindow().addWindow(
                        new InformationDialogComponent(
                                "Status successfully changed to available",
                                "Information"));
            } catch (TransactionException e) {
                Main.getCurrent().getMainWindow().addWindow(
                        new TransactionErrorDialogComponent(e));
            }
        }
    }
}
