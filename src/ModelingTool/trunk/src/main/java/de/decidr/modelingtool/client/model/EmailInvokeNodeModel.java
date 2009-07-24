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
 * TODO: add comment
 * 
 * @author Johannes Engelhardt, Jonas Schlaak
 */
public class EmailInvokeNodeModel extends InvokeNodeModel {

    // JS rename
    private final String toVariableId = "to";
    private final String ccVariableId = "cc";
    private final String bccVariableId = "bcc";
    private final String subjectVariableId = "subject";
    private final String messageVariableId = "message";
    private final String attachmentVariableId = "attachment";

    /**
     * TODO: add comment
     * 
     * @param parentModel
     */
    public EmailInvokeNodeModel(HasChildModels parentModel) {
        super(parentModel);
        // TODO Auto-generated constructor stub
    }

    public EmailInvokeNodeModel() {
        super();
    }

    public Long getToVariableId() {
        return properties.get(toVariableId);
    }

    public Long getCcVariableId() {
        return properties.get(ccVariableId);
    }

    public Long getBccVariableId() {
        return properties.get(bccVariableId);
    }

    public Long getSubjectVariableId() {
        return properties.get(subjectVariableId);
    }

    public Long getMessageVariableId() {
        return properties.get(messageVariableId);
    }

    public Long getAttachmentVariableId() {
        return properties.get(attachmentVariableId);
    }

    public void setToVariableId(Long toVariableId) {
        this.properties.set(this.toVariableId, toVariableId);
    }

    public void setCcVariableId(Long ccVariableId) {
        this.properties.set(this.ccVariableId, ccVariableId);
    }

    public void setBccVariableId(Long bccVariableId) {
        this.properties.set(this.ccVariableId, ccVariableId);
    }

    public void setSubjectVariableId(Long subjectVariableId) {
        this.properties.set(this.bccVariableId, bccVariableId);
    }

    public void setMessageVariableId(Long messageVariableId) {
        this.properties.set(this.subjectVariableId, subjectVariableId);
    }

    public void setAttachmentVariableId(Long attachmentVariableId) {
        this.properties.set(this.attachmentVariableId, attachmentVariableId);
    }

}
