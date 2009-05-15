package de.decidr.model.permissions;

public interface AccessControlList {

	public void clearRules();

	public void setRule(de.decidr.model.permissions.Role role, de.decidr.model.permissions.Permission permission, de.decidr.model.permissions.Asserter[] asserters, AssertMode mode);

	public void allow(de.decidr.model.permissions.Role role, de.decidr.model.permissions.Permission permission);

	public Boolean isAllowed(de.decidr.model.permissions.Role role, de.decidr.model.permissions.Permission permission);
}