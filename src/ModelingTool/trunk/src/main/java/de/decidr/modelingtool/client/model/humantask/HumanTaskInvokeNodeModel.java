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
 * TODO: add comment
 * 
 * @author Johannes Engelhardt, Jonas Schlaak
 */
public class HumanTaskInvokeNodeModel extends InvokeNodeModel {

    // JS rename
    private final String userVariableId = "user";
    private final String formVariableId = "form";
    private final String notify = "notify";
    private final String formElements = "formElements";

    /**
     * TODO: add comment
     * 
     * @param parentModel
     */
    public HumanTaskInvokeNodeModel(HasChildModels parentModel) {
        super(parentModel);
    }

    public HumanTaskInvokeNodeModel() {
        super();
    }

    public Long getUserVariableId() {
        return properties.get(userVariableId);
    }

    public Long getFormVariableId() {
        return properties.get(formVariableId);
    }

    public Boolean getNotify() {
        return properties.get(notify);
    }

    public List<FormElement> getFormElements() {
        return properties.get(formElements);
    }

    public void setUserVariableId(Long userVariableId) {
        this.properties.set(this.userVariableId, userVariableId);
    }

    public void setFormVariableId(Long formVariableId) {
        this.properties.set(this.formVariableId, formVariableId);
    }

    public void setNotifyVariableId(Boolean notify) {
        this.properties.set(this.notify, notify);
    }

    public void setFormElements(List<FormElement> formElements) {
        this.properties.set(this.formElements, formElements);
    }

}
