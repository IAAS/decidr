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

import java.util.Collection;

import org.junit.Test;

import de.decidr.model.CommandsTest;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.workflowinstance.DeleteWorkflowInstanceCommand;
import de.decidr.model.commands.workflowinstance.GetAllWorkitemsCommand;
import de.decidr.model.commands.workflowinstance.GetParticipatingUsersCommand;
import de.decidr.model.commands.workflowinstance.RemoveAllWorkItemsCommand;
import de.decidr.model.commands.workflowinstance.WorkflowInstanceCommand;

/**
 * This class tests the commands in
 * <code>de.decidr.model.commands.workflowinstance</code>. Some of the methods
 * can't be tested properly within the confines of a unit test, as they interact
 * with web services.
 * 
 * @author Reinhold
 */
public class WorkflowInstanceCommandsTest extends CommandsTest {

    /**
     * Test method for
     * {@link DeleteWorkflowInstanceCommand#DeleteWorkFlowInstanceCommand(Role, Long)}
     * .
     */
    @Test
    public void testDeleteWorkFlowInstanceCommand() {
        fail("Not yet implemented"); // RR DeleteWorkFlowInstanceCommand
    }

    /**
     * Test method for
     * {@link GetAllWorkitemsCommand#GetAllWorkitemsCommand(Role, Long)}.
     */
    @Test
    public void testGetAllWorkitemsCommand() {
        fail("Not yet implemented"); // RR GetAllWorkitemsCommand
    }

    /**
     * Test method for
     * {@link GetParticipatingUsersCommand#GetParticipatingUsersCommand(Role, Long)}
     * .
     */
    @Test
    public void testGetParticipatingUsersCommand() {
        fail("Not yet implemented"); // RR GetParticipatingUsersCommand
    }

    /**
     * Test method for
     * {@link RemoveAllWorkItemsCommand#RemoveAllWorkItemsCommand(Role, String, Long)}
     * .
     */
    @Test
    public void testRemoveAllWorkItemsCommand() {
        fail("Not yet implemented"); // RR RemoveAllWorkItemsCommand
    }

    /**
     * Test method for
     * {@link WorkflowInstanceCommand#WorkflowInstanceCommand(Role, Collection, Long)}
     * .
     */
    @Test
    public void testWorkflowInstanceCommand() {
        fail("Not yet implemented"); // RR WorkflowInstanceCommand
    }
}
