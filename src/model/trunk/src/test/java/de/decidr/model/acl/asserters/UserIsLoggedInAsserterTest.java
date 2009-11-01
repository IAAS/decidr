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

package de.decidr.model.acl.asserters;

import static org.junit.Assert.*;

import org.junit.Test;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.testing.DecidrAclTest;

/**
 * GH: add comment
 *
 * @author GH
 */
public class UserIsLoggedInAsserterTest extends DecidrAclTest {

    /**
     * Test method for {@link UserIsLoggedInAsserter#assertRule(Role, Permission)}.
     */
    @Test
    public void testAssertRule() {
        UserIsLoggedInAsserter asserter = new UserIsLoggedInAsserter();
        assertTrue(asserter.assertRule(new SuperAdminRole(DecidrGlobals.getSettings().getSuperAdmin().getId()), new Permission("*")));
        assertFalse(asserter.assertRule(new SuperAdminRole(), new Permission("*")));
    }
}
