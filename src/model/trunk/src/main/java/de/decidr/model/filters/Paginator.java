package de.decidr.model.filters;

/**
 * A recurring problem in the GUI is that you can’t display a list of tens of
 * thousands of items at once, but they must be spread over several pages.
 * Additionally, such queries would put a huge strain on the database.
 * <p>
 * The paginator behaves like a filter, but additionally retrieves the total
 * number of items that are available. To avoid confusion with other filters,
 * the paginator does not implement the Filter interface. This is due to the
 * fact that a paginator must always be applied to Hibernate Criteria after all
 * other filters have been applied.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class Paginator {
    /**
     * This value is retrieved with each apply by adding a COUNT(*) to the
     * query.
     */
    protected Integer totalNumberOfItems = 0;

    /**
     * Each page has at least one item.
     */
    protected Integer itemsPerPage = 1;
    /**
     * The first item has the index 0. startIndex is always <= endIndex
     */
    protected Integer startIndex = 0;

    /**
     * The last item has the index totalNumberOfItems - 1. endIndex is always >=
     * startIndex
     */
    protected Integer endIndex = 0;

    /**
     * Retrieves the total number of items and applies the page settings to the
     * given Criteria.
     * 
     * @param criteria
     *            the Criteria to paginate
     * @return the modified Criteria
     */
    public PaginatingCriteria apply(PaginatingCriteria criteria) {
        this.totalNumberOfItems = criteria.rowCount();
        criteria.setMaxResults(this.getItemsPerPage());
        criteria.setFirstResult(this.getStartIndex());
        return criteria;
    }

    /**
     * @return the total number of items.
     */
    public Integer getTotalNumberOfItems() {
        return this.totalNumberOfItems;
    }

    /**
     * @return the total number of pages.
     */
    public Integer getTotalNumberOfPages() {
        return this.totalNumberOfItems / itemsPerPage;
    }

    /**
     * Sets the current page. If the paginator doesn't have that many pages, it
     * is set to the highest possible page instead.
     * <p>
     * Changes startIndex and endIndex accordingly.
     * 
     * @param currentPage
     */
    public void setCurrentPage(Integer currentPage) {
        setStartIndex(this.itemsPerPage
                * (Math.min(getTotalNumberOfPages(), currentPage) - 1));
    }

    /**
     * @return the current page.
     */
    public Integer getCurrentPage() {
        return (startIndex / itemsPerPage) + 1;
    }

    /**
     * Sets the number of items per page. Must be a value >= 1. The end index is
     * updated using the new value.
     * 
     * @param itemsPerPage
     */
    public void setItemsPerPage(Integer itemsPerPage) {
        if (itemsPerPage < 1) {
            throw new IllegalArgumentException(
                    "Must display at least one item per page.");
        }
        this.itemsPerPage = itemsPerPage;
        setStartIndex(this.startIndex);
    }

    /**
     * @return the number of items per page, at least 1.
     */
    public Integer getItemsPerPage() {
        return this.itemsPerPage;
    }

    /**
     * Sets the start index. EndIndex is updated using itemsPerPage.
     * 
     * @param startIndex
     */
    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
        this.endIndex = this.startIndex + this.itemsPerPage - 1;
    }

    /**
     * @return the start index
     */
    public Integer getStartIndex() {
        return this.startIndex;
    }

    /**
     * @param endIndex
     *            the new end index
     */
    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }

    /**
     * @return the end index
     */
    public Integer getEndIndex() {
        return this.endIndex;
    }
}