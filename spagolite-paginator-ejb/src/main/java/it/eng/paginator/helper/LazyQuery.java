/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 */

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
