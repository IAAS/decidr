package de.decidr.model.entities;

// Generated 12.10.2009 16:53:44 by Hibernate Tools 3.2.4.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Tenant generated by hbm2java
 */
public class Tenant implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private File logo;
    private File simpleColorScheme;
    private File advancedColorScheme;
    private User admin;
    private File currentColorScheme;
    private String name;
    private String description;
    private Date approvedSince;
    private Set<UserIsMemberOfTenant> userIsMemberOfTenants = new HashSet<UserIsMemberOfTenant>(
            0);
    private Set<WorkflowModel> workflowModels = new HashSet<WorkflowModel>(0);
    private Set<Invitation> invitations = new HashSet<Invitation>(0);
    private Set<User> users = new HashSet<User>(0);
    private Set<DeployedWorkflowModel> deployedWorkflowModels = new HashSet<DeployedWorkflowModel>(
            0);

    public Tenant() {
    }

    public Tenant(User admin, String name, String description) {
        this.admin = admin;
        this.name = name;
        this.description = description;
    }

    public Tenant(File logo, File simpleColorScheme, File advancedColorScheme,
            User admin, File currentColorScheme, String name,
            String description, Date approvedSince,
            Set<UserIsMemberOfTenant> userIsMemberOfTenants,
            Set<WorkflowModel> workflowModels, Set<Invitation> invitations,
            Set<User> users, Set<DeployedWorkflowModel> deployedWorkflowModels) {
        this.logo = logo;
        this.simpleColorScheme = simpleColorScheme;
        this.advancedColorScheme = advancedColorScheme;
        this.admin = admin;
        this.currentColorScheme = currentColorScheme;
        this.name = name;
        this.description = description;
        this.approvedSince = approvedSince;
        this.userIsMemberOfTenants = userIsMemberOfTenants;
        this.workflowModels = workflowModels;
        this.invitations = invitations;
        this.users = users;
        this.deployedWorkflowModels = deployedWorkflowModels;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public File getLogo() {
        return this.logo;
    }

    public void setLogo(File logo) {
        this.logo = logo;
    }

    public File getSimpleColorScheme() {
        return this.simpleColorScheme;
    }

    public void setSimpleColorScheme(File simpleColorScheme) {
        this.simpleColorScheme = simpleColorScheme;
    }

    public File getAdvancedColorScheme() {
        return this.advancedColorScheme;
    }

    public void setAdvancedColorScheme(File advancedColorScheme) {
        this.advancedColorScheme = advancedColorScheme;
    }

    public User getAdmin() {
        return this.admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public File getCurrentColorScheme() {
        return this.currentColorScheme;
    }

    public void setCurrentColorScheme(File currentColorScheme) {
        this.currentColorScheme = currentColorScheme;
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

    public Date getApprovedSince() {
        return this.approvedSince;
    }

    public void setApprovedSince(Date approvedSince) {
        this.approvedSince = approvedSince;
    }

    public Set<UserIsMemberOfTenant> getUserIsMemberOfTenants() {
        return this.userIsMemberOfTenants;
    }

    public void setUserIsMemberOfTenants(
            Set<UserIsMemberOfTenant> userIsMemberOfTenants) {
        this.userIsMemberOfTenants = userIsMemberOfTenants;
    }

    public Set<WorkflowModel> getWorkflowModels() {
        return this.workflowModels;
    }

    public void setWorkflowModels(Set<WorkflowModel> workflowModels) {
        this.workflowModels = workflowModels;
    }

    public Set<Invitation> getInvitations() {
        return this.invitations;
    }

    public void setInvitations(Set<Invitation> invitations) {
        this.invitations = invitations;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<DeployedWorkflowModel> getDeployedWorkflowModels() {
        return this.deployedWorkflowModels;
    }

    public void setDeployedWorkflowModels(
            Set<DeployedWorkflowModel> deployedWorkflowModels) {
        this.deployedWorkflowModels = deployedWorkflowModels;
    }

}
