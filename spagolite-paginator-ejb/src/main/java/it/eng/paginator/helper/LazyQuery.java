package it.eng.paginator.helper;

import it.eng.spagoLite.db.base.table.Param;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author manuel.bertuzzi@eng.it
 */
public class LazyQuery {

    private final Query query;
    private final String countDistinctField;

    private final Set<Param> criteriaQueryParams;
    private final CriteriaQuery criteriaQuery;
    private final Integer maxResults;

    public LazyQuery(Query query, String countDistinctField) {
        this.query = query;
        this.countDistinctField = countDistinctField;
        this.criteriaQuery = null;
        this.criteriaQueryParams = null;
        this.maxResults = null;
    }

    public LazyQuery(Query query) {
        this.query = query;
        this.countDistinctField = null;
        this.criteriaQuery = null;
        this.criteriaQueryParams = null;
        this.maxResults = null;
    }

    public LazyQuery(CriteriaQuery criteriaQuery, Set<Param> criteriaQueryParams) {
        this.criteriaQueryParams = criteriaQueryParams;
        this.criteriaQuery = criteriaQuery;
        this.query = null;
        this.countDistinctField = null;
        this.maxResults = null;
    }

    public LazyQuery(CriteriaQuery criteriaQuery) {
        this.criteriaQueryParams = new HashSet<>();
        this.criteriaQuery = criteriaQuery;
        this.query = null;
        this.countDistinctField = null;
        this.maxResults = null;
    }

    public LazyQuery(CriteriaQuery criteriaQuery, Set<Param> criteriaQueryParams, int maxResults) {
        this.criteriaQueryParams = criteriaQueryParams;
        this.criteriaQuery = criteriaQuery;
        this.maxResults = maxResults;
        this.query = null;
        this.countDistinctField = null;

    }

    public LazyQuery(CriteriaQuery criteriaQuery, int maxResults) {
        this.criteriaQueryParams = new HashSet<>();
        this.criteriaQuery = criteriaQuery;
        this.maxResults = maxResults;
        this.query = null;
        this.countDistinctField = null;
    }

    public Query getQuery() {
        return query;
    }

    public Set<Param> getCriteriaQueryParams() {
        return criteriaQueryParams;
    }

    public CriteriaQuery getCriteriaQuery() {
        return criteriaQuery;
    }

    public String getCountDistinctField() {
        return countDistinctField;
    }

    public boolean isCriteriaQuery() {
        return criteriaQuery != null;
    }

    public boolean isQuery() {
        return query != null;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

}
