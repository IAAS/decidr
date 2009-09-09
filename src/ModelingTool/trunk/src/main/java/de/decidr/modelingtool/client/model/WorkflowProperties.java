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

    private final String recipientFieldName = "recipient";
    private final String faultMessageFieldName = "faultMessage";
    private final String successMessageFieldName = "successMessage";
    private final String notifyOnSuccessFieldName = "notify";

    private NodePropertyData properties;

    public WorkflowProperties() {
        properties = new NodePropertyData();
    }

    public NodePropertyData getProperties() {
        return properties;
    }

    public void setProperties(NodePropertyData properties) {
        this.properties = properties;
    }

    public Long getRecipientVariableId() {
        return properties.get(recipientFieldName);
    }

    public Long getFaultMessageVariableId() {
        return properties.get(faultMessageFieldName);
    }

    public Long getSuccessMessageVariableId() {
        return properties.get(successMessageFieldName);
    }

    public Boolean getNotifyOnSuccess() {
        return properties.get(notifyOnSuccessFieldName);
    }

    public void setRecipientVariableId(Long recipientVariableId) {
        this.properties.set(recipientFieldName, recipientVariableId);
    }

    public void setFaultMessageVariableId(Long faultMessageVariableId) {
        this.properties.set(faultMessageFieldName, faultMessageVariableId);
    }

    public void setSuccessMessageVariableId(Long successMessageVariableId) {
        this.properties.set(successMessageFieldName, successMessageVariableId);
    }

    public void setNotifyOnSuccess(Boolean notifyOnSuccess) {
        this.properties.set(notifyOnSuccessFieldName, notifyOnSuccess);
    }
}
