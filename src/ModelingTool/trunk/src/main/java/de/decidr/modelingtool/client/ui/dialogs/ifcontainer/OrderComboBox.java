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

package de.decidr.modelingtool.client.ui.dialogs.ifcontainer;

import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;

import de.decidr.modelingtool.client.ModelingToolWidget;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class OrderComboBox extends SimpleComboBox<String> {

    /**
     * TODO: add comment
     * 
     */
    public OrderComboBox(int count) {
        /*
         * Add all possible order "ranks" a condition can have to the selection
         * list. There always has to be one default condition (which is executed
         * if no other condition is true). Therefore, there can be n-1 proper
         * order "ranks" for n conditions.
         */
        this.add(ModelingToolWidget.messages.fallback());
        for (Integer i = 1; i < count; i++) {
            this.add(i.toString());
        }
        this.setEditable(false);
    }

}
