package de.decidr.model.entities;

// Generated 17.07.2009 15:40:18 by Hibernate Tools 3.2.4.GA

import java.util.HashSet;
import java.util.Set;

/**
 * Server generated by hbm2java
 */
public class Server implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private ServerType serverType;
    private String location;
    private byte load;
    private boolean locked;
    private boolean dynamicallyAdded;
    private Set<WorkflowModelIsDeployedOnServer> workflowModelIsDeployedOnServers = new HashSet<WorkflowModelIsDeployedOnServer>(
            0);
    private Set<WorkflowInstance> workflowInstances = new HashSet<WorkflowInstance>(
            0);

    public Server() {
    }

    public Server(ServerType serverType, String location, byte load,
            boolean locked, boolean dynamicallyAdded) {
        this.serverType = serverType;
        this.location = location;
        this.load = load;
        this.locked = locked;
        this.dynamicallyAdded = dynamicallyAdded;
    }

    public Server(
            ServerType serverType,
            String location,
            byte load,
            boolean locked,
            boolean dynamicallyAdded,
            Set<WorkflowModelIsDeployedOnServer> workflowModelIsDeployedOnServers,
            Set<WorkflowInstance> workflowInstances) {
        this.serverType = serverType;
        this.location = location;
        this.load = load;
        this.locked = locked;
        this.dynamicallyAdded = dynamicallyAdded;
        this.workflowModelIsDeployedOnServers = workflowModelIsDeployedOnServers;
        this.workflowInstances = workflowInstances;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServerType getServerType() {
        return this.serverType;
    }

    public void setServerType(ServerType serverType) {
        this.serverType = serverType;
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

    public Set<WorkflowModelIsDeployedOnServer> getWorkflowModelIsDeployedOnServers() {
        return this.workflowModelIsDeployedOnServers;
    }

    public void setWorkflowModelIsDeployedOnServers(
            Set<WorkflowModelIsDeployedOnServer> workflowModelIsDeployedOnServers) {
        this.workflowModelIsDeployedOnServers = workflowModelIsDeployedOnServers;
    }

    public Set<WorkflowInstance> getWorkflowInstances() {
        return this.workflowInstances;
    }

    public void setWorkflowInstances(Set<WorkflowInstance> workflowInstances) {
        this.workflowInstances = workflowInstances;
    }

}
