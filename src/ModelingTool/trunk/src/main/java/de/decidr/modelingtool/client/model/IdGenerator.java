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

package de.decidr.modelingtool.client.model;

import java.util.Date;

/**
 * A generator class for unique ids.
 * 
 * @author Jonas Schlaak
 */
public class IdGenerator {

    public static Long id = 0L;

    /**
     * Generates a unique id. For the id, the following is true: id =
     * max(timestamp, last generated id + 1)
     * 
     * @return the generated id
     */
    public static Long generateId() {
        /*
         * To ensure that the generated ids are unique, timestamp has to be
         * greater that the last generated id.
         */
        Long time = new Date().getTime();
        if (time > id) {
            id = time;
        } else {
            id++;
        }
        return id;
    }

    /**
     * Sets an "highest id" for this creator, i.e. all following generated ids
     * must me greater than this id.
     * 
     * @param highestId
     *            the highest id
     */
    public static void setHighestId(Long highestId) {
        id = highestId;
    }

}
