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

package de.decidr.ui.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.entities.User;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.model.facades.WorkflowModelFacade;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This class represents the server side component of the modeling tool widget
 * which is integrated into the Vaadin web portal. It is used to communicate
 * with the client side of the modeling tool widget. The modeling tool is an
 * abstract component which is wrapped by a window and displayed to the user.
 * 
 * @author AT
 * @author Jonas Schlaak
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2353", currentReviewState = State.Passed)
public class ModelingTool extends AbstractComponent {

    private static final long serialVersionUID = -2284244108529453836L;

    private Logger logger = DefaultLogger.getLogger(ModelingTool.class);

    private HttpSession session = null;
    private Long tenantId = null;
    private Role role = null;
    private TenantFacade tenantFacade = null;
    private WorkflowModelFacade workflowModelFacade = null;
    private Long workflowModelId = null;
    private HashMap<Long, String> userMap = null;
    private String name = "";
    private String description = "";

    /**
     * Initializes the server side components which are needed to gain access to
     * the database.
     */
    public ModelingTool(Long workflowModelId) {
        super();
        session = Main.getCurrent().getSession();
        role = (Role) session.getAttribute("role");
        tenantId = (Long) Main.getCurrent().getSession().getAttribute(
                "tenantId");
        tenantFacade = new TenantFacade(role);
        workflowModelFacade = new WorkflowModelFacade(role);
        this.workflowModelId = workflowModelId;
        this.setSizeFull();
        this.setImmediate(true);
        this.setDebugId("modelingTool");

        Main.getCurrent().getUIDirector().getTemplateView().getContent()
                .getWidth();
    }

    @Override
    public String getTag() {
        return "modelingtool";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.AbstractComponent#changeVariables(java.lang.Object,
     * java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);
        logger.debug("[Modeling Tool] Trying to store the DWDL...");
        if (variables.containsKey("dwdl")) {
            String dwdl = variables.get("dwdl").toString();
            logger.debug("This is the dwdl:\n" + dwdl);
            try {
                workflowModelFacade.saveWorkflowModel(workflowModelId, name,
                        description, dwdl);
                logger.debug("[Modeling Tool] DWDL stored successfully.");
            } catch (TransactionException e) {
                Main.getCurrent().addWindow(
                        new TransactionErrorDialogComponent(e));
                logger.debug("[Modeling Tool] DWDL storing failed.");
            }
            Main.getCurrent().getMainWindow().addWindow(
                    new InformationDialogComponent(
                            "Workflow Model saved successfully.", "Success"));
        } else {
            logger.debug("[Modeling Tool] Client variables did not"
                    + " contain a dwdl key.");
        }
    }

    /**
     * Converts a user map into an XML string. This has to be done because the
     * Vaadin interface can only transmit simple types.
     * 
     * @param userMap
     *            list of tenant users as map
     * @return xml formatted user list
     */
    private String convertUserMapToString(HashMap<Long, String> userMap) {
        Document doc = new Document();

        doc.setRootElement(new Element("userlist"));

        for (Long userId : userMap.keySet()) {
            Element user = new Element("user");
            user.setAttribute("id", userId.toString());
            user.setAttribute("name", userMap.get(userId));
            doc.getRootElement().addContent(user);
        }

        return new XMLOutputter().outputString(doc);
    }

    private String getDWDL() {
        try {
            WorkflowModel workflowModel = workflowModelFacade
                    .getWorkflowModel(workflowModelId);
            String dwdl = new String(workflowModel.getDwdl());
            name = workflowModel.getName();
            description = workflowModel.getDescription();
            logger.debug("[Modeling Tool] Retrieving dwdl document was"
                    + " successfull");
            logger.debug("This is the dwdl:\n" + dwdl);
            return dwdl;
        } catch (TransactionException e) {
            Main.getCurrent().addWindow(new TransactionErrorDialogComponent(e));
            logger.debug("[Modeling Tool] Retrieving dwdl document failed");
            return null;
        }
    }

    private String getUsers() {
        userMap = new HashMap<Long, String>();
        try {
            logger.debug("[Modeling Tool] Trying to get "
                    + "the tenant user list...");
            List<User> users = tenantFacade.getUsersOfTenant(tenantId, null);
            for (User user : users) {

                if (user.getUserProfile() == null
                        || user.getUserProfile().getUsername().equals("")) {
                    /*
                     * If the username is empty, we want to display the email
                     * address as username.
                     */
                    userMap.put(user.getId(), user.getEmail());
                } else {
                    /*
                     * username is not empty, but we want to set the username to
                     * a more "fancy" string, for example: John Doe (jdoe42)
                     */
                    Long id = user.getId();
                    String username = user.getUserProfile().getUsername();
                    userMap.put(id, username);
                }
            }
            logger.debug("[Modeling Tool] Succeded retrieving "
                    + "tenant user list. No. of users: " + userMap.size());
            String userXMLString = convertUserMapToString(userMap);
            return userXMLString;
        } catch (TransactionException exception) {
            Main.getCurrent().addWindow(
                    new TransactionErrorDialogComponent(exception));
            logger.debug("[Modeling Tool] Failed retrieving tenant user list.");
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.ui.AbstractComponent#paintContent(com.vaadin.terminal.PaintTarget
     * )
     */
    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        target.addVariable(this, "dwdl", getDWDL());
        target.addVariable(this, "users", getUsers());

        /*
         * Set width of the modeling too scroll panel. Height is a constant
         * because the getHeight() values are wrong
         */
        int width = new Float(Main.getCurrent().getUIDirector()
                .getTemplateView().getContent().getWidth()).intValue();
        target.addVariable(this, "width", width);
        // Main.getCurrent().getUIDirector().getTemplateView().getContent().getHeight();
        target.addVariable(this, "height", 500);
    }
}
