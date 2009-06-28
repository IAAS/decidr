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

import com.vaadin.data.Item;
import com.vaadin.ui.Form;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.model.permissions.UserRole;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * This action invites a list of users and/or email addresses to join the tenant
 *
 * @author Geoffrey-Alexeij Heinze
 */
public class InviteUserToTenantAction implements ClickListener{

    private HttpSession session = Main.getCurrent().getSession();
    
    private Long userId = (Long)session.getAttribute("userId");
    private TenantFacade tenantFacade = new TenantFacade(new UserRole(userId));
    
    private Form inviteForm = null;
    private Item tenant = null;
    
    public InviteUserToTenantAction(Form form){
        inviteForm = form;
    }
    
    /* (non-Javadoc)
     * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        tenant = (Item)session.getAttribute("tenant");
        
        List<String> emails = new ArrayList<String>();
        List<String> userNames = new ArrayList<String>(); 
        for (Integer c = 1; c <= inviteForm.getItemPropertyIds().size(); c++ ){
            if(inviteForm.getItemProperty("user"+c.toString()) != null){
                if(inviteForm.getItemProperty("user"+c.toString()).getValue().toString().contains("@")){
                    emails.add(inviteForm.getItemProperty("user"+c.toString()).getValue().toString());
                }else{
                    userNames.add(inviteForm.getItemProperty("user"+c.toString()).getValue().toString());
                }
            }
        }
      //TODO: remove
        Main.getCurrent().getMainWindow().showNotification(userNames.toString());
        Main.getCurrent().getMainWindow().showNotification(emails.toString());
        
        try {
            tenantFacade.inviteUsersAsMembers((Long)tenant.getItemProperty("id").getValue(), emails, userNames);
        } catch (TransactionException e) {
            Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent());
        }
    }

}
