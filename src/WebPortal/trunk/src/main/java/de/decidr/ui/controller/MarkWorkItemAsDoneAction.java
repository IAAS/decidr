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

import javax.servlet.http.HttpSession;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.WorkItemFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * Marks a selected work item as done.
 * 
 * @author AT
 */
public class MarkWorkItemAsDoneAction implements ClickListener {

    private HttpSession session = Main.getCurrent().getSession();

    private Long userId = (Long) session.getAttribute("userId");
    private WorkItemFacade workItemFacade = new WorkItemFacade(new UserRole(
            userId));

    private Long workItemId = null;

    /**
     * Constructor which gets a work item id as a parameter to know which work
     * item is to be marked done.
     * 
     */
    public MarkWorkItemAsDoneAction(Long workItemId) {
        this.workItemId = workItemId;
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
            workItemFacade.markWorkItemAsDone(workItemId);
        } catch (TransactionException exception) {
            Main.getCurrent().addWindow(new TransactionErrorDialogComponent(exception));
        }

    }

}
