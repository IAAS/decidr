package de.decidr.model.filters;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

/**
 * Filters entities by searching for a single keyword in a given set of
 * properties. At least one property must contain the given keyword. Whether the
 * search is case sensitive or not depends on collation set used by the
 * underlying database.The search is performed using the pattern "keyword%".
 * <p>
 * This means that searching for the keyword "Arbeit" will find "Arbeitsamt" but
 * not "Fleißarbeit"
 * <p>
 * A full text search is not yet available due to the limitations of InnoDB (no
 * fulltext indices).
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public abstract class KeywordFilter implements de.decidr.model.filters.Filter {

    private String keyword = "";

    private Collection<String> properties = null;

    /**
     * {@inheritDoc}
     */
    public void apply(Criteria criteria) {

        if ((properties == null) || (keyword == null)) {
            return;
        }

        Disjunction keywordCrit = Restrictions.disjunction();

        /*
         * where (prop1 like "keyword%") or (prop2 like "keyword%") or (prop3
         * like "keyword%") ...
         */
        for (String property : properties) {
            keywordCrit.add(Restrictions.like(property, keyword + "%"));
        }

        criteria.add(keywordCrit);
    }

    /**
     * @param keyword
     *            the keyword to search for
     */
    public void setKeyword(String keyword) {
        /*
         * Manually escape the keyword assuming the default MySQL escape
         * sequence. At the very least, we must escape "%" manually, but it
         * won't hurt to escape the rest as well.
         */
        this.keyword = keyword.replace("\\", "\\\\").replace("_", "\\_")
                .replace("%", "\\%");
    }

    /**
     * @return the keyword to search for
     */
    public String getKeyword() {
        return this.keyword;
    }

    /**
     * @param properties
     *            the properties to search
     */
    public void setProperties(Collection<String> properties) {
        this.properties = properties;
    }

    /**
     * @return the properties to search
     */
    public Collection<String> getProperties() {
        return properties;
    }
}