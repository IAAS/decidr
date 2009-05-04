package de.decidr.model.entities;

// Generated 04.05.2009 13:48:05 by Hibernate Tools 3.2.2.GA

import java.util.HashSet;
import java.util.Set;

/**
 * Server generated by hbm2java
 */
public class Server implements java.io.Serializable {

	private Long id;
	private String location;
	private byte load;
	private boolean locked;
	private boolean dynamicallyAdded;
	private Set<DeployedWorkflowModel> deployedWorkflowModels = new HashSet<DeployedWorkflowModel>(
			0);

	public Server() {
	}

	public Server(String location, byte load, boolean locked,
			boolean dynamicallyAdded) {
		this.location = location;
		this.load = load;
		this.locked = locked;
		this.dynamicallyAdded = dynamicallyAdded;
	}

	public Server(String location, byte load, boolean locked,
			boolean dynamicallyAdded,
			Set<DeployedWorkflowModel> deployedWorkflowModels) {
		this.location = location;
		this.load = load;
		this.locked = locked;
		this.dynamicallyAdded = dynamicallyAdded;
		this.deployedWorkflowModels = deployedWorkflowModels;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public byte getLoad() {
		return this.load;
	}

	public void setLoad(byte load) {
		this.load = load;
	}

	public boolean isLocked() {
		return this.locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isDynamicallyAdded() {
		return this.dynamicallyAdded;
	}

	public void setDynamicallyAdded(boolean dynamicallyAdded) {
		this.dynamicallyAdded = dynamicallyAdded;
	}

	public Set<DeployedWorkflowModel> getDeployedWorkflowModels() {
		return this.deployedWorkflowModels;
	}

	public void setDeployedWorkflowModels(
			Set<DeployedWorkflowModel> deployedWorkflowModels) {
		this.deployedWorkflowModels = deployedWorkflowModels;
	}

}
