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
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
@SuppressWarnings("serial")
public class RoleEditorUser extends BaseModelData {

    public static final String USERID = "userid";
    public static final String DISPLAYNAME = "displayname";

    /**
     * TODO: add comment
     * 
     */
    public RoleEditorUser(Long userId, String displayName) {
        this.setUserId(userId);
        this.setDisplayName(displayName);
    }

    /**
     * TODO: add comment
     * 
     * @return the userid
     */
    public Long getUserId() {
        return get(USERID);
    }

    /**
     * TODO: add comment
     * 
     * @param userId
     *            the userid to set
     */
    public void setUserId(Long userId) {
        set(USERID, userId);
    }

    /**
     * TODO: add comment
     * 
     * @return the username
     */
    public String getDisplayName() {
        return get(DISPLAYNAME);
    }

    /**
     * TODO: add comment
     * 
     * @param username
     *            the username to set
     */
    public void setDisplayName(String username) {
        set(DISPLAYNAME, username);
    }

}
