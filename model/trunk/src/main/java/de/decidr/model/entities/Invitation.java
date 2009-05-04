package de.decidr.model.entities;

// Generated 04.05.2009 13:48:05 by Hibernate Tools 3.2.2.GA

/**
 * Invitation generated by hbm2java
 */
public class Invitation implements java.io.Serializable {

	private Long id;
	private WorkflowModel administrateWorkflowModel;
	private User sender;
	private User receiver;
	private Tenant joinTenant;
	private WorkflowInstance participateInWorkflowInstance;

	public Invitation() {
	}

	public Invitation(User sender, User receiver) {
		this.sender = sender;
		this.receiver = receiver;
	}

	public Invitation(WorkflowModel administrateWorkflowModel, User sender,
			User receiver, Tenant joinTenant,
			WorkflowInstance participateInWorkflowInstance) {
		this.administrateWorkflowModel = administrateWorkflowModel;
		this.sender = sender;
		this.receiver = receiver;
		this.joinTenant = joinTenant;
		this.participateInWorkflowInstance = participateInWorkflowInstance;
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

}
