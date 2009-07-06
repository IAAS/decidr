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

package de.decidr.modelingtool.client.model;

/**
 * This class serves as a container for the properties of a workflow, such as
 * fault and success messages.
 * 
 * @author Jonas Schlaak
 */
public class WorkflowProperties {

    private Long recipientVariableId = null;
    private Long faultMessageVariableId = null;
    private Long successMessageVariableId = null;
    private Boolean notifyOnSuccess = false;

    public Long getRecipientVariableId() {
        return recipientVariableId;
    }

    public Long getFaultMessageVariableId() {
        return faultMessageVariableId;
    }

    public Long getSuccessMessageVariableId() {
        return successMessageVariableId;
    }

    public Boolean getNotifyOnSuccess() {
        return notifyOnSuccess;
    }

    public void setRecipientVariableId(Long recipientVariableId) {
        this.recipientVariableId = recipientVariableId;
    }

    public void setFaultMessageVariableId(Long faultMessageVariableId) {
        this.faultMessageVariableId = faultMessageVariableId;
    }

    public void setSuccessMessageVariableId(Long successMessageVariableId) {
        this.successMessageVariableId = successMessageVariableId;
    }

    public void setNotifyOnSuccess(Boolean notifyOnSuccess) {
        this.notifyOnSuccess = notifyOnSuccess;
    }
}
