package de.decidr.model.entities;

// Generated 13.06.2009 19:54:01 by Hibernate Tools 3.2.4.GA

/**
 * UserAdministratesWorkflowInstance generated by hbm2java
 */
public class UserAdministratesWorkflowInstance implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private UserAdministratesWorkflowInstanceId id;
    private WorkflowInstance workflowInstance;
    private User user;

    public UserAdministratesWorkflowInstance() {
    }

    public UserAdministratesWorkflowInstance(
            UserAdministratesWorkflowInstanceId id,
            WorkflowInstance workflowInstance, User user) {
        this.id = id;
        this.workflowInstance = workflowInstance;
        this.user = user;
    }

    public UserAdministratesWorkflowInstanceId getId() {
        return this.id;
    }

    public void setId(UserAdministratesWorkflowInstanceId id) {
        this.id = id;
    }

    public WorkflowInstance getWorkflowInstance() {
        return this.workflowInstance;
    }

    public void setWorkflowInstance(WorkflowInstance workflowInstance) {
        this.workflowInstance = workflowInstance;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
