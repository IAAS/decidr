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

import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;

import de.decidr.modelingtool.client.model.variable.Variable;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class TypeSelectorListener extends SelectionChangedListener<Variable> {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.extjs.gxt.ui.client.event.SelectionChangedListener#selectionChanged
     * (com.extjs.gxt.ui.client.event.SelectionChangedEvent)
     */
    @Override
    public void selectionChanged(SelectionChangedEvent<Variable> se) {
        // TODO Auto-generated method stub
    }
//    public void selectionChanged(
//          SelectionChangedEvent<Variable> se) {
//      /*
//       * If type selector changes, get all variables with
//       * selected type from workflow
//       */
//      leftOperandField.setEnabled(true);
//      leftOperandField.getStore().removeAll();
//      leftOperandField.clearSelections();
//      leftOperandField
//              .getStore()
//              .add(
//                      VariablesFilter
//                              .getVariablesOfType(
//                                      VariableType
//                                              .getTypeFromLocalName(typeSelector
//                                                      .getSimpleValue()))
//                              .getModels());
//
//      operatorList.setEnabled(true);
//      updateOperatorListEntries();
//
//      /*
//       * If type selector changes, get all variables with
//       * selected type from workflow
//       */
//      rightOperandField.setEnabled(true);
//      rightOperandField.getStore().removeAll();
//      rightOperandField.clearSelections();
//      rightOperandField
//              .getStore()
//              .add(
//                      VariablesFilter
//                              .getVariablesOfType(
//                                      VariableType
//                                              .getTypeFromLocalName(typeSelector
//                                                      .getSimpleValue()))
//                              .getModels());
//  }
}
