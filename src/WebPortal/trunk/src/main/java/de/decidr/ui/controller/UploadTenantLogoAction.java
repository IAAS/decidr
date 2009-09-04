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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Item;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.SucceededEvent;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * This action handles the upload of the tenant logo
 *
 * @author Geoff
 */
public class UploadTenantLogoAction implements Upload.SucceededListener, 
                                               Upload.FailedListener, 
                                               Upload.Receiver{
	
    private HttpSession session = Main.getCurrent().getSession();

    private Long userId = (Long)session.getAttribute("userId");
    private TenantFacade tenantFacade = new TenantFacade(new UserRole(userId)); 
    
    private File file = null;
    private Item tenant = null;
    	
    /* (non-Javadoc)
    * @see com.vaadin.ui.Upload.SucceededListener#uploadSucceeded(com.vaadin.ui.Upload.SucceededEvent)
    */
    @Override
    public void uploadSucceeded(SucceededEvent event) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            int index = file.getCanonicalPath().indexOf('.');
            String suffix = file.getCanonicalPath().substring(index);
            
            tenant = (Item)session.getAttribute("tenant");
            tenantFacade.setLogo((Long)tenant.getItemProperty("id").getValue(), fis, suffix, file.getName());
            
        } catch (final java.io.FileNotFoundException e) {
            Main.getCurrent().addWindow(new TransactionErrorDialogComponent());
        }catch(TransactionException exception){
            Main.getCurrent().addWindow(new TransactionErrorDialogComponent());
        }catch(IOException exception){
            Main.getCurrent().addWindow(new TransactionErrorDialogComponent());
        }
    }
    
    /* (non-Javadoc)
    * @see com.vaadin.ui.Upload.FailedListener#uploadFailed(com.vaadin.ui.Upload.FailedEvent)
    */
    @Override
    public void uploadFailed(FailedEvent event) {
        // TODO Auto-generated method stub
    }
    
    /* (non-Javadoc)
    * @see com.vaadin.ui.Upload.Receiver#receiveUpload(java.lang.String, java.lang.String)
    */
    @Override
    public OutputStream receiveUpload(String filename, String MIMEType) {
        FileOutputStream fos = null;
        
        //TODO: change to correct path
        file = new File(filename);
        
        try {
            fos = new FileOutputStream(file);
        } catch (final java.io.FileNotFoundException e) {
            Main.getCurrent().addWindow(new TransactionErrorDialogComponent());
            return null;
        }
        
        return fos; 
    
    }

}
