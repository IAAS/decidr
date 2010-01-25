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

import de.decidr.setup.model.DefaultTenant;

/**
 * Retrieves information about a server from the user and generates an SQL
 * script.
 * 
 * @author Johannes Engelhardt
 */
public class InputDefaultTenant {
    private static final String NULL = "NULL";

    private static String createSql(DefaultTenant dt) {
        StringBuilder sql = new StringBuilder();
        sql.append("REPLACE INTO `tenant` (`id`,`name`,`description`,"
                + "`logoId`,`simpleColorSchemeId`,`advancedColorSchemeId`,"
                + "`currentColorSchemeId`,`approvedSince`,`adminId`)\n");

        sql.append("VALUES (" + dt.getId() + "," + dt.getName() + ","
                + dt.getDescription() + "," + NULL + "," + NULL + "," + NULL
                + "," + NULL + "," + dt.getApprovedSince() + ","
                + dt.getAdminID() + ");\n\n");

        return sql.toString();
    }

    public static String getSql() {
        DefaultTenant dt = getDefaultTenant();
        return createSql(dt);
    }

    public static DefaultTenant getDefaultTenant() {
        return new DefaultTenant();
    }
}
