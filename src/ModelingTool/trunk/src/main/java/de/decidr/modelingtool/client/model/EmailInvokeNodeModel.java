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
public class EmailInvokeNodeModel extends NodeModel {

    private String toVariableName = "";
    private String ccVariableName = "";
    private String bccVariableName = "";
    private String subjectVariableName = "";
    private String messageVariableName = "";
    private String attachmentVariableName = "";

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

    public String getToVariableName() {
        return toVariableName;
    }

    public String getCcVariableName() {
        return ccVariableName;
    }

    public String getBccVariableName() {
        return bccVariableName;
    }

    public String getSubjectVariableName() {
        return subjectVariableName;
    }

    public String getMessageVariableName() {
        return messageVariableName;
    }

    public String getAttachmentVariableName() {
        return attachmentVariableName;
    }

    public void setToVariableName(String toVariableName) {
        this.toVariableName = toVariableName;
    }

    public void setCcVariableName(String ccVariableName) {
        this.ccVariableName = ccVariableName;
    }

    public void setBccVariableName(String bccVariableName) {
        this.bccVariableName = bccVariableName;
    }

    public void setSubjectVariableName(String subjectVariableName) {
        this.subjectVariableName = subjectVariableName;
    }

    public void setMessageVariableName(String messageVariableName) {
        this.messageVariableName = messageVariableName;
    }

    public void setAttachmentVariableName(String attachmentVariableName) {
        this.attachmentVariableName = attachmentVariableName;
    }

}
