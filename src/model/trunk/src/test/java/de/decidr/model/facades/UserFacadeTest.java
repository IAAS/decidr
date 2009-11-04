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
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleImmutableEntry;

import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.vaadin.data.Item;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.asserters.UserIsEnabledAsserter;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.entities.RegistrationRequest;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserAdministratesWorkflowInstance;
import de.decidr.model.entities.UserAdministratesWorkflowModel;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.testing.LowLevelDatabaseTest;

/**
 * Test case for <code>{@link UserFacade}</code>. Some of the methods can't be
 * tested easily within the confines of a unit test, as they interact with web
 * services. These methods will be tested at a later point in time when the most
 * important test cases are written or during the system test.
 * 
 * @author Reinhold
 */
public class UserFacadeTest extends LowLevelDatabaseTest {

    static Long adminID;
    static UserFacade adminFacade;
    static UserFacade userFacade;
    static UserFacade nullFacade;

    static UserProfile classProfile = new UserProfile();
    static Long testUserID;
    static final String TEST_EMAIL = "decidr.iaas@googlemail.com";
    static final String TEST_PASSWORD = "asd";
    static final String TEST_USERNAME = "tEsstUsser";
    static final String USERNAME_PREFIX = "testuser";

    private static boolean checkPWD(String pwd) throws TransactionException {
        try {
            adminFacade.getUserIdByLogin(TEST_EMAIL, pwd);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    /*-
    private static void confirmChangeEmailRequestExceptionHelper(
            String failmsg, UserFacade facade, Long userID, String authKey) {
        try {
            facade.confirmChangeEmailRequest(userID, authKey);
            fail(failmsg);
        } catch (TransactionException e) {
            // supposed to be thrown
        }
    }*/

    /**
     * Needs to be called from inside a {@link LowLevelDatabaseTest}.
     */
    public static void deleteTestUsers() {
        Transaction trans = session.beginTransaction();
        session.createQuery(
                "delete from User WHERE email LIKE '" + TEST_EMAIL + "'")
                .executeUpdate();
        session.createQuery("delete from User WHERE email LIKE ''")
                .executeUpdate();
        session.createQuery("delete from User WHERE email LIKE 'asd%@desk.de'")
                .executeUpdate();
        session.createQuery(
                "DELETE UserProfile WHERE username LIKE '" + TEST_USERNAME
                        + "%'").executeUpdate();
        session.createQuery(
                "DELETE UserProfile WHERE username LIKE '" + USERNAME_PREFIX
                        + "%'").executeUpdate();
        trans.commit();
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

    static long getInvalidUserID() {
        long invalidID = Long.MIN_VALUE;

        for (long l = invalidID; session.createQuery(
                "FROM User WHERE id = :given").setLong("given", l)
                .uniqueResult() != null; l++)
            invalidID = l + 1;
        return invalidID;
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

    public static String getTestEmail(int n) {
        return "asd" + ((n > 0) ? n : "") + "@desk.de";
    }

    private static void getUserIdByLoginExceptionHelper(String failmsg,
            UserFacade facade, String emailName, String pwd) {
        try {
            facade.getUserIdByLogin(emailName, pwd);
            fail(failmsg);
        } catch (TransactionException e) {
            // supposed to be thrown
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
    }

    private static void registerUserExceptionHelper(String failmsg,
            UserFacade facade, String email, String passwd, UserProfile profile) {
        try {
            facade.registerUser(email, passwd, profile);
            fail(failmsg);
        } catch (TransactionException e) {
            // supposed to be thrown
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
    }

    /*-
    private static void requestChangeEmailExceptionHelper(String failmsg,
            UserFacade facade, Long userID, String newEmail) {
        try {
            facade.requestChangeEmail(userID, newEmail);
            fail(failmsg);
        } catch (TransactionException e) {
            // supposed to be thrown
        }
    }*/

    private static void resetPWD() throws TransactionException {
        adminFacade.setPassword(testUserID, null, TEST_PASSWORD);
    }

    private static void setEmailAddressExceptionHelper(String failmsg,
            UserFacade facade, Long userID, String newEmail)
            throws TransactionException {
        try {
            facade.setEmailAddress(userID, newEmail);
            fail(failmsg);
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
    }

    /**
     * Initialises the facade instances and registers a User, testing
     * {@link UserFacade#registerUser(String, String, UserProfile)}.
     */
    @BeforeClass
    public static void setUpBeforeClass() throws NullPointerException,
            TransactionException {
        deleteTestUsers();

        adminID = DecidrGlobals.getSettings().getSuperAdmin().getId();
        adminFacade = new UserFacade(new SuperAdminRole(adminID));
        userFacade = new UserFacade(new UserRole(adminID));
        nullFacade = new UserFacade(null);

        UserProfile testProfile = new UserProfile();
        testProfile.setFirstName("test");
        testProfile.setLastName("user");
        testProfile.setCity("boringtown");
        testProfile.setStreet("ancient st.");
        testProfile.setPostalCode("112");
        testProfile.setUsername(USERNAME_PREFIX);

        registerUserExceptionHelper(
                "registering user with null facade succeeded", nullFacade,
                getTestEmail(0), "asd", testProfile);
        registerUserExceptionHelper(
                "registering user with normal user facade succeeded",
                userFacade, getTestEmail(0), "asd", testProfile);

        testProfile.setUsername(USERNAME_PREFIX + "1");
        adminFacade.registerUser(getTestEmail(1), "asd", testProfile);

        testProfile.setUsername(USERNAME_PREFIX + "2");
        registerUserExceptionHelper("registering user twice succeeded",
                adminFacade, getTestEmail(1), "asd", testProfile);

        registerUserExceptionHelper(
                "registering user with empty password succeeded", adminFacade,
                getTestEmail(2), "", testProfile);

        registerUserExceptionHelper(
                "registering user with null password succeeded", adminFacade,
                getTestEmail(3), null, testProfile);

        registerUserExceptionHelper(
                "registering user with empty email succeeded", adminFacade, "",
                "asd", testProfile);
        registerUserExceptionHelper(
                "registering user with null email succeeded", adminFacade,
                null, "asd", testProfile);

        registerUserExceptionHelper(
                "registering user with null profile succeeded", adminFacade,
                getTestEmail(4), "asd", null);

        registerUserExceptionHelper(
                "registering user with empty profile succeeded", adminFacade,
                getTestEmail(5), "asd", new UserProfile());

        testProfile.setFirstName(null);
        testProfile.setLastName(null);
        testProfile.setCity(null);
        testProfile.setStreet(null);
        testProfile.setPostalCode(null);
        testProfile.setUsername(USERNAME_PREFIX + "3");
        adminFacade.registerUser(getTestEmail(6), "asd", testProfile);
        registerUserExceptionHelper(
                "invalid profile (double username) succeeded", adminFacade,
                getTestEmail(7), "asd", testProfile);

        testProfile.setUsername(null);
        registerUserExceptionHelper(
                "invalid profile (null username) succeeded", adminFacade,
                getTestEmail(8), "asd", testProfile);
        testProfile.setUsername("");
        registerUserExceptionHelper(
                "invalid profile (empty username) succeeded", adminFacade,
                getTestEmail(8), "asd", testProfile);
        testProfile.setUsername(USERNAME_PREFIX);

        deleteTestUsers();
    }

    @Before
    public void setUpTestCase() throws TransactionException {
        classProfile = new UserProfile();
        classProfile.setFirstName("test");
        classProfile.setLastName("user");
        classProfile.setUsername(TEST_USERNAME);
        classProfile.setStreet("ancient st.");
        classProfile.setPostalCode("112");
        classProfile.setCity("boringtown");

        testUserID = adminFacade.registerUser(TEST_EMAIL, TEST_PASSWORD,
                classProfile);
        assertNotNull(testUserID);

        adminFacade.setDisabledSince(testUserID, null);
        adminFacade.setDisabledSince(adminID, null);

        userFacade = new UserFacade(new UserRole(testUserID));
    }

    @After
    public void tearDownTestCase() {
        deleteTestUsers();
    }

    /**
     * Test method for {@link UserFacade#requestChangeEmail(Long, String)} and
     * {@link UserFacade#confirmChangeEmailRequest(Long, String)}.
     */
    // cannot currently be tested due to incomplete testing environment
    /*-
     @Test
     public void testChangeEmailRequest() throws TransactionException {
     long invalidID = getInvalidUserID();

     User user;
     for (UserFacade facade : new UserFacade[] {
     new UserFacade(new UserRole(testUserID)), adminFacade }) {
     confirmChangeEmailRequestExceptionHelper(
     "managed to confirm a nonexistent ChangeEmailRequest without an authKey",
     facade, testUserID, "");
     facade.requestChangeEmail(testUserID, "invalid@example.com");
     user = (User) session.get(User.class, testUserID);
     assertNotNull(user);
     ChangeEmailRequest request = user.getChangeEmailRequest();
     assertNotNull(request);
     facade.confirmChangeEmailRequest(testUserID, request.getAuthKey());

     confirmChangeEmailRequestExceptionHelper(
     "authenticating a ChangeEmailRequest twice succeeded",
     facade, testUserID, request.getAuthKey());

     confirmChangeEmailRequestExceptionHelper(
     "authenticating a ChangeEmailRequest with null user ID succeeded",
     facade, null, request.getAuthKey());
     confirmChangeEmailRequestExceptionHelper(
     "authenticating a ChangeEmailRequest with invalid user ID succeeded",
     facade, invalidID, request.getAuthKey());
     confirmChangeEmailRequestExceptionHelper(
     "authenticating a ChangeEmailRequest with null authKey",
     facade, testUserID, null);
     confirmChangeEmailRequestExceptionHelper(
     "authenticating a ChangeEmailRequest with empty authKey",
     facade, testUserID, "");
     confirmChangeEmailRequestExceptionHelper(
     "authenticating a ChangeEmailRequest with null user ID & authKey succeeded",
     facade, null, null);

     requestChangeEmailExceptionHelper(
     "requesting email change with null user ID and email succeeded",
     facade, null, null);
     requestChangeEmailExceptionHelper(
     "requesting email change with null email succeeded",
     facade, testUserID, null);
     requestChangeEmailExceptionHelper(
     "requesting email change with empty email succeeded",
     facade, testUserID, "");
     requestChangeEmailExceptionHelper(
     "requesting email change with null user ID succeeded",
     facade, null, getTestEmail(0));
     requestChangeEmailExceptionHelper(
     "requesting email change with invalid user ID succeeded",
     facade, invalidID, getTestEmail(1));

     deleteTestUsers();
     setUpTestCase();
     }

     confirmChangeEmailRequestExceptionHelper(
     "managed to confirm a nonexistent ChangeEmailRequest without an authKey with null facade",
     nullFacade, testUserID, "");
     requestChangeEmailExceptionHelper(
     "managed to request an email change with null facade",
     nullFacade, testUserID, "invalid@example.com");

     adminFacade.requestChangeEmail(testUserID, "invalid@example.com");
     user = (User) session.get(User.class, testUserID);
     assertNotNull(user);
     ChangeEmailRequest request = user.getChangeEmailRequest();
     assertNotNull(request);
     confirmChangeEmailRequestExceptionHelper(
     "managed to confirm a ChangeEmailRequest using null facade",
     nullFacade, testUserID, request.getAuthKey());
     adminFacade.confirmChangeEmailRequest(testUserID, request.getAuthKey());
     }
     */

    /**
     * Test method for
     * {@link UserFacade#getAdministratedWorkflowInstances(Long)} .
     */
    @Test
    public void testGetAdministratedWorkflowInstances()
            throws TransactionException {
        Long invalidID = getInvalidUserID();

        try {
            userFacade.getAdministratedWorkflowInstances(testUserID);
            fail("succeeded getting administrated workflow instances as normal user");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        try {
            userFacade.getAdministratedWorkflowInstances(invalidID);
            fail("succeeded getting administrated workflow instances as normal user with invalid ID");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.getAdministratedWorkflowInstances(invalidID);
            fail("succeeded getting administrated workflow instances as admin user with invalid ID");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        List<Item> WFIs = adminFacade
                .getAdministratedWorkflowInstances(testUserID);
        assertNotNull(WFIs);
        assertTrue(WFIs.isEmpty());

        User u = ((UserAdministratesWorkflowInstance) session.createQuery(
                "from UserAdministratesWorkflowInstance").list().get(0))
                .getUser();
        WFIs = adminFacade.getAdministratedWorkflowInstances(u.getId());
        assertNotNull(WFIs);
        assertFalse(WFIs.isEmpty());
    }

    /**
     * Test method for {@link UserFacade#getAdministratedWorkflowModels(Long)}.
     */
    @Test
    public void testGetAdministratedWorkflowModels()
            throws TransactionException {
        Long invalidID = getInvalidUserID();

        try {
            userFacade.getAdministratedWorkflowModels(testUserID);
            fail("succeeded getting administrated workflow models as normal user");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        try {
            userFacade.getAdministratedWorkflowModels(invalidID);
            fail("succeeded getting administrated workflow models as normal user with invalid ID");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.getAdministratedWorkflowModels(invalidID);
        } catch (EntityNotFoundException e) {
            // supposed to be thrown
        }

        List<Item> WFIs = adminFacade
                .getAdministratedWorkflowModels(testUserID);
        assertNotNull(WFIs);
        assertTrue(WFIs.isEmpty());

        User u = ((UserAdministratesWorkflowModel) session.createQuery(
                "from UserAdministratesWorkflowModel").list().get(0)).getUser();
        WFIs = adminFacade.getAdministratedWorkflowModels(u.getId());
        assertNotNull(WFIs);
        assertFalse(WFIs.isEmpty());
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

        if (userList == null || userList.isEmpty()) {
            fail("No users were retrieved despite there being at least one");
            // fail() throws an error which the java compiler doesn't know
            // about. This means we need to indicate that the method is always
            // being exited here.
            return;
        }

        for (Item item : userList) {
            if (item.getItemProperty("id").getValue().equals(testUserID)) {
                testUserItem = item;
                break;
            }
        }

        if (testUserItem == null) {
            fail("couldn't get user created previously");
            // fail() throws an error which the java compiler doesn't know
            // about. This means we need to indicate that the method is always
            // being exited here.
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

    /**
     * Test method for {@link UserFacade#getHighestUserRole(Long)}.
     */
    @Test
    public void testGetHighestUserRole() throws TransactionException {
        assertEquals(adminFacade.actor.getClass(), adminFacade
                .getHighestUserRole(adminID));
        assertNull(userFacade.getHighestUserRole(testUserID));
    }

    /**
     * Test method for {@link UserFacade#getUserIdByLogin(String, String)}.
     */
    @Test
    public void testGetUserIdByLogin() throws TransactionException {
        for (UserFacade facade : new UserFacade[] { userFacade, adminFacade }) {
            assertEquals(testUserID, facade.getUserIdByLogin(TEST_USERNAME,
                    TEST_PASSWORD));
            assertEquals(testUserID, facade.getUserIdByLogin(TEST_EMAIL,
                    TEST_PASSWORD));

            getUserIdByLoginExceptionHelper(
                    "getting user ID with null email/username succeeded",
                    facade, null, TEST_PASSWORD);
            getUserIdByLoginExceptionHelper(
                    "getting user ID with empty email/username succeeded",
                    facade, "", TEST_PASSWORD);
            getUserIdByLoginExceptionHelper(
                    "getting user ID with null password succeeded", facade,
                    TEST_USERNAME, null);
            getUserIdByLoginExceptionHelper(
                    "getting user ID with empty password succeeded", facade,
                    TEST_USERNAME, "");
            getUserIdByLoginExceptionHelper(
                    "getting user ID with wrong password succeeded", facade,
                    TEST_USERNAME, TEST_PASSWORD + "blrgh");
            getUserIdByLoginExceptionHelper(
                    "getting user ID with null password succeeded", facade,
                    TEST_EMAIL, null);
            getUserIdByLoginExceptionHelper(
                    "getting user ID with empty password succeeded", facade,
                    TEST_EMAIL, "");
            getUserIdByLoginExceptionHelper(
                    "getting user ID with wrong password succeeded", facade,
                    TEST_EMAIL, TEST_PASSWORD + "blrgh");
        }

        getUserIdByLoginExceptionHelper("getting user ID with null facade",
                nullFacade, TEST_USERNAME, TEST_PASSWORD);
    }

    /**
     * Test method for {@link UserFacade#getWorkItems(Long, List, Paginator)}.
     */
    @Test
    public void testGetWorkItems() throws TransactionException {
        List<Item> workItems;
        Long workingUserID;

        try {
            nullFacade.getWorkItems(testUserID, null, null);
            fail("managed to get user's work items with null facade");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        workingUserID = ((WorkItem) session.createQuery("from WorkItem").list()
                .get(0)).getUser().getId();

        adminFacade.setDisabledSince(testUserID, null);
        assertTrue(new UserFacade(new UserRole(testUserID)).getWorkItems(
                testUserID, null, null).isEmpty());

        for (UserFacade facade : new UserFacade[] {
                new UserFacade(new UserRole(workingUserID)), adminFacade }) {
            assertFalse(facade.getWorkItems(workingUserID, null, null)
                    .isEmpty());
            workItems = facade.getWorkItems(workingUserID, null, null);
            for (Item item : workItems) {
                assertNotNull(item.getItemProperty("id").getValue());
                assertEquals(workingUserID, item.getItemProperty("userId")
                        .getValue());
                assertNotNull(item.getItemProperty("creationDate").getValue());
                assertNotNull(item.getItemProperty("tenantName").getValue());
                assertNotNull(item.getItemProperty("workItemStatus").getValue());
                assertNotNull(item.getItemProperty("workflowInstanceId")
                        .getValue());
                assertNotNull(item.getItemProperty("workItemName").getValue());
            }
        }
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

        UserProfile newProfile = new UserProfile();
        newProfile.setUsername(TEST_USERNAME + "asd");
        adminFacade.setProfile(testUserID, newProfile);
        assertNotNull(getProfile(adminFacade.getUserProfile(testUserID, false),
                false).getKey());

        // make sure fields are deleted
        userProfile = getProfile(adminFacade.getUserProfile(testUserID), true);
        assertNotNull(userProfile.getKey());
        testProfile = userProfile.getValue();
        assertEquals(null, testProfile.getCity());
        assertEquals(null, testProfile.getFirstName());
        assertEquals(null, testProfile.getLastName());
        assertEquals(null, testProfile.getPostalCode());
        assertEquals(null, testProfile.getStreet());
        assertEquals(TEST_USERNAME + "asd", testProfile.getUsername());

        try {
            adminFacade.setProfile(testUserID, null);
            fail("managed to set null profile");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
    }

    /**
     * Test method for {@link UserFacade#confirmRegistration(Long, String)} and
     * {@link UserFacade#isRegistered(Long)}.
     */
    @Test
    public void testRegistration() throws TransactionException {
        String authKey;
        long invalidID = getInvalidUserID();

        UserProfile testProfile = new UserProfile();
        testProfile.setFirstName("test");
        testProfile.setLastName("user");
        testProfile.setCity("boringtown");
        testProfile.setStreet("ancient st.");
        testProfile.setPostalCode("112");
        testProfile.setUsername(USERNAME_PREFIX);

        testProfile.setUsername(USERNAME_PREFIX + "1");
        Long userId = adminFacade.registerUser(getTestEmail(1), "asd",
                testProfile);

        assertFalse(adminFacade.isRegistered(testUserID));
        assertFalse(userFacade.isRegistered(testUserID));

        User user = (User) session.get(User.class, testUserID);
        assertNotNull(user);
        RegistrationRequest request = user.getRegistrationRequest();
        authKey = request.getAuthKey();

        try {
            nullFacade.isRegistered(testUserID);
            fail("checking registration with null facade succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        try {
            adminFacade.confirmRegistration(testUserID, null);
            fail("confirming registration with null authkey succeeded");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.confirmRegistration(testUserID, "");
            fail("confirming registration with empty authkey succeeded");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.confirmRegistration(testUserID, "wrong");
            fail("confirming registration with wrong authkey succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        try {
            nullFacade.confirmRegistration(testUserID, authKey);
            fail("confirming registration with null facade succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        adminFacade.confirmRegistration(testUserID, authKey);
        try {
            adminFacade.confirmRegistration(testUserID, authKey);
            fail("confirming registration twice succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        /*
         * synchronizes the session - otherwise session.get(User.class, userId)
         * returns null due to transaction isolation.
         */
        session.beginTransaction().commit();

        user = (User) session.get(User.class, userId);
        assertNotNull(user);
        request = user.getRegistrationRequest();
        authKey = request.getAuthKey();

        UserFacade userFacade = new UserFacade(new UserRole(userId));
        userFacade.confirmRegistration(userId, authKey);
        try {
            userFacade.confirmRegistration(userId, authKey);
            fail("confirming registration twice succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        try {
            adminFacade.confirmRegistration(invalidID, authKey);
            fail("confirming registration with invalid ID succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        assertTrue(adminFacade.isRegistered(testUserID));
        assertTrue(userFacade.isRegistered(testUserID));

        try {
            nullFacade.isRegistered(testUserID);
            fail("checking registration with null facade succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        try {
            adminFacade.isRegistered(invalidID);
            fail("checking registration with non-existend ID succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
    }

    /**
     * Test method for {@link UserFacade#setDisabledSince(Long, Date)}.
     */
    @Test
    public void testSetDisabledSince() throws TransactionException {
        try {
            userFacade.setDisabledSince(testUserID, new Date());
            fail("setting user disabled with normal user facade succeeded.");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            nullFacade.setDisabledSince(testUserID, new Date());
            fail("setting user disabled with null facade succeeded.");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        // Disabling the superadmin user account must not have any effect.
        adminFacade.setDisabledSince(adminFacade.actor.getActorId(),
                DecidrGlobals.getTime().getTime());
        assertTrue(new UserIsEnabledAsserter().assertRule(adminFacade.actor,
                null));

        Date testDate = DecidrGlobals.getTime(true).getTime();
        adminFacade.setDisabledSince(testUserID, testDate);
        assertEquals(testDate, adminFacade.getUserProfile(testUserID)
                .getItemProperty("disabledSince").getValue());

        testDate = DecidrGlobals.getTime(true).getTime();
        adminFacade.setDisabledSince(testUserID, testDate);
        assertEquals(testDate, adminFacade.getUserProfile(testUserID)
                .getItemProperty("disabledSince").getValue());

        testDate = new Date((new Date().getTime() - 1000000) / 1000 * 1000);
        adminFacade.setDisabledSince(testUserID, testDate);
        assertEquals(testDate, adminFacade.getUserProfile(testUserID)
                .getItemProperty("disabledSince").getValue());

        testDate = new Date((new Date().getTime() + 1000000) / 1000 * 1000);
        adminFacade.setDisabledSince(testUserID, testDate);
        assertEquals(testDate, adminFacade.getUserProfile(testUserID)
                .getItemProperty("disabledSince").getValue());

        adminFacade.setDisabledSince(testUserID, null);
        assertNull(adminFacade.getUserProfile(testUserID).getItemProperty(
                "disabledSince").getValue());
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
        testProfile.setUsername(USERNAME_PREFIX);

        Long secondUserID = adminFacade.registerUser(getTestEmail(2), "asd",
                testProfile);

        adminFacade.setEmailAddress(testUserID, TEST_EMAIL + ".vu");
        assertEquals(TEST_EMAIL + ".vu", adminFacade.getUserProfile(testUserID,
                true).getItemProperty("email").getValue());
        adminFacade.setEmailAddress(testUserID, TEST_EMAIL);
        adminFacade.setEmailAddress(testUserID, TEST_EMAIL);

        for (UserFacade facade : new UserFacade[] { userFacade, adminFacade,
                nullFacade }) {
            setEmailAddressExceptionHelper("setting same email succeeded",
                    facade, secondUserID, TEST_EMAIL);
            setEmailAddressExceptionHelper("setting null email succeeded",
                    facade, testUserID, null);
            setEmailAddressExceptionHelper("setting empty email succeeded",
                    facade, testUserID, "");
            setEmailAddressExceptionHelper(
                    "setting email for null user ID succeeded", facade, null,
                    getTestEmail(4));
        }

        setEmailAddressExceptionHelper(
                "setting email with null facade succeeded", nullFacade,
                testUserID, TEST_EMAIL);
        userFacade.setEmailAddress(testUserID, TEST_EMAIL);
    }

    /**
     * Test method for {@link UserFacade#setPassword(Long, String, String)}.
     */
    @Test
    public void testSetPassword() throws TransactionException {
        UserFacade userFacade = new UserFacade(new UserRole(testUserID));

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

        try {
            userFacade.setPassword(testUserID, null, "Fuuni PWD");
            resetPWD();
            fail("setting null password using admin facade succeeded");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }

        try {
            adminFacade.setPassword(testUserID, TEST_PASSWORD, null);
            resetPWD();
            fail("setting null password using admin facade succeeded");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
        try {
            userFacade.setPassword(testUserID, TEST_PASSWORD, null);
            resetPWD();
            fail("setting null password using normal user facade succeeded");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
        try {
            nullFacade.setPassword(testUserID, TEST_PASSWORD, null);
            resetPWD();
            fail("setting null password using null facade succeeded");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
    }

    /**
     * Test method for {@link UserFacade#setUnavailableSince(Long, Date)}.
     */
    @Test
    public void testSetUnavailableSince() throws TransactionException {
        UserFacade userFacade = new UserFacade(new BasicRole(0L));

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
        // remove milliseconds
        testDate.setTime((testDate.getTime() / 1000) * 1000);
        adminFacade.setUnavailableSince(testUserID, testDate);
        assertEquals(testDate, adminFacade.getUserProfile(testUserID)
                .getItemProperty("unavailableSince").getValue());

        testDate = DecidrGlobals.getTime().getTime();
        testDate.setTime(testDate.getTime() / 1000 * 1000);
        adminFacade.setUnavailableSince(testUserID, testDate);
        assertEquals(testDate, adminFacade.getUserProfile(testUserID)
                .getItemProperty("unavailableSince").getValue());

        testDate = new Date(testDate.getTime() - 1000000);
        adminFacade.setUnavailableSince(testUserID, testDate);
        assertEquals(testDate, adminFacade.getUserProfile(testUserID)
                .getItemProperty("unavailableSince").getValue());

        testDate = new Date(testDate.getTime() + 2000000);
        adminFacade.setUnavailableSince(testUserID, testDate);
        assertEquals(testDate, adminFacade.getUserProfile(testUserID)
                .getItemProperty("unavailableSince").getValue());

        adminFacade.setDisabledSince(testUserID, null);
        assertNull(adminFacade.getUserProfile(testUserID).getItemProperty(
                "disabledSince").getValue());
    }

    /**
     * Test method for {@link UserFacade#setCurrentTenantId(Long, Long)} and
     * {@link UserFacade#getCurrentTenantId(Long)},
     * {@link UserFacade#getJoinedTenants(Long)},
     * {@link UserFacade#getUserRoleForTenant(Long, Long)} and
     * {@link UserFacade#removeFromTenant(Long, Long)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testTenant() throws TransactionException {
        Long invalidUserID = getInvalidUserID();
        Long tenantUserID = null;
        Long tenantID = null;
        List<Item> joinedTenants;

        // find first tenant with a user and get that user's ID
        for (Tenant t : (List<Tenant>) session.createQuery("FROM Tenant")
                .list()) {
            if (t.getUserIsMemberOfTenants().iterator().hasNext()) {
                tenantID = t.getId();
                tenantUserID = t.getUserIsMemberOfTenants().iterator().next()
                        .getUser().getId();
                break;
            }
        }
        assertNotNull("Incomplete test data: no tenant currently has a user",
                tenantID);
        assertNotNull("Incomplete test data: no tenant currently has a user",
                tenantUserID);

        UserFacade userFacade = new UserFacade(new UserRole(tenantUserID));

        try {
            nullFacade.setCurrentTenantId(testUserID, null);
            fail("could set current tenant with null facade");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            nullFacade.getCurrentTenantId(testUserID);
            fail("could get current tenant with null facade");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            userFacade.setCurrentTenantId(null, null);
            fail("could set current tenant with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
        try {
            userFacade.getCurrentTenantId(null);
            fail("could get current tenant with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.setCurrentTenantId(null, tenantID);
            fail("could set current tenant with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.getCurrentTenantId(null);
            fail("could get current tenant with null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.setCurrentTenantId(invalidUserID, tenantID);
            fail("could set current tenant with invalid user ID");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.getCurrentTenantId(invalidUserID);
            fail("could get current tenant with null invalid user ID");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.setCurrentTenantId(invalidUserID, null);
            fail("managed to set default current tenant ID with invalid user ID");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        adminFacade.setCurrentTenantId(testUserID, null);

        userFacade.setCurrentTenantId(tenantUserID, tenantID);
        assertEquals(tenantID, userFacade.getCurrentTenantId(tenantUserID));
        adminFacade.setCurrentTenantId(tenantUserID, tenantID);
        assertEquals(tenantID, adminFacade.getCurrentTenantId(tenantUserID));

        userFacade.setCurrentTenantId(tenantUserID, null);
        assertNull(userFacade.getCurrentTenantId(tenantUserID));

        adminFacade.setCurrentTenantId(tenantUserID, null);
        assertNull(userFacade.getCurrentTenantId(tenantUserID));

        adminFacade.setCurrentTenantId(testUserID, tenantID);

        joinedTenants = userFacade.getJoinedTenants(tenantUserID);
        assertFalse(joinedTenants.isEmpty());
        for (Item item : joinedTenants) {
            assertNotNull(item.getItemProperty("id").getValue());
            assertNotNull(item.getItemProperty("name").getValue());
            assertNotNull(userFacade.getUserRoleForTenant(tenantUserID,
                    (Long) item.getItemProperty("id").getValue()));
        }

        joinedTenants = adminFacade.getJoinedTenants(tenantUserID);
        assertFalse(joinedTenants.isEmpty());
        for (Item item : joinedTenants) {
            assertNotNull(item.getItemProperty("id").getValue());
            assertNotNull(item.getItemProperty("name").getValue());
            assertNotNull(adminFacade.getUserRoleForTenant(tenantUserID,
                    (Long) item.getItemProperty("id").getValue()));
        }

        userFacade = new UserFacade(new UserRole(testUserID));
        assertTrue(userFacade.getJoinedTenants(testUserID).isEmpty());
        assertTrue(adminFacade.getJoinedTenants(testUserID).isEmpty());
        assertNull(userFacade.getUserRoleForTenant(testUserID, tenantID));
        assertNull(adminFacade.getUserRoleForTenant(testUserID, tenantID));
        assertFalse(adminFacade.removeFromTenant(testUserID, tenantID));

        try {
            // DH so users can't leave tenants? ~rr
            // RR users can leave their tenant -> LeaveTenantCommand. Tenant
            // admins can remove users from their tenant ->
            // RemoveFromTenantCommand.
            assertFalse(userFacade.removeFromTenant(testUserID, tenantID));
            fail("Managed to remove user from tenant using normal user facade");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            nullFacade.getJoinedTenants(testUserID);
            fail("Managed to get joined tenants with null facade");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            nullFacade.getUserRoleForTenant(tenantUserID, tenantID);
            fail("Managed to get user role for tenant with null facade");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            nullFacade.removeFromTenant(tenantUserID, tenantID);
            fail("Managed to remove user from tenant with null facade");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            userFacade.getJoinedTenants(null);
            fail("Managed to get joined tenants with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
        try {
            userFacade.getUserRoleForTenant(null, tenantID);
            fail("Managed to get user role for tenant with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
        try {
            userFacade.getUserRoleForTenant(tenantUserID, null);
            fail("Managed to get user role for tenant with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
        try {
            userFacade.removeFromTenant(null, tenantID);
            fail("Managed to remove user from tenant with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
        try {
            userFacade.removeFromTenant(tenantUserID, null);
            fail("Managed to remove user from tenant with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.getJoinedTenants(null);
            fail("Managed to get joined tenants with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.getUserRoleForTenant(null, tenantID);
            fail("Managed to get user role for tenant with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.getUserRoleForTenant(tenantUserID, null);
            fail("Managed to get user role for tenant with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.removeFromTenant(null, tenantID);
            fail("Managed to remove user from tenant with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.removeFromTenant(tenantUserID, null);
            fail("Managed to remove user from tenant with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.getJoinedTenants(invalidUserID);
            fail("Managed to get joined tenants with invalid user ID");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.getUserRoleForTenant(invalidUserID, tenantID);
            fail("Managed to get user role for tenant with invalid user ID");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.removeFromTenant(invalidUserID, tenantID);
            fail("Managed to remove user from tenant with invalid user ID");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
    }
}
