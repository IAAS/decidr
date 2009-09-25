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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.AbstractMap.SimpleImmutableEntry;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.vaadin.data.Item;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.LowLevelDatabaseTest;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.testsuites.DatabaseTestSuite;

/**
 * Test case for <code>{@link UserFacade}</code>. Some of the methods can't be
 * tested easily within the confines of a unit test, as they interact with web
 * services. These methods will be tested at a later point in time when the most
 * important test cases are written or during the system test.
 * 
 * @author Reinhold
 */
public class UserFacadeTest extends LowLevelDatabaseTest {

    static UserFacade adminFacade;
    static UserFacade userFacade;
    static UserFacade nullFacade;

    static Set<UserFacade> allFacades = new HashSet<UserFacade>(4);

    static UserProfile classProfile = new UserProfile();
    static Long testUserID;
    static final String TEST_EMAIL = "decidr.iaas@googlemail.com";
    static final String TEST_PASSWORD = "asd";
    static final String TEST_USERNAME = "tEsstUsser";

    /**
     * Initialises the facade instances and registers a User, testing
     * {@link UserFacade#registerUser(String, String, UserProfile)}.
     */
    @BeforeClass
    public static void setUpBeforeClass() throws NullPointerException,
            TransactionException {
        if (!DatabaseTestSuite.running()) {
            fail("Needs to run inside " + DatabaseTestSuite.class.getName());
        }

        adminFacade = new UserFacade(new SuperAdminRole(DecidrGlobals
                .getSettings().getSuperAdmin().getId()));
        userFacade = new UserFacade(new BasicRole(0L));
        nullFacade = new UserFacade(null);

        allFacades.add(adminFacade);
        allFacades.add(userFacade);
        allFacades.add(nullFacade);

        UserProfile testProfile = new UserProfile();
        testProfile.setFirstName("test");
        testProfile.setLastName("user");
        testProfile.setCity("boringtown");
        testProfile.setStreet("ancient st.");
        testProfile.setPostalCode("112");
        testProfile.setUsername("testuser");

        registerUserExceptionHelper(
                "registering user with null facade succeeded", nullFacade,
                "asd@desk.de", "asd", testProfile);
        registerUserExceptionHelper(
                "registering user with normal user facade succeeded",
                userFacade, "asd@desk.de", "asd", testProfile);

        testProfile.setUsername("testuser1");
        adminFacade.registerUser("asd1@desk.de", "asd", testProfile);

        testProfile.setUsername("testuser2");
        registerUserExceptionHelper("registering user twice succeeded",
                adminFacade, "asd1@desk.de", "asd", testProfile);

        testProfile.setUsername("testuser3");
        adminFacade.registerUser("asd2@desk.de", "", testProfile);

        testProfile.setUsername("testuser4");
        registerUserExceptionHelper(
                "registering user with null password succeeded", adminFacade,
                "asd3@desk.de", null, testProfile);

        testProfile.setUsername("testuser5");
        registerUserExceptionHelper(
                "registering user with empty email succeeded", adminFacade, "",
                "asd", testProfile);
        registerUserExceptionHelper(
                "registering user with null email succeeded", adminFacade,
                null, "asd", testProfile);

        registerUserExceptionHelper(
                "registering user with null profile succeeded", adminFacade,
                "asd4@desk.de", "asd", null);

        registerUserExceptionHelper(
                "registering user with empty profile succeeded", adminFacade,
                "asd5@desk.de", "asd", new UserProfile());

        testProfile.setFirstName(null);
        testProfile.setLastName(null);
        testProfile.setCity(null);
        testProfile.setStreet(null);
        testProfile.setPostalCode(null);
        testProfile.setUsername("testuser6");
        adminFacade.registerUser("asd6@desk.de", "asd", testProfile);
        registerUserExceptionHelper(
                "invalid profile (double username) succeeded", adminFacade,
                "asd7@desk.de", "asd", testProfile);

        testProfile.setUsername(null);
        registerUserExceptionHelper(
                "invalid profile (empty username) succeeded", adminFacade,
                "asd@desk.de", "asd", testProfile);
        testProfile.setUsername("testuser");
    }

    private static void registerUserExceptionHelper(String failmsg,
            UserFacade facade, String email, String passwd, UserProfile profile) {
        try {
            facade.registerUser(email, passwd, profile);
            fail(failmsg);
        } catch (TransactionException e) {
            // supposed to be thrown
        }
    }

    @Before
    public void setUpTestCase() throws TransactionException {
        classProfile.setFirstName("test");
        classProfile.setLastName("user");
        classProfile.setUsername(TEST_USERNAME);
        classProfile.setStreet("ancient st.");
        classProfile.setPostalCode("112");
        classProfile.setCity("boringtown");

        testUserID = adminFacade.registerUser(TEST_EMAIL, TEST_PASSWORD,
                classProfile);
    }

    @After
    public void tearDownTestCase() {
        session.createQuery("delete from User").executeUpdate();
    }

    /**
     * Test method for {@link UserFacade#getAllUsers(List, Paginator)}.
     */
    @Test
    public void testGetAllUsers() throws TransactionException {
        List<Item> userList;
        Item testUserItem = null;

        userList = adminFacade.getAllUsers(null, null);
        adminFacade.getAllUsers(new ArrayList<Filter>(), null);
        adminFacade.getAllUsers(null, new Paginator());
        adminFacade.getAllUsers(new ArrayList<Filter>(), new Paginator());

        for (Item item : userList) {
            if (item.getItemProperty("id").getValue().equals(testUserID)) {
                testUserItem = item;
                break;
            }
        }

        if (testUserItem == null) {
            fail("couldn't get user created previously");
            // fail() throws an unchecked exception which the java compiler
            // doesn't know about. This means we need to indicate that the
            // method is always being exited here.
            return;
        }

        assertEquals(TEST_EMAIL, testUserItem.getItemProperty("email")
                .getValue());
        assertEquals(classProfile.getFirstName(), testUserItem.getItemProperty(
                "firstName").getValue());
        assertEquals(classProfile.getLastName(), testUserItem.getItemProperty(
                "lastName").getValue());
        assertEquals(TEST_USERNAME, testUserItem.getItemProperty("username")
                .getValue());

        getAllUsersExceptionHelper(
                "getting all users with normal user facade succeeded",
                userFacade, null, null);
        getAllUsersExceptionHelper(
                "getting all users with normal user facade succeeded",
                userFacade, new ArrayList<Filter>(), null);
        getAllUsersExceptionHelper(
                "getting all users with normal user facade succeeded",
                userFacade, null, new Paginator());
        getAllUsersExceptionHelper(
                "getting all users with normal user facade succeeded",
                userFacade, new ArrayList<Filter>(), new Paginator());

        getAllUsersExceptionHelper(
                "getting all users with normal user facade succeeded",
                nullFacade, null, null);
        getAllUsersExceptionHelper(
                "getting all users with normal user facade succeeded",
                nullFacade, new ArrayList<Filter>(), null);
        getAllUsersExceptionHelper(
                "getting all users with normal user facade succeeded",
                nullFacade, null, new Paginator());
        getAllUsersExceptionHelper(
                "getting all users with normal user facade succeeded",
                nullFacade, new ArrayList<Filter>(), new Paginator());
    }

    private static void getAllUsersExceptionHelper(String failmsg,
            UserFacade facade, List<Filter> filters, Paginator paginator) {
        try {
            facade.getAllUsers(filters, paginator);
            fail(failmsg);
        } catch (TransactionException e) {
            // supposed to be thrown
        }
    }

    /**
     * Test method for {@link UserFacade#getUserIdByLogin(String, String)}.
     */
    @Test
    public void testGetUserIdByLogin() throws TransactionException {
        for (UserFacade facade : allFacades) {
            assertEquals(testUserID, facade.getUserIdByLogin(TEST_USERNAME,
                    null));
            assertEquals(testUserID, facade.getUserIdByLogin(TEST_USERNAME, ""));
            assertEquals(testUserID, facade.getUserIdByLogin(TEST_USERNAME,
                    "sdfsdfsdf"));
            assertEquals(testUserID, facade.getUserIdByLogin(TEST_USERNAME,
                    TEST_PASSWORD));
            assertEquals(testUserID, facade.getUserIdByLogin(TEST_EMAIL,
                    TEST_PASSWORD));
            try {
                facade.getUserIdByLogin(null, null);
                fail("getting user ID with null email/username succeeded");
            } catch (EntityNotFoundException e) {
                // supposed to be thrown
            }
            try {
                facade.getUserIdByLogin("", null);
                fail("getting user ID with empty email/username succeeded");
            } catch (EntityNotFoundException e) {
                // supposed to be thrown
            }
            try {
                facade.getUserIdByLogin(TEST_EMAIL, null);
                fail("getting user ID with null password succeeded");
            } catch (EntityNotFoundException e) {
                // supposed to be thrown
            }
            try {
                facade.getUserIdByLogin(TEST_EMAIL, "");
                fail("getting user ID with empty password succeeded");
            } catch (EntityNotFoundException e) {
                // supposed to be thrown
            }
            try {
                facade.getUserIdByLogin(TEST_EMAIL, "asdfgthsf");
                fail("getting user ID with wrong password succeeded");
            } catch (EntityNotFoundException e) {
                // supposed to be thrown
            }
        }
    }

    /**
     * Test method for {@link UserFacade#setEmailAddress(Long, String)}.
     */
    @Test
    public void testSetEmailAddress() throws TransactionException {
        UserProfile testProfile = new UserProfile();
        testProfile.setFirstName("test");
        testProfile.setLastName("user");
        testProfile.setCity("boringtown");
        testProfile.setStreet("ancient st.");
        testProfile.setPostalCode("112");
        testProfile.setUsername("test_user");

        Long secondUserID = adminFacade.registerUser("asds1@desk.de", "asd",
                testProfile);

        adminFacade.setEmailAddress(testUserID, TEST_EMAIL + ".vu");
        assertEquals(TEST_EMAIL + ".vu", adminFacade.getUserProfile(testUserID,
                true).getItemProperty("email").getValue());
        adminFacade.setEmailAddress(testUserID, TEST_EMAIL);
        adminFacade.setEmailAddress(testUserID, TEST_EMAIL);

        for (UserFacade facade : allFacades) {
            setEmailAddressExceptionHelper("setting same email succeeded",
                    facade, secondUserID, TEST_EMAIL);
            setEmailAddressExceptionHelper("setting null email succeeded",
                    facade, testUserID, null);
            setEmailAddressExceptionHelper("setting empty email succeeded",
                    facade, testUserID, "");
            setEmailAddressExceptionHelper(
                    "setting email for null user ID succeeded", adminFacade,
                    null, "test@example.com");
        }

        setEmailAddressExceptionHelper(
                "setting email with null facade succeeded", nullFacade,
                testUserID, TEST_EMAIL);
        setEmailAddressExceptionHelper(
                "setting email with normal user facade succeeded", userFacade,
                testUserID, TEST_EMAIL);
    }

    private static void setEmailAddressExceptionHelper(String failmsg,
            UserFacade facade, Long userID, String newEmail) {
        try {
            facade.setEmailAddress(userID, newEmail);
            fail(failmsg);
        } catch (TransactionException e) {
            // supposed to be thrown
        }
    }

    /**
     * Test method for {@link UserFacade#setDisableSince(Long, Date)}.
     */
    @Test
    public void testSetDisableSince() throws TransactionException {
        try {
            userFacade.setDisableSince(testUserID, new Date());
            fail("setting user disabled with normal user facade succeeded.");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            nullFacade.setDisableSince(testUserID, new Date());
            fail("setting user disabled with null facade succeeded.");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        Date testDate = new Date();
        adminFacade.setDisableSince(testUserID, testDate);
        assertEquals(testDate, adminFacade.getUserProfile(testUserID)
                .getItemProperty("disabledSince").getValue());

        testDate = DecidrGlobals.getTime().getTime();
        adminFacade.setDisableSince(testUserID, testDate);
        assertEquals(testDate, adminFacade.getUserProfile(testUserID)
                .getItemProperty("disabledSince").getValue());

        testDate = new Date(new Date().getTime() - 1000000);
        adminFacade.setDisableSince(testUserID, testDate);
        assertEquals(testDate, adminFacade.getUserProfile(testUserID)
                .getItemProperty("disabledSince").getValue());

        testDate = new Date(new Date().getTime() + 1000000);
        adminFacade.setDisableSince(testUserID, testDate);
        assertEquals(testDate, adminFacade.getUserProfile(testUserID)
                .getItemProperty("disabledSince").getValue());

        adminFacade.setDisableSince(testUserID, null);
        assertNull(adminFacade.getUserProfile(testUserID).getItemProperty(
                "disabledSince").getValue());
    }

    /**
     * Test method for {@link UserFacade#setUnavailableSince(Long, Date)}.
     */
    @Test
    public void testSetUnavailableSince() throws TransactionException {
        try {
            userFacade.setUnavailableSince(testUserID, new Date());
            fail("setting user disabled with normal user facade succeeded.");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            nullFacade.setUnavailableSince(testUserID, new Date());
            fail("setting user disabled with null facade succeeded.");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        Date testDate = new Date();
        adminFacade.setUnavailableSince(testUserID, testDate);
        assertEquals(testDate, adminFacade.getUserProfile(testUserID)
                .getItemProperty("unavailableSince").getValue());

        testDate = DecidrGlobals.getTime().getTime();
        adminFacade.setUnavailableSince(testUserID, testDate);
        assertEquals(testDate, adminFacade.getUserProfile(testUserID)
                .getItemProperty("unavailableSince").getValue());

        testDate = new Date(new Date().getTime() - 1000000);
        adminFacade.setUnavailableSince(testUserID, testDate);
        assertEquals(testDate, adminFacade.getUserProfile(testUserID)
                .getItemProperty("unavailableSince").getValue());

        testDate = new Date(new Date().getTime() + 1000000);
        adminFacade.setUnavailableSince(testUserID, testDate);
        assertEquals(testDate, adminFacade.getUserProfile(testUserID)
                .getItemProperty("unavailableSince").getValue());

        adminFacade.setDisableSince(testUserID, null);
        assertNull(adminFacade.getUserProfile(testUserID).getItemProperty(
                "unavailableSince").getValue());
    }

    /**
     * Test method for {@link UserFacade#setPassword(Long, String, String)}.
     */
    @Test
    public void testSetPassword() throws TransactionException {
        assertTrue(adminFacade.setPassword(testUserID, TEST_PASSWORD,
                "Fuuni PWD"));
        assertTrue(checkPWD("Fuuni PWD"));
        resetPWD();
        assertTrue(adminFacade.setPassword(testUserID, TEST_PASSWORD + "wronk",
                "Fuuni PWD"));
        assertTrue(checkPWD("Fuuni PWD"));
        resetPWD();
        assertTrue(adminFacade.setPassword(testUserID, null, "Fuuni PWD"));
        assertTrue(checkPWD("Fuuni PWD"));
        resetPWD();

        assertTrue(userFacade.setPassword(testUserID, TEST_PASSWORD,
                "Fuuni PWD"));
        assertTrue(checkPWD("Fuuni PWD"));
        resetPWD();
        assertFalse(userFacade.setPassword(testUserID, TEST_PASSWORD + "wronk",
                "Fuuni PWD"));
        assertFalse(checkPWD("Fuuni PWD"));
        resetPWD();
        assertFalse(userFacade.setPassword(testUserID, null, "Fuuni PWD"));
        assertFalse(checkPWD("Fuuni PWD"));
        resetPWD();

        assertTrue(nullFacade.setPassword(testUserID, TEST_PASSWORD,
                "Fuuni PWD"));
        assertTrue(checkPWD("Fuuni PWD"));
        resetPWD();
        assertFalse(nullFacade.setPassword(testUserID, TEST_PASSWORD + "wronk",
                "Fuuni PWD"));
        assertFalse(checkPWD("Fuuni PWD"));
        resetPWD();
        assertFalse(nullFacade.setPassword(testUserID, null, "Fuuni PWD"));
        assertFalse(checkPWD("Fuuni PWD"));
        resetPWD();

        try {
            adminFacade.setPassword(testUserID, TEST_PASSWORD, null);
            resetPWD();
            fail("setting null password using admin facade succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            userFacade.setPassword(testUserID, TEST_PASSWORD, null);
            resetPWD();
            fail("setting null password using admin facade succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            nullFacade.setPassword(testUserID, TEST_PASSWORD, null);
            resetPWD();
            fail("setting null password using admin facade succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
    }

    private boolean checkPWD(String pwd) throws TransactionException {
        try {
            userFacade.getUserIdByLogin(TEST_EMAIL, pwd);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    private void resetPWD() throws TransactionException {
        adminFacade.setPassword(testUserID, null, TEST_PASSWORD);
    }

    /**
     * Test method for {@link UserFacade#setProfile(Long, Item)},
     * {@link UserFacade#getUserProfile(Long, Boolean)} and
     * {@link UserFacade#getUserProfile(Long)}.
     */
    @Test
    public void testProfile() throws TransactionException {
        Map.Entry<User, UserProfile> userProfile = getProfile(adminFacade
                .getUserProfile(testUserID), true);
        UserProfile testProfile = userProfile.getValue();
        User testUser = userProfile.getKey();

        assertNotNull(testUser);

        testProfile.setCity("Acity");
        adminFacade.setProfile(testUserID, testProfile);
        userProfile = getProfile(adminFacade.getUserProfile(testUserID), true);
        assertEquals("Acity", userProfile.getValue().getCity());
        assertNotNull(userProfile.getKey());

        adminFacade.setProfile(testUserID, new UserProfile());
        assertNotNull(getProfile(adminFacade.getUserProfile(testUserID, false),
                false).getKey());

        // make sure only proper fields are deleted
        userProfile = getProfile(adminFacade.getUserProfile(testUserID), true);
        assertNotNull(userProfile.getKey());
        testProfile = userProfile.getValue();
        assertTrue("".equals(testProfile.getCity()));
        assertTrue("".equals(testProfile.getFirstName()));
        assertTrue("".equals(testProfile.getLastName()));
        assertTrue("".equals(testProfile.getPostalCode()));
        assertTrue("".equals(testProfile.getStreet()));
        assertFalse("".equals(testProfile.getUsername()));

        try {
            adminFacade.setProfile(testUserID, null);
            fail("managed to set null profile");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
    }

    private static Map.Entry<User, UserProfile> getProfile(Item profileItem,
            boolean parseProfile) {
        User user = new User();
        UserProfile profile = new UserProfile();
        SimpleImmutableEntry<User, UserProfile> returnEntry;

        user.setId((Long) profileItem.getItemProperty("id").getValue());
        user.setAuthKey((String) profileItem.getItemProperty("authKey")
                .getValue());
        user.setEmail((String) profileItem.getItemProperty("email").getValue());
        user.setDisabledSince((Date) profileItem.getItemProperty(
                "disabledSince").getValue());
        user.setUnavailableSince((Date) profileItem.getItemProperty(
                "unavailableSince").getValue());
        user.setRegisteredSince((Date) profileItem.getItemProperty(
                "registeredSince").getValue());
        user.setCreationDate((Date) profileItem.getItemProperty("creationDate")
                .getValue());

        if (parseProfile) {
            profile.setCity((String) profileItem.getItemProperty("city")
                    .getValue());
            profile.setFirstName((String) profileItem.getItemProperty(
                    "firstName").getValue());
            profile.setLastName((String) profileItem
                    .getItemProperty("lastName").getValue());
            profile.setPostalCode((String) profileItem.getItemProperty(
                    "postalCode").getValue());
            profile.setStreet((String) profileItem.getItemProperty("street")
                    .getValue());
            profile.setUsername((String) profileItem
                    .getItemProperty("username").getValue());
        }

        returnEntry = new SimpleImmutableEntry<User, UserProfile>(user, profile);
        return returnEntry;
    }

    /**
     * Test method for {@link UserFacade#removeFromTenant(Long, Long)}.
     */
    @Test
    public void testRemoveFromTenant() {
        fail("Not yet implemented"); // RR removeFromTenant
    }

    /**
     * Test method for {@link UserFacade#confirmRegistration(Long, String)} and
     * {@link UserFacade#isRegistered(Long)}.
     */
    @Test
    public void testRegistration() {
        fail("Not yet implemented"); // RR confirmRegistration
        fail("Not yet implemented"); // RR isRegistered
    }

    /**
     * Test method for
     * {@link UserFacade#confirmChangeEmailRequest(Long, String)}.
     */
    @Test
    public void testConfirmChangeEmailRequest() {
        // DH wie kommt der ChangeEmailRequest in die Datenbank?
        fail("Not yet implemented"); // RR confirmChangeEmailRequest
    }

    /**
     * Test method for {@link UserFacade#getHighestUserRole(Long)}.
     */
    @Test
    public void testGetHighestUserRole() {
        fail("Not yet implemented"); // RR getHighestUserRole
    }

    /**
     * Test method for {@link UserFacade#getUserRoleForTenant(Long, Long)}.
     */
    @Test
    public void testGetUserRoleForTenant() {
        fail("Not yet implemented"); // RR getUserRoleForTenant
    }

    /**
     * Test method for
     * {@link facades.UserFacade#getAdminstratedWorkflowInstances(Long)}.
     */
    @Test
    public void testGetAdminstratedWorkflowInstances() {
        fail("Not yet implemented"); // RR getAdminstratedWorkflowInstances
    }

    /**
     * Test method for {@link UserFacade#getJoinedTenants(Long)}.
     */
    @Test
    public void testGetJoinedTenants() {
        fail("Not yet implemented"); // RR getJoinedTenants
    }

    /**
     * Test method for {@link UserFacade#getAdministratedWorkflowModels(Long)}.
     */
    @Test
    public void testGetAdministratedWorkflowModels() {
        fail("Not yet implemented"); // RR getAdministratedWorkflowModels
    }

    /**
     * Test method for {@link UserFacade#setCurrentTenantId(Long, Long)} and
     * {@link UserFacade#getCurrentTenantId(Long).
     */
    @Test
    public void testCurrentTenantId() {
        fail("Not yet implemented"); // RR setCurrentTenantId
        fail("Not yet implemented"); // RR getCurrentTenantId
    }

    /**
     * Test method for {@link UserFacade#getWorkItems(Long, List, Paginator)}.
     */
    @Test
    public void testGetWorkItems() {
        fail("Not yet implemented"); // RR getWorkItems
    }
}
