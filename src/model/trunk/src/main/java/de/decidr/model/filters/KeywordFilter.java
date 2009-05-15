package de.decidr.model.filters;

import org.hibernate.Criteria;

public abstract class KeywordFilter implements de.decidr.model.filters.Filter {
	protected String keyword;

	public void apply(Criteria criteria) {
		throw new UnsupportedOperationException();
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getKeyword() {
		return this.keyword;
	}
}