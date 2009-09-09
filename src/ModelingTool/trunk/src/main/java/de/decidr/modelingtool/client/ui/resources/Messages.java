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
 * 
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public interface Messages extends Constants {

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

    String nameLabel();

    String descriptionLabel();

    String formLabel();

    String notifyLabel();

    String workItemForm();

    String workItemLabel();

    String workItemOutputVar();

    String humanTaskActivityWarning();

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

    String conditionOrderWarning();

    /* Variable editor */
    String enterVariableName();

    String addVariable();

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

    /* ValueEditor */
    String valueSingular();

    String valuePlural();

    String wrongSingular();

    String wrongPlural();

    String invalidFormat();

    String invalidUserIds();

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

    String decimalSeparator();

    String dateFormat();

}
