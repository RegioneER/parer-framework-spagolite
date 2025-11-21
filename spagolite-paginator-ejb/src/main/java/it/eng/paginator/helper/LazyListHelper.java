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
import org.hibernate.query.internal.NativeQueryImpl;
import org.springframework.util.Assert;
import org.apache.commons.lang3.StringUtils;

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
	    // MEV #39292 blocco modificato per gestire JPQL e native
	    if (lazyListBean.getCountResultSize() <= 0) {
		// Creiamo la query di conteggio, usando il nuovo metodo che sa gestire entrambi i
		// tipi
		Query count = createQuery(lazyQuery.getCountQueryString(),
			lazyQuery.getQueryParams(), lazyQuery.isNativeQuery(), null // <- La query
										    // di conteggio
										    // non mappa a
										    // nessuna
										    // classe/DTO
		);
		// Il risultato di una query di conteggio nativa potrebbe essere BigDecimal,
		// "castiamo" a Number
		lazyListBean.setCountResultSize(((Number) count.getSingleResult()).intValue());
	    }

	    // Eseguiamo la query per i dati, usando il nuovo metodo "potenziato" per gestire i 2
	    // casi
	    resultList = execQuery(lazyQuery.getQueryString(), lazyQuery.getQueryParams(),
		    lazyListBean.getMaxResult(), lazyListBean.getFirstResult(),
		    lazyListBean.getOrderByColumnName(), lazyListBean.getOrderBySortingRule(),
		    lazyQuery.isNativeQuery(), // <- Passiamo il flag
		    lazyQuery.getResultSetMappingName() // <- Passiamo il nome della mappatura
	    );
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

    // ===================================================================================
    // MEV #39292 - NUOVO METODO: sarà il NUOVO punto d'ingresso per le query native paginate.
    // ===================================================================================
    public <T extends AbstractBaseTable> T getTableBeanForNativeQuery(Query nativeDataQuery,
	    Function<List, T> transformFunc, String countDistinctField,
	    String resultSetMappingName) {

	final LazyListBean lazyListBean = new LazyListBean();
	setRecordLimits(lazyListBean, nativeDataQuery);

	// Sappiamo che è una query nativa
	boolean isNative = true;

	// Creiamo la query di conteggio.
	String countQueryString = QueryUtils.selectToCount(nativeDataQuery, countDistinctField);

	// Usiamo il nuovo costruttore di LazyQuery, passando esplicitamente le informazioni.
	lazyListBean.setLazyQuery(new LazyQuery(QueryUtils.queryStringFrom(nativeDataQuery),
		countQueryString, QueryUtils.extractParameters(nativeDataQuery), isNative,
		resultSetMappingName // Memorizzazione del nome della mappa per le pagine successive
	));

	lazyListBean.setResultListToTableBeanFunc(transformFunc);

	// Delechiamo l'esecuzione al "motore", che ora ha tutte le info corrette.
	return getTableBean(lazyListBean, transformFunc);
    }

    // MEV #39292 - METODO MODIFICATO
    private <T extends AbstractBaseTable> T getTableBean(Query query,
	    Function<List, T> tansformFunc, String countDistinctField, LazyListBean lazyListBean) {
	setRecordLimits(lazyListBean, query);

	// Recupera l'info per sapere se la query è nativa o neno
	boolean isNative = QueryUtils.isNativeQuery(query);

	// Creiamo la query di conteggio.
	String countQueryString = QueryUtils.selectToCount(query, countDistinctField);

	// Otteniamo il nome della mappatura, se presente.
	String resultSetMappingName = getResultSetMappingName(query);

	// Usiamo il nuovo costruttore di LazyQuery
	lazyListBean.setLazyQuery(new LazyQuery(QueryUtils.queryStringFrom(query), countQueryString,
		QueryUtils.extractParameters(query), isNative, // Passiamo il flag
		resultSetMappingName // Passiamo il nome della mappatura
	));
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

    // MEV #39292 - METODO NUOVO
    private String getResultSetMappingName(Query query) {
	if (QueryUtils.isNativeQuery(query)) {
	    try {
		// Usiamo la reflection per accedere a un campo privato di Hibernate.
		NativeQueryImpl<?> nativeQuery = query.unwrap(NativeQueryImpl.class);
		java.lang.reflect.Field field = nativeQuery.getClass()
			.getDeclaredField("sqlResultSetMappingName");
		field.setAccessible(true);
		return (String) field.get(nativeQuery);
	    } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
		// Nessun problema se fallisce, significa che non c'è una mappatura.
		return null;
	    }
	}
	return null;
    }

    // MEV #39292 - METODO MODIFICATO per discernere tra native e non
    private Query createQuery(String queryString, Map<String, Object> params, boolean isNative,
	    String resultSetMappingName) {
	Query query;
	if (isNative) {
	    if (StringUtils.isNotBlank(resultSetMappingName)) {
		query = entityManager.createNativeQuery(queryString, resultSetMappingName);
	    } else {
		query = entityManager.createNativeQuery(queryString);
	    }
	} else {
	    query = entityManager.createQuery(queryString);
	}

	if (params != null) {
	    params.keySet().forEach(k -> query.setParameter(k, params.get(k)));
	}
	return query;
    }

    // MEV #39292 - METODO NUOVO
    private Query createQuery(String queryString, Map<String, Object> params) {
	// Mantenuto per retrocompatibilità, delega al nuovo metodo assumendo JPQL.
	return createQuery(queryString, params, false, null);
    }

    // MEV #39292 - METODO MODIFICATO
    private List execQuery(final String queryString, Map<String, Object> params, int maxResults,
	    int firstResult, String sortColumnName, int sortingRule, boolean isNative,
	    String resultSetMappingName) {

	String sortedQueryString = QueryUtils.handleOrderBy(queryString, sortColumnName,
		sortingRule);

	Query sortedQuery = createQuery(sortedQueryString, params, isNative, resultSetMappingName);

	sortedQuery.setFirstResult(firstResult);
	sortedQuery.setMaxResults(maxResults);
	return sortedQuery.getResultList();
    }

    // MEV #39292 - METODO NUOVO
    private List execQuery(final String queryString, Map<String, Object> params, int maxResults,
	    int firstResult, String sortColumnName, int sortingRule) {
	// Mantenuto per retrocompatibilità, delega al nuovo metodo assumendo JPQL.
	return execQuery(queryString, params, maxResults, firstResult, sortColumnName, sortingRule,
		false, null);
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