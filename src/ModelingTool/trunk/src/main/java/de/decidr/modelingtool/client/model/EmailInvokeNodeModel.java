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
 * @author JE, Jonas Schlaak
 */
public class EmailInvokeNodeModel extends InvokeNodeModel {

    private Long toVariableId = null;
    private Long ccVariableId = null;
    private Long bccVariableId = null;
    private Long subjectVariableId = null;
    private Long messageVariableId = null;
    private Long attachmentVariableId = null;

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
        return toVariableId;
    }

    public Long getCcVariableId() {
        return ccVariableId;
    }

    public Long getBccVariableId() {
        return bccVariableId;
    }

    public Long getSubjectVariableId() {
        return subjectVariableId;
    }

    public Long getMessageVariableId() {
        return messageVariableId;
    }

    public Long getAttachmentVariableId() {
        return attachmentVariableId;
    }

    public void setToVariableId(Long toVariableId) {
        this.toVariableId = toVariableId;
    }

    public void setCcVariableId(Long ccVariableId) {
        this.ccVariableId = ccVariableId;
    }

    public void setBccVariableId(Long bccVariableId) {
        this.bccVariableId = bccVariableId;
    }

    public void setSubjectVariableId(Long subjectVariableId) {
        this.subjectVariableId = subjectVariableId;
    }

    public void setMessageVariableId(Long messageVariableId) {
        this.messageVariableId = messageVariableId;
    }

    public void setAttachmentVariableId(Long attachmentVariableId) {
        this.attachmentVariableId = attachmentVariableId;
    }

}
