package de.decidr.model.entities;

// Generated 07.05.2009 13:21:40 by Hibernate Tools 3.2.2.GA

/**
 * WorkflowModelIsDeployedOnServer generated by hbm2java
 */
public class WorkflowModelIsDeployedOnServer implements java.io.Serializable {

	private WorkflowModelIsDeployedOnServerId id;
	private DeployedWorkflowModel deployedWorkflowModel;
	private Server server;
	private long odeVersion;

	public WorkflowModelIsDeployedOnServer() {
	}

	public WorkflowModelIsDeployedOnServer(
			WorkflowModelIsDeployedOnServerId id,
			DeployedWorkflowModel deployedWorkflowModel, Server server,
			long odeVersion) {
		this.id = id;
		this.deployedWorkflowModel = deployedWorkflowModel;
		this.server = server;
		this.odeVersion = odeVersion;
	}

	public WorkflowModelIsDeployedOnServerId getId() {
		return this.id;
	}

	public void setId(WorkflowModelIsDeployedOnServerId id) {
		this.id = id;
	}

	public DeployedWorkflowModel getDeployedWorkflowModel() {
		return this.deployedWorkflowModel;
	}

	public void setDeployedWorkflowModel(
			DeployedWorkflowModel deployedWorkflowModel) {
		this.deployedWorkflowModel = deployedWorkflowModel;
	}

	public Server getServer() {
		return this.server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public long getOdeVersion() {
		return this.odeVersion;
	}

	public void setOdeVersion(long odeVersion) {
		this.odeVersion = odeVersion;
	}

}
