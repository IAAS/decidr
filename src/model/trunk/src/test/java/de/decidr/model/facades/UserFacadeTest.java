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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

import de.decidr.model.DatabaseTestsuite;
import de.decidr.model.LowLevelDatabaseTest;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;

/**
 * Test case for <code>{@link UserFacade}</code>. Some of the methods can't be
 * tested properly within the confines of a unit test, as they interact with web
 * services.
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
     * {@link UserFacade#registerUser(String, String, Item)}.
     */
    @BeforeClass
    public static void setUpBeforeClass() throws NullPointerException,
            TransactionException {
        if (!DatabaseTestsuite.running()) {
            fail("Needs to run inside " + DatabaseTestsuite.class.getName());
        }

        adminFacade = new UserFacade(new SuperAdminRole());
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

        Item testItem = new BeanItem(testProfile, new String[] { "firstName",
                "lastName", "city", "street", "postalCode", "username" });

        registerUserExceptionHelper(
                "registering user with null facade succeeded", nullFacade,
                "asd@desk.de", "asd", testItem);
        registerUserExceptionHelper(
                "registering user with normal user facade succeeded",
                userFacade, "asd@desk.de", "asd", testItem);

        testProfile.setUsername("testuser1");
        testItem = new BeanItem(testProfile, new String[] { "firstName",
                "lastName", "city", "street", "postalCode", "username" });
        adminFacade.registerUser("asd1@desk.de", "asd", testItem);

        testProfile.setUsername("testuser2");
        testItem = new BeanItem(testProfile, new String[] { "firstName",
                "lastName", "city", "street", "postalCode", "username" });
        registerUserExceptionHelper("registering user twice succeeded",
                adminFacade, "asd1@desk.de", "asd", testItem);

        testProfile.setUsername("testuser3");
        testItem = new BeanItem(testProfile, new String[] { "firstName",
                "lastName", "city", "street", "postalCode", "username" });
        adminFacade.registerUser("asd2@desk.de", "", testItem);

        testProfile.setUsername("testuser4");
        testItem = new BeanItem(testProfile, new String[] { "firstName",
                "lastName", "city", "street", "postalCode", "username" });
        registerUserExceptionHelper(
                "registering user with null password succeeded", adminFacade,
                "asd3@desk.de", null, testItem);

        testProfile.setUsername("testuser5");
        testItem = new BeanItem(testProfile, new String[] { "firstName",
                "lastName", "city", "street", "postalCode", "username" });
        registerUserExceptionHelper(
                "registering user with empty email succeeded", adminFacade, "",
                "asd", testItem);
        registerUserExceptionHelper(
                "registering user with null email succeeded", adminFacade,
                null, "asd", testItem);

        registerUserExceptionHelper(
                "registering user with null profile succeeded", adminFacade,
                "asd4@desk.de", "asd", null);

        registerUserExceptionHelper(
                "registering user with empty profile succeeded", adminFacade,
                "asd5@desk.de", "asd", new BeanItem(new UserProfile()));

        testProfile.setFirstName(null);
        testProfile.setLastName(null);
        testProfile.setCity(null);
        testProfile.setStreet(null);
        testProfile.setPostalCode(null);
        testProfile.setUsername("testuser6");
        testItem = new BeanItem(testProfile, new String[] { "firstName",
                "lastName", "city", "street", "postalCode", "username" });
        adminFacade.registerUser("asd6@desk.de", "asd", testItem);

        testProfile.setUsername(null);
        testItem = new BeanItem(testProfile, new String[] { "firstName",
                "lastName", "city", "street", "postalCode", "username" });
        registerUserExceptionHelper(
                "invalid profile (empty username) succeeded", adminFacade,
                "asd@desk.de", "asd", testItem);
        testProfile.setUsername("testuser");
    }

    private static void registerUserExceptionHelper(String failmsg,
            UserFacade facade, String email, String passwd, Item profile) {
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

        Item profileItem = new BeanItem(classProfile, new String[] {
                "firstName", "lastName", "city", "street", "postalCode",
                "username" });
        testUserID = adminFacade.registerUser(TEST_EMAIL, TEST_PASSWORD,
                profileItem);
    }

    @After
    public void tearDownTestCase() {
        // RR delete all users
        session.createQuery("delete from User").executeUpdate();
    }

    /**
     * Test method for {@link UserFacade#getAllUsers(List, Paginator)}.
     */
    @SuppressWarnings("null")
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
        UserProfile testProfile = new UserProfile();
        testProfile.setFirstName("test");
        testProfile.setLastName("user");
        testProfile.setCity("boringtown");
        testProfile.setStreet("ancient st.");
        testProfile.setPostalCode("112");
        testProfile.setUsername(TEST_EMAIL);

        Item testItem = new BeanItem(testProfile, new String[] { "firstName",
                "lastName", "city", "street", "postalCode", "username" });
        Long emailUserID = adminFacade.registerUser(TEST_EMAIL + ".vu",
                TEST_PASSWORD, testItem);

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
                facade.getUserIdByLogin(TEST_EMAIL, null);
            } catch (EntityNotFoundException e) {
                // supposed to be thrown
            }
            try {
                facade.getUserIdByLogin(TEST_EMAIL, "");
            } catch (EntityNotFoundException e) {
                // supposed to be thrown
            }
            try {
                facade.getUserIdByLogin(TEST_EMAIL, "asdfgthsf");
            } catch (EntityNotFoundException e) {
                // supposed to be thrown
            }

            assertEquals(emailUserID, facade.getUserIdByLogin(TEST_EMAIL, null));
            assertEquals(emailUserID, facade.getUserIdByLogin(TEST_EMAIL, ""));
            assertEquals(emailUserID, facade.getUserIdByLogin(TEST_EMAIL,
                    "sdfsdfsdf"));
            assertEquals(emailUserID, facade.getUserIdByLogin(TEST_EMAIL,
                    TEST_PASSWORD));
            assertEquals(emailUserID, facade.getUserIdByLogin(TEST_EMAIL
                    + ".vu", TEST_PASSWORD));
            try {
                facade.getUserIdByLogin(TEST_EMAIL + ".vu", null);
            } catch (EntityNotFoundException e) {
                // supposed to be thrown
            }
            try {
                facade.getUserIdByLogin(TEST_EMAIL + ".vu", "");
            } catch (EntityNotFoundException e) {
                // supposed to be thrown
            }
            try {
                facade.getUserIdByLogin(TEST_EMAIL + ".vu", "asdfgthsf");
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
        adminFacade.setEmailAddress(testUserID, TEST_EMAIL + ".vu");
        fail("Not yet implemented"); // RR setEmailAddress
        adminFacade.setEmailAddress(testUserID, TEST_EMAIL);
    }

    /**
     * Test method for {@link UserFacade#setDisableSince(Long, Date)}.
     */
    @Test
    public void testSetDisableSince() {
        fail("Not yet implemented"); // RR setDisableSince
    }

    /**
     * Test method for {@link UserFacade#setUnavailableSince(Long, Date)}.
     */
    @Test
    public void testSetUnavailableSince() {
        fail("Not yet implemented"); // RR setUnavailableSince
    }

    /**
     * Test method for {@link UserFacade#setPassword(Long, String, String)}.
     */
    @Test
    public void testSetPassword() {
        fail("Not yet implemented"); // RR setPassword
    }

    /**
     * Test method for {@link UserFacade#setProfile(Long, Item)} and
     * {@link UserFacade#getUserProfile(Long)}.
     */
    @Test
    public void testProfile() {
        fail("Not yet implemented"); // RR setProfile
        fail("Not yet implemented"); // RR getUserProfile
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
        fail("Not yet implemented"); // RR confirmChangeEmailRequest
    }

    /**
     * Test method for {@link UserFacade#getInvitation(Long)} and
     * {@link UserFacade#authKeyMatches(Long, String)}.
     */
    @Test
    public void testInvitation() {
        fail("Not yet implemented"); // RR getInvitation
        fail("Not yet implemented"); // RR authKeyMatches
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
     * Test method for {@link UserFacade#getWorkItems(Long, List, Paginator)}.
     */
    @Test
    public void testGetWorkItems() {
        fail("Not yet implemented"); // RR getWorkItems
    }
}
