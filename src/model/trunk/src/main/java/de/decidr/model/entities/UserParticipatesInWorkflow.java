package de.decidr.model.entities;

// Generated 09.10.2009 15:53:47 by Hibernate Tools 3.2.4.GA

/**
 * UserParticipatesInWorkflow generated by hbm2java
 */
public class UserParticipatesInWorkflow implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private UserParticipatesInWorkflowId id;
    private WorkflowInstance workflowInstance;
    private User user;

    public UserParticipatesInWorkflow() {
    }

    public UserParticipatesInWorkflow(UserParticipatesInWorkflowId id,
            WorkflowInstance workflowInstance, User user) {
        this.id = id;
        this.workflowInstance = workflowInstance;
        this.user = user;
    }

    public UserParticipatesInWorkflowId getId() {
        return this.id;
    }

    public void setId(UserParticipatesInWorkflowId id) {
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
