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

import junit.framework.TestSuite;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * This class adds the capability to add global preconditions to all tests.
 * 
 * @author Reinhold
 */
public class GlobalPreconditionsSuite extends TestSuite {
    @BeforeClass
    public static void setUpBeforeSuite() {
        // nothing as of yet
    }

    @Before
    public void setUpSuiteCase() {
        // nothing as of yet
    }

    @After
    public void tearDownSuiteCase() {
        // nothing as of yet
    }

    @AfterClass
    public static void tearDownAfterSuite() {
        // nothing as of yet
    }
}
