package de.decidr.model.permissions;

import java.security.BasicPermission;

public class Permission extends BasicPermission {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Permission(String name) {
		super(name);
	}
}