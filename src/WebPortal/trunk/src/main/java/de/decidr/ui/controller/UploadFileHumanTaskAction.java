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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashSet;

import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

import de.decidr.model.acl.permissions.FilePermission;
import de.decidr.model.acl.permissions.FileReadPermission;
import de.decidr.model.acl.roles.UserRole;

import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.FileFacade;
import de.decidr.ui.view.DeleteUploadComponent;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.StartConfigurationWindow;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * This class handles the actions when the upload for a human task is started,
 * finished and received.
 * 
 * @author AT
 */
public class UploadFileHumanTaskAction implements FailedListener, Receiver,
        SucceededListener, FinishedListener {

    private StartConfigurationWindow startConfigurationWindow = new StartConfigurationWindow();

    private DeleteUploadComponent deleteUploadComponent = new DeleteUploadComponent();

    private java.io.File file = null;

    private FileFacade fileFacade = new FileFacade(new UserRole((Long) Main
            .getCurrent().getSession().getAttribute("userId")));
    
    private Long fileId = null;

    /**
     * Returns the filed Id
     *
     * @return fileId
     */
    public Long getFileId() {
        return fileId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.Upload.Receiver#receiveUpload(java.lang.String,
     * java.lang.String)
     */
    @Override
    public OutputStream receiveUpload(String filename, String MIMEType) {
        FileOutputStream fos = null;

        file = new java.io.File(filename);

        try {
            fos = new FileOutputStream(file);
        } catch (final FileNotFoundException exception) {
            Main.getCurrent().getMainWindow().showNotification(
                    "File not found!");
            return null;
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
        Main.getCurrent().getMainWindow().showNotification(
                "File " + event.getFilename() + "uplaod not successful!");

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.ui.Upload.FinishedListener#uploadFinished(com.vaadin.ui.Upload
     * .FinishedEvent)
     */
    @Override
    public void uploadFinished(FinishedEvent event) {
        deleteUploadComponent.getLabel().setValue(event.getFilename());
        startConfigurationWindow.getUpload().setVisible(false);
        startConfigurationWindow.getAssignmentForm().getLayout().addComponent(
                deleteUploadComponent);
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
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            HashSet<Class<? extends FilePermission>> filePermission = new HashSet<Class<? extends FilePermission>>();
            filePermission.add(FileReadPermission.class);
            fileId = fileFacade.createFile(fis, event.getLength(), event
                    .getFilename(), event.getMIMEType(), true,
                    filePermission);
            Main.getCurrent().getMainWindow().showNotification(
                    "File " + event.getFilename()
                            + "successfully temporarily saved!");
        } catch (FileNotFoundException exception) {
            Main.getCurrent().getMainWindow()
                    .showNotification("File not found");
        }catch(TransactionException exception){
            Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent());
        }
    }

}