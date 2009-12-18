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

package de.decidr.ui.beans;

import java.io.Serializable;
import java.util.Date;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.entities.WorkflowModel;

/**
 * TODO: add comment
 * 
 * @author AT
 */
@Reviewed(currentReviewState = State.PassedWithComments, lastRevision = "2498", reviewers = { "RR" })
public class WorkflowModelBean implements Serializable {

    private Long id;
    private String tenantName;
    private String name;
    private String description;
    private boolean published;
    private boolean executable;
    private Date creationDate;
    private Date modifiedDate;
    private byte[] dwdl;

    /**
     * TODO: add comment
     * 
     */
    public WorkflowModelBean(WorkflowModel workflowModel) {
        this.name = workflowModel.getName();
        this.tenantName = workflowModel.getTenant().getName();
        this.modifiedDate = workflowModel.getModifiedDate();
        this.id = workflowModel.getId();
        this.description = workflowModel.getDescription();
        this.published = workflowModel.isPublished();
        this.executable = workflowModel.isExecutable();
        this.creationDate = workflowModel.getCreationDate();
        this.dwdl = workflowModel.getDwdl();
    }

    /**
     * TODO: add comment
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * TODO: add comment
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * TODO: add comment
     * 
     * @return the tenantName
     */
    public String getTenantName() {
        return tenantName;
    }

    /**
     * TODO: add comment
     * 
     * @param tenantName
     *            the tenantName to set
     */
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    /**
     * TODO: add comment
     * 
     * @return the modifiedDate
     */
    public Date getModifiedDate() {
        return modifiedDate;
    }

    /**
     * TODO: add comment
     * 
     * @param modifiedDate
     *            the modifiedDate to set
     */
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    /**
     * TODO: add comment
     * 
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * TODO: add comment
     * 
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * TODO: add comment
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * TODO: add comment
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * TODO: add comment
     * 
     * @return the published
     */
    public boolean isPublished() {
        return published;
    }

    /**
     * TODO: add comment
     * 
     * @param published
     *            the published to set
     */
    public void setPublished(boolean published) {
        this.published = published;
    }

    /**
     * TODO: add comment
     * 
     * @return the executable
     */
    public boolean isExecutable() {
        return executable;
    }

    /**
     * TODO: add comment
     * 
     * @param executable
     *            the executable to set
     */
    public void setExecutable(boolean executable) {
        this.executable = executable;
    }

    /**
     * TODO: add comment
     * 
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * TODO: add comment
     * 
     * @param creationDate
     *            the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * TODO: add comment
     * 
     * @return the dwdl
     */
    public byte[] getDwdl() {
        return dwdl;
    }

    /**
     * TODO: add comment
     * 
     * @param dwdl
     *            the dwdl to set
     */
    public void setDwdl(byte[] dwdl) {
        this.dwdl = dwdl;
    }
}
