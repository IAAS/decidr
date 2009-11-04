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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.decidr.model.facades.FileFacade;
import de.decidr.model.testing.DecidrAclTest;

/**
 * Test case for <code>{@link FileReplacePermission}</code>.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class FileReplacePermissionTest extends DecidrAclTest {

    /**
     * Test method for {@link EntityPermission#getId()}.
     */
    @Test
    public void testGetId() {
        FileReplacePermission filePerm = new FileReplacePermission(1l);
        assertTrue(filePerm.getId() == 1l);

        filePerm = new FileReplacePermission(0l);
        assertTrue(filePerm.getId() == 0l);

        filePerm = new FileReplacePermission(-1l);
        assertTrue(filePerm.getId() == -1l);
    }
}
