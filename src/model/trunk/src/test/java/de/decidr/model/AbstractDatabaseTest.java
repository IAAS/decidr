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
 * RR: add comment
 * 
 * @author Reinhold
 */
public abstract class AbstractDatabaseTest {

    /**
     * RR: add comment
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // RR do hibernate settings & make sure DB is available
        fail("not yet implemented");
    }

    /**
     * RR: add comment
     * 
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // RR Do DB cleanup
        fail("not yet implemented");
    }

    /**
     * RR: add comment
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        // RR ensure a well-defined state of the DB
        fail("not yet implemented");
    }

    /**
     * RR: add comment
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        // RR clean up if necessary
        fail("not yet implemented");
    }
}
