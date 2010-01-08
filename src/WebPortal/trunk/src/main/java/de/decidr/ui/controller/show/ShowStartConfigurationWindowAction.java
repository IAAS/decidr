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

import java.nio.charset.Charset;

import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import com.vaadin.data.Item;
import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.WorkflowModelFacade;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.workflowmodel.dwdl.Workflow;
import de.decidr.model.workflowmodel.dwdl.transformation.DWDL2WSC;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;
import de.decidr.model.workflowmodel.wsc.TConfiguration;
import de.decidr.ui.controller.UIDirector;
import de.decidr.ui.view.CreateWorkflowInstanceComponent;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.SiteFrame;
import de.decidr.ui.view.windows.StartConfigurationWindow;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This action takes the configuration file and unmarshalls it to an object.
 * This object is given as parameter to the StartConfigurationWindow where the
 * object is parsed so that the values stored in the file can be shown to the
 * user as a form and a tree.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2360", currentReviewState = State.Passed)
public class ShowStartConfigurationWindowAction implements ClickListener {

    private static final long serialVersionUID = 1L;

    private HttpSession session = Main.getCurrent().getSession();

    private Role role = (Role) session.getAttribute("role");

    private WorkflowModelFacade workflowModelFacade = new WorkflowModelFacade(
            role);

    private UIDirector uiDirector = Main.getCurrent().getUIDirector();

    private SiteFrame siteFrame = uiDirector.getTemplateView();

    private Table table = null;

    private byte[] wsc = null;

    private TConfiguration tConfiguration = null;

    private Long workflowModelId = null;

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        Logger logger = DefaultLogger.getLogger(ShowStartConfigurationWindowAction.class);
        logger.debug("ShowStartConfigWindowAction ... button clicked");
        
        table = ((CreateWorkflowInstanceComponent) siteFrame.getContent())
                .getInstanceTable();
        logger.debug("ShowStartConfigWindowAction ... retrieved table");
        
        Item item = table.getItem(table.getValue());
        logger.debug("ShowStartConfigWindowAction ... retrieved selected item");
                
        workflowModelId = (Long) item.getItemProperty("id").getValue();
        logger.debug("ShowStartConfigWindowAction ... retrieved selected workflow model id");
        
        try {
            wsc = workflowModelFacade
                    .getLastStartConfiguration(workflowModelId);
            logger.debug("ShowStartConfigWindowAction ... retrieved last start config byte[]");
            
            
            byte[] dwdl = workflowModelFacade.getWorkflowModel(
                    workflowModelId).getDwdl();
            logger.debug("ShowStartConfigWindowAction ... retrieved dwdl");
            if (dwdl == null){
                logger.debug("ShowStartConfigWindowAction ... retrieved dwdl, but it's null!");
            }
            
            Workflow workflow = TransformUtil.bytesToWorkflow(dwdl);
            logger.debug("ShowStartConfigWindowAction ... retrieved workflow");

            if (wsc == null) {
                logger.debug("ShowStartConfigWindowAction ... wsc is null");
                DefaultLogger.getLogger(
                        ShowStartConfigurationWindowAction.class).debug(
                        (new String(dwdl, Charset.forName("UTF-8"))));
                DWDL2WSC dwdl2wsc = new DWDL2WSC();
                tConfiguration = dwdl2wsc.getStartConfiguration(workflow);
                logger.debug("ShowStartConfigWindowAction ... retrieved start config from workflow");
            } else {
                tConfiguration = TransformUtil.bytesToConfiguration(wsc);
                logger.debug("ShowStartConfigWindowAction ... retrieved start config");
                
            }

            if (tConfiguration == null){
                logger.debug("ShowStartConfigWindowAction ... start config is null!");
            }
            
            logger.debug("ShowStartConfigWindowAction ... adding StartConfigurationWindow");
            
            Main.getCurrent().getMainWindow().addWindow(
                    new StartConfigurationWindow(tConfiguration,
                            workflowModelId, workflow));
        } catch (JAXBException exception) {
            Main.getCurrent().addWindow(
                    new TransactionErrorDialogComponent(exception));
        } catch (TransactionException exception) {
            Main.getCurrent().addWindow(
                    new TransactionErrorDialogComponent(exception));
        }
    }
}
