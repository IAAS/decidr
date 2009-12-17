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
import de.decidr.model.entities.TenantSummaryView;

/**
 * This bean represents the tenant summary view from the model in the GUI. The
 * properties are reachable through getter and setter. The bean has following
 * properties:
 * <li> id : Long </li>
 * <li> tenantName : String </li>
 * <li> approvedSince : Date </li>
 * <li> adminFirstName : String </li>
 * <li> adminLastName : String </li>
 * <li> numWorkflowModels : Long </li>
 * <li> numDeployedWorkflowModels : Long </li>
 * <li> numWorkflowInstances : Long </li>
 * <li> numMembers : Long </li>
 * 
 * @author AT
 */
@Reviewed(currentReviewState = State.NeedsReview, lastRevision = "", reviewers = { "" })
public class TenantSummaryViewBean implements Serializable {

    private long id;
    private String tenantName;
    private Date approvedSince;
    private String adminFirstName;
    private String adminLastName;
    private Long numWorkflowModels;
    private Long numDeployedWorkflowModels;
    private Long numWorkflowInstances;
    private Long numMembers;

    /**
     * Constructor which initializes the properties from the given tenant summary view
     * 
     */
    public TenantSummaryViewBean(TenantSummaryView tenantSummaryView) {
        this.id = tenantSummaryView.getId();
        this.tenantName = tenantSummaryView.getTenantName();
        this.approvedSince = tenantSummaryView.getApprovedSince();
        this.adminFirstName = tenantSummaryView.getAdminFirstName();
        this.adminLastName = tenantSummaryView.getAdminLastName();
        this.numWorkflowModels = tenantSummaryView.getNumWorkflowModels();
        this.numDeployedWorkflowModels = tenantSummaryView
                .getNumDeployedWorkflowModels();
        this.numWorkflowInstances = tenantSummaryView.getNumWorkflowInstances();
        this.numMembers = tenantSummaryView.getNumMembers();
    }

    
    public long getId() {
        return id;
    }

    
    public void setId(long id) {
        this.id = id;
    }

    
    public String getTenantName() {
        return tenantName;
    }

    
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    
    public Date getApprovedSince() {
        return approvedSince;
    }

    
    public void setApprovedSince(Date approvedSince) {
        this.approvedSince = approvedSince;
    }

   
    public String getAdminFirstName() {
        return adminFirstName;
    }

    
    public void setAdminFirstName(String adminFirstName) {
        this.adminFirstName = adminFirstName;
    }

    
    public String getAdminLastName() {
        return adminLastName;
    }

    
    public void setAdminLastName(String adminLastName) {
        this.adminLastName = adminLastName;
    }

    
    public Long getNumWorkflowModels() {
        return numWorkflowModels;
    }

    
    public void setNumWorkflowModels(Long numWorkflowModels) {
        this.numWorkflowModels = numWorkflowModels;
    }

    
    public Long getNumDeployedWorkflowModels() {
        return numDeployedWorkflowModels;
    }

    
    public void setNumDeployedWorkflowModels(Long numDeployedWorkflowModels) {
        this.numDeployedWorkflowModels = numDeployedWorkflowModels;
    }

    
    public Long getNumWorkflowInstances() {
        return numWorkflowInstances;
    }

    
    public void setNumWorkflowInstances(Long numWorkflowInstances) {
        this.numWorkflowInstances = numWorkflowInstances;
    }

    
    public Long getNumMembers() {
        return numMembers;
    }

    
    public void setNumMembers(Long numMembers) {
        this.numMembers = numMembers;
    }

}
