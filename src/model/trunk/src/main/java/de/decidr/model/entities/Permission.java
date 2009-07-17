package de.decidr.model.entities;

// Generated 17.07.2009 15:40:18 by Hibernate Tools 3.2.4.GA

import java.util.HashSet;
import java.util.Set;

/**
 * Permission generated by hbm2java
 */
public class Permission implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private Set<RoleHasPermission> roleHasPermissions = new HashSet<RoleHasPermission>(
            0);

    public Permission() {
    }

    public Permission(String name) {
        this.name = name;
    }

    public Permission(String name, Set<RoleHasPermission> roleHasPermissions) {
        this.name = name;
        this.roleHasPermissions = roleHasPermissions;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RoleHasPermission> getRoleHasPermissions() {
        return this.roleHasPermissions;
    }

    public void setRoleHasPermissions(Set<RoleHasPermission> roleHasPermissions) {
        this.roleHasPermissions = roleHasPermissions;
    }

}
