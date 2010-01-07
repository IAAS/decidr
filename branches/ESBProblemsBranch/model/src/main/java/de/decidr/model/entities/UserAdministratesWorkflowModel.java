package de.decidr.model.entities;

// Generated 16.11.2009 18:35:09 by Hibernate Tools 3.2.4.GA

/**
 * UserAdministratesWorkflowModel generated by hbm2java
 */
public class UserAdministratesWorkflowModel implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private UserAdministratesWorkflowModelId id;
    private WorkflowModel workflowModel;
    private User user;

    public UserAdministratesWorkflowModel() {
        //default empty JavaBean constructor
    }

    public UserAdministratesWorkflowModel(UserAdministratesWorkflowModelId id,
            WorkflowModel workflowModel, User user) {
        //generated full constructor
        this.id = id;
        this.workflowModel = workflowModel;
        this.user = user;
    }

    public UserAdministratesWorkflowModelId getId() {
        return this.id;
    }

    public void setId(UserAdministratesWorkflowModelId id) {
        this.id = id;
    }

    public WorkflowModel getWorkflowModel() {
        return this.workflowModel;
    }

    public void setWorkflowModel(WorkflowModel workflowModel) {
        this.workflowModel = workflowModel;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}