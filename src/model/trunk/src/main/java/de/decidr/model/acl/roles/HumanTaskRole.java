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
 * The role representing the DecidR Human Task Web service.
 * 
 * @author Daniel Huss
 * @author Reinhold
 * @version 0.1
 */
public class HumanTaskRole extends BasicRole {

    private static final Long HUMAN_TASK_ACTOR_ID = -1337L;

    private static HumanTaskRole instance = new HumanTaskRole();

    /**
     * @return the singleton instance.
     */
    public static HumanTaskRole getInstance() {
        return instance;
    }

    /**
     * Singleton constructor.
     */
    private HumanTaskRole() {
        super(HUMAN_TASK_ACTOR_ID);
    }
}
