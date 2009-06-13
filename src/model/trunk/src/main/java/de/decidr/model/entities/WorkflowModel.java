package de.decidr.model.entities;

// Generated 13.06.2009 19:54:01 by Hibernate Tools 3.2.4.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * WorkflowModel generated by hbm2java
 */
public class WorkflowModel implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private long version;
    private Tenant tenant;
    private User modifiedByUser;
    private String name;
    private String description;
    private boolean published;
    private boolean executable;
    private Date creationDate;
    private Date modifiedDate;
    private byte[] dwdl;
    private Set<Invitation> invitations = new HashSet<Invitation>(0);
    private Set<UserAdministratesWorkflowModel> userAdministratesWorkflowModels = new HashSet<UserAdministratesWorkflowModel>(
            0);
    private Set<DeployedWorkflowModel> deployedWorkflowModels = new HashSet<DeployedWorkflowModel>(
            0);

    public WorkflowModel() {
    }

    public WorkflowModel(Tenant tenant, String name, String description,
            boolean published, boolean executable, Date creationDate,
            Date modifiedDate, byte[] dwdl) {
        this.tenant = tenant;
        this.name = name;
        this.description = description;
        this.published = published;
        this.executable = executable;
        this.creationDate = creationDate;
        this.modifiedDate = modifiedDate;
        this.dwdl = dwdl;
    }

    public WorkflowModel(
            Tenant tenant,
            User modifiedByUser,
            String name,
            String description,
            boolean published,
            boolean executable,
            Date creationDate,
            Date modifiedDate,
            byte[] dwdl,
            Set<Invitation> invitations,
            Set<UserAdministratesWorkflowModel> userAdministratesWorkflowModels,
            Set<DeployedWorkflowModel> deployedWorkflowModels) {
        this.tenant = tenant;
        this.modifiedByUser = modifiedByUser;
        this.name = name;
        this.description = description;
        this.published = published;
        this.executable = executable;
        this.creationDate = creationDate;
        this.modifiedDate = modifiedDate;
        this.dwdl = dwdl;
        this.invitations = invitations;
        this.userAdministratesWorkflowModels = userAdministratesWorkflowModels;
        this.deployedWorkflowModels = deployedWorkflowModels;
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

    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public User getModifiedByUser() {
        return this.modifiedByUser;
    }

    public void setModifiedByUser(User modifiedByUser) {
        this.modifiedByUser = modifiedByUser;
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

    public boolean isPublished() {
        return this.published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isExecutable() {
        return this.executable;
    }

    public void setExecutable(boolean executable) {
        this.executable = executable;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public byte[] getDwdl() {
        return this.dwdl;
    }

    public void setDwdl(byte[] dwdl) {
        this.dwdl = dwdl;
    }

    public Set<Invitation> getInvitations() {
        return this.invitations;
    }

    public void setInvitations(Set<Invitation> invitations) {
        this.invitations = invitations;
    }

    public Set<UserAdministratesWorkflowModel> getUserAdministratesWorkflowModels() {
        return this.userAdministratesWorkflowModels;
    }

    public void setUserAdministratesWorkflowModels(
            Set<UserAdministratesWorkflowModel> userAdministratesWorkflowModels) {
        this.userAdministratesWorkflowModels = userAdministratesWorkflowModels;
    }

    public Set<DeployedWorkflowModel> getDeployedWorkflowModels() {
        return this.deployedWorkflowModels;
    }

    public void setDeployedWorkflowModels(
            Set<DeployedWorkflowModel> deployedWorkflowModels) {
        this.deployedWorkflowModels = deployedWorkflowModels;
    }

}
