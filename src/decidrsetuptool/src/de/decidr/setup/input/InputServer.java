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

package de.decidr.setup.input;

import de.decidr.setup.helpers.StringRequest;
import de.decidr.setup.model.Server;

/**
 * TODO: add comment
 * 
 * @author Johannes Engelhardt
 */
public class InputServer {

    public static Server getServer(int serverTypeId, String defaultVal) {
	System.out.println("------------------------------------------------");
	System.out.println("Set up " + idToName(serverTypeId) + " Server");
	System.out.println("------------------------------------------------");

	Server srv = new Server();

	srv.setServerTypeId(Integer.toString(serverTypeId));
	srv.setLocation(StringRequest.getResult("Server Location", defaultVal));
	
	return srv;
    }

    private static String idToName(int serverTypeId) {
        switch (serverTypeId) {
        case 1:
            return "Ode";
        case 2:
            return "WebPortal";
        case 3:
            return "Esb";
        case 4:
            return "Storage";
        default:
            return "";
        }
    }

}
