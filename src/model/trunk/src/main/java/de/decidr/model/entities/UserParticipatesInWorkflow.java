package de.decidr.model.entities;

// Generated 29.05.2009 18:32:43 by Hibernate Tools 3.2.2.GA

/**
 * UserParticipatesInWorkflow generated by hbm2java
 */
public class UserParticipatesInWorkflow implements java.io.Serializable {

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