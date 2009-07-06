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

import de.decidr.modelingtool.client.ModelingTool;

/**
 * This enumaration type holds all possible exit condition types a for each
 * container can have.
 * 
 * @author Jonas Schlaak
 */
public enum ExitCondition {
    AND(ModelingTool.messages.andConLabel()), XOR(ModelingTool.messages
            .xorConLabel());

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

    public String getLabel() {
        return label;
    }

    public static ExitCondition getTypeFromLabel(String localName) {
        ExitCondition result = null;
        for (ExitCondition e : ExitCondition.values()) {
            if (localName == e.getLabel())
                result = e;
        }
        return result;
    }
}
