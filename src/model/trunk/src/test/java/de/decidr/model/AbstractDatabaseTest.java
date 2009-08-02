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

package de.decidr.model;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * This is the abstract base class for all test classes testing database
 * interaction.<br>
 * It insures a working hibernate configuration and a consistent database for
 * each test.
 * 
 * @author Reinhold
 */
public abstract class AbstractDatabaseTest {

    /**
     * Fails if hibernate is not working properly and no working condition can
     * be produced. Creates a testing database.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        // RR do hibernate settings & make sure DB is available
        fail("not yet implemented");
    }

    /**
     * Removes the testing database.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        // RR Do DB cleanup
        fail("not yet implemented");
    }

    /**
     * Creates testing tables and entries without utilising model functionality
     * other than entities.
     */
    @Before
    public void setUp() {
        // RR ensure a well-defined state of the DB
        fail("not yet implemented");
    }

    /**
     * Removes testing tables.
     */
    @After
    public void tearDown() {
        // RR clean up if necessary
        fail("not yet implemented");
    }
}
