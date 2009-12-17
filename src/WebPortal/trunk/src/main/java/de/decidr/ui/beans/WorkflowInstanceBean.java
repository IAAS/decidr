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
import de.decidr.model.entities.Server;
import de.decidr.model.entities.WorkflowInstance;

/**
 * TODO: add comment
 *
 * @author AT
 */
@Reviewed(currentReviewState = State.NeedsReview, lastRevision = "", reviewers = { "" })
public class WorkflowInstanceBean implements Serializable{
    
    private String model;
    private Long id;
    private Server server;
    private String odePid;
    private byte[] startConfiguration;
    private Date startedDate;
    private Date completedDate;
    
    /**
     * TODO: add comment
     *
     */
    public WorkflowInstanceBean(WorkflowInstance workflowInstance) {
        this.model = workflowInstance.getDeployedWorkflowModel().getName();
        this.startedDate = workflowInstance.getStartedDate();
        this.completedDate = workflowInstance.getCompletedDate();
        this.id = workflowInstance.getId();
        this.server = workflowInstance.getServer();
        this.odePid = workflowInstance.getOdePid();
        this.startConfiguration = workflowInstance.getStartConfiguration();
    }
    
    /**
     * TODO: add comment
     *
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * TODO: add comment
     *
     * @param model the model to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * TODO: add comment
     *
     * @return the startedDate
     */
    public Date getStartedDate() {
        return startedDate;
    }

    /**
     * TODO: add comment
     *
     * @param startedDate the startedDate to set
     */
    public void setStartedDate(Date startedDate) {
        this.startedDate = startedDate;
    }

    /**
     * TODO: add comment
     *
     * @return the completedDate
     */
    public Date getCompletedDate() {
        return completedDate;
    }

    /**
     * TODO: add comment
     *
     * @param completedDate the completedDate to set
     */
    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
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
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * TODO: add comment
     *
     * @return the server
     */
    public Server getServer() {
        return server;
    }

    /**
     * TODO: add comment
     *
     * @param server the server to set
     */
    public void setServer(Server server) {
        this.server = server;
    }

    /**
     * TODO: add comment
     *
     * @return the odePid
     */
    public String getOdePid() {
        return odePid;
    }

    /**
     * TODO: add comment
     *
     * @param odePid the odePid to set
     */
    public void setOdePid(String odePid) {
        this.odePid = odePid;
    }

    /**
     * TODO: add comment
     *
     * @return the startConfiguration
     */
    public byte[] getStartConfiguration() {
        return startConfiguration;
    }

    /**
     * TODO: add comment
     *
     * @param startConfiguration the startConfiguration to set
     */
    public void setStartConfiguration(byte[] startConfiguration) {
        this.startConfiguration = startConfiguration;
    }

    

}
