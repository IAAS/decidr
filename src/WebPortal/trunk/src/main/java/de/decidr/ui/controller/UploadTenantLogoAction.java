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
import java.io.OutputStream;
import java.util.HashSet;

import javax.servlet.http.HttpSession;

import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.SucceededEvent;

import de.decidr.model.acl.permissions.FilePermission;
import de.decidr.model.acl.permissions.FileReadPermission;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.FileFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * This action handles the upload of the tenant logo
 * 
 * @author Geoff
 */
public class UploadTenantLogoAction implements Upload.SucceededListener,
		Upload.FailedListener, Upload.Receiver {

	private HttpSession session = Main.getCurrent().getSession();

	private Long userId = (Long) session.getAttribute("userId");
	private FileFacade fileFacade = new FileFacade(new UserRole(userId));

	private File file = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.ui.Upload.Receiver#receiveUpload(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public OutputStream receiveUpload(String filename, String MIMEType) {
		FileOutputStream fos = null;
		FileInputStream fis = null;

		file = new File(filename);

		try {
			fis = new FileInputStream(file);
			fos = new FileOutputStream(file);

			HashSet<Class<? extends FilePermission>> filePermission = new HashSet<Class<? extends FilePermission>>();
			filePermission.add(FileReadPermission.class);
			
			Long fileId = fileFacade.createFile(fis, file.length(),
					filename, MIMEType, true, filePermission);
			
			Main.getCurrent().getMainWindow().setData(fileId);
			Main.getCurrent().getMainWindow().showNotification(
					"File " + filename + "successfully temporarily saved!");
		} catch (final java.io.FileNotFoundException e) {
			Main.getCurrent().addWindow(new TransactionErrorDialogComponent(e));
		} catch (TransactionException exception) {
			Main.getCurrent().getMainWindow().addWindow(
					new TransactionErrorDialogComponent(exception));
		}

		return fos;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.ui.Upload.FailedListener#uploadFailed(com.vaadin.ui.Upload
	 * .FailedEvent)
	 */
	@Override
	public void uploadFailed(FailedEvent event) {
		Main.getCurrent().getMainWindow().showNotification("Upload failed");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.ui.Upload.SucceededListener#uploadSucceeded(com.vaadin.ui.
	 * Upload.SucceededEvent)
	 */
	@Override
	public void uploadSucceeded(SucceededEvent event) {
		Main.getCurrent().getMainWindow().showNotification("Upload succeeded");
	}

}
