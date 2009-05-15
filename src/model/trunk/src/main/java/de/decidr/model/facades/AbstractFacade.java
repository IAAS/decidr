package de.decidr.model.facades;

public abstract class AbstractFacade {
	protected Long actorId;

	public AbstractFacade(Long actorId) {
		this.actorId = actorId;
	}
}