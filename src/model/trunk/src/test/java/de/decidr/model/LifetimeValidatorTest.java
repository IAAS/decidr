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

import static org.junit.Assert.fail;
import static org.junit.Assert.*;

import org.hibernate.Session;
import org.junit.Test;

import de.decidr.model.entities.ChangeEmailRequest;
import de.decidr.model.entities.Invitation;
import de.decidr.model.entities.PasswordResetRequest;
import de.decidr.model.entities.RegistrationRequest;

/**
 * RR: add comment
 * 
 * @author Reinhold
 */
public class LifetimeValidatorTest extends AbstractDatabaseTest {

    /**
     * Test method for
     * {@link LifetimeValidator#isPasswordResetRequestValid(PasswordResetRequest, Session)}
     * .
     */
    @Test
    public void testIsPasswordResetRequestValid() {
        Session session = null;
        PasswordResetRequest validRequest = null;
        PasswordResetRequest invalidRequest = null;

        // RR create hibernate session

        // RR create valid password request
        assertTrue(LifetimeValidator.isPasswordResetRequestValid(validRequest,
                session));

        // RR create invalid password request
        assertFalse(LifetimeValidator.isPasswordResetRequestValid(invalidRequest,
                session));
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link LifetimeValidator#isInvitationValid(Invitation)}.
     */
    @Test
    public void testIsInvitationValid() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for
     * {@link LifetimeValidator#isRegistrationRequestValid(RegistrationRequest)}
     * .
     */
    @Test
    public void testIsRegistrationRequestValid() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for
     * {@link LifetimeValidator#isChangeEmailRequestValid(ChangeEmailRequest)}.
     */
    @Test
    public void testIsChangeEmailRequestValid() {
        fail("Not yet implemented"); // RR
    }
}
