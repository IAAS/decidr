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

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.CommandsTest;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.workitem.CreateWorkItemCommand;
import de.decidr.model.commands.workitem.DeleteWorkItemCommand;
import de.decidr.model.commands.workitem.GetWorkItemCommand;
import de.decidr.model.commands.workitem.SetDataCommand;
import de.decidr.model.commands.workitem.SetStatusCommand;
import de.decidr.model.commands.workitem.WorkItemCommand;
import de.decidr.model.enums.WorkItemStatus;

/**
 * This class tests the commands in
 * <code>de.decidr.model.commands.workitem</code>.
 * 
 * @author Reinhold
 */
public class WorkItemCommandsTest extends CommandsTest {

    @BeforeClass
    public static void disable() {
        fail("This test class has not yet been implemented");
    }

    /**
     * Test method for
     * {@link CreateWorkItemCommand#CreateWorkItemCommand(Role, Long, Long, String, String, String, byte[], Boolean)}
     * .
     */
    @Test
    public void testCreateWorkItemCommand() {
        fail("Not yet implemented"); // RR CreateWorkItemCommand
    }

    /**
     * Test method for
     * {@link DeleteWorkItemCommand#DeleteWorkItemCommand(Role, Long)}.
     */
    @Test
    public void testDeleteWorkItemCommand() {
        fail("Not yet implemented"); // RR DeleteWorkItemCommand
    }

    /**
     * Test method for {@link GetWorkItemCommand#GetWorkItemCommand(Role, Long)}
     * .
     */
    @Test
    public void testGetWorkItemCommand() {
        fail("Not yet implemented"); // RR GetWorkItemCommand
    }

    /**
     * Test method for {@link SetDataCommand#SetDataCommand(Role, Long, byte[])}
     * .
     */
    @Test
    public void testSetDataCommand() {
        fail("Not yet implemented"); // RR SetDataCommand
    }

    /**
     * Test method for
     * {@link SetStatusCommand#SetStatusCommand(Role, Long, WorkItemStatus)}.
     */
    @Test
    public void testSetStatusCommand() {
        fail("Not yet implemented"); // RR SetStatusCommand
    }

    /**
     * Test method for {@link WorkItemCommand#WorkItemCommand(Role, Long)}.
     */
    @Test
    public void testWorkItemCommand() {
        fail("Not yet implemented"); // RR WorkItemCommand
    }
}
