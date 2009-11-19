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

package de.decidr.setup.model;

public class Server {

    private String id = "''";
    private String location;
    private String load = "'0'";
    private String locked = "'0'";
    private String dynamicallyAdded = "'0'";
    private String serverTypeId;
    private String lastLoadUpdate = "NULL";

    public String getDynamicallyAdded() {
        return dynamicallyAdded;
    }

    public String getId() {
        return id;
    }

    public String getLastLoadUpdate() {
        return lastLoadUpdate;
    }

    public String getLoad() {
        return load;
    }

    public String getLocation() {
        return location;
    }

    public String getLocked() {
        return locked;
    }

    public String getServerTypeId() {
        return serverTypeId;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setServerTypeId(String serverTypeId) {
        this.serverTypeId = serverTypeId;
    }

}
