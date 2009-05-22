package de.decidr.model.facades;

import de.decidr.model.permissions.Role;

public abstract class AbstractFacade {
	protected Role actor;

	public AbstractFacade(Role actor) {
		this.actor = actor;
	}
}