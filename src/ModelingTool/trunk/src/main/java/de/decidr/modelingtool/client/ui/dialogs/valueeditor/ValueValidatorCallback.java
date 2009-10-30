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

import de.decidr.modelingtool.client.ModelingToolWidget;

/**
 * A callback class which serves as a container to hold the message, which
 * inputs of which textfields of the {@link ValueEditorWindow} have a wrong
 * format.
 * 
 * @author Jonas Schlaak
 */
public class ValueValidatorCallback {

    private SortedSet<Integer> values = new TreeSet<Integer>();

    /**
     * Returns the callback message
     * 
     * @return the callback message
     */
    public String getMessage() {
        String message = new String();
        if (values.size() == 1) {
            message = ModelingToolWidget.getMessages().valueSingular()
                    + values.first() + " "
                    + ModelingToolWidget.getMessages().wrongSingular();
        } else {
            message = ModelingToolWidget.getMessages().valuePlural();
            for (Iterator<Integer> i = values.iterator(); i.hasNext();) {
                message = message + i.next();
                if (i.hasNext()) {
                    message = message + ", ";
                }
            }
            message = message + " "
                    + ModelingToolWidget.getMessages().wrongPlural();
        }
        return message;
    }

    /**
     * Adds the number of a text fields which input has a wrong format to the
     * callback message.
     * 
     * @param valueNumber
     *            the number of the value that has an incorrect format
     */
    public void addValueToMessage(int valueNumber) {
        values.add(valueNumber);
    }

}
