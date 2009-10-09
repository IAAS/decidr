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

import de.decidr.modelingtool.client.ui.EmailInvokeNode;

/**
 * This class holds all properties of a {@link EmailInvokeNode}.
 * 
 * @author Johannes Engelhardt, Jonas Schlaak
 */
public class EmailInvokeNodeModel extends InvokeNodeModel {

    private final String toFieldName = "to";
    private final String ccFieldName = "cc";
    private final String bccFieldName = "bcc";
    private final String subjectFieldName = "subject";
    private final String messageFieldName = "message";
    private final String attachmentFieldName = "attachment";

    /**
     * Default constructor. No properties are set by default.
     * 
     * @param parentModel
     *            the model of the parent node
     */
    public EmailInvokeNodeModel(HasChildModels parentModel) {
        super(parentModel);
        this.name = this.getClass().getName();
    }

    public Long getToVariableId() {
        return properties.get(toFieldName);
    }

    public Long getCcVariableId() {
        return properties.get(ccFieldName);
    }

    public Long getBccVariableId() {
        return properties.get(bccFieldName);
    }

    public Long getSubjectVariableId() {
        return properties.get(subjectFieldName);
    }

    public Long getMessageVariableId() {
        return properties.get(messageFieldName);
    }

    public Long getAttachmentVariableId() {
        return properties.get(attachmentFieldName);
    }

    public void setToVariableId(Long toVariableId) {
        this.properties.set(this.toFieldName, toVariableId);
    }

    public void setCcVariableId(Long ccVariableId) {
        this.properties.set(this.ccFieldName, ccVariableId);
    }

    public void setBccVariableId(Long bccVariableId) {
        this.properties.set(this.bccFieldName, bccVariableId);
    }

    public void setSubjectVariableId(Long subjectVariableId) {
        this.properties.set(this.subjectFieldName, subjectVariableId);
    }

    public void setMessageVariableId(Long messageVariableId) {
        this.properties.set(this.messageFieldName, messageVariableId);
    }

    public void setAttachmentVariableId(Long attachmentVariableId) {
        this.properties.set(this.attachmentFieldName, attachmentVariableId);
    }

}
