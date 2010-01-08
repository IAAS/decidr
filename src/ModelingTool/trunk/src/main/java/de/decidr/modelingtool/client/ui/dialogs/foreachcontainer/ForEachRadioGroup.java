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

package de.decidr.modelingtool.client.ui.dialogs.foreachcontainer;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;

import de.decidr.modelingtool.client.model.container.foreach.ExitCondition;

/**
 * This class is an extension of {@link RadioGroup}. Its purpose is to make it
 * easier to set and get values that are already of the type
 * {@link ExitCondition} from a group of radio buttons. The number and labels of
 * the radio buttons are automatically defined by the amount of different exit
 * conditions in {@link ExitCondition} their local names.
 * 
 * @author Jonas Schlaak
 */
public class ForEachRadioGroup extends RadioGroup {

    private List<Radio> radioList;

    public ForEachRadioGroup() {
        super();
        radioList = new ArrayList<Radio>();
        /*
         * Go through all values of the enum type ExitCondition and add it to
         * the RadioGroup and the radioList.
         */
        for (ExitCondition exitCon : ExitCondition.values()) {
            Radio radio = new Radio();
            radio.setBoxLabel(exitCon.getLabel());
            this.add(radio);
            radioList.add(radio);
        }
    }

    /**
     * 
     * This method returns the associated value of type {@link ExitCondition} of
     * the radio button that the user has selected.
     * 
     * @return the selected exit condition
     */
    public ExitCondition getSelectedValue() {
        ExitCondition exit = null;
        /*
         * Go trough the list of radio buttons, if the value of a radio button
         * is true, it means that the radio button was selected.
         */
        for (Radio radio : radioList) {
            if (radio.getValue() == true) {
                exit = ExitCondition.getTypeFromLabel(radio.getBoxLabel());
            }
        }
        return exit;
    }

    /**
     * 
     * This method sets the appropriate radio button to a selected state.
     * 
     * @param value
     *            the exit condition
     */
    public void setSelectedValue(ExitCondition value) {
        for (Radio radio : radioList) {
            if (ExitCondition.getTypeFromLabel(radio.getBoxLabel()) == value) {
                radio.setValue(true);
            } else {
                radio.setValue(false);
            }
        }
    }

    /**
     * Return true if one radio button is currently selected.
     * 
     * @return the result
     */
    public Boolean isSelected() {
        Boolean result = false;
        for (Radio radio : radioList) {
            /* If one radio button value is true, that means it was selected */
            if (radio.getValue()) {
                result = true;
            }
        }
        return result;
    }

}
