package de.decidr.model.entities;

// Generated 12.10.2009 16:53:44 by Hibernate Tools 3.2.4.GA

/**
 * TenantSummaryView generated by hbm2java
 */
public class TenantSummaryView implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private String tenantName;
    private String adminFirstName;
    private String adminLastName;
    private long numWorkflowModels;
    private long numDeployedWorkflowModels;
    private long numWorkflowInstances;
    private long numMembers;

    public TenantSummaryView() {
    }

    public TenantSummaryView(long id, String tenantName,
            long numWorkflowModels, long numDeployedWorkflowModels,
            long numWorkflowInstances, long numMembers) {
        this.id = id;
        this.tenantName = tenantName;
        this.numWorkflowModels = numWorkflowModels;
        this.numDeployedWorkflowModels = numDeployedWorkflowModels;
        this.numWorkflowInstances = numWorkflowInstances;
        this.numMembers = numMembers;
    }

    public TenantSummaryView(long id, String tenantName, String adminFirstName,
            String adminLastName, long numWorkflowModels,
            long numDeployedWorkflowModels, long numWorkflowInstances,
            long numMembers) {
        this.id = id;
        this.tenantName = tenantName;
        this.adminFirstName = adminFirstName;
        this.adminLastName = adminLastName;
        this.numWorkflowModels = numWorkflowModels;
        this.numDeployedWorkflowModels = numDeployedWorkflowModels;
        this.numWorkflowInstances = numWorkflowInstances;
        this.numMembers = numMembers;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTenantName() {
        return this.tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getAdminFirstName() {
        return this.adminFirstName;
    }

    public void setAdminFirstName(String adminFirstName) {
        this.adminFirstName = adminFirstName;
    }

    public String getAdminLastName() {
        return this.adminLastName;
    }

    public void setAdminLastName(String adminLastName) {
        this.adminLastName = adminLastName;
    }

    public long getNumWorkflowModels() {
        return this.numWorkflowModels;
    }

    public void setNumWorkflowModels(long numWorkflowModels) {
        this.numWorkflowModels = numWorkflowModels;
    }

    public long getNumDeployedWorkflowModels() {
        return this.numDeployedWorkflowModels;
    }

    public void setNumDeployedWorkflowModels(long numDeployedWorkflowModels) {
        this.numDeployedWorkflowModels = numDeployedWorkflowModels;
    }

    public long getNumWorkflowInstances() {
        return this.numWorkflowInstances;
    }

    public void setNumWorkflowInstances(long numWorkflowInstances) {
        this.numWorkflowInstances = numWorkflowInstances;
    }

    public long getNumMembers() {
        return this.numMembers;
    }

    public void setNumMembers(long numMembers) {
        this.numMembers = numMembers;
    }

}
