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

import de.decidr.model.filters.Paginator;

/**
 * RR: add comment
 * 
 * @author Reinhold
 */
public class UserFacadeTest {

    /**
     * RR: add comment
     * 
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * Test method for {@link UserFacade#registerUser(String, String, Item)}.
     */
    @Test
    public void testRegisterUser() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#getUserIdByLogin(String, String)}.
     */
    @Test
    public void testGetUserIdByLogin() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#authKeyMatches(Long, String)}.
     */
    @Test
    public void testAuthKeyMatches() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#setEmailAddress(Long, String)}.
     */
    @Test
    public void testSetEmailAddress() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#setDisableSince(Long, Date)}.
     */
    @Test
    public void testSetDisableSince() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#setUnavailableSince(Long, Date)}.
     */
    @Test
    public void testSetUnavailableSince() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#setPassword(Long, String, String)}.
     */
    @Test
    public void testSetPassword() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#requestPasswordReset(String)}.
     */
    @Test
    public void testRequestPasswordReset() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#setProfile(Long, Item)}.
     */
    @Test
    public void testSetProfile() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#leaveTenant(Long, Long)}.
     */
    @Test
    public void testLeaveTenant() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#removeFromTenant(Long, Long)}.
     */
    @Test
    public void testRemoveFromTenant() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#confirmPasswordReset(Long, String)}.
     */
    @Test
    public void testConfirmPasswordReset() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#confirmRegistration(Long, String)}.
     */
    @Test
    public void testConfirmRegistration() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for
     * {@link UserFacade#confirmChangeEmailRequest(Long, String)}.
     */
    @Test
    public void testConfirmChangeEmailRequest() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#confirmInvitation(Long)}.
     */
    @Test
    public void testConfirmInvitation() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#refuseInviation(Long)}.
     */
    @Test
    public void testRefuseInviation() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#getUserProfile(Long)}.
     */
    @Test
    public void testGetUserProfile() {
        fail("Not yet implemented"); // R
    }

    /**
     * Test method for {@link UserFacade#getAllUsers(List, Paginator)}.
     */
    @Test
    public void testGetAllUsers() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#getHighestUserRole(Long)}.
     */
    @Test
    public void testGetHighestUserRole() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#getUserRoleForTenant(Long, Long)}.
     */
    @Test
    public void testGetUserRoleForTenant() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for
     * {@link facades.UserFacade#getAdminstratedWorkflowInstances(Long)}.
     */
    @Test
    public void testGetAdminstratedWorkflowInstances() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#getJoinedTenants(Long)}.
     */
    @Test
    public void testGetJoinedTenants() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#getAdministratedWorkflowModels(Long)}.
     */
    @Test
    public void testGetAdministratedWorkflowModels() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#getWorkItems(Long, List, Paginator)}.
     */
    @Test
    public void testGetWorkItems() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#getInvitation(Long)}.
     */
    @Test
    public void testGetInvitation() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link UserFacade#isRegistered(Long)}.
     */
    @Test
    public void testIsRegistered() {
        fail("Not yet implemented"); // RR
    }
}
