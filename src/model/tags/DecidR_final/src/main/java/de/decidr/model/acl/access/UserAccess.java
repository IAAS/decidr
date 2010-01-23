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
package de.decidr.model.acl.access;

/**
 * This interface is usually used to flag commands. Commands flagged with
 * UserAccess are expected to access users. The methods allows the ACL to
 * determine which users are accessed.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public interface UserAccess extends Access {

    /**
     * 
     * @return accessed user ID
     */
    public Long[] getUserIds();
}
