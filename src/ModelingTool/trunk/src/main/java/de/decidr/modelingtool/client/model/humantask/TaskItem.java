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

package de.decidr.modelingtool.client.model.humantask;

/**
 * This class is container for the form elements of a human task. Every form
 * elements consists of a label (which is a brief description of the input that
 * the form element is supposed to hold) and the id of a variable which actually
 * is supposed to hold the input of the form element.
 * 
 * @author Jonas Schlaak
 */
public class TaskItem {

    private String label = null;
    private Long variableId = null;

    /**
     * Constructor for a tak item with a given label and the id of a
     * {@link Variable}.
     * 
     * @param label
     *            the label of the task item
     * @param variableId
     *            the id of the variable
     */
    public TaskItem(String label, Long variableId) {
        this.label = label;
        this.variableId = variableId;
    }

    public String getLabel() {
        return label;
    }

    public Long getVariableId() {
        return variableId;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setVariableId(Long variableId) {
        this.variableId = variableId;
    }

}
