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
package de.decidr.model.acl.permissions;

import java.security.BasicPermission;

/**
 * Represents a resource to which access is granted or denied.
 * 
 * The DecidR Permission follows the same naming convention as BasicPermission:
 * <P>
 * The name for a Permission is the name of the given permission (for example,
 * "exit", "setFactory", "print.queueJob", etc). The naming convention follows
 * the hierarchical property naming convention. An asterisk may appear by
 * itself, or if immediately preceded by a "." may appear at the end of the
 * name, to signify a wildcard match. For example, "*" and "java.*" are valid,
 * while "*java", "a*b", and "java*" are not valid.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class Permission extends BasicPermission {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new Permission with the given name.
     * 
     * @param name
     *            name of the new permission
     */
    public Permission(String name) {
        super(name);
    }

    @Override
    public boolean equals(Object other) {
        // consider permissions equal even if the class names differ.
        return (other instanceof Permission)
                && this.getName().equals(((Permission) other).getName());
    }

    /**
     * Returns a new instance of {@link Permission} that implies this
     * permission. This method guarantees that there is no other permission
     * besides the return value and this permission that also implies this
     * permission.
     * 
     * @return the next implying permission. If there is no implying permission,
     *         this instance is returned.
     */
    public Permission getNextImplyingPermission() {
        String name = getName();
        int lastSeparatorIndex = name.lastIndexOf(".");

        if (lastSeparatorIndex == -1) {
            if ("*".equals(name)) {
                // there is no implying permission for *
                return this;
            } else {
                return new Permission("*");
            }
        }

        String removedPart = name.substring(lastSeparatorIndex);
        String implyingName = name.substring(0, lastSeparatorIndex);

        if (!".*".equals(removedPart)) {
            // "permission.*" is implied by "permission", but
            // "workflowmodels.properties" is implied by "workflowmodels.*"
            implyingName += ".*";
        }

        return new Permission(implyingName);
    }
}