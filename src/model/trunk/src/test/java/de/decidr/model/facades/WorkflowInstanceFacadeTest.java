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

package de.decidr.model.facades;

import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.SuperAdminRole;

/**
 * Test case for <code>{@link WorkflowInstanceFacade}</code>.
 * 
 * @author Reinhold
 */
// RR create start WF instance?
// RR ensure order Model -> Instance -> Item?
public class WorkflowInstanceFacadeTest {

    static WorkflowInstanceFacade adminFacade;
    static WorkflowInstanceFacade userFacade;
    static WorkflowInstanceFacade nullFacade;

    /**
     * Initialises the facade instances.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        adminFacade = new WorkflowInstanceFacade(new SuperAdminRole());
        userFacade = new WorkflowInstanceFacade(new BasicRole(0L));
        nullFacade = new WorkflowInstanceFacade(null);
    }

    /**
     * Test method for
     * {@link WorkflowInstanceFacade#getParticipatingUsers(Long)},
     * {@link WorkflowInstanceFacade#stopWorkflowInstance(Long)} and
     * {@link WorkflowInstanceFacade#deleteWorkflowInstance(Long)}.
     */
    @Test
    public void testStopWorkflowInstance() {
        fail("Not yet implemented"); // RR getParticipatingUsers
        fail("Not yet implemented"); // RR stopWorkflowInstance
        fail("Not yet implemented"); // RR deleteWorkflowInstance
    }

    /**
     * Test method for {@link WorkflowInstanceFacade#getAllWorkItems(Long)} and
     * {@link WorkflowInstanceFacade#removeAllWorkItems(String, Long)}.
     */
    @Test
    public void testWorkItems() {
        fail("Not yet implemented"); // RR getAllWorkItems
        fail("Not yet implemented"); // RR removeAllWorkItems
    }
}
