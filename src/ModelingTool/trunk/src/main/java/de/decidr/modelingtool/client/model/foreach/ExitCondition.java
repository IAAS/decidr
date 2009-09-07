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

package de.decidr.modelingtool.client.model.foreach;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.ui.dialogs.foreachcontainer.ForEachWindow;

/**
 * This enumaration type holds all possible exit condition types a for each
 * container can have.
 * 
 * @author Jonas Schlaak
 */
public enum ExitCondition {

    AND(ModelingToolWidget.messages.andConLabel()),
    XOR(ModelingToolWidget.messages.xorConLabel());

    private String label;

    /**
     * 
     * Default constructor of an enumeration type
     * 
     * @param label
     *            the label of the radio button
     */
    private ExitCondition(String label) {
        this.label = label;
    }

    /**
     * Returns the label of an exit conditions. Labels are there to label the
     * radio buttons in {@link ForEachWindow}. Labels are localized.
     * 
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the type of exit condition for a given label.
     * 
     * @param label
     *            the label
     * @return the type of exit condition
     */
    public static ExitCondition getTypeFromLabel(String label) {
        ExitCondition result = null;
        for (ExitCondition e : ExitCondition.values()) {
            if (label == e.getLabel())
                result = e;
        }
        return result;
    }
}
