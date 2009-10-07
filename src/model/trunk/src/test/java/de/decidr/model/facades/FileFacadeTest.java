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

import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.testsuites.DatabaseTestSuite;

/**
 * Test case for <code>{@link FileFacade}</code>.
 * 
 * @author Reinhold
 */
public class FileFacadeTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        if (!DatabaseTestSuite.running()) {
            fail("Needs to run inside " + DatabaseTestSuite.class.getName());
        }
    }

    /**
     * Test method for
     * {@link FileFacade#createFile(InputStream, String, String, Boolean)} and
     * {@link FileFacade#replaceFile(Long, FileInputStream, String, String)},
     * {@link FileFacade#getFileInfo(Long)},
     * {@link FileFacade#getFileData(Long)}, {@link FileFacade#deleteFile(Long)}
     * .
     */
    @Test
    public void testCreateFile() {
        InputStream testFile = FileFacadeTest.class
                .getResourceAsStream("decidr.jpg");
        fail("Not yet implemented"); // RR createFile
        fail("Not yet implemented"); // RR replaceFile
        fail("Not yet implemented"); // RR getFileInfo
        fail("Not yet implemented"); // RR getFileData
        fail("Not yet implemented"); // RR deleteFile
    }
}
