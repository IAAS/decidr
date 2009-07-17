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

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class Condition {

    private String label;
    private Long operand1;
    private Operator operator;
    private Long operand2;

    public String getLabel() {
        return label;
    }

    public Long getOperand1() {
        return operand1;
    }

    public Operator getOperator() {
        return operator;
    }

    public Long getOperand2() {
        return operand2;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setOperand1(Long operand1) {
        this.operand1 = operand1;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void setOperand2(Long operand2) {
        this.operand2 = operand2;
    }

}
