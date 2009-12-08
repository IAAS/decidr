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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.vaadin.ui.Upload;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.InformationDialogComponent;

/**
 * This action handles the upload of the tenant logo.
 * 
 * @author Geoff
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2350", currentReviewState = State.Passed)
public class UploadTenantLogoAction implements Upload.Receiver {

    private static final long serialVersionUID = 1L;
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
            file = File.createTempFile("decidr", "tmp");

            fos = new FileOutputStream(file);
        } catch (IOException e) {
            Main.getCurrent().getMainWindow().addWindow(
            // Aleks, GH: a little more verbose, if you please! ~rr
                    new InformationDialogComponent("IOException", "Failure"));
        }

        return fos;
    }

    public File getFile() {
        return file;
    }
}
