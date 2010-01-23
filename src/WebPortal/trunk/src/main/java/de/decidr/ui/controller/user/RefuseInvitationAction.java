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

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This action refuses an invitation.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2351", currentReviewState = State.PassedWithComments)
public class RefuseInvitationAction implements ClickListener {

    private static final long serialVersionUID = 1L;

    private Long userId = null;
    private UserFacade userFacade = null;

    private Long invitationId = null;

    /**
     * Requires the ID of an invitation
     * 
     * @param invId
     *            ID of the invitation
     * @param uId
     *            TODO document
     */
    public RefuseInvitationAction(Long invId, Long uId) {
        invitationId = invId;
        userId = uId;
        if (userId != null) {
            userFacade = new UserFacade(new UserRole(userId));
        } else {
            userFacade = new UserFacade(new UserRole());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {

        try {
            userFacade.refuseInviation(invitationId);
            Main.getCurrent().getMainWindow().removeWindow(
                    event.getButton().getWindow());
        } catch (TransactionException e) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(e));
        }
    }
}