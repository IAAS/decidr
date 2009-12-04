package de.decidr.model.entities;

// Generated 16.11.2009 18:35:09 by Hibernate Tools 3.2.4.GA

import java.util.Date;

/**
 * Invitation generated by hbm2java
 */
public class Invitation implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private WorkflowModel administrateWorkflowModel;
    private User sender;
    private User receiver;
    private Tenant joinTenant;
    private WorkflowInstance participateInWorkflowInstance;
    private Date creationDate;

    public Invitation() {
        //default empty JavaBean constructor
    }

    public Invitation(User sender, User receiver, Date creationDate) {
        //generated minimal constructor
        this.sender = sender;
        this.receiver = receiver;
        this.creationDate = creationDate;
    }

    public Invitation(WorkflowModel administrateWorkflowModel, User sender,
            User receiver, Tenant joinTenant,
            WorkflowInstance participateInWorkflowInstance, Date creationDate) {
        //generated full constructor
        this.administrateWorkflowModel = administrateWorkflowModel;
        this.sender = sender;
        this.receiver = receiver;
        this.joinTenant = joinTenant;
        this.participateInWorkflowInstance = participateInWorkflowInstance;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkflowModel getAdministrateWorkflowModel() {
        return this.administrateWorkflowModel;
    }

    public void setAdministrateWorkflowModel(
            WorkflowModel administrateWorkflowModel) {
        this.administrateWorkflowModel = administrateWorkflowModel;
    }

    public User getSender() {
        return this.sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return this.receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Tenant getJoinTenant() {
        return this.joinTenant;
    }

    public void setJoinTenant(Tenant joinTenant) {
        this.joinTenant = joinTenant;
    }

    public WorkflowInstance getParticipateInWorkflowInstance() {
        return this.participateInWorkflowInstance;
    }

    public void setParticipateInWorkflowInstance(
            WorkflowInstance participateInWorkflowInstance) {
        this.participateInWorkflowInstance = participateInWorkflowInstance;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}