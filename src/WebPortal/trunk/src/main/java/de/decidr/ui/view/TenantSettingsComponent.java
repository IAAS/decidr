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

import de.decidr.ui.controller.CssHandler;
import de.decidr.ui.controller.tenant.RestoreDefaultTenantSettingsAction;
import de.decidr.ui.controller.tenant.SaveTenantSettingsAction;
import de.decidr.ui.controller.tenant.UploadTenantLogoAction;

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
	private Button cancelButton = null;
	private Button restoreDefaultSettingsButton = null;
	private Button showAdvancedOptionsButton = null;
	private Button showBasicOptionsButton = null;
	private NativeSelect backgroundSelect = null;
	private NativeSelect foregroundSelect = null;
	private NativeSelect fontSizeSelect = null;
	private NativeSelect fontSelect = null;
	String name = "logo.png";
	private CssHandler fileReader = null;

	private Embedded logoEmbedded = null;

	final String[] colors = new String[] { "aqua", "black", "blue", "fuchsia",
			"gray", "green", "lime", "maroon", "navy", "olive", "purple",
			"red", "silver", "teal", "white", "yellow" };

	final String[] fontSizes = new String[] { "9", "10", "11", "12", "13",
			"14", "15", "16" };

	final String[] fonts = new String[] { "Times New Roman", "Arial",
			"Courier New", "Verdana" };

	/**
	 * Default constructor.
	 * 
	 */
	public TenantSettingsComponent() {
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

		tenantSettingsLabel = new Label("<h2>Tenant Settings</h2>");
		tenantSettingsLabel.setContentMode(Label.CONTENT_XHTML);

		textArea = new TextField();
		textArea.setRows(10);
		textArea.setColumns(30);
		textArea.setCaption("Description");

		logoUpload = new Upload("Upload Logo", new UploadTenantLogoAction());
		logoUpload.setButtonCaption("Upload Logo");

		saveButton = new Button("Save", new SaveTenantSettingsAction());
		cancelButton = new Button("Cancel");
		restoreDefaultSettingsButton = new Button("Restore default settings",
				new RestoreDefaultTenantSettingsAction());

		logoEmbedded = new Embedded("", new ThemeResource("img/decidrlogo.png"));
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
		buttonHorizontalLayout.addComponent(cancelButton);
		buttonHorizontalLayout.addComponent(restoreDefaultSettingsButton);

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
