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

package de.decidr.ui.controller.tenant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;

import javax.servlet.http.HttpSession;

import com.vaadin.terminal.UploadStream;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.SucceededEvent;

import de.decidr.model.acl.permissions.FilePermission;
import de.decidr.model.acl.permissions.FileReadPermission;
import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.FileFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This action handles the upload of the tenant logo
 * 
 * @author Geoff
 */
public class UploadTenantLogoAction implements Upload.Receiver//Upload.SucceededListener,
		//Upload.FailedListener, Upload.Receiver, Upload.FinishedListener, Upload.ProgressListener 
{

	private HttpSession session = Main.getCurrent().getSession();

	private Long userId = (Long) session.getAttribute("userId");
	private FileFacade fileFacade = new FileFacade(new TenantAdminRole(userId));

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

		try {
			file = File.createTempFile("decidr", "temp");

			fos = new FileOutputStream(file);
		} catch (IOException e) {
			Main.getCurrent().getMainWindow().addWindow(
					new InformationDialogComponent("IOException", "Failure"));
		}
		
		return fos;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.ui.Upload.FailedListener#uploadFailed(com.vaadin.ui.Upload
	 * .FailedEvent)
	 
	@Override
	public void uploadFailed(FailedEvent event) {
		Main.getCurrent().getMainWindow().showNotification("Upload failed");
	}

	
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.ui.Upload.SucceededListener#uploadSucceeded(com.vaadin.ui.
	 * Upload.SucceededEvent)
	 
	@Override
	public void uploadSucceeded(SucceededEvent event) {

		if (file == null) {
			Main.getCurrent().getMainWindow().addWindow(
					new InformationDialogComponent(
							"Illegalt Argument File must not be null",
							"Failure"));
		} else {
			FileInputStream fis;
			try {
				fis = new FileInputStream(file);
				HashSet<Class<? extends FilePermission>> filePermission = new HashSet<Class<? extends FilePermission>>();
				filePermission.add(FileReadPermission.class);

				Long fileId = fileFacade.createFile(fis, file.length(), event
						.getFilename(), event.getMIMEType(), true,
						filePermission);

				Main.getCurrent().getMainWindow().setData(fileId);
				Main.getCurrent().getMainWindow().addWindow(
						new InformationDialogComponent("File "
								+ event.getFilename()
								+ "successfully uploaded!", "Success"));
			} catch (FileNotFoundException e) {
				Main.getCurrent().getMainWindow().addWindow(
						new InformationDialogComponent(
								"File couldn't be found", "File not found"));
			} catch (TransactionException e) {
				Main.getCurrent().getMainWindow().addWindow(
						new TransactionErrorDialogComponent(e));
			}

		}

	}

	 (non-Javadoc)
	 * @see com.vaadin.ui.Upload.FinishedListener#uploadFinished(com.vaadin.ui.Upload.FinishedEvent)
	 
	@Override
	public void uploadFinished(FinishedEvent event) {
		if (file == null) {
			Main.getCurrent().getMainWindow().addWindow(
					new InformationDialogComponent(
							"Illegalt Argument File must not be null",
							"Failure"));
		} else {
			FileInputStream fis;
			try {
				fis = new FileInputStream(file);
				HashSet<Class<? extends FilePermission>> filePermission = new HashSet<Class<? extends FilePermission>>();
				filePermission.add(FileReadPermission.class);

				Long fileId = fileFacade.createFile(fis, file.length(), event
						.getFilename(), event.getMIMEType(), true,
						filePermission);

				Main.getCurrent().getMainWindow().setData(fileId);
				Main.getCurrent().getMainWindow().addWindow(
						new InformationDialogComponent("File "
								+ event.getFilename()
								+ "successfully uploaded!", "Success"));
				
			} catch (FileNotFoundException e) {
				Main.getCurrent().getMainWindow().addWindow(
						new InformationDialogComponent(
								"File couldn't be found", "File not found"));
			} catch (TransactionException e) {
				Main.getCurrent().getMainWindow().addWindow(
						new TransactionErrorDialogComponent(e));
			}

		}
		
	}

	 (non-Javadoc)
	 * @see com.vaadin.ui.Upload.ProgressListener#updateProgress(long, long)
	 
	@Override
	public void updateProgress(long readBytes, long contentLength) {
		// TODO Auto-generated method stub
		
	}
*/
	
	public File getFile(){
		return file;
	}
}
