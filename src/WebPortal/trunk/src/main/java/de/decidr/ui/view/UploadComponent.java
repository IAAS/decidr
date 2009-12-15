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

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.HashSet;

import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;

import de.decidr.model.acl.permissions.FilePermission;
import de.decidr.model.acl.permissions.FileReadPermission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.entities.File;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.FileFacade;
import de.decidr.ui.controller.UIDirector;
import de.decidr.ui.controller.UploadAction;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This component provides an upload field to upload data for the work item the
 * user works on. If a file is uploaded, the component is changed into a text
 * field and a button, so the user can delete the last file he uploaded.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2454", currentReviewState = State.Passed)
public class UploadComponent extends CustomComponent {

    private Long fileId = null;
    private Receiver receiver = null;

    private HorizontalLayout horizontalLayout = null;

    private TextField fileInfo = null;

    private Button button = null;

    private com.vaadin.ui.Upload upload = null;

    private FileFacade fileFacade = new FileFacade((Role) Main.getCurrent()
            .getSession().getAttribute("role"));

    private UIDirector uiDirector = Main.getCurrent().getUIDirector();
    private SiteFrame siteFrame = uiDirector.getTemplateView();

    boolean deleted = false;

    /**
     * Constructor with a file ID. The file ID is stored and the init method is
     * called to initialize the components.
     * 
     * @param fileId
     *            - The file ID which is stored in the work item.
     */
    public UploadComponent(Long fileId, Receiver receiver) {
        this.fileId = fileId;
        this.receiver = receiver;
        init();
    }

    /**
     * Initializes the components from the upload component.
     */
    private void init() {
        // Checks if the file ID is null. This can only happen by the first
        // time, if there isn't a file ID stored in the work item. If it is
        // null, the deleted flag is set to true
        if (fileId == null) {
            deleted = true;
        }
        horizontalLayout = new HorizontalLayout();
        this.setCompositionRoot(horizontalLayout);
        horizontalLayout.removeAllComponents();
        // If the file is "deleted", that means if there isn't a file id in the
        // work item or the user has deleted his last upload then the component
        // is built with an upload component from vaadin, so that the user is
        // able to upload a new file. If the file is not deleted or a file id is
        // stored in the work item then the filename is shown to the user and he
        // can delete this file and upload a new one
        if (deleted) {
            upload = new com.vaadin.ui.Upload("File URI", receiver);
            horizontalLayout.addComponent(upload);

            upload.addListener(new Upload.SucceededListener() {

                @Override
                public void uploadSucceeded(SucceededEvent event) {
                    UploadAction action = (UploadAction) receiver;
                    java.io.File file = action.getFile();
                    if (file == null) {
                        Main
                                .getCurrent()
                                .getMainWindow()
                                .addWindow(
                                        new InformationDialogComponent(
                                                "Illegal Argument: File must not be null!",
                                                "Failure"));
                    } else {
                        FileInputStream fis;
                        try {
                            fis = new FileInputStream(file);
                            HashSet<Class<? extends FilePermission>> filePermission = new HashSet<Class<? extends FilePermission>>();
                            filePermission.add(FileReadPermission.class);

                            if (fileId != null) {
                                fileFacade.replaceFile(fileId, fis, file
                                        .length(), event.getFilename(), event
                                        .getMIMEType());
                            } else {
                                fileId = fileFacade.createFile(fis, file
                                        .length(), event.getFilename(), event
                                        .getMIMEType(), true, filePermission);
                            }

                            Main.getCurrent().getMainWindow().setData(fileId);

                            Main.getCurrent().getMainWindow().addWindow(
                                    new InformationDialogComponent("File "
                                            + event.getFilename()
                                            + " successfully uploaded!",
                                            "Success"));

                            if (siteFrame.getContent() instanceof TenantSettingsComponent
                                    && siteFrame.getHeader() instanceof Header) {
                                TenantSettingsComponent tsc = (TenantSettingsComponent) siteFrame
                                        .getContent();
                                Header header = (Header) siteFrame.getHeader();
                                Resource resource = new FileResource(file,
                                        getApplication());
                                tsc.getLogoEmbedded().setSource(resource);
                                header.getDecidrLogo().setSource(resource);
                                tsc.requestRepaint();
                                header.requestRepaint();
                            }

                            UploadComponent.this.deleted = false;
                            UploadComponent.this.init();
                        } catch (FileNotFoundException e) {
                            Main.getCurrent().getMainWindow().addWindow(
                                    new InformationDialogComponent(
                                            "File couldn't be found!",
                                            "File not found"));
                        } catch (TransactionException e) {
                            Main.getCurrent().getMainWindow().addWindow(
                                    new TransactionErrorDialogComponent(e));
                        }
                    }
                }
            });
        } else {
            File file;
            try {
                file = fileFacade.getFileInfo(fileId);

                fileInfo = new TextField("File info");
                button = new Button("Delete file");
                button.addListener(new Button.ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {
                        UploadComponent.this.deleted = true;
                        UploadComponent.this.init();
                    }
                });

                horizontalLayout.setSpacing(true);
                horizontalLayout.addComponent(fileInfo);
                horizontalLayout.addComponent(button);

                fileInfo.setValue(file.getFileName());
            } catch (TransactionException e) {
                Main.getCurrent().getMainWindow().addWindow(
                        new InformationDialogComponent(
                                "File couldn't be found!", "File not found"));
            }
        }
    }
}
