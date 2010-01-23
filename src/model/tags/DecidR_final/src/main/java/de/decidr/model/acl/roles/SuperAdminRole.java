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
package de.decidr.model.acl.roles;

/**
 * Represents the super admin.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class SuperAdminRole extends TenantAdminRole {

    /**
     * Creates a new SuperAdminRole with its actor ID set to unknown;
     */
    public SuperAdminRole() {
        this(UNKNOWN_USER_ID);
    }

    /**
     * Constructor.
     * 
     * @param userId
     */
    public SuperAdminRole(Long userId) {
        super(userId);
    }
}