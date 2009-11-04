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

package de.decidr.model.acl.roles;

import static org.junit.Assert.*;

import org.junit.Test;

import de.decidr.model.testing.DecidrAclTest;

/**
 * Test case for <code>{@link TenantAdminRole}</code>.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class TenantAdminRoleTest extends DecidrAclTest {

    /**
     * Test method for {@link TenantAdminRole#TenantAdminRole(Long)}.
     */
    @Test
    public void testTenantAdminRoleLong() {
        TenantAdminRole role = new TenantAdminRole(1l);

        assertNotNull(role);
        assertTrue(role.getActorId() == 1l);

        role = new TenantAdminRole(0l);
        assertTrue(role.getActorId() == 0l);

        role = new TenantAdminRole(-1l);
        assertTrue(role.getActorId() == -1l);
    }

    /**
     * Test method for {@link TenantAdminRole#TenantAdminRole()}.
     */
    @Test
    public void testTenantAdminRole() {
        TenantAdminRole role = new TenantAdminRole();

        assertNotNull(role);
        assertTrue(role.getActorId() == UserRole.UNKNOWN_USER_ID);
    }
}
