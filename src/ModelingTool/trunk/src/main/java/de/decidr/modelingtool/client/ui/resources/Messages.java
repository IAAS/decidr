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
 * This class holds all methods for accessing localized strings. The string
 * itself can be found in the properties file.
 * 
 * @author Jonas Schlaak
 */
public interface Messages extends Constants {

    String addTaskItem();

    String addValue();

    String andConLabel();

    String attachmentFieldLabel();

    String bccFieldLabel();

    String cancelButton();

    String ccFieldLabel();

    /* Nodes */
    String changePropertyButton();

    String changeValueButton();

    String condition();

    String conditionFieldsEmpty();

    String conditionOrderWarning();

    String conditionTypeMismatch();

    String conditionWrongOperator();

    String configLabel();

    String configVarColumn();

    /* Menu */
    String confirmClearWorkflow();

    String dateFormat();

    String defaultCondition();

    String delTaskItem();

    String delValue();

    String delVariable();

    String descriptionLabel();

    /* Canvas Size Editor */
    String editCanvasSize();

    String editorHeading();

    String editTaskItems();

    String editVariable();

    /* Email */
    String emailActivity();

    String emailActivityWarning();

    /* Variable editor */
    String enterVariableName();

    String exitConLabel();

    String faultMessageFieldLabel();

    String flowContainerWarning();

    /* For Each container */
    String forEachContainer();

    String formLabel();

    String height();

    /* Human task */
    String humanTaskActivity();

    String humanTaskActivityWarning();

    /* If container */
    String ifContainer();

    String invalidFormat();

    String invalidUserIds();

    String iterationVarLabel();

    String messageFieldLabel();

    String nameColumn();

    /* New Variable Window */
    String nameLabel();

    String newStringValue();

    String newVariable();

    String noPropertyWindowMessage();

    String notDeletableMessage();

    String notifyCheckBox();

    String notifyLabel();

    String okButton();

    String operationNotAllowedMessage();

    String parallelLabel();

    String propertiesMenuItem();

    String recipientFieldLabel();

    String sizeMax();

    String subjectFieldLabel();

    String successMessageFieldLabel();

    String taskData();

    String taskItemForm();

    String taskItemHint();

    String taskItemInputVar();

    String taskItemLabel();

    String taskItemWarning();

    String taskNameLabel();

    String toFieldLabel();

    String typeBoolean();

    String typeColumn();

    String typeDate();

    String typeFile();

    String typeFloat();

    String typeForm();

    String typeInteger();

    String typeLabel();

    String typeRole();

    /* Variable types */
    String typeString();

    String userLabel();

    String valueColumn();

    String valuePlural();

    /* ValueEditor */
    String valueSingular();

    String variablesMenuItem();

    /* windows common */
    String warningTitle();

    String width();

    /* Workflow */
    String workflowProperty();

    String wrongPlural();

    String wrongSingular();

    String xorConLabel();

}
