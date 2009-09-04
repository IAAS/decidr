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

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;
import com.vaadin.data.Item;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.model.facades.WorkflowModelFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;
import de.decidr.ui.view.client.ui.VModelingTool;

/**
 * TODO: add comment
 * 
 * @author AT, Jonas Schlaak
 */
@SuppressWarnings("serial")
public class Server extends AbstractComponent {

    private HttpSession session = null;
    private Long userId = null;
    private Long tenantId = null;
    private String tenantName = null;
    private TenantFacade tenantFacade = null;
    private WorkflowModelFacade workflowModelFacade = null;
    private Long workflowModelId = null;
    private HashMap<Long, String> userList = null;

    /**
     * TODO: add comment
     * 
     */
    public Server() {
        super();
        session = Main.getCurrent().getSession();
        userId = (Long) session.getAttribute("userId");
        tenantName = (String) session.getAttribute("tenant");
        tenantFacade = new TenantFacade(new UserRole(userId));
        workflowModelFacade = new WorkflowModelFacade(new UserRole(userId));
        // TODO: get workflow id properly
        workflowModelId = 0L;
    }

    private String getDWDL() {

        try {

            Item workflowModel = workflowModelFacade
                    .getWorkflowModel(workflowModelId);
            return new String((byte[]) workflowModel.getItemProperty("dwdl")
                    .getValue());

        } catch (TransactionException e) {
            Main.getCurrent().addWindow(new TransactionErrorDialogComponent());
            return null;
        }
    }

    private String getUsers() {
        session = Main.getCurrent().getSession();
        userId = (Long) session.getAttribute("userId");
        tenantName = (String) session.getAttribute("tenant");
        tenantFacade = new TenantFacade(new UserRole(userId));
        try {
            tenantId = tenantFacade.getTenantId(tenantName);
            List<Item> users = tenantFacade.getUsersOfTenant(tenantId, null);
            for (Item item : users) {
                if (!item.getItemProperty("username").equals("")) {
                    userList.put((Long) item.getItemProperty("id").getValue(),
                            (String) item.getItemProperty("first_name")
                                    .getValue()
                                    + " "
                                    + (String) item
                                            .getItemProperty("last_name")
                                            .getValue()
                                    + " ("
                                    + (String) item.getItemProperty("username")
                                            .getValue() + ")");
                } else {
                    userList.put((Long) item.getItemProperty("id").getValue(),
                            (String) item.getItemProperty("email").getValue());
                }
            }
            return convertUserHashMapToString(userList);
        } catch (TransactionException exception) {
            Main.getCurrent().addWindow(new TransactionErrorDialogComponent());
            return null;
        }

    }

    private String convertUserHashMapToString(HashMap<Long, String> userList) {
        Document doc = XMLParser.createDocument();

        Element root = doc.createElement("userlist");
        for (Long userId : userList.keySet()) {
            Element user = doc.createElement("user");
            user.setAttribute("id", userId.toString());
            user.setAttribute("name", userList.get(user));
            root.appendChild(user);
        }

        return doc.toString();
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
        if (variables.containsKey("dwdl")) {
            byte[] dwdl = (byte[]) variables.get("dwdl");
            try {
                workflowModelFacade.saveWorkflowModel(workflowModelId, "", "",
                        dwdl);
            } catch (TransactionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.AbstractComponent#getTag()
     */
    @Override
    public String getTag() {
        return VModelingTool.TAGNAME;
    }

}
