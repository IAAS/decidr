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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.vaadin.ui.Form;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.WorkflowModelFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This action appoints a list of users as workflow admins.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "TK", "DH", "RR" }, lastRevision = "2354", currentReviewState = State.Passed)
public class AppointWorkflowAdminAction implements ClickListener {

    private static final long serialVersionUID = 1L;

    private HttpSession session = Main.getCurrent().getSession();

    private Role role = (Role) session.getAttribute("role");
    private WorkflowModelFacade wfmFacade = new WorkflowModelFacade(role);

    private Form appointForm = null;
    private Long wfmId = null;

    public AppointWorkflowAdminAction(Form form, Long workflowmodel) {
        appointForm = form;
        wfmId = workflowmodel;
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        List<String> emails = new ArrayList<String>();
        List<String> userNames = new ArrayList<String>();

        for (Integer c = 1; c <= appointForm.getItemPropertyIds().size(); c++) {

            if (appointForm.getItemProperty("user" + c.toString()) != null) {
                if (appointForm.getItemProperty("user" + c.toString())
                        .getValue().toString().contains("@")) {
                    emails.add(appointForm.getItemProperty(
                            "user" + c.toString()).getValue().toString());
                } else {
                    userNames.add(appointForm.getItemProperty(
                            "user" + c.toString()).getValue().toString());
                }
            }
        }

        try {
            wfmFacade.setWorkflowAdministrators(wfmId, emails, userNames);
        } catch (TransactionException e) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(e));
        }
    }
}
