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
import de.decidr.model.entities.WorkItemSummaryView;

/**
 * TODO: add comment
 * 
 * @author AT
 */
@Reviewed(currentReviewState = State.PassedWithComments, lastRevision = "2498", reviewers = { "RR" })
public class WorkItemSummaryViewBean implements Serializable {

    private long id;
    private String workItemName;
    private String tenantName;
    private Date creationDate;
    private String workItemStatus;
    private long userId;
    private long workflowInstanceId;

    /**
     * TODO: add comment
     */
    public WorkItemSummaryViewBean(WorkItemSummaryView workItemSummaryView) {
        this.id = workItemSummaryView.getId();
        this.workItemName = workItemSummaryView.getWorkItemName();
        this.tenantName = workItemSummaryView.getTenantName();
        this.creationDate = workItemSummaryView.getCreationDate();
        this.workItemStatus = workItemSummaryView.getWorkItemStatus();
        this.userId = workItemSummaryView.getUserId();
        this.workflowInstanceId = workItemSummaryView.getWorkflowInstanceId();
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
     * @return the id
     */
    public long getId() {
        return id;
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
     * @return the userId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * TODO: add comment
     * 
     * @return the workflowInstanceId
     */
    public long getWorkflowInstanceId() {
        return workflowInstanceId;
    }

    /**
     * TODO: add comment
     * 
     * @return the workItemName
     */
    public String getWorkItemName() {
        return workItemName;
    }

    /**
     * TODO: add comment
     * 
     * @return the workItemStatus
     */
    public String getWorkItemStatus() {
        return workItemStatus;
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
     * @param id
     *            the id to set
     */
    public void setId(long id) {
        this.id = id;
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
     * @param userId
     *            the userId to set
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * TODO: add comment
     * 
     * @param workflowInstanceId
     *            the workflowInstanceId to set
     */
    public void setWorkflowInstanceId(long workflowInstanceId) {
        this.workflowInstanceId = workflowInstanceId;
    }

    /**
     * TODO: add comment
     * 
     * @param workItemName
     *            the workItemName to set
     */
    public void setWorkItemName(String workItemName) {
        this.workItemName = workItemName;
    }

    /**
     * TODO: add comment
     * 
     * @param workItemStatus
     *            the workItemStatus to set
     */
    public void setWorkItemStatus(String workItemStatus) {
        this.workItemStatus = workItemStatus;
    }
}
