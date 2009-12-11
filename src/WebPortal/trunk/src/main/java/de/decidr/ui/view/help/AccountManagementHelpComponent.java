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

package de.decidr.ui.view.help;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;

/**
 * This component is a part of the integrated manual and contains information
 * related to account management.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2356", currentReviewState = State.PassedWithComments)
public class AccountManagementHelpComponent extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    private Button registerAsTenantAdminButton = null;
    private Label registerAsTenantAdminLabel = null;

    private Button registerAsUserButton = null;
    private Label registerAsUserLabel = null;

    private Button changeEmailButton = null;
    private Label changeEmailLabel = null;

    private Button changePasswordButton = null;
    private Label changePasswordLabel = null;

    private Button changeAddressButton = null;
    private Label changeAddressLabel = null;

    private Button createNewTenantButton = null;
    private Label createNewTenantLabel = null;

    private Button switchTenantButton = null;
    private Label switchTenantLabel = null;

    private Button leaveTenantButton = null;
    private Label leaveTenantLabel = null;

    private Button changeStatusButton = null;
    private Label changeStatusLabel = null;

    public AccountManagementHelpComponent() {
        setMargin(false, true, true, true);

        registerAsTenantAdminLabel = new Label(
                "<ol>\n"
                        + "\t<li>Follow the registration link to see "
                        + "the registration form in your browser.</li>\n"
                        + "\t<li>Choose to create a new tenant along with the account.</li>\n"
                        + "\t<li>Fill in the data required to proceed (e.g. email address"
                        + " and additional address data).</li>\n"
                        + "\t<li>Please make sure that your email address is valid, "
                        + "because it is required for the activation of your account.</li>\n"
                        + "\t<li>Press the &quot;Continue&quot; button.</li>\n"
                        + "\t<li>You will receive a confirmation email</li>\n</ol>\n<br/>"
                        + "Possible problems:<br/>"
                        + "You can't register, because your data is invalid."
                        + " This can have several reasons:\n<ul>\n"
                        + "\t<li>Your username/tenant name/email address are already in use."
                        + " Please check your data and try again.</li>\n"
                        + "\t<li>Your username or tenant name are not long enough."
                        + " Please make sure that your username or tenant name contain more"
                        + " than 3 characters.</li>\n</ul>\n<br/>",
                Label.CONTENT_RAW);
        registerAsTenantAdminLabel.setVisible(false);
        registerAsTenantAdminButton = new Button(
                "How do I register as a tenant admin?", new ToggleLabelAction(
                        registerAsTenantAdminLabel));
        registerAsTenantAdminButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(registerAsTenantAdminButton);
        this.addComponent(registerAsTenantAdminLabel);

        registerAsUserLabel = new Label(
                "<ol>\n"
                        + "\t<li>Follow the registration link to see "
                        + "the registration form in your browser.</li>\n"
                        + "\t<li>Fill in the data required to proceed (e.g. email address"
                        + " and additional address data).</li>\n"
                        + "\t<li>Please make sure that your email address is valid, "
                        + "because it is required for the activation of your account.</li>\n"
                        + "\t<li>Press the &quot;Continue&quot; button.</li>\n"
                        + "\t<li>You will receive a confirmation mail</li>\n</ol>\n<br/>"
                        + "Possible problems:<br/>"
                        + "You can't register, because your data is invalid."
                        + " This can have several reasons:\n<ul>\n"
                        + "\t<li>Your username/email address are already in use."
                        + " Please check your data and try again.</li>\n"
                        + "\t<li>Your username is not long enough."
                        + " Please make sure that your username contains more"
                        + " than 3 characters.</li>\n</ul>\n<br/>",
                Label.CONTENT_XHTML);
        registerAsUserLabel.setVisible(false);
        registerAsUserButton = new Button(
                "How do I register as a normal user?", new ToggleLabelAction(
                        registerAsUserLabel));
        registerAsUserButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(registerAsUserButton);
        this.addComponent(registerAsUserLabel);

        changeEmailLabel = new Label("<ol>\n"
                + "\t<li>Log into DecidR and navigate to the "
                + "&quot;Profile Page&quot; using the navigation bar.</li>\n"
                + "\t<li>The profile page will display your data.</li>\n"
                + "\t<li>Click on the &quot;Change Email&quot; link.</li>\n"
                + "\t<li>Type your new email address "
                + "and confirm it.</li>\n</ol>\n<br/>", Label.CONTENT_XHTML);
        changeEmailLabel.setVisible(false);
        changeEmailButton = new Button("How do I change my email address?",
                new ToggleLabelAction(changeEmailLabel));
        changeEmailButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(changeEmailButton);
        this.addComponent(changeEmailLabel);

        changePasswordLabel = new Label("<ol>\n"
                + "\t<li>Log into DecidR and navigate to the "
                + "&quot;Profile Page&quot; using the navigation bar.</li>\n"
                + "\t<li>The profile page will display your data.</li>\n"
                + "\t<li>Click on the &quot;Change password&quot; link.</li>\n"
                + "\t<li>The page will display a new window where you can"
                + " enter your old and desired new passwords.</li>\n"
                + "</ol>\n<br/>", Label.CONTENT_XHTML);
        changePasswordLabel.setVisible(false);
        changePasswordButton = new Button("How do I change my password?",
                new ToggleLabelAction(changePasswordLabel));
        changePasswordButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(changePasswordButton);
        this.addComponent(changePasswordLabel);

        changeAddressLabel = new Label("<ol>\n"
                + "\t<li>Log into DecidR and navigate to the "
                + "&quot;Profile Page&quot; using the navigation bar.</li>\n"
                + "\t<li>The profile page will display your data.</li>\n"
                + "\t<li>Change the entries for &quot;First name&quot;, "
                + "&quot;Surname&quot;, &quot;Street&quot;, "
                + "&quot;Postal code&quot; and &quot;City&quot;.</li>\n"
                + "\t<li>Click on the &quot;Save&quot; button.</li>\n"
                + "</ol>\n<br/>", Label.CONTENT_XHTML);
        changeAddressLabel.setVisible(false);
        changeAddressButton = new Button("How do I change my address data?",
                new ToggleLabelAction(changeAddressLabel));
        changeAddressButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(changeAddressButton);
        this.addComponent(changeAddressLabel);

        createNewTenantLabel = new Label("<ol>\n"
                + "\t<li>Log into DecidR and navigate to "
                + "&quot;Create Tenant&quot; using the navigation bar.</li>\n"
                + "\t<li>Enter the tenant information.</li>\n"
                + "\t<li>Click on the &quot;Save&quot; button.</li>\n"
                // aleks, gh: No error message or anything? Nothing happens? ~rr
                // RR please ask TK, he wrote all these texts
                + "\t<li>If the tenant isn't already registered you are not "
                + "the tenant admin of the newly created tenant</li>\n"
                + "</ol>\n<br/>", Label.CONTENT_XHTML);
        createNewTenantLabel.setVisible(false);
        createNewTenantButton = new Button("How do I create a new tenant?",
                new ToggleLabelAction(createNewTenantLabel));
        createNewTenantButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(createNewTenantButton);
        this.addComponent(createNewTenantLabel);

        switchTenantLabel = new Label("<ol>\n"
                + "\t<li>Log into DecidR and navigate to "
                + "&quot;Change tenant&quot; using the navigation bar.</li>\n"
                + "\t<li>The page will display a list of your tenants.</li>\n"
                + "\t<li>Select the tenant you want to switch to and "
                + "press the &quot;Switch&quot; button.</li>\n"
                + "</ol>\n<br/>", Label.CONTENT_XHTML);
        switchTenantLabel.setVisible(false);
        switchTenantButton = new Button("How do I switch tenants?",
                new ToggleLabelAction(switchTenantLabel));
        switchTenantButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(switchTenantButton);
        this.addComponent(switchTenantLabel);

        leaveTenantLabel = new Label("Please make that you are not the tenant "
                + "admin nor participating in a particular workflow. "
                + "Otherwise you cannot leave the tenant.<ol>\n"
                + "\t<li>Log into DecidR and navigate to the "
                + "&quot;Profile Page&quot; using the navigation"
                + " bar.</li>\n"
                + "\t<li>The profile page will display your data."
                + " Click on the &quot;Leave tenant&quot; link.</li>\n"
                + "\t<li>The page will display a list of tenants you "
                + "joined. Choose the tenant you want to leave "
                + "and confirm with the &quot;OK&quot; button."
                + "</li>\n</ol>\n<br/>", Label.CONTENT_XHTML);
        leaveTenantLabel.setVisible(false);
        leaveTenantButton = new Button("How do I leave a tenant?",
                new ToggleLabelAction(leaveTenantLabel));
        leaveTenantButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(leaveTenantButton);
        this.addComponent(leaveTenantLabel);

        changeStatusLabel = new Label("<ol>\n"
                + "\t<li>Log into DecidR and navigate to the "
                + "&quot;Profile Page&quot; using the navigation bar.</li>\n"
                + "\t<li>Check or uncheck the checkbox "
                + "&quot;Set my status to unavailable&quot;.</li>\n"
                + "\t<li>Click on the &quot;Save&quot; button.</li>\n"
                + "</ol>\n<br/>", Label.CONTENT_XHTML);
        changeStatusLabel.setVisible(false);
        changeStatusButton = new Button(
                "How do I change my status to (un)available?",
                new ToggleLabelAction(changeStatusLabel));
        changeStatusButton.setStyleName(Button.STYLE_LINK);

        this.addComponent(changeStatusButton);
        this.addComponent(changeStatusLabel);
    }
}
