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

package de.decidr.model.commands;

import static org.junit.Assert.fail;

import org.junit.Test;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.system.AddServerCommand;
import de.decidr.model.enums.ServerTypeEnum;

/**
 * This class tests the commands in <code>de.decidr.model.commands.system</code>
 * .
 * 
 * @author Reinhold
 */
public class SystemCommandsTest {

    /**
     * Test method for
     * {@link AddServerCommand#AddServerCommand(Role, ServerTypeEnum, String, Byte, Boolean, Boolean)}
     * .
     */
    @Test
    public void testAddServerCommand() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link AddServerCommand#getNewServer()}.
     */
    @Test
    public void testGetNewServer() {
        fail("Not yet implemented"); // R
    }
}
