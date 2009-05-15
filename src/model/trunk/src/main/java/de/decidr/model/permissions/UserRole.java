package de.decidr.model.permissions;

public class UserRole implements Role {
	protected Long userId;

	public UserRole(Long userId) {
		this.userId = userId;
	}
}