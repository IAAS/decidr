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

package de.decidr.modelingtool.client.model.ifcondition;

import de.decidr.modelingtool.client.model.ConnectionModel;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class Condition extends ConnectionModel {

    private String label;
    private Long operand1Id;
    private Operator operator;
    private Long operand2Id;

    public Condition() {
        super();
        // JS null fields
    }

    public String getLabel() {
        return label;
    }

    public Long getOperand1Id() {
        return operand1Id;
    }

    public Operator getOperator() {
        return operator;
    }

    public Long getOperand2Id() {
        return operand2Id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setOperand1Id(Long operand1Id) {
        this.operand1Id = operand1Id;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void setOperand2Id(Long operand2Id) {
        this.operand2Id = operand2Id;
    }

}
