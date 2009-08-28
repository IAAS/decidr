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

package de.decidr.modelingtool.client.io;

import java.util.HashMap;

/**
 * An interface for exchanging data between the Modeling Tool and the WebPortal.
 * 
 * @author Jonas Schlaak
 */
public interface DataExchanger {

    /**
     * Returns the workflow model that is to be edited by the modeling tool as
     * dwdl string.
     * 
     * @return the workflow model as dwdl string
     */
    public String loadDWDL();

    /**
     * This method is called by the modeling tool when the current workflow
     * model has to be saved.
     * 
     * @param dwdl
     *            the dwdl to be saved
     */
    public void saveDWDL(String dwdl);

    /**
     * 
     * Returns a list of the tenant user names with their id and an appropriate
     * display name.
     * 
     * @return the list of user names
     */
    public HashMap<Long, String> getUsers();

}
