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

import it.eng.paginator.util.CriteriaQueryUtils;
import it.eng.paginator.util.QueryUtils;
import it.eng.spagoLite.db.base.table.AbstractBaseTable;
import it.eng.spagoLite.db.base.table.LazyListBean;
import it.eng.spagoLite.db.base.table.LazyQuery;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Function;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

@Stateless(mappedName = "java:app/paginator/LazyListHelper")
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class LazyListHelper {
    @PersistenceContext
    private EntityManager entityManager;

    public AbstractBaseTable getTableBean(LazyListBean llBean) {
	Assert.notNull(llBean.getLazyQuery(),
		"è stata richiamato il caricamento lazy ma non ci sono dati per eseguire la query");
	return getTableBean(llBean, llBean.getResultListToTableBeanFunc());
    }

    private <T extends AbstractBaseTable> T getTableBean(LazyListBean lazyListBean,
	    Function<List, T> tansformFunc) {
	final LazyQuery lazyQuery = lazyListBean.getLazyQuery();
	List resultList;
	if (lazyQuery.isCriteriaQuery()) {
	    if (lazyListBean.getCountResultSize() <= 0) {
		final TypedQuery<Long> query = entityManager
			.createQuery(lazyQuery.getCountCriteriaQuery());
		lazyListBean.setCountResultSize(query.getSingleResult().intValue());
	    }
	    resultList = execQuery(lazyQuery.getCriteriaQuery(), lazyListBean.getMaxResult(),
		    lazyListBean.getFirstResult(), lazyListBean.getOrderByColumnName(),
		    lazyListBean.getOrderBySortingRule());
	} else {
	    if (lazyListBean.getCountResultSize() <= 0) {
		Query count = createQuery(lazyQuery.getCountQueryString(),
			lazyQuery.getQueryParams());
		lazyListBean
			.setCountResultSize(Long.class.cast(count.getSingleResult()).intValue());
	    }
	    resultList = execQuery(lazyQuery.getQueryString(), lazyQuery.getQueryParams(),
		    lazyListBean.getMaxResult(), lazyListBean.getFirstResult(),
		    lazyListBean.getOrderByColumnName(), lazyListBean.getOrderBySortingRule());
	}

	final T tableBean = tansformFunc.apply(resultList);
	if (tableBean != null) {
	    tableBean.setLazyListBean(lazyListBean);
	}
	return tableBean;
    }

    public <T extends AbstractBaseTable> T getTableBean(Query query,
	    Function<List, T> tansformFunc) {
	return getTableBean(query, tansformFunc, "", new LazyListBean());
    }

    /**
     * @param cq            CriteriaQuery per recuperare i record dal DB
     * @param transformFunc Function che prende come parametro d'ingresso una List (i risultati dal
     *                      DB) e restituisce il *TableBean
     *
     * @return *TableBean calcolato dalla lambda sulla base dei dati ottenuti dalla query
     */
    public <T extends AbstractBaseTable> T getTableBean(CriteriaQuery cq, Integer maxResults,
	    Function<List, T> transformFunc) {
	final LazyListBean lazyListBean = new LazyListBean();
	setRecordLimits(lazyListBean, maxResults);
	return getTableBean(cq, transformFunc, lazyListBean);
    }

    public <T extends AbstractBaseTable> T getTableBean(CriteriaQuery cq,
	    Function<List, T> transformFunc) {
	return getTableBean(cq, 0, transformFunc);

    }

    public <T extends AbstractBaseTable> T getTableBean(Query query, Function<List, T> tansformFunc,
	    String countDistinctField) {

	final LazyListBean lazyListBean = new LazyListBean();
	lazyListBean.setCountDistinctField(countDistinctField);
	setRecordLimits(lazyListBean, query);
	return getTableBean(query, tansformFunc, countDistinctField, lazyListBean);
    }

    private <T extends AbstractBaseTable> T getTableBean(Query query,
	    Function<List, T> tansformFunc, String countDistinctField, LazyListBean lazyListBean) {
	setRecordLimits(lazyListBean, query);
	lazyListBean.setLazyQuery(new LazyQuery(QueryUtils.queryStringFrom(query),
		QueryUtils.selectToCount(query, countDistinctField),
		QueryUtils.extractParameters(query)));
	lazyListBean.setResultListToTableBeanFunc(tansformFunc);
	return getTableBean(lazyListBean, tansformFunc);
    }

    private void setRecordLimits(LazyListBean lazyListBean, Integer maxResults) {
	if (maxResults != null && maxResults > 0 && maxResults != Integer.MAX_VALUE) {
	    lazyListBean.setMaxResult(maxResults);
	}
    }

    private void setRecordLimits(LazyListBean lazyListBean, Query query) {
	setRecordLimits(lazyListBean, query.getMaxResults());
    }

    private <T extends AbstractBaseTable> T getTableBean(CriteriaQuery cq,
	    Function<List, T> tansformFunc, LazyListBean lazyListBean) {
	lazyListBean.setResultListToTableBeanFunc(tansformFunc);
	lazyListBean.setLazyQuery(
		new LazyQuery(cq, CriteriaQueryUtils.countCriteria(entityManager, cq)));
	return getTableBean(lazyListBean, tansformFunc);
    }

    private Query createQuery(String queryString, Map<String, Object> params) {
	Query query = entityManager.createQuery(queryString);
	params.keySet().forEach(k -> query.setParameter(k, params.get(k)));
	return query;
    }

    private List execQuery(final String queryString, Map<String, Object> params, int maxResults,
	    int firstResult, String sortColumnName, int sortingRule) {
	String sortedQueryString = QueryUtils.handleOrderBy(queryString, sortColumnName,
		sortingRule);
	Query sortedQuerty = createQuery(sortedQueryString, params);
	sortedQuerty.setFirstResult(firstResult);
	sortedQuerty.setMaxResults(maxResults);
	return sortedQuerty.getResultList();
    }

    private List execQuery(final CriteriaQuery cq, int maxResults, int firstResult,
	    String sortColumnName, int sortingRule) {
	CriteriaQueryUtils.handleOrderBy(cq, sortColumnName, sortingRule,
		entityManager.getCriteriaBuilder());
	TypedQuery query = entityManager.createQuery(cq);
	query.setFirstResult(firstResult);
	query.setMaxResults(maxResults);
	return query.getResultList();
    }
}
