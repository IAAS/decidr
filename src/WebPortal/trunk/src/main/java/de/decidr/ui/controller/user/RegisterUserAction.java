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

package de.decidr.ui.controller.user;

import com.vaadin.ui.Form;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.controller.UIDirector;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.RegisterUserComponent;
import de.decidr.ui.view.SiteFrame;
import de.decidr.ui.view.WelcomePageComponent;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This action creates a new user.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2342", currentReviewState = State.PassedWithComments)
public class RegisterUserAction implements ClickListener {

    private static final long serialVersionUID = 1L;
    private UIDirector uiDirector = Main.getCurrent().getUIDirector();
    private SiteFrame siteFrame = uiDirector.getTemplateView();

    private Role role = (Role) Main.getCurrent().getSession().getAttribute(
            "role");

    private UserFacade userFacade = new UserFacade(role);

    private RegisterUserComponent content = null;

    private UserProfile userProfile = null;

    @Override
    public void buttonClick(ClickEvent event) {
        content = (RegisterUserComponent) Main.getCurrent().getUIDirector()
                .getTemplateView().getContent();

        Form form = content.getRegistrationForm();
        boolean notEmpty = true;

        // GH, Aleks: *English* comments!!!
        // Geht die einzelnen Felder durch und prüft ob die Felder nicht leer
        // (notEmpty) sind.
        // Sobald ein Feld leer ist, wird notEmpty auf false gesetzt. Dabei
        // werden nur die Felder durchgegangen, die
        // required sind und deren Wert leer ist.
        for (Object propertyId : content.getRegistrationForm()
                .getItemPropertyIds()) {
            if (notEmpty) {
                if (form.getField(propertyId).isRequired()
                        && form.getField(propertyId).getValue().toString()
                                .equals("")) {
                    notEmpty = false;
                }
            }
        }

        // GH, Aleks: *English* comments!!!
        // Hier wird nun geschaut ob die Felder leer sind. Wenn sie nicht leer
        // sind, dann
        // speichere die Einträge und registriere den User. Wenn die Felder leer
        // sind,
        // dann wird der User aufgefordert die nötigen Felder auszufüllen.
        if (notEmpty) {
            content.saveRegistrationForm();

            try {
                userFacade.registerUser(content.getRegistrationForm()
                        .getItemProperty("email").getValue().toString(),
                        content.getRegistrationForm().getItemProperty(
                                "password").getValue().toString(),
                        fillUserProfile());
                Main.getCurrent().getMainWindow().addWindow(
                        new InformationDialogComponent(
                                "User successfully registered!",
                                "Registration successful"));
                siteFrame.setContent(new WelcomePageComponent());
            } catch (NullPointerException e) {
                Main
                        .getCurrent()
                        .getMainWindow()
                        .addWindow(new InformationDialogComponent(
                        // Aleks, GH: what about providing a useful error
                                // message? Sounds fun, no? ~rr
                                "Null selection not allowed", "Null selection"));
            } catch (TransactionException e) {
                Main.getCurrent().getMainWindow().addWindow(
                        new TransactionErrorDialogComponent(e));
            }
        } else {
            Main.getCurrent().getMainWindow().addWindow(
                    new InformationDialogComponent(
                            "Please enter the required information!",
                            "Missing Information"));
        }
    }

    private UserProfile fillUserProfile() {
        userProfile = new UserProfile();
        userProfile.setFirstName(content.getRegistrationForm().getItemProperty(
                "firstName").getValue().toString());
        userProfile.setLastName(content.getRegistrationForm().getItemProperty(
                "lastName").getValue().toString());
        userProfile.setCity(content.getRegistrationForm().getItemProperty(
                "city").getValue().toString());
        userProfile.setPostalCode(content.getRegistrationForm()
                .getItemProperty("postalCode").getValue().toString());
        userProfile.setStreet(content.getRegistrationForm().getItemProperty(
                "street").getValue().toString());
        userProfile.setUsername(content.getRegistrationForm().getItemProperty(
                "userName").getValue().toString());
        return userProfile;
    }
}