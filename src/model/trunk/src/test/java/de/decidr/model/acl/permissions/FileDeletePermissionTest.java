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

package de.decidr.model.acl.permissions;

import static org.junit.Assert.*;

import org.junit.Test;

import de.decidr.model.testing.DecidrAclTest;

/**
 * TODO: add comment
 *
 * @author GH
 */
public class FileDeletePermissionTest extends DecidrAclTest {

    /**
     * Test method for {@link FileDeletePermission#FileDeletePermission(java.lang.Long)}.
     */
    @Test
    public void testFileDeletePermission() {
        FileDeletePermission filePerm = new FileDeletePermission(1l);
        assertNotNull(filePerm);
        assertTrue(filePerm.getName().endsWith("delete1"));
        
        filePerm = new FileDeletePermission(0l);
        assertTrue(filePerm.getName().endsWith("delete0")); 
        
        filePerm = new FileDeletePermission(-1l);
        assertTrue(filePerm.getName().endsWith("delete-1"));
    }

    /**
     * Test method for {@link EntityPermission#getId()}.
     */
    @Test
    public void testGetId() {
        FileDeletePermission filePerm = new FileDeletePermission(1l);
        assertTrue(filePerm.getId() == 1l);

        filePerm = new FileDeletePermission(0l);
        assertTrue(filePerm.getId() == 0l);
        
        filePerm = new FileDeletePermission(-1l);
        assertTrue(filePerm.getId() == -1l);
    }

}
