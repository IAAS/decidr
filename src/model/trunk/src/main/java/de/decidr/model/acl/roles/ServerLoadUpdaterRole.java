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
 * The role representing the DecidR ODE Monitoring Web service.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class ServerLoadUpdaterRole extends BasicRole {

    private static final Long SERVER_LOAD_UPDATER_ACTOR_ID = -0x1337L;

    private static ServerLoadUpdaterRole instance = new ServerLoadUpdaterRole();

    /**
     * @return the singleton instance.
     */
    public static ServerLoadUpdaterRole getInstance() {
        return instance;
    }

    /**
     * Singleton constructor.
     */
    private ServerLoadUpdaterRole() {
        super(SERVER_LOAD_UPDATER_ACTOR_ID);
    }
}
