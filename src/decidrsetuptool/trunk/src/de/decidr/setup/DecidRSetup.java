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

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.setup.input.FileIO;
import de.decidr.setup.input.InputDefaultTenant;
import de.decidr.setup.input.InputServer;
import de.decidr.setup.input.InputServerType;
import de.decidr.setup.input.InputSuperAdmin;
import de.decidr.setup.input.InputSystemSettings;

/**
 * The main class.
 * 
 * @author Johannes Engelhardt
 */
@Reviewed(currentReviewState = State.Passed, reviewers = { "RR" }, lastRevision = "2670")
public class DecidRSetup {

    public static void main(String[] args) {
        StringBuilder sql = new StringBuilder();
        sql.append(InputSuperAdmin.getSql());
        sql.append(InputSystemSettings.getSql());
        sql.append(InputServerType.getSql());

        // web portal
        sql.append(InputServer.getSql(2, "localhost:8080"));
        // esb
        sql.append(InputServer.getSql(3, "localhost:8080"));
        // ode
        sql.append(InputServer.getSql(1, "localhost:8080"));

        sql.append(InputDefaultTenant.getSql());

        FileIO.writeToFile(sql.toString());
    }
}
