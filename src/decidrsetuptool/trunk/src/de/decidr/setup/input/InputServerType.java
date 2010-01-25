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

/**
 * Returns the SQL script of the server_type table.
 * 
 * @author Johannes Engelhardt
 */
public class InputServerType {

    public static String getSql() {
        return "REPLACE INTO `server_type` (`id`, `name`) VALUES (1, 'Ode'), "
                + "(2, 'WebPortal'), (3, 'Esb');\n\n";
    }
}
