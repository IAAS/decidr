package de.decidr.model.entities;

// Generated 15.05.2009 17:31:29 by Hibernate Tools 3.2.2.GA

import java.util.HashSet;
import java.util.Set;

/**
 * Role generated by hbm2java
 */
public class Role implements java.io.Serializable {

	private Long id;
	private String name;
	private Set<Permission> roleHasPermission = new HashSet<Permission>(0);

	public Role() {
	}

	public Role(String name) {
		this.name = name;
	}

	public Role(String name, Set<Permission> roleHasPermission) {
		this.name = name;
		this.roleHasPermission = roleHasPermission;
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

	public Set<Permission> getRoleHasPermission() {
		return this.roleHasPermission;
	}

	public void setRoleHasPermission(Set<Permission> roleHasPermission) {
		this.roleHasPermission = roleHasPermission;
	}

}
