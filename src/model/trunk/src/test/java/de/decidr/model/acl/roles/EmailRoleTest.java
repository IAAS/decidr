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
 * TODO: add comment
 *
 * @author GH
 */
public class EmailRoleTest extends DecidrAclTest {

    /**
     * Test method for {@link de.decidr.model.acl.roles.EmailRole#getInstance()}.
     */
    @Test
    public void testGetInstance() {
        Role role = EmailRole.getInstance();
        Role role2 = null;
        
        assertNotNull(role);
        
        role2 = EmailRole.getInstance();
        assertEquals(role, role2);
    }

}
