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

    public Permission(String name) {
        super(name);
    }
}