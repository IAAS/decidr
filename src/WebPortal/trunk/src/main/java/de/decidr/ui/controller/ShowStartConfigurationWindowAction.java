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
import de.decidr.model.facades.WorkflowModelFacade;
import de.decidr.model.workflowmodel.dwdl.translator.TransformUtil;
import de.decidr.model.workflowmodel.wsc.TConfiguration;
import de.decidr.ui.view.CreateWorkflowInstanceComponent;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.SiteFrame;
import de.decidr.ui.view.StartConfigurationWindow;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * This action takes the configuration file and unmarshall it to an object. This
 * object is given as parameter to the StartConfigurationWindow where the object
 * is parsed so that the values stored in the file can be shown to the user as a
 * form and a tree.
 * 
 * @author AT
 */
public class ShowStartConfigurationWindowAction implements ClickListener {

    private HttpSession session = Main.getCurrent().getSession();

    private Long userId = (Long) session.getAttribute("userId");

    private WorkflowModelFacade workflowModelFacade = new WorkflowModelFacade(
            new UserRole(userId));

    private UIDirector uiDirector = UIDirector.getInstance();

    private SiteFrame siteFrame = uiDirector.getTemplateView();

    private Table table = null;

    private byte[] wsc = null;

    private TConfiguration tConfiguration = null;

    private Long workflowModelId = null;

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        table = ((CreateWorkflowInstanceComponent) siteFrame.getContent())
                .getInstanceTable();
        workflowModelId = (Long) table.getContainerProperty(table.getValue(),
                "id").getValue();
        try {
            wsc = workflowModelFacade
                    .getLastStartConfiguration(workflowModelId);
            tConfiguration = TransformUtil.bytes2Configuration(wsc);
            Main.getCurrent().getMainWindow().addWindow(
                    new StartConfigurationWindow(tConfiguration,
                            workflowModelId));
        } catch (JAXBException exception) {
            Main.getCurrent().addWindow(new TransactionErrorDialogComponent());
        } catch (TransactionException exception) {
            Main.getCurrent().addWindow(new TransactionErrorDialogComponent());
        }

    }

}