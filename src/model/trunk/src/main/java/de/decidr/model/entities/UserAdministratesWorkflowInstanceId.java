package de.decidr.model.entities;

// Generated 21.08.2009 18:16:16 by Hibernate Tools 3.2.4.GA

/**
 * UserAdministratesWorkflowInstanceId generated by hbm2java
 */
public class UserAdministratesWorkflowInstanceId implements
        java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private long userId;
    private long workflowInstanceId;

    public UserAdministratesWorkflowInstanceId() {
    }

    public UserAdministratesWorkflowInstanceId(long userId,
            long workflowInstanceId) {
        this.userId = userId;
        this.workflowInstanceId = workflowInstanceId;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getWorkflowInstanceId() {
        return this.workflowInstanceId;
    }

    public void setWorkflowInstanceId(long workflowInstanceId) {
        this.workflowInstanceId = workflowInstanceId;
    }

    @Override
    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof UserAdministratesWorkflowInstanceId))
            return false;
        UserAdministratesWorkflowInstanceId castOther = (UserAdministratesWorkflowInstanceId) other;

        return (this.getUserId() == castOther.getUserId())
                && (this.getWorkflowInstanceId() == castOther
                        .getWorkflowInstanceId());
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 37 * result + (int) this.getUserId();
        result = 37 * result + (int) this.getWorkflowInstanceId();
        return result;
    }

}
