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

package de.decidr.ui.controller.show;

import javax.servlet.http.HttpSession;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.ui.controller.tenant.LeaveTenantAction;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.ConfirmDialogWindow;

/**
 * This action displays the {@link ConfirmDialogWindow}.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2348", currentReviewState = State.Passed)
public class ShowLeaveTenantDialogAction implements ClickListener {

    private static final long serialVersionUID = 1L;
    private HttpSession session = null;
    private Long tenantId = null;

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        session = Main.getCurrent().getSession();
        tenantId = (Long) session.getAttribute("tenantId");

        Main.getCurrent().getMainWindow().addWindow(
                new ConfirmDialogWindow("Please confirm that you want "
                        + "to leave this tenant.", new LeaveTenantAction(
                        tenantId)));
    }
}
