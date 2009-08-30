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

package de.decidr.ui.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Item;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;


import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.modelingtool.client.io.DataExchanger;
import de.decidr.ui.data.Server;

import de.decidr.ui.controller.UIDirector;

/**
 * TODO: add comment
 * 
 * @author AT
 */
public class ModelingTool extends AbstractComponent implements DataExchanger {

    private HttpSession session = null;

    private Long userId = null;

    private Long tenantId = null;

    private String tenantName = null;

    private TenantFacade tenantFacade = null;

    private HashMap<Long, String> userList = null;
    
    private Server server1 = null;

    @Override
    public String getTag() {
        return "modelingtool";
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
        server1 = new Server();
        Object[] server = new Object[1];
        server[1] = server1;
        target.addAttribute("server", server);
        super.paintContent(target);
        
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.AbstractComponent#changeVariables(java.lang.Object,
     * java.util.Map)
     */
    @Override
    public void changeVariables(Object source, Map variables) {
        // TODO Auto-generated method stub
        super.changeVariables(source, variables);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.io.DataExchanger#getUsers()
     */
    @Override
    public HashMap<Long, String> getUsers() {
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
                                    +" " + (String) item
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
            return userList;
        } catch (TransactionException exception) {
            Main.getCurrent().addWindow(new TransactionErrorDialogComponent());
            return null;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.io.DataExchanger#loadDWDL()
     */
    @Override
    public String loadDWDL() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.modelingtool.client.io.DataExchanger#saveDWDL(java.lang.String)
     */
    @Override
    public void saveDWDL(String dwdl) {
        // TODO Auto-generated method stub

    }

}
