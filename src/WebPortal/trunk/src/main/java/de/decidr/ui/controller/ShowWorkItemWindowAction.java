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
import javax.xml.bind.JAXBException;

import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.WorkItemFacade;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;
import de.decidr.model.workflowmodel.humantask.THumanTaskData;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.SiteFrame;
import de.decidr.ui.view.TransactionErrorDialogComponent;
import de.decidr.ui.view.WorkItemComponent;
import de.decidr.ui.view.WorkItemWindow;

/**
 * This action opens a window for representing the work item the user has
 * selected to work on.
 * 
 * @author AT
 */
public class ShowWorkItemWindowAction implements ClickListener {

    private HttpSession session = Main.getCurrent().getSession();

    private Long userId = (Long) session.getAttribute("userId");

    private WorkItemFacade workItemFacade = new WorkItemFacade(new UserRole(
            userId));

    private UIDirector uiDirector = UIDirector.getInstance();

    private SiteFrame siteFrame = uiDirector.getTemplateView();

    private Table table = null;

    private Long workItemId = null;

    private byte[] ht = null;

    private THumanTaskData tHumanTaskData = null;

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        table = ((WorkItemComponent) siteFrame.getContent()).getWorkItemTable();
        workItemId = (Long) table.getContainerProperty(table.getValue(), "id")
                .getValue();
        try {
            ht = (byte[]) workItemFacade.getWorkItem(workItemId)
                    .getItemProperty("data").getValue();
            tHumanTaskData = TransformUtil.bytesToHumanTask(ht);
            Main.getCurrent().getMainWindow().addWindow(
                    new WorkItemWindow(tHumanTaskData, workItemId));
        } catch (TransactionException exception) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(exception));
        } catch (JAXBException exception) {
            Main.getCurrent().getMainWindow().showNotification("JAXB error!");
        }
    }

}
