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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.FileFacade;
import de.decidr.model.facades.TenantFacade;
import de.decidr.ui.controller.UIDirector;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.SiteFrame;
import de.decidr.ui.view.TenantSettingsComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This action shows the TenantSettingsComponent in the content area
 * 
 * @author AT
 */
public class ShowTenantSettingsAction implements ClickListener {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7134005638789261L;

	private UIDirector uiDirector = Main.getCurrent().getUIDirector();
	private SiteFrame siteFrame = uiDirector.getTemplateView();

	private Long userId = (Long) Main.getCurrent().getSession().getAttribute(
			"userId");
	private String tenantName = null;

	TenantFacade tenantFacade = new TenantFacade(new TenantAdminRole(userId));

	FileFacade fileFacade = new FileFacade(new TenantAdminRole(userId));

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
	 * ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		Long tenantId;
		try {
			tenantId = (Long)Main.getCurrent().getSession().getAttribute("tenantId");
			tenantName = tenantFacade.getTenant(tenantId).getName();
			
			String description = "";
			if(tenantFacade.getTenantSettings(tenantId).getItemProperty("description").getValue() != null){
				description = tenantFacade.getTenantSettings(tenantId)
				.getItemProperty("description").getValue().toString();
			}
			
			
			InputStream in = tenantFacade.getLogo(tenantId);
                        File file = new File("themes/"+tenantName+"/img/decidrlogo.png");
			
                        if (in == null){
                            in = tenantFacade.getLogo(DecidrGlobals.DEFAULT_TENANT_ID);
                        }

                        if (in == null){
                            in = new FileInputStream("themes/decidr/img/decidrlogo.png");
                        }
                        
                        if (in == null){
                            Main.getCurrent().getMainWindow().showNotification("no input stream");
                        }
                        
	                file.createNewFile();
	                        
	                OutputStream out = new FileOutputStream(file);
	                byte[] buf = new byte[1024];
	                int i = in.read(buf);
	                while(i != -1){
	                    out.write(buf, 0, i);
	                    i = in.read(buf);
	                }
	                out.close();
	                        
			
			in.close();
			
			
			
			siteFrame.setContent(new TenantSettingsComponent(tenantName, description, file.getName()));
		} catch (TransactionException e) {
			Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent(e));
		} catch (FileNotFoundException e) {
                    Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent(e));
		} catch (IOException e) {
                    Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent(e));
		}
		
		
		
		
		
		
	}
}
