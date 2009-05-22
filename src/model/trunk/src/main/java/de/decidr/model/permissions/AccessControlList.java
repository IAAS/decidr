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

package de.decidr.model.permissions;

/**
 * The access control list stores a set of rules that define which roles have
 * access to certain permissions and which conditions must be met so that access
 * is granted.
 * 
 * It acts as a white list, i.e. access is denied by default to all roles and
 * permissions and must be explicitly granted.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public interface AccessControlList {

    /**
     * Removes all rules from the access control list.
     */
    public void clearRules();

    /**
     * Adds a new rule to the access control list. There can only be one rule
     * for each pair of role <-> permission. If a rule for the given role and
     * permission already exists, it will be overwritten.
     * 
     * @param role
     *            the role requesting accesss
     * @param permission
     *            the resource to which access is granted or denied
     * @param asserters
     *            the conditions under which access is granted or denied
     * @param mode
     *            whether all or just one asserter must return true in order to
     *            grant access.
     */
    public void setRule(Role role, Permission permission, Asserter[] asserters,
            AssertMode mode);

    /**
     * Grants access to the given role and permission.
     * 
     * @param role
     *            the role to which access is granted
     * @param permission
     *            the resource to which access is granted
     */
    public void allow(Role role, Permission permission);

    /**
     * Checks whether the given role has access to the given permission. If no
     * rule has been set, accesss is denied.
     * 
     * @param role
     *            the role requesting access
     * @param permission
     *            the permission to which access is requested
     * @return true if access is granted, false otherwise.
     */
    public Boolean isAllowed(Role role, Permission permission);
}