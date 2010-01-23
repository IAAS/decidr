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

package de.decidr.modelingtool.client.ui.dialogs.valueeditor;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * This class models the user object used in {@link RoleEditor}. It has a user
 * id of type long (which is the id in the user database) and a display name
 * string to make the user object "human readable".
 * 
 * @author Jonas Schlaak
 */
@SuppressWarnings("serial")
public class RoleEditorUser extends BaseModelData {

    public static final String USERID = "userid";
    public static final String DISPLAYNAME = "displayname";

    /**
     * Constructs a user object with a given id and display name
     */
    public RoleEditorUser(Long userId, String displayName) {
        this.setUserId(userId);
        this.setDisplayName(displayName);
    }

    /**
     * Gets the display name.
     * 
     * @return the username
     */
    public String getDisplayName() {
        return get(DISPLAYNAME);
    }

    /**
     * Gets the user id.
     * 
     * @return the userid
     */
    public Long getUserId() {
        return get(USERID);
    }

    /**
     * Sets the display name.
     * 
     * @param username
     *            the username to set
     */
    public void setDisplayName(String username) {
        set(DISPLAYNAME, username);
    }

    /**
     * Sets the user id.
     * 
     * @param userId
     *            the userid to set
     */
    public void setUserId(Long userId) {
        set(USERID, userId);
    }

}
