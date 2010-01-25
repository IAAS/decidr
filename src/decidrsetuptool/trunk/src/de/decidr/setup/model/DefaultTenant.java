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

import de.decidr.setup.helpers.DateUtils;

public class DefaultTenant {

    private String id = "'1'";

    private String name = "'DefaultTenant'";

    private String description = "'This is the automatically generated default tenant.'";

    private String approvedSince = DateUtils.now();

    public String getAdminID() {
        return new SuperAdmin().getId();
    }

    public String getApprovedSince() {
        return approvedSince;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
