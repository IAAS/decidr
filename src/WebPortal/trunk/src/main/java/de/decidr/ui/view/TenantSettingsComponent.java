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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.HashSet;


import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Upload.SucceededEvent;

import de.decidr.model.acl.permissions.FilePermission;
import de.decidr.model.acl.permissions.FileReadPermission;
import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.FileFacade;
import de.decidr.model.facades.TenantFacade;
import de.decidr.ui.controller.CssHandler;
import de.decidr.ui.controller.tenant.RestoreDefaultTenantSettingsAction;
import de.decidr.ui.controller.tenant.SaveTenantSettingsAction;
import de.decidr.ui.controller.tenant.UploadTenantLogoAction;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * The tenant can change his settings. He change his theme by choosing a given
 * background or font or he can enter his own CSS and upload it.
 * 
 * @author AT
 */
public class TenantSettingsComponent extends CustomComponent {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7841061147447361631L;

	private VerticalLayout verticalLayout = null;
	private HorizontalLayout browseHorizontalLayout = null;
	private HorizontalLayout buttonHorizontalLayout = null;
	private HorizontalLayout schemeHorizontalLayout = null;

	private Panel browsePanel = null;
	private Panel schemePanel = null;
	private Panel buttonPanel = null;

	private Label tenantSettingsLabel = null;

	private Upload logoUpload = null;

	private TextField textArea = null;
	private TextField cssTextArea = null;

	private Button saveButton = null;
	private Button restoreDefaultSettingsButton = null;
	private Button showAdvancedOptionsButton = null;
	private Button showBasicOptionsButton = null;
	private NativeSelect backgroundSelect = null;
	private NativeSelect foregroundSelect = null;
	private NativeSelect fontSizeSelect = null;
	private NativeSelect fontSelect = null;

	private Embedded logoEmbedded = null;

	final String[] colors = new String[] { "aqua", "black", "blue", "fuchsia",
			"gray", "green", "lime", "maroon", "navy", "olive", "purple",
			"red", "silver", "teal", "white", "yellow" };

	final String[] fontSizes = new String[] { "9", "10", "11", "12", "13",
			"14", "15", "16" };

	final String[] fonts = new String[] { "Times New Roman", "Arial",
			"Courier New", "Verdana" };
	
	private Long tenantId;
	private String tenantDescription;
	private String tenantName;
	private String logoFileName;
	
	private Long userId;
	private TenantFacade tenantFacade = null;
	private FileFacade fileFacade = null;

	/**
	 * Default constructor.
	 * 
	 */
	public TenantSettingsComponent(Long tenantId) {
	        this.tenantId = tenantId;
	        
	        userId = (Long) Main.getCurrent().getSession().getAttribute(
	        "userId");
	        tenantFacade = new TenantFacade(new TenantAdminRole(userId));
	        fileFacade = new FileFacade(new TenantAdminRole((Long)Main.getCurrent().getSession().getAttribute("userId")));
	        
	        tenantDescription = "";
	        try {
                    tenantName = tenantFacade.getTenant(tenantId).getName();
                    if(tenantFacade.getTenantSettings(tenantId).getItemProperty("description").getValue() != null){
                        tenantDescription = tenantFacade.getTenantSettings(tenantId)
                            .getItemProperty("description").getValue().toString();
                    }
                } catch (TransactionException e) {
                    Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent(e));
                }
                
                
                
                // get logo
//              InputStream in = tenantFacade.getLogo(tenantId);
//                File file = new File("themes/"+tenantName+"/img/decidrlogo.png");
//                file.createNewFile();
//                
//                if (in == null){
//                    in = tenantFacade.getLogo(DecidrGlobals.DEFAULT_TENANT_ID);
//                }
//
//                
//                if (in == null){
//                    Main.getCurrent().getMainWindow().showNotification("no input stream");
//                }else{
//                        OutputStream out = new FileOutputStream(file);
//                      byte[] buf = new byte[1024];
//                      int i = in.read(buf);
//                      while(i != -1){
//                          out.write(buf, 0, i);
//                          i = in.read(buf);
//                      }
//                      out.close();
//                }
//                      
//              
//              in.close();
                
                // get css
	        
		init();
	}

	/**
	 * This method initializes the components for the tenant settings component.
	 * 
	 */
	private void init() {
		verticalLayout = new VerticalLayout();
		browseHorizontalLayout = new HorizontalLayout();
		buttonHorizontalLayout = new HorizontalLayout();
		schemeHorizontalLayout = new HorizontalLayout();

		browsePanel = new Panel();
		schemePanel = new Panel();
		schemePanel.setCaption("Color Scheme");
		buttonPanel = new Panel();

		tenantSettingsLabel = new Label("<h2>Tenant Settings for " + tenantName
				+ "</h2>");
		tenantSettingsLabel.setContentMode(Label.CONTENT_XHTML);

		textArea = new TextField();
		textArea.setRows(10);
		textArea.setColumns(30);
		textArea.setCaption("Description");
		textArea.setValue(tenantDescription);

		logoUpload = new Upload("Upload Logo", new UploadTenantLogoAction());
		logoUpload.setButtonCaption("Upload Logo");

		logoUpload.addListener(new Upload.SucceededListener() {
			
			@Override
			public void uploadSucceeded(SucceededEvent event) {
				UploadTenantLogoAction action = (UploadTenantLogoAction) TenantSettingsComponent.this
						.getUpload().getReceiver();
				File file = action.getFile();
				if (file == null) {
					Main.getCurrent().getMainWindow().addWindow(
							new InformationDialogComponent(
									"Illegal Argument: File must not be null",
									"Failure"));
				} else {
					FileInputStream fis;
					try {
						fis = new FileInputStream(file);
						HashSet<Class<? extends FilePermission>> filePermission = new HashSet<Class<? extends FilePermission>>();
						filePermission.add(FileReadPermission.class);

						Long fileId = fileFacade.createFile(fis, file.length(),
								event.getFilename(), event.getMIMEType(), true,
								filePermission);

						Main.getCurrent().getMainWindow().setData(fileId);
						Main.getCurrent().getMainWindow()
								.addWindow(
										new InformationDialogComponent("File "
												+ event.getFilename()
												+ " successfully uploaded!",
												"Success"));
					} catch (FileNotFoundException e) {
						Main.getCurrent().getMainWindow().addWindow(
								new InformationDialogComponent(
										"File couldn't be found",
										"File not found"));
					} catch (TransactionException e) {
						Main.getCurrent().getMainWindow().addWindow(
								new TransactionErrorDialogComponent(e));
					}

				}

				Main.getCurrent().getMainWindow().addWindow(
				            new InformationDialogComponent(
				                    Main.getCurrent().getContext().getBaseDirectory().getPath(),
				                    "Path"));
			}
		});

		saveButton = new Button("Save", new SaveTenantSettingsAction());
		restoreDefaultSettingsButton = new Button("Restore default settings",
				new RestoreDefaultTenantSettingsAction());

		logoEmbedded = new Embedded("", new ThemeResource("img/"+logoFileName));
		logoEmbedded.setCaption("Logo");
		logoEmbedded.setImmediate(true);

		setCompositionRoot(verticalLayout);

		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(tenantSettingsLabel);
		verticalLayout.addComponent(textArea);
		verticalLayout.addComponent(logoEmbedded);
		verticalLayout.addComponent(browsePanel);

		browsePanel.addComponent(browseHorizontalLayout);
		browseHorizontalLayout.setSpacing(true);
		browseHorizontalLayout.addComponent(logoUpload);

		verticalLayout.addComponent(schemePanel);

		schemePanel.addComponent(schemeHorizontalLayout);
		schemeHorizontalLayout.setSpacing(true);

		changeToBasic();

		verticalLayout.addComponent(buttonPanel);

		buttonPanel.addComponent(buttonHorizontalLayout);
		buttonHorizontalLayout.setSpacing(true);
		buttonHorizontalLayout.addComponent(saveButton);
		buttonHorizontalLayout.addComponent(restoreDefaultSettingsButton);

	}

	/**
	 * Returns the embedded ui object
	 * 
	 * @return the logoEmbedded
	 */
	public Embedded getLogoEmbedded() {
		return logoEmbedded;
	}

	/**
	 * This method changes the view from basic to advanced. So the text area for
	 * the CSS is visible.
	 * 
	 */
	public void changeToAdvanced() {
		showBasicOptionsButton = new Button("Show basic options");
		showBasicOptionsButton.addListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				changeToBasic();
			}

		});
		cssTextArea = new TextField();
		cssTextArea.setRows(20);
		cssTextArea.setColumns(30);
		getSchemeHorizontalLayout().removeAllComponents();
		getSchemeHorizontalLayout().addComponent(cssTextArea);
		getSchemeHorizontalLayout().addComponent(showBasicOptionsButton);
	}

	/**
	 * This method changes the view from advanced to basic. So only the basic
	 * information are visible and the tenant can choose a given background or
	 * font.
	 * 
	 */
	public void changeToBasic() {
		backgroundSelect = new NativeSelect("Background");
		foregroundSelect = new NativeSelect("Foreground");
		fontSelect = new NativeSelect("Font");
		fontSizeSelect = new NativeSelect("Font size");

		fillSelects();

		showAdvancedOptionsButton = new Button("Show advanced options");
		showAdvancedOptionsButton.setSwitchMode(true);
		showAdvancedOptionsButton.addListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				changeToAdvanced();
			}

		});

		getSchemeHorizontalLayout().removeAllComponents();
		getSchemeHorizontalLayout().addComponent(backgroundSelect);
		getSchemeHorizontalLayout().addComponent(foregroundSelect);
		getSchemeHorizontalLayout().addComponent(fontSelect);
		getSchemeHorizontalLayout().addComponent(fontSizeSelect);
		getSchemeHorizontalLayout().addComponent(showAdvancedOptionsButton);
		getSchemeHorizontalLayout().setComponentAlignment(
				showAdvancedOptionsButton, Alignment.BOTTOM_RIGHT);
	}

	/**
	 * Fills the Native Select components
	 * 
	 */
	private void fillSelects() {
		for (int i = 0; i < colors.length; i++) {
			backgroundSelect.addItem(colors[i]);
			foregroundSelect.addItem(colors[i]);
		}

		for (int i = 0; i < fonts.length; i++) {
			fontSelect.addItem(fonts[i]);
		}

		for (int i = 0; i < fontSizes.length; i++) {
			fontSizeSelect.addItem(fontSizes[i]);
		}
		backgroundSelect.setNullSelectionAllowed(false);
		backgroundSelect.setValue("aqua");
		backgroundSelect.setImmediate(true);

		foregroundSelect.setNullSelectionAllowed(false);
		foregroundSelect.setValue("aqua");
		foregroundSelect.setImmediate(true);

		fontSelect.setNullSelectionAllowed(false);
		fontSelect.setValue("Arial");
		fontSelect.setImmediate(true);

		fontSizeSelect.setNullSelectionAllowed(false);
		fontSizeSelect.setValue("12");
		fontSizeSelect.setImmediate(true);
	}

	/**
	 * Returns the horizontal layout for the CSS panel.
	 * 
	 * @return schemeHorizontalLayout
	 */
	public HorizontalLayout getSchemeHorizontalLayout() {
		return schemeHorizontalLayout;
	}

	/**
	 * Returns the text area.
	 * 
	 * @return textArea
	 */
	public TextField getTenantDescription() {
		return textArea;
	}

	/**
	 * Gets the background select
	 * 
	 * @return the backgroundSelect
	 */
	public NativeSelect getBackgroundSelect() {
		return backgroundSelect;
	}

	/**
	 * Gets the foreground select
	 * 
	 * @return the foregroundSelect
	 */
	public NativeSelect getForegroundSelect() {
		return foregroundSelect;
	}

	/**
	 * Gets the font select
	 * 
	 * @return the fontSelect
	 */
	public NativeSelect getFontSelect() {
		return fontSelect;
	}

	/**
	 * Returns the upload
	 * 
	 * @return logoUpload
	 */
	public Upload getUpload() {
		return logoUpload;
	}

	/**
	 * Returns the css text field
	 * 
	 * @return the cssTextField
	 */
	public TextField getCssTextField() {
		return cssTextArea;
	}

	/**
	 * Gets the scheme panel
	 * 
	 * @return the schemePanel
	 */
	public Panel getSchemePanel() {
		return schemePanel;
	}

	/**
	 * Gets the font size select
	 * 
	 * @return the fontSizeSelect
	 */
	public NativeSelect getFontSizeSelect() {
		return fontSizeSelect;
	}

	/**
	 * Gets the show advanced options button
	 * 
	 * @return the showAdvancedOptionsButton
	 */
	public Button getShowAdvancedOptionsButton() {
		return showAdvancedOptionsButton;
	}
}
