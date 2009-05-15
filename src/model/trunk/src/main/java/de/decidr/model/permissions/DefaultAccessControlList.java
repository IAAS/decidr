package de.decidr.model.permissions;

public class DefaultAccessControlList implements AccessControlList {

	public void init() {
	}

	public void clearRules() {
	}

	public void setRule(Role role, Permission permission, Asserter[] asserters,
			AssertMode mode) {
	}

	public void allow(Role role, Permission permission) {
	}

	public Boolean isAllowed(Role role, Permission permission) {
		return null;
	}

	public AccessControlList getInstance() {
		return null;
	}
}