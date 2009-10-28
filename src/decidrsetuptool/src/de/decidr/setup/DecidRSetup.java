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

package de.decidr.setup;

import de.decidr.setup.input.InputServer;
import de.decidr.setup.input.InputServerType;
import de.decidr.setup.input.InputSuperAdmin;
import de.decidr.setup.input.InputSystemSettings;

/**
 * TODO: add comment
 * 
 * @author Johannes Engelhardt
 */
public class DecidRSetup {

    /**
     * TODO: add comment
     * 
     * @param args
     */
    public static void main(String[] args) {
        StringBuilder sql = new StringBuilder();

        sql.append(InputSuperAdmin.getSql());
        sql.append(InputSystemSettings.getSql());
        sql.append(InputServerType.getSql());

        // web portal
        sql.append(InputServer.getSql(2, "http://localhost:8080/WebPortal"));
        // esb
        sql.append(InputServer.getSql(3, "http://localhost:8280"));
        // storage
        sql.append(InputServer.getSql(4, "http://localhost:8080/Storage"));
        // ode
        sql.append(InputServer.getSql(1, "http://localhost:8080/ode"));

        System.out.println(sql.toString());
    }

}
