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

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.filters.Paginator;
import de.decidr.model.testsuites.DatabaseTestSuite;

/**
 * Test case for <code>{@link WorkflowModelFacade}</code>. Some of the methods
 * can't be tested easily within the confines of a unit test, as they interact
 * with web services. These methods will be tested at a later point in time when
 * the most important test cases are written or during the system test.
 * 
 * @author Reinhold
 */
public class WorkflowModelFacadeTest {

    static WorkflowModelFacade adminFacade;
    static WorkflowModelFacade userFacade;
    static WorkflowModelFacade nullFacade;

    /**
     * Initialises the facade instances.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        if (!DatabaseTestSuite.running()) {
            fail("Needs to run inside " + DatabaseTestSuite.class.getName());
        }

        adminFacade = new WorkflowModelFacade(new SuperAdminRole());
        userFacade = new WorkflowModelFacade(new BasicRole(0L));
        nullFacade = new WorkflowModelFacade(null);
    }

    /**
     * Test method for
     * {@link WorkflowModelFacade#saveWorkflowModel(Long, String, String, byte[])}
     * .
     */
    @Test
    public void testSaveWorkflowModel() {
        fail("Not yet implemented"); // RR saveWorkflowModel
    }

    /**
     * Test method for {@link WorkflowModelFacade#getWorkflowModel(Long)}.
     */
    @Test
    public void testGetWorkflowModel() {
        fail("Not yet implemented"); // RR getWorkflowModel
    }

    /**
     * Test method for {@link WorkflowModelFacade#publishWorkflowModels(List)}.
     */
    @Test
    public void testPublishWorkflowModels() {
        fail("Not yet implemented"); // RR publishWorkflowModels
    }

    /**
     * Test method for {@link WorkflowModelFacade#unpublishWorkflowModels(List)}
     * .
     */
    @Test
    public void testUnpublishWorkflowModels() {
        fail("Not yet implemented"); // RR unpublishWorkflowModels
    }

    /**
     * Test method for {@link WorkflowModelFacade#setExecutable(Long, Boolean)}.
     */
    @Test
    public void testSetExecutable() {
        fail("Not yet implemented"); // RR setExecutable
    }

    /**
     * Test method for
     * {@link WorkflowModelFacade#getWorkflowAdministrators(Long)}.
     */
    @Test
    public void testGetWorkflowAdministrators() {
        fail("Not yet implemented"); // RR getWorkflowAdministrators
    }

    /**
     * Test method for {@link WorkflowModelFacade#deleteWorkflowModels(List)}.
     */
    @Test
    public void testDeleteWorkflowModels() {
        fail("Not yet implemented"); // RR deleteWorkflowModels
    }

    /**
     * Test method for
     * {@link WorkflowModelFacade#getWorkflowInstances(Long, Paginator)}.
     */
    @Test
    public void testGetWorkflowInstances() {
        fail("Not yet implemented"); // RR getWorkflowInstances
    }

    /**
     * Test method for
     * {@link WorkflowModelFacade#getAllPublishedWorkflowModels(List, Paginator)}
     * .
     */
    @Test
    public void testGetAllPublishedWorkflowModels() {
        fail("Not yet implemented"); // RR getAllPublishedWorkflowModels
    }

    /**
     * Test method for
     * {@link WorkflowModelFacade#getLastStartConfiguration(Long)}.
     */
    @Test
    public void testGetLastStartConfiguration() {
        fail("Not yet implemented"); // RR getLastStartConfiguration
    }
}
