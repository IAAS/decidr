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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.entities.SystemSettings;

/**
 * Provides generators for URLs like confirmation links
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class URLGenerator {
    
    //GH remove //, only for testing
    //private static SystemSettings settings = DecidrGlobals.getSettings();
    private static String encoding = "UTF-8";

    
    /**
     * 
     * Returns the URL for an invitation
     * GH: comment
     * 
     * @param userId
     * @param invitationId
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getInvitationURL(String userId, String invitationId) 
                         throws UnsupportedEncodingException{
        String url = "http://google.de/";// + settings.getDomain() + "/"; 
        url += "?uid=" + URLEncoder.encode(userId, encoding);
        url += "&iid=" + URLEncoder.encode(invitationId, encoding);
        return url;
    }
    
}
