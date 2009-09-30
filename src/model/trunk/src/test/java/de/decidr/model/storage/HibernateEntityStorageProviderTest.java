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

package de.decidr.model.storage;

import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.testsuites.DatabaseTestSuite;

/**
 * MF: add comment
 * 
 * @author Markus Fischer
 */
public class HibernateEntityStorageProviderTest {

    @BeforeClass
    public static void disable() {
        fail("This test class has not yet been implemented");
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        if (!DatabaseTestSuite.running()) {
            fail("Needs to run inside " + DatabaseTestSuite.class.getName());
        }

        // MF implement
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // MF implement
    }

    @Before
    public void setUp() throws Exception {
        // MF implement
    }

    @After
    public void tearDown() throws Exception {
        // MF implement
    }

    /**
     * Test method for
     * {@link HibernateEntityStorageProvider#applyConfig(Properties)}.
     */
    @Test
    public void testApplyConfig() {
        fail("Not yet implemented"); // MF applyConfig
    }

    /**
     * Test method for {@link HibernateEntityStorageProvider#getFile(Long)}.
     */
    @Test
    public void testGetFile() {
        fail("Not yet implemented"); // MF getFile
    }

    /**
     * Test method for
     * {@link HibernateEntityStorageProvider#isApplicable(Properties)}.
     */
    @Test
    public void testIsApplicable() {
        fail("Not yet implemented"); // MF isApplicable
    }

    /**
     * Test method for
     * {@link HibernateEntityStorageProvider#putFile(FileInputStream, Long)}.
     */
    @Test
    public void testPutFile() {
        fail("Not yet implemented"); // MF putFile
    }

    /**
     * Test method for {@link HibernateEntityStorageProvider#removeFile(Long)}.
     */
    @Test
    public void testRemoveFile() {
        fail("Not yet implemented"); // MF removeFile
    }
}
