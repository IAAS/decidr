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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.ui.controller.UploadAction;
import de.decidr.ui.controller.tenant.RestoreDefaultTenantSettingsAction;
import de.decidr.ui.controller.tenant.SaveTenantSettingsAction;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * The tenant can change his settings. He can change his theme by choosing a
 * given background or font or he can enter his own CSS and upload it.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "TK", "JS" }, lastRevision = "2377", currentReviewState = State.Passed)
public class TenantSettingsComponent extends CustomComponent {

    private static final long serialVersionUID = -7841061147447361631L;

    private VerticalLayout verticalLayout = null;
    private HorizontalLayout browseHorizontalLayout = null;
    private HorizontalLayout buttonHorizontalLayout = null;
    private HorizontalLayout schemeHorizontalLayout = null;

    private Panel browsePanel = null;
    private Panel schemePanel = null;
    private Panel buttonPanel = null;

    private Label tenantSettingsLabel = null;

    private UploadComponent logoUpload = null;

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

    private String tenantDescription;
    private String tenantName;
    private String logoFileName = "logo.png";
    private String css = "";

    private Long logoId;
    private Long tenantId;

    private Role role;
    private TenantFacade tenantFacade = null;

    /**
     * Default constructor.
     * 
     */
    public TenantSettingsComponent(Long tenantId) {
        this.tenantId = tenantId;
        role = (Role) Main.getCurrent().getSession().getAttribute("role");
        tenantFacade = new TenantFacade(role);

        tenantDescription = "";
        try {
            tenantName = tenantFacade.getTenant(tenantId).getName();
            if (tenantFacade.getTenant(tenantId).getDescription() != null) {
                tenantDescription = tenantFacade.getTenant(tenantId)
                        .getDescription();
            }
            logoId = tenantFacade.getTenant(tenantId).getLogo().getId();
        } catch (TransactionException e) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(e));
        }
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

        logoUpload = new UploadComponent(logoId, new UploadAction());

        saveButton = new Button("Save", new SaveTenantSettingsAction());
        restoreDefaultSettingsButton = new Button("Restore Default Settings",
                new RestoreDefaultTenantSettingsAction());

        logoEmbedded = new Embedded("",
                new ThemeResource("img/" + logoFileName));
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
        try {
            InputStream in = tenantFacade.getCurrentColorScheme(tenantId);
            StringBuilder sb = new StringBuilder();
            if (in != null) {

                String line;

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                css = sb.toString();
            }
            showBasicOptionsButton = new Button("Show basic options");
            showBasicOptionsButton.addListener(new Button.ClickListener() {

                /**
                 * Serial version uid
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(ClickEvent event) {
                    changeToBasic();
                }

            });
            cssTextArea = new TextField();
            cssTextArea.setRows(20);
            cssTextArea.setColumns(30);
            cssTextArea.setValue(css);
            getSchemeHorizontalLayout().removeAllComponents();
            getSchemeHorizontalLayout().addComponent(cssTextArea);
            getSchemeHorizontalLayout().addComponent(showBasicOptionsButton);
        } catch (TransactionException e) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(e));
        } catch (UnsupportedEncodingException e) {
            Main.getCurrent().getMainWindow().addWindow(
                    new InformationDialogComponent(
                            "Failed to encode the css file", "Failure"));
        } catch (IOException e) {
            Main.getCurrent().getMainWindow().addWindow(
                    new InformationDialogComponent(
                            "Failed to load the css file", "Failure"));
        }

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
        fontSizeSelect = new NativeSelect("Font Size");

        fillSelects();

        showAdvancedOptionsButton = new Button("Show advanced options");
        showAdvancedOptionsButton.setSwitchMode(true);
        showAdvancedOptionsButton.addListener(new Button.ClickListener() {

            /**
             * Serial version uid
             */
            private static final long serialVersionUID = 1L;

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
