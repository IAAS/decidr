package de.decidr.model.filters;

import org.hibernate.Criteria;

public class Paginator {
	protected Long totalNumberOfItems;
	protected Long currentPage;
	protected Long itemsPerPage;
	protected Long startIndex;
	protected Long endIndex;

	public void apply(Criteria criteria) {
		throw new UnsupportedOperationException();
	}

	public Long getTotalNumberOfItems() {
		return this.totalNumberOfItems;
	}

	public void setCurrentPage(Long currentPage) {
		this.currentPage = currentPage;
	}

	public Long getCurrentPage() {
		return this.currentPage;
	}

	public void setItemsPerPage(Long itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public Long getItemsPerPage() {
		return this.itemsPerPage;
	}

	public void setStartIndex(Long startIndex) {
		this.startIndex = startIndex;
	}

	public Long getStartIndex() {
		return this.startIndex;
	}

	public void setEndIndex(Long endIndex) {
		this.endIndex = endIndex;
	}

	public Long getEndIndex() {
		return this.endIndex;
	}
}