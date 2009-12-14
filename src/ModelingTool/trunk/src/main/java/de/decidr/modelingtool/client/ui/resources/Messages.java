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

package de.decidr.modelingtool.client.ui.resources;

import com.google.gwt.i18n.client.Constants;

/**
 * This class hold all method for accessing localized strings. The string itself
 * can be found in the properties file
 * 
 * @author Jonas Schlaak
 */
public interface Messages extends Constants {

    /* Menu */
    String confirmClearWorkflow();

    String variablesMenuItem();

    String propertiesMenuItem();

    String operationNotAllowedMessage();

    /* Workflow */
    String workflowProperty();

    String recipientFieldLabel();

    String faultMessageFieldLabel();

    String successMessageFieldLabel();

    String notifyCheckBox();

    /* Nodes */
    String changePropertyButton();

    String notDeletableMessage();

    String noPropertyWindowMessage();

    /* Email */
    String emailActivity();

    String toFieldLabel();

    String ccFieldLabel();

    String bccFieldLabel();

    String subjectFieldLabel();

    String messageFieldLabel();

    String attachmentFieldLabel();

    String changeValueButton();

    String emailActivityWarning();

    /* Human task */
    String humanTaskActivity();

    String taskData();

    String userLabel();

    String taskNameLabel();

    String descriptionLabel();

    String formLabel();

    String notifyLabel();

    String editTaskItems();

    String taskItemForm();

    String taskItemLabel();

    String taskItemHint();

    String taskItemInputVar();

    String addTaskItem();

    String delTaskItem();

    String humanTaskActivityWarning();

    String taskItemWarning();

    /* For Each container */
    String forEachContainer();

    String iterationVarLabel();

    String exitConLabel();

    String andConLabel();

    String xorConLabel();

    String parallelLabel();

    String flowContainerWarning();

    /* If container */
    String ifContainer();

    String condition();

    String defaultCondition();

    String conditionFieldsEmpty();

    String conditionTypeMismatch();

    String conditionWrongOperator();

    String conditionOrderWarning();

    /* Variable editor */
    String enterVariableName();

    String newVariable();

    String delVariable();

    String editVariable();

    String addValue();

    String delValue();

    String newStringValue();

    String editorHeading();

    String nameColumn();

    String typeColumn();

    String valueColumn();

    String configVarColumn();

    /* New Variable Window */
    String nameLabel();

    String typeLabel();

    String configLabel();

    /* ValueEditor */
    String valueSingular();

    String valuePlural();

    String wrongSingular();

    String wrongPlural();

    String invalidFormat();

    String invalidUserIds();

    /* Canvas Size Editor */
    String width();

    String height();
    
    
    String sizeMax();

    /* windows common */
    String warningTitle();

    String okButton();

    String cancelButton();

    /* Variable types */
    String typeString();

    String typeInteger();

    String typeFloat();

    String typeBoolean();

    String typeFile();

    String typeDate();

    String typeRole();

    String typeForm();

    String dateFormat();



}
