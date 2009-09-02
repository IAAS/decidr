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

package de.decidr.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.entities.ChangeEmailRequest;
import de.decidr.model.entities.Invitation;
import de.decidr.model.entities.PasswordResetRequest;
import de.decidr.model.entities.RegistrationRequest;
import de.decidr.model.testsuites.DatabaseTestSuite;

/**
 * Unit tests for <code>{@link LifetimeValidator}</code>.
 * 
 * @author Reinhold
 */
public class LifetimeValidatorTest {

    @BeforeClass
    public static final void setUp() {
        if (!DatabaseTestSuite.running()) {
            fail("Needs to run inside " + DatabaseTestSuite.class.getName());
        }
    }

    /**
     * Test method for
     * {@link LifetimeValidator#isPasswordResetRequestValid(PasswordResetRequest)}
     * .
     */
    @Test
    public void testIsPasswordResetRequestValid() {
        PasswordResetRequest validRequest = new PasswordResetRequest(null,
                DecidrGlobals.getTime().getTime(), null);
        PasswordResetRequest invalidRequest = new PasswordResetRequest(
                null,
                new Date(
                        DecidrGlobals.getTime().getTimeInMillis()
                                - (DecidrGlobals
                                        .getSettings()
                                        .getPasswordResetRequestLifetimeSeconds() * 1000)
                                + 1), null);
        PasswordResetRequest emptyRequest = new PasswordResetRequest();

        assertTrue(LifetimeValidator.isPasswordResetRequestValid(validRequest));
        assertFalse(LifetimeValidator
                .isPasswordResetRequestValid(invalidRequest));
        assertFalse(LifetimeValidator.isPasswordResetRequestValid(emptyRequest));
    }

    /**
     * Test method for {@link LifetimeValidator#isInvitationValid(Invitation)}.
     */
    @Test
    public void testIsInvitationValid() {
        Invitation validRequest = new Invitation(null, null, DecidrGlobals
                .getTime().getTime());
        Invitation invalidRequest = new Invitation(null, null, new Date(
                DecidrGlobals.getTime().getTimeInMillis()
                        - (DecidrGlobals.getSettings()
                                .getInvitationLifetimeSeconds() * 1000) + 1));
        Invitation emptyRequest = new Invitation();

        assertTrue(LifetimeValidator.isInvitationValid(validRequest));
        assertFalse(LifetimeValidator.isInvitationValid(invalidRequest));
        assertFalse(LifetimeValidator.isInvitationValid(emptyRequest));
    }

    /**
     * Test method for
     * {@link LifetimeValidator#isRegistrationRequestValid(RegistrationRequest)}
     * .
     */
    @Test
    public void testIsRegistrationRequestValid() {
        RegistrationRequest validRequest = new RegistrationRequest(null,
                DecidrGlobals.getTime().getTime(), null);
        RegistrationRequest invalidRequest = new RegistrationRequest(null,
                new Date(DecidrGlobals.getTime().getTimeInMillis()
                        - (DecidrGlobals.getSettings()
                                .getInvitationLifetimeSeconds() * 1000) + 1),
                null);
        RegistrationRequest emptyRequest = new RegistrationRequest();

        assertTrue(LifetimeValidator.isRegistrationRequestValid(validRequest));
        assertFalse(LifetimeValidator
                .isRegistrationRequestValid(invalidRequest));
        assertFalse(LifetimeValidator.isRegistrationRequestValid(emptyRequest));
    }

    /**
     * Test method for
     * {@link LifetimeValidator#isChangeEmailRequestValid(ChangeEmailRequest)}.
     */
    @Test
    public void testIsChangeEmailRequestValid() {
        ChangeEmailRequest validRequest = new ChangeEmailRequest(null, null,
                DecidrGlobals.getTime().getTime(), null);
        ChangeEmailRequest invalidRequest = new ChangeEmailRequest(null, null,
                new Date(DecidrGlobals.getTime().getTimeInMillis()
                        - (DecidrGlobals.getSettings()
                                .getInvitationLifetimeSeconds() * 1000) + 1),
                null);
        ChangeEmailRequest emptyRequest = new ChangeEmailRequest();

        assertTrue(LifetimeValidator.isChangeEmailRequestValid(validRequest));
        assertFalse(LifetimeValidator.isChangeEmailRequestValid(invalidRequest));
        assertFalse(LifetimeValidator.isChangeEmailRequestValid(emptyRequest));
    }
}
