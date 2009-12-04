package de.decidr.model.entities;

// Generated 16.11.2009 18:35:09 by Hibernate Tools 3.2.4.GA

/**
 * WorkflowModelIsDeployedOnServerId generated by hbm2java
 */
public class WorkflowModelIsDeployedOnServerId implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private long deployedWorkflowModelId;
    private long serverId;

    public WorkflowModelIsDeployedOnServerId() {
        //default empty JavaBean constructor
    }

    public WorkflowModelIsDeployedOnServerId(long deployedWorkflowModelId,
            long serverId) {
        //generated full constructor
        this.deployedWorkflowModelId = deployedWorkflowModelId;
        this.serverId = serverId;
    }

    public long getDeployedWorkflowModelId() {
        return this.deployedWorkflowModelId;
    }

    public void setDeployedWorkflowModelId(long deployedWorkflowModelId) {
        this.deployedWorkflowModelId = deployedWorkflowModelId;
    }

    public long getServerId() {
        return this.serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    @Override
    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof WorkflowModelIsDeployedOnServerId))
            return false;
        WorkflowModelIsDeployedOnServerId castOther = (WorkflowModelIsDeployedOnServerId) other;

        return (this.getDeployedWorkflowModelId() == castOther
                .getDeployedWorkflowModelId())
                && (this.getServerId() == castOther.getServerId());
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 37 * result + (int) this.getDeployedWorkflowModelId();
        result = 37 * result + (int) this.getServerId();
        return result;
    }

}