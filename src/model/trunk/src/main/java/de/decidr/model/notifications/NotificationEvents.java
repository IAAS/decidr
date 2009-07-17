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

package de.decidr.model.notifications;

import de.decidr.model.entities.Invitation;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.WorkItem;

/**
 * Provides a very simple means of sending notifications to users.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public final class NotificationEvents {

    // FIXME ChangeEmailRequestNotification

    public static void createdWorkItem(WorkItem newWorkItem) {

    }

    public static void createdPasswordResetRequest(User user) {
        // FIXME actually notify user
    }

    public static void disapprovedTenant(String email, String tenantName) {
        // FIXME actually notify user
    }

    public static void invitedRegisteredUserAsTenantMember(Invitation invitation) {
        // FIXME actually notify user
    }

    public static void invitedUnregisteredUserAsTenantMember(
            Invitation invitation) {
        // FIXME actually notify user
    }

    public static void removedFromTenant(User user, Tenant tenant) {
        // FIXME actually notify user
    }

    public static void refusedInvitation(Invitation invitation) {
        // FIXME actually notify user
    }

    public static void generatedNewPassword(User user, String newPassword) {
        // FIXME actually notify user

    }

    // where = null => anywhere
    public static void requestNewODEInstance(String where) {
        // FIXME actually notify user
    }

}
