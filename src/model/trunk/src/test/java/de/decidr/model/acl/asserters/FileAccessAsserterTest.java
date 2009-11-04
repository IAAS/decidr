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

package de.decidr.model.acl.asserters;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.permissions.FileDeletePermission;
import de.decidr.model.acl.permissions.FilePermission;
import de.decidr.model.acl.permissions.FileReadPermission;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.FileFacade;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.facades.UserFacadeTest;
import de.decidr.model.testing.LowLevelDatabaseTest;

/**
 * Test case for <code>{@link FileAccessAsserter}</code>.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class FileAccessAsserterTest extends LowLevelDatabaseTest {

    private static UserFacade userFacade;
    private static FileFacade fileFacade;

    private static Long superAdminId;
    private static Long userId;
    private static Long fileId;

    private static final String USERNAME_PREFIX = "testuser";

    @BeforeClass
    public static void setUpBeforeClass() throws TransactionException,
            FileNotFoundException {
        UserFacadeTest.deleteTestUsers();

        // create test users
        superAdminId = DecidrGlobals.getSettings().getSuperAdmin().getId();
        userFacade = new UserFacade(new SuperAdminRole(superAdminId));

        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName("test");
        userProfile.setLastName("user");
        userProfile.setCity("testcity");
        userProfile.setStreet("test st.");
        userProfile.setPostalCode("12test");

        userProfile.setUsername(USERNAME_PREFIX + "User");
        userId = userFacade.registerUser(UserFacadeTest.getTestEmail(1),
                "qwertz", userProfile);

        fileFacade = new FileFacade(new SuperAdminRole(superAdminId));

        File file = new File("decidr.jpg");
        FileInputStream fileStream = new FileInputStream("decidr.jpg");
        Set<Class<? extends FilePermission>> permissions = new HashSet<Class<? extends FilePermission>>();

        permissions.add(FileReadPermission.class);
        permissions.add(FileDeletePermission.class);
        fileId = fileFacade.createFile(fileStream, file.length(), "decidr.jpg",
                new MimetypesFileTypeMap().getContentType(file), true,
                permissions);
    }

    @AfterClass
    public static void cleanUpAfterClass() throws TransactionException {
        fileFacade.deleteFile(fileId);
        UserFacadeTest.deleteTestUsers();
    }

    /**
     * Test method for {@link FileAccessAsserter#assertRule(Role, Permission)}.
     * 
     * @throws TransactionException
     */
    @Test
    public void testAssertRule() throws TransactionException {
        FileAccessAsserter asserter = new FileAccessAsserter();
        assertTrue(asserter.assertRule(new SuperAdminRole(superAdminId),
                new Permission("*")));
        assertFalse(asserter.assertRule(new UserRole(userId), new Permission(
                "*")));
    }

}
