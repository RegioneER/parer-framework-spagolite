package it.eng.spagoLite.db.base.table;

import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.Map;

public class LazyQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String queryString;
    private final Map<String, Object> queryParams;
    private final String countQueryString;

    private final CriteriaQuery criteriaQuery;
    private final CriteriaQuery countCriteriaQuery;

    public LazyQuery(String queryString, String countQueryString, Map<String, Object> queryParams) {
        this.queryString = queryString;
        this.countQueryString = countQueryString;
        this.queryParams = queryParams;
        this.criteriaQuery = null;
        this.countCriteriaQuery = null;
    }

    public LazyQuery(CriteriaQuery criteriaQuery, CriteriaQuery countCriteriaQuery) {
        this.queryString = null;
        this.countQueryString = null;
        this.queryParams = null;
        this.criteriaQuery = criteriaQuery;
        this.countCriteriaQuery = countCriteriaQuery;
    }

    public String getQueryString() {
        return queryString;
    }

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public String getCountQueryString() {
        return countQueryString;
    }

    public CriteriaQuery getCriteriaQuery() {
        return criteriaQuery;
    }

    public CriteriaQuery getCountCriteriaQuery() {
        return countCriteriaQuery;
    }

    public boolean isCriteriaQuery() {
        return criteriaQuery != null;
    }
}
