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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import com.vaadin.ui.Upload.Receiver;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.InformationDialogComponent;
/**
 * This class handles the actions when the upload for a human task is started,
 * finished and received.
 * 
 * @author AT
 */
@Reviewed(reviewers = "RR", lastRevision = "2343", currentReviewState = State.PassedWithComments)
public class UploadAction implements Receiver {

    private static final long serialVersionUID = 1L;


    private File file = null;

    /**
     * Returns the filed
     * 
     * @return file
     */
    public File getFile() {
        return file;
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

         try {
             file = File.createTempFile("decidr", "tmp");

             fos = new FileOutputStream(file);
         } catch (IOException e) {
             Main.getCurrent().getMainWindow().addWindow(
                     new InformationDialogComponent(
                             "An error occured wihle uploading your file:<br/>"+ e.getMessage()
                             , "Upload Failure"));
         }

         return fos;
    	
        /*FileOutputStream fos = null;
        FileInputStream fis = null;

        file = new java.io.File(filename);

        try {
            fis = new FileInputStream(file);
            fos = new FileOutputStream(file);
            HashSet<Class<? extends FilePermission>> filePermission = new HashSet<Class<? extends FilePermission>>();
            filePermission.add(FileReadPermission.class);
            fileId = fileFacade.createFile(fis, file.length(), filename,
                    MIMEType, true, filePermission);
            Main.getCurrent().getMainWindow().showNotification(
                    "File " + filename + "successfully temporarily saved!");

            deleteUploadComponent.getLabel().setValue(filename);
            startConfigurationWindow.getUpload().setVisible(false);
            startConfigurationWindow.getAssignmentForm().getLayout()
                    .addComponent(deleteUploadComponent);

            Main.getCurrent().getMainWindow().setData(fileId);
        } catch (FileNotFoundException exception) {
            Main.getCurrent().getMainWindow()
                    .showNotification("File not found.");
        } catch (TransactionException exception) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(exception));
        }

        // GH, Aleks: this will return null if an Exception is thrown - is that
        // on purpose? ~rr
        return fos;*/
    }
}
