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

import java.util.List;

import de.decidr.modelingtool.client.model.HasChildModels;
import de.decidr.modelingtool.client.model.InvokeNodeModel;

/**
 * This class holds all properties of a {@link HumanTaskInvokeNodeModel}.
 * 
 * 
 * @author Johannes Engelhardt, Jonas Schlaak
 */
public class HumanTaskInvokeNodeModel extends InvokeNodeModel {

    private final String userFieldName = "user";
    private final String workItemNameFieldName = "workitemName";
    private final String workItemDescriptionFieldName = "workitemDesc";
    private final String formFieldName = "form";
    private final String notifyFieldName = "notify";
    private final String taskItemsFieldName = "taskItems";

    /**
     * Default constructor. No properties are set by default.
     * 
     * @param parentModel
     *            the model of the parent node
     */
    public HumanTaskInvokeNodeModel(HasChildModels parentModel) {
        super(parentModel);
    }

    public Long getUserVariableId() {
        return properties.get(userFieldName);
    }

    public Long getWorkItemNameVariableId() {
        return properties.get(workItemNameFieldName);
    }

    public Long getWorkItemDescriptionVariableId() {
        return properties.get(workItemDescriptionFieldName);
    }

    public Long getFormVariableId() {
        return properties.get(formFieldName);
    }

    public Boolean getNotify() {
        return properties.get(notifyFieldName);
    }

    public List<TaskItem> getTaskItems() {
        return properties.get(taskItemsFieldName);
    }

    public void setUserVariableId(Long userVariableId) {
        this.properties.set(this.userFieldName, userVariableId);
    }

    public void setWorkItemNameVariableId(Long workItemNameVariableId) {
        this.properties.set(this.workItemNameFieldName, workItemNameVariableId);
    }

    public void setWorkItemDescriptionVariableId(
            Long workItemDescriptionVariableId) {
        this.properties.set(this.workItemDescriptionFieldName,
                workItemDescriptionVariableId);
    }

    public void setFormVariableId(Long formVariableId) {
        this.properties.set(this.formFieldName, formVariableId);
    }

    public void setNotifyVariableId(Boolean notify) {
        this.properties.set(this.notifyFieldName, notify);
    }

    public void setTaskItems(List<TaskItem> taskItems) {
        this.properties.set(this.taskItemsFieldName, taskItems);
    }

}
