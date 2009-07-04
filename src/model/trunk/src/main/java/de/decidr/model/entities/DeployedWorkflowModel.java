package de.decidr.model.entities;

// Generated 04.07.2009 15:27:24 by Hibernate Tools 3.2.4.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * DeployedWorkflowModel generated by hbm2java
 */
public class DeployedWorkflowModel implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private long version;
    private WorkflowModel originalWorkflowModel;
    private Tenant tenant;
    private String name;
    private String description;
    private byte[] dwdl;
    private byte[] wsdl;
    private byte[] soapTemplate;
    private Date deployDate;
    private Set<WorkflowModelIsDeployedOnServer> workflowModelIsDeployedOnServers = new HashSet<WorkflowModelIsDeployedOnServer>(
            0);
    private Set<StartConfiguration> startConfigurations = new HashSet<StartConfiguration>(
            0);
    private Set<WorkflowInstance> workflowInstances = new HashSet<WorkflowInstance>(
            0);

    public DeployedWorkflowModel() {
    }

    public DeployedWorkflowModel(Tenant tenant, String name,
            String description, byte[] dwdl, byte[] wsdl, byte[] soapTemplate,
            Date deployDate) {
        this.tenant = tenant;
        this.name = name;
        this.description = description;
        this.dwdl = dwdl;
        this.wsdl = wsdl;
        this.soapTemplate = soapTemplate;
        this.deployDate = deployDate;
    }

    public DeployedWorkflowModel(
            WorkflowModel originalWorkflowModel,
            Tenant tenant,
            String name,
            String description,
            byte[] dwdl,
            byte[] wsdl,
            byte[] soapTemplate,
            Date deployDate,
            Set<WorkflowModelIsDeployedOnServer> workflowModelIsDeployedOnServers,
            Set<StartConfiguration> startConfigurations,
            Set<WorkflowInstance> workflowInstances) {
        this.originalWorkflowModel = originalWorkflowModel;
        this.tenant = tenant;
        this.name = name;
        this.description = description;
        this.dwdl = dwdl;
        this.wsdl = wsdl;
        this.soapTemplate = soapTemplate;
        this.deployDate = deployDate;
        this.workflowModelIsDeployedOnServers = workflowModelIsDeployedOnServers;
        this.startConfigurations = startConfigurations;
        this.workflowInstances = workflowInstances;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getVersion() {
        return this.version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public WorkflowModel getOriginalWorkflowModel() {
        return this.originalWorkflowModel;
    }

    public void setOriginalWorkflowModel(WorkflowModel originalWorkflowModel) {
        this.originalWorkflowModel = originalWorkflowModel;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getDwdl() {
        return this.dwdl;
    }

    public void setDwdl(byte[] dwdl) {
        this.dwdl = dwdl;
    }

    public byte[] getWsdl() {
        return this.wsdl;
    }

    public void setWsdl(byte[] wsdl) {
        this.wsdl = wsdl;
    }

    public byte[] getSoapTemplate() {
        return this.soapTemplate;
    }

    public void setSoapTemplate(byte[] soapTemplate) {
        this.soapTemplate = soapTemplate;
    }

    public Date getDeployDate() {
        return this.deployDate;
    }

    public void setDeployDate(Date deployDate) {
        this.deployDate = deployDate;
    }

    public Set<WorkflowModelIsDeployedOnServer> getWorkflowModelIsDeployedOnServers() {
        return this.workflowModelIsDeployedOnServers;
    }

    public void setWorkflowModelIsDeployedOnServers(
            Set<WorkflowModelIsDeployedOnServer> workflowModelIsDeployedOnServers) {
        this.workflowModelIsDeployedOnServers = workflowModelIsDeployedOnServers;
    }

    public Set<StartConfiguration> getStartConfigurations() {
        return this.startConfigurations;
    }

    public void setStartConfigurations(
            Set<StartConfiguration> startConfigurations) {
        this.startConfigurations = startConfigurations;
    }

    public Set<WorkflowInstance> getWorkflowInstances() {
        return this.workflowInstances;
    }

    public void setWorkflowInstances(Set<WorkflowInstance> workflowInstances) {
        this.workflowInstances = workflowInstances;
    }

}
