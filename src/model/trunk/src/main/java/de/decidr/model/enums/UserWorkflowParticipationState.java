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

package de.decidr.model.enums;

/**
 * The state of a user regarding the question
 * "what do I have to do to allow this user to participate in a workflow instance?"
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public enum UserWorkflowParticipationState {
    IsUnknownUser, NeedsTenantMembership, IsAlreadyTenantMember
}
