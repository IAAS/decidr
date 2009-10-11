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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.decidr.model.testing.DecidrAclTest;

/**
 * TODO: add comment
 *
 * @author GH
 */
public class BasicRoleTest extends DecidrAclTest {

    /**
     * Test method for {@link BasicRole#BasicRole(Long)}.
     */
    @Test
    public void testBasicRole() {
        BasicRole role = new BasicRole(1l);
        
        assertNotNull(role);
        
        role = new BasicRole(null);
        assertNotNull(role);
    }

    /**
     * Test method for {@link BasicRole#getActorId()}.
     */
    @Test
    public void testGetActorId() {
        BasicRole role = new BasicRole(1l);
        assertTrue(role.getActorId() == 1l);

        role = new BasicRole(0l);
        assertTrue(role.getActorId() == 0l);

        role = new BasicRole(-1l);
        assertTrue(role.getActorId() == -1l);

        role = new BasicRole(null);
        assertTrue(role.getActorId() == null);
    }

    /**
     * Test method for {@link BasicRole#equals(Object)}.
     */
    @Test
    public void testEqualsObject() {
        BasicRole role1 = new BasicRole(1l);
        BasicRole role2 = new BasicRole(0l);
        UserRole role3 = new UserRole();
        
        assertTrue(role1.equals(new BasicRole(1l)));
        assertTrue(role1.equals(role2));
        assertFalse(role1.equals(role3));
        assertFalse(role3.equals(role1));
        
    }

}
