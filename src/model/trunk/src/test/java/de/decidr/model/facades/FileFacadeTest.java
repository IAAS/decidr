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
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.permissions.FileDeletePermission;
import de.decidr.model.acl.permissions.FilePermission;
import de.decidr.model.acl.permissions.FileReadPermission;
import de.decidr.model.acl.permissions.FileReplacePermission;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.entities.File;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.testing.LowLevelDatabaseTest;

/**
 * Test case for <code>{@link FileFacade}</code>.
 * 
 * @author Reinhold
 */
public class FileFacadeTest extends LowLevelDatabaseTest {

    static FileFacade adminFacade;
    static FileFacade userFacade;
    static FileFacade nullFacade;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        adminFacade = new FileFacade(new SuperAdminRole(DecidrGlobals
                .getSettings().getSuperAdmin().getId()));
        userFacade = new FileFacade(new BasicRole(0L));
        nullFacade = new FileFacade(null);
    }

    static long getInvalidFileID() {
        long invalidID = Long.MIN_VALUE;

        for (long l = invalidID; session.createQuery(
                "FROM File WHERE id = :given").setLong("given", l)
                .uniqueResult() != null; l++)
            invalidID = l + 1;
        return invalidID;
    }

    /**
     * Returns the size of an {@link InputStream}. If the returned value is
     * {@link Long#MAX_VALUE}, the stream is probably larger than a {@link Long}
     * can express.
     * <p>
     * <em>Note: This might destroy the data contained in the stream. It should probably be re-opened!</em>
     * 
     * @see InputStream#skip(long)
     */
    public static Long getInputStreamSize(InputStream in) {
        long size;
        assertNotNull(in);

        try {
            size = in.skip(Long.MAX_VALUE);
        } catch (IOException e) {
            size = -1;
        }

        return (size < 0) ? 0 : size;
    }

    public static boolean compareInputStreams(InputStream a, InputStream b)
            throws IOException {
        boolean result = true;

        int lastByteA;
        int lastByteB;
        while ((lastByteA = a.read()) == (lastByteB = b.read())
                && lastByteA != -1) {
            // skip equal bytes
        }

        if (lastByteA != -1 || lastByteB != -1) {
            result = false;
        }

        return result;
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
    public void testFile() throws TransactionException, IOException {
        String testName = "/decidr.jpg";
        String testMime = "image/jpeg";
        InputStream testFile = FileFacadeTest.class
                .getResourceAsStream(testName);
        assertNotNull(testFile);
        Long streamSize = getInputStreamSize(FileFacadeTest.class
                .getResourceAsStream(testName));
        Long invalidID = getInvalidFileID();
        Set<Class<? extends FilePermission>> publicPermissions = new HashSet<Class<? extends FilePermission>>();
        publicPermissions.add(FileReadPermission.class);
        publicPermissions.add(FileDeletePermission.class);
        publicPermissions.add(FileReplacePermission.class);
        testFile.mark(Integer.MAX_VALUE);

        try {
            nullFacade.createFile(testFile, streamSize, testName, testMime,
                    new Boolean(true), publicPermissions);
            fail("calling createFile with nullFacade succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        testFile.reset();
        try {
            nullFacade
                    .replaceFile(0L, testFile, streamSize, testName, testMime);
            fail("calling replaceFile with nullFacade succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
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
            testFile.reset();
            testID = facade.createFile(testFile, streamSize, testName,
                    testMime, false, publicPermissions);
            compareFile = facade.getFileInfo(testID);
            assertEquals(testName, compareFile.getFileName());
            assertEquals(testMime, compareFile.getMimeType());
            assertFalse(compareFile.isTemporary());

            testFile.reset();
            facade
                    .replaceFile(testID, testFile, streamSize, testName,
                            testMime);
            compareFile = facade.getFileInfo(testID);
            assertEquals(testName, compareFile.getFileName());
            assertEquals(testMime, compareFile.getMimeType());
            assertFalse(compareFile.isTemporary());

            compareData = facade.getFileData(testID);
            assertEquals(streamSize, getInputStreamSize(compareData));
            testFile.reset();
            assertTrue(compareInputStreams(testFile, facade.getFileData(testID)));

            try {
                facade.createFile(null, streamSize, testName, testMime, false,
                        publicPermissions);
                fail("managed to create file with null parameter");
            } catch (IllegalArgumentException e) {
                // supposed to happen
            }
            testFile.reset();
            try {
                facade.createFile(testFile, -1L, testName, testMime, false,
                        publicPermissions);
                fail("managed to create file with negative size");
            } catch (IllegalArgumentException e) {
                // supposed to happen
            }
            testFile.reset();
            try {
                facade.createFile(testFile, null, testName, testMime, false,
                        publicPermissions);
                fail("managed to create file with null parameter");
            } catch (IllegalArgumentException e) {
                // supposed to happen
            }
            testFile.reset();
            try {
                facade.createFile(testFile, streamSize, null, testMime, false,
                        publicPermissions);
                fail("managed to create file with null parameter");
            } catch (IllegalArgumentException e) {
                // supposed to happen
            }
            testFile.reset();
            try {
                facade.createFile(testFile, streamSize, testName, null, false,
                        publicPermissions);
                fail("managed to create file with null parameter");
            } catch (IllegalArgumentException e) {
                // supposed to happen
            }

            testFile.reset();
            try {
                facade.replaceFile(null, testFile, streamSize, testName,
                        testMime);
                fail("managed to replace file with null parameter");
            } catch (IllegalArgumentException e) {
                // supposed to happen
            }
            testFile.reset();
            try {
                facade.replaceFile(invalidID, testFile, streamSize, testName,
                        testMime);
                fail("managed to replace file with invalid file ID");
            } catch (TransactionException e) {
                // supposed to happen
            }
            try {
                facade
                        .replaceFile(testID, null, streamSize, testName,
                                testMime);
                fail("managed to replace file with null parameter");
            } catch (IllegalArgumentException e) {
                // supposed to happen
            }
            testFile.reset();
            try {
                facade.replaceFile(testID, testFile, null, testName, testMime);
                fail("managed to replace file with null parameter");
            } catch (IllegalArgumentException e) {
                // supposed to happen
            }
            testFile.reset();
            try {
                facade.replaceFile(testID, testFile, -1L, testName, testMime);
                fail("managed to replace file with negative size");
            } catch (IllegalArgumentException e) {
                // supposed to happen
            }
            testFile.reset();
            try {
                facade
                        .replaceFile(testID, testFile, streamSize, null,
                                testMime);
                fail("managed to replace file with null parameter");
            } catch (IllegalArgumentException e) {
                // supposed to happen
            }
            testFile.reset();
            try {
                facade
                        .replaceFile(testID, testFile, streamSize, testName,
                                null);
                fail("managed to replace file with null parameter");
            } catch (IllegalArgumentException e) {
                // supposed to happen
            }

            try {
                facade.getFileInfo(null);
                fail("managed to get file info with null parameter");
            } catch (IllegalArgumentException e) {
                // supposed to happen
            }
            try {
                facade.getFileInfo(invalidID);
                fail("managed to get file info with invalid file ID");
            } catch (TransactionException e) {
                // supposed to happen
            }
            try {
                facade.getFileData(null);
                fail("managed to get file data with null parameter");
            } catch (IllegalArgumentException e) {
                // supposed to happen
            }
            try {
                facade.getFileData(invalidID);
                fail("managed to get file data with invalid file ID");
            } catch (TransactionException e) {
                // supposed to happen
            }
            try {
                facade.deleteFile(null);
                fail("managed to delete file with null parameter");
            } catch (IllegalArgumentException e) {
                // supposed to happen
            }
            try {
                facade.deleteFile(invalidID);
                fail("managed to delete file with invalid file ID");
            } catch (TransactionException e) {
                // supposed to happen
            }

            assertTrue(facade.deleteFile(testID));
            assertFalse(facade.deleteFile(testID));
        }
    }
}
