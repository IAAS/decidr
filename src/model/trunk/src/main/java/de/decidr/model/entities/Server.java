package de.decidr.model.entities;

// Generated 12.06.2009 08:13:27 by Hibernate Tools 3.2.4.GA

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
    private Set<WorkflowModelIsDeployedOnServer> workflowModelIsDeployedOnServers = new HashSet<WorkflowModelIsDeployedOnServer>(
            0);
    private Set<WorkflowInstance> workflowInstances = new HashSet<WorkflowInstance>(
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

    public Server(
            String location,
            byte load,
            boolean locked,
            boolean dynamicallyAdded,
            Set<WorkflowModelIsDeployedOnServer> workflowModelIsDeployedOnServers,
            Set<WorkflowInstance> workflowInstances) {
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

    // The following is extra code specified in the hbm.xml files
    private static final long serialVersionUID = 1L;
    // end of extra code specified in the hbm.xml files

}
