package de.decidr.model.entities;

// Generated 07.08.2009 16:15:56 by Hibernate Tools 3.2.4.GA

/**
 * StartConfiguration generated by hbm2java
 */
public class StartConfiguration implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private DeployedWorkflowModel deployedWorkflowModel;
    private byte[] startConfiguration;

    public StartConfiguration() {
    }

    public StartConfiguration(long id,
            DeployedWorkflowModel deployedWorkflowModel,
            byte[] startConfiguration) {
        this.id = id;
        this.deployedWorkflowModel = deployedWorkflowModel;
        this.startConfiguration = startConfiguration;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DeployedWorkflowModel getDeployedWorkflowModel() {
        return this.deployedWorkflowModel;
    }

    public void setDeployedWorkflowModel(
            DeployedWorkflowModel deployedWorkflowModel) {
        this.deployedWorkflowModel = deployedWorkflowModel;
    }

    public byte[] getStartConfiguration() {
        return this.startConfiguration;
    }

    public void setStartConfiguration(byte[] startConfiguration) {
        this.startConfiguration = startConfiguration;
    }

}
