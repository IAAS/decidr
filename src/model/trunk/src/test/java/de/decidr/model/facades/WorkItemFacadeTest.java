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

import de.decidr.model.DatabaseTestsuite;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.SuperAdminRole;

/**
 * Test case for <code>{@link WorkItemFacade}</code>.
 * 
 * @author Reinhold
 */
public class WorkItemFacadeTest {

    static WorkItemFacade adminFacade;
    static WorkItemFacade userFacade;
    static WorkItemFacade nullFacade;

    /**
     * Initialises the facade instances.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        if (!DatabaseTestsuite.running()) {
            fail("Needs to run inside " + DatabaseTestsuite.class.getName());
        }

        adminFacade = new WorkItemFacade(new SuperAdminRole());
        userFacade = new WorkItemFacade(new BasicRole(0L));
        nullFacade = new WorkItemFacade(null);
    }

    /**
     * Test method for
     * {@link WorkItemFacade#createWorkItem(Long, Long, String, String, String, byte[], Boolean)}
     * , {@link WorkItemFacade#getWorkItem(Long)},
     * {@link WorkItemFacade#setData(Long, byte[])},
     * {@link WorkItemFacade#setDataAndMarkAsDone(Long, byte[])},
     * {@link WorkItemFacade#getWorkItemAndMarkAsInProgress(Long)},
     * {@link WorkItemFacade#markWorkItemAsDone(Long)} and
     * {@link WorkItemFacade#deleteWorkItem(Long)}.
     */
    @Test
    public void testWorkItem() {
        fail("Not yet implemented"); // RR createWorkItem
        fail("Not yet implemented"); // RR getWorkItem
        fail("Not yet implemented"); // RR setData
        fail("Not yet implemented"); // RR setDataAndMarkAsDone
        fail("Not yet implemented"); // RR getWorkItemAndMarkAsInProgress
        fail("Not yet implemented"); // RR markWorkItemAsDone
        fail("Not yet implemented"); // RR deleteWorkItem
    }
}
