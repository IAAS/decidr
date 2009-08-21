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

import de.decidr.model.TransactionTest;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.SuperAdminRole;

/**
 * Test case for <code>{@link WorkItemFacade}</code>.
 * 
 * @author Reinhold
 */
public class WorkItemFacadeTest extends TransactionTest {

    static WorkItemFacade adminFacade;
    static WorkItemFacade userFacade;
    static WorkItemFacade nullFacade;

    /**
     * Initialises the facade instances.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        adminFacade = new WorkItemFacade(new SuperAdminRole());
        userFacade = new WorkItemFacade(new BasicRole(0L));
        nullFacade = new WorkItemFacade(null);
    }

    /**
     * Test method for {@link WorkItemFacade#getWorkItem(Long)}.
     */
    @Test
    public void testGetWorkItem() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for
     * {@link WorkItemFacade#createWorkItem(Long, Long, String, String, String, byte[], Boolean)}
     * .
     */
    @Test
    public void testCreateWorkItem() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link WorkItemFacade#setData(Long, byte[])}.
     */
    @Test
    public void testSetData() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link WorkItemFacade#setDataAndMarkAsDone(Long, byte[])}
     * .
     */
    @Test
    public void testSetDataAndMarkAsDone() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link WorkItemFacade#markWorkItemAsDone(Long)}.
     */
    @Test
    public void testMarkWorkItemAsDone() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link WorkItemFacade#deleteWorkItem(Long)}.
     */
    @Test
    public void testDeleteWorkItem() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for
     * {@link WorkItemFacade#getWorkItemAndMarkAsInProgress(Long)}.
     */
    @Test
    public void testGetWorkItemAndMarkAsInProgress() {
        fail("Not yet implemented"); // RR
    }
}
