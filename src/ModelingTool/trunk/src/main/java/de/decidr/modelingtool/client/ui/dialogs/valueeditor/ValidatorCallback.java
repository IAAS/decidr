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

package de.decidr.modelingtool.client.ui.dialogs.valueeditor;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 
 * 
 * @author Jonas Schlaak
 */
public class ValidatorCallback {

    private SortedSet<Integer> values = new TreeSet<Integer>();

    /**
     * TODO: add comment
     * 
     * @return the callback message
     */
    public String getMessage() {
        String message = new String();
        if (values.size() == 1) {
            message = "The value " + values.first() + " has a wrong format.";
        } else {
            message = "The values ";
            for (Iterator<Integer> i = values.iterator(); i.hasNext();) {
                message = message + i.next();
                if (i.hasNext()) {
                    message = message + ", ";
                }
            }
            message = message + " have a wrong format.";
        }
        return message;
    }

    /**
     * 
     * TODO: add comment
     * 
     * @param valueNumber
     *            the number of the value that has an incorrect format
     */
    public void addValueToMessage(int valueNumber) {
        values.add(valueNumber);
    }

}
