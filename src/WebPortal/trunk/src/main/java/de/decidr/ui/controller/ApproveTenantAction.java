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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * This action approves a list of tenants.
 *
 * @author Geoffrey-Alexeij Heinze
 */
public class ApproveTenantAction implements ClickListener{
    
    private HttpSession session = Main.getCurrent().getSession();
    
    private Long userId = (Long)session.getAttribute("userId");
    private TenantFacade tenantFacade = new TenantFacade(new UserRole(userId));
    
    private Table table = null;
    
    /**
     * Contructor, requires the table which contains the data
     *
     * @param table: requires Table with data
     */
    public ApproveTenantAction(Table table){
        this.table = table;
    }
    
    /* (non-Javadoc)
     * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        List<Long> tenants = new ArrayList<Long>();
        Set<?> value = (Set<?>) table.getValue();
        if (value != null && value.size() != 0){
            for (Iterator iter = value.iterator(); iter.hasNext();){
                tenants.add((Long)table.getContainerProperty(iter.next(), "id").getValue());
            }
        }
        try {
            tenantFacade.approveTenants(tenants);
        } catch (TransactionException e) {
            Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent());
        }
    }
}