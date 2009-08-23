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

import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.vaadin.data.Item;

import de.decidr.model.TransactionTest;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.filters.Paginator;

/**
 * Test case for <code>{@link UserFacade}</code>.
 * 
 * @author Reinhold
 */
public class UserFacadeTest extends TransactionTest {

    static UserFacade adminFacade;
    static UserFacade userFacade;
    static UserFacade nullFacade;

    /**
     * Initialises the facade instances and registers a User, testing
     * {@link UserFacade#registerUser(String, String, Item)}.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        adminFacade = new UserFacade(new SuperAdminRole());
        userFacade = new UserFacade(new BasicRole(0L));
        nullFacade = new UserFacade(null);
        fail("Not yet implemented"); // RR registerUser
    }

    /**
     * Test method for {@link UserFacade#getAllUsers(List, Paginator)}.
     */
    @Test
    public void testGetAllUsers() {
        fail("Not yet implemented"); // RR getAllUsers
    }

    /**
     * Test method for {@link UserFacade#getUserIdByLogin(String, String)}.
     */
    @Test
    public void testGetUserIdByLogin() {
        fail("Not yet implemented"); // RR getUserIdByLogin
    }

    /**
     * Test method for {@link UserFacade#authKeyMatches(Long, String)}.
     */
    @Test
    public void testAuthKeyMatches() {
        fail("Not yet implemented"); // RR authKeyMatches
    }

    /**
     * Test method for {@link UserFacade#setEmailAddress(Long, String)}.
     */
    @Test
    public void testSetEmailAddress() {
        fail("Not yet implemented"); // RR setEmailAddress
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
     * Test method for {@link UserFacade#leaveTenant(Long, Long)}.
     */
    @Test
    public void testLeaveTenant() {
        fail("Not yet implemented"); // RR leaveTenant
    }

    /**
     * Test method for {@link UserFacade#removeFromTenant(Long, Long)}.
     */
    @Test
    public void testRemoveFromTenant() {
        fail("Not yet implemented"); // RR removeFromTenant
    }

    /**
     * Test method for {@link UserFacade#requestPasswordReset(String)} and
     * {@link UserFacade#confirmPasswordReset(Long, String)}.
     */
    @Test
    public void testPasswordReset() {
        fail("Not yet implemented"); // RR requestPasswordReset
        fail("Not yet implemented"); // RR confirmPasswordReset
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
     * Test method for {@link UserFacade#getInvitation(Long)},
     * {@link UserFacade#confirmInvitation(Long)} and
     * {@link UserFacade#refuseInviation(Long)}.
     */
    @Test
    public void testInvitation() {
        fail("Not yet implemented"); // RR getInvitation
        fail("Not yet implemented"); // RR confirmInvitation
        fail("Not yet implemented"); // RR refuseInviation
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
