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

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.entities.File;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.testing.DecidrDatabaseTest;

/**
 * Test case for <code>{@link FileFacade}</code>.
 * 
 * @author Reinhold
 */
public class FileFacadeTest extends DecidrDatabaseTest {

    static FileFacade adminFacade;
    static FileFacade userFacade;
    static FileFacade nullFacade;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        adminFacade = new FileFacade(null);
        userFacade = new FileFacade(null);
        nullFacade = new FileFacade(null);
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
    public void testCreateFile() throws TransactionException {
        String testName = "/decidr.jpg";
        String testMime = "image/jpeg";
        InputStream testFile = FileFacadeTest.class
                .getResourceAsStream(testName);
        assertNotNull(testFile);

        try {
            nullFacade.createFile(testFile, testName, testMime, true);
            fail("calling createFile with nullFacade succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        // try {
        // RR implement when consistent
        // nullFacade.replaceFile(0L, testFile, testName, testMime);
        // fail("calling replaceFile with nullFacade succeeded");
        // } catch (TransactionException e) {
        // // supposed to be thrown
        // }
        try {
            nullFacade.getFileInfo(0L);
            fail("calling getFileInfo with nullFacade succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            nullFacade.getFileData(0L);
            fail("calling getFileData with nullFacade succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            nullFacade.deleteFile(0L);
            fail("calling deleteFile with nullFacade succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        Long testID;
        File compareFile;
        InputStream compareData;
        for (FileFacade facade : new FileFacade[] { userFacade, adminFacade }) {
            // RR how to set the stupid permissions?
            testID = facade.createFile(testFile, testName, testMime, false);
            // RR implement when consistent
            // facade.replaceFile(testID, testFile, testName, testMime);
            compareFile = facade.getFileInfo(testID);
            assertEquals(testName, compareFile.getFileName());
            assertEquals(testMime, compareFile.getMimeType());
            compareData = facade.getFileData(testID);
            assertTrue(facade.deleteFile(testID));

            fail("Not yet implemented"); // RR createFile
            fail("Not yet implemented"); // RR replaceFile
            fail("Not yet implemented"); // RR getFileInfo
            fail("Not yet implemented"); // RR getFileData
            fail("Not yet implemented"); // RR deleteFile
        }
    }
}
