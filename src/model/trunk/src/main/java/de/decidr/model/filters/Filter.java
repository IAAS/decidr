package de.decidr.model.filters;

import org.hibernate.Criteria;

public interface Filter {

	public void apply(Criteria criteria);
}