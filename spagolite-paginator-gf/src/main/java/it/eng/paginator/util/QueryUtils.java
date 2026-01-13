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

package it.eng.paginator.util;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Parameter;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import it.eng.spagoLite.db.base.sorting.SortingRule;
import org.hibernate.query.NativeQuery;

public abstract class QueryUtils {

    private QueryUtils() {
	throw new IllegalStateException("Da utilizzare tramite i metodi statici");
    }

    private static final String MULTI_COL_DISTINCT_REGEXP = "(?i)\\s*SELECT COUNT\\s*\\(\\s*(DISTINCT [A-Za-z0-9_.]+\\s*,[A-Za-z0-9_,.\\s]+)\\)(.*)";
    private static final String MULTI_COL_DISTINCT_ALIAS_REGEXP = "(?i)\\s*SELECT COUNT\\s*\\(\\s*(DISTINCT [A-Za-z0-9_.]+\\s*,[A-Za-z0-9_,.\\s]+)\\)\\s*(AS\\s+[A-Za-z0-9_]+)\\s*(.*)";

    public static String fixOracleMultiColumnDistinct(final String sql) {
	final String queryString = normalizeQueryString(sql);
	String subQuery = null;
	String countAlias = "";
	if (queryString.matches(MULTI_COL_DISTINCT_ALIAS_REGEXP)) {
	    countAlias = queryString.replaceFirst(MULTI_COL_DISTINCT_ALIAS_REGEXP, " $2 ");
	    subQuery = queryString.replaceFirst(MULTI_COL_DISTINCT_ALIAS_REGEXP, "SELECT $1 $3");

	} else if (queryString.matches(MULTI_COL_DISTINCT_REGEXP)) {
	    subQuery = queryString.replaceFirst(MULTI_COL_DISTINCT_REGEXP, "SELECT $1 $2");
	}
	if (subQuery != null) {
	    return normalizeQueryString(
		    "SELECT COUNT(*)" + countAlias + " FROM (" + subQuery + ")");
	} else {
	    return queryString;
	}
    }

    public static String selectToCount(final String selectQuery) {
	// Questa versione ora è meno sicura, ma la lasciamo per retrocompatibilità.
	// Assumerà che sia JPQL.
	return selectToCountFromJpql(selectQuery, null);
    }

    public static String removeOrderBy(final String selectQuery) {
	String sql = normalizeQueryString(selectQuery);
	final int orderByIndex = sql.toUpperCase().indexOf(" ORDER BY ");
	if (orderByIndex > 0) {
	    return sql.substring(0, orderByIndex);
	}
	return sql;
    }

    public static String normalizeQueryString(String queryString) {
	String multipleSpace = "\\s{2,}";
	return queryString.replaceAll(multipleSpace, " ");
    }

    // ===================================================================================
    // MEV #39292 - Adeguamento/integrazione metodi per gestione paginazione anche
    // con query SQL native
    // ===================================================================================

    // NUOVO METODO: Determina se un oggetto Query è stato creato come nativo o JPQL.
    public static boolean isNativeQuery(Query query) {
	try {
	    // Tentiamo di "scartare" l'oggetto Query per vedere se sotto c'è un NativeQuery.
	    // Questo funziona perché createNativeQuery restituisce un'implementazione diversa
	    // rispetto a createQuery. Se l'unwrap fallisce, lancia eccezione.
	    // NB: il controllo di unwrap viene fatto su NativeQuery perchè è specifica.
	    // Se facessimo il controllo su Query.class (per capire se è query JPQL)
	    // restiuirebbe sempre true! L'interfaccia org.hibernate.query.Query è infatti
	    // l'interfaccia base di Hibernate per tutte le query.
	    // Sia l'implementazione per le query JPQL (QueryImpl) che quella per le query native
	    // (NativeQueryImpl) estendono questa interfaccia.
	    query.unwrap(NativeQuery.class);
	    return true;
	} catch (RuntimeException e) {
	    // L'unwrap lancia una HibernateException (che estende
	    // RuntimeException), non una ClassCastException diretta.
	    return false;
	}
    }

    // METODO MODIFICATO: Ora gestisce entrambi i tipi di query.
    public static String queryStringFrom(Query query) {
	if (isNativeQuery(query)) {
	    // Per le query native, questo è il modo corretto di ottenere la stringa SQL.
	    return query.unwrap(NativeQuery.class).getQueryString();
	} else {
	    // Manteniamo il vecchio metodo per le query JPQL.
	    final org.hibernate.query.Query hibernateQuery = query
		    .unwrap(org.hibernate.query.Query.class);
	    return hibernateQuery.getQueryString();
	}
    }

    // METODO MODIFICATO: Ora "biforca" la logica a seconda del tipo di query.
    public static String selectToCount(Query query, String distinctField) {
	String originalQueryString = queryStringFrom(query);

	if (isNativeQuery(query)) {
	    // Se è nativa, usiamo la nostra nuova logica di manipolazione di stringhe.
	    return selectToCountFromNative(originalQueryString, distinctField);
	} else {
	    // Altrimenti, usiamo la vecchia logica per JPQL.
	    return selectToCountFromJpql(originalQueryString, distinctField);
	}
    }

    // Rinominato il vecchio metodo per chiarezza, la sua logica non cambia.
    public static String selectToCountFromJpql(String selectQuery, String distinctField) {
	StringBuilder stringBuilder = new StringBuilder("SELECT COUNT(");
	if (StringUtils.isBlank(distinctField)) {
	    stringBuilder.append("*");
	} else {
	    stringBuilder.append("DISTINCT ").append(distinctField);
	}
	stringBuilder.append(")");
	String countQuery = normalizeQueryString(selectQuery);
	countQuery = removeOrderBy(countQuery);
	stringBuilder.append(countQuery.substring(countQuery.toUpperCase().indexOf(" FROM ")));
	return stringBuilder.toString();
    }

    // NUOVO METODO: La logica di conteggio per SQL Nativo.
    public static String selectToCountFromNative(String nativeSqlString,
	    String countDistinctField) {
	String originalSql = normalizeQueryString(nativeSqlString);

	// Rimuove la clausola ORDER BY, perché non serve nel conteggio.
	// Usiamo lastIndexOf perché l'ORDER BY è sempre alla fine.
	int orderByIndex = originalSql.toUpperCase().lastIndexOf(" ORDER BY ");
	if (orderByIndex != -1) {
	    originalSql = originalSql.substring(0, orderByIndex);
	}

	// Trova l'inizio della clausola FROM
	int fromIndex = originalSql.toUpperCase().indexOf(" FROM ");
	if (fromIndex == -1) {
	    throw new IllegalArgumentException(
		    "La query nativa non contiene una clausola FROM valida: " + nativeSqlString);
	}

	// Costruisce la parte COUNT
	String countClause;
	if (StringUtils.isNotBlank(countDistinctField)) {
	    // Nota: countDistinctField qui deve essere il nome della COLONNA SQL (es.
	    // "u.ID_UNITA_DOC")
	    countClause = "SELECT COUNT(DISTINCT " + countDistinctField + ") ";
	} else {
	    countClause = "SELECT COUNT(*) ";
	}

	// Unisce la parte COUNT con il resto della query a partire dal FROM
	return countClause + originalSql.substring(fromIndex);
    }

    // ===================================================================================
    // FINE MEV #39292
    // ===================================================================================

    public static String handleOrderBy(Query query, String sortColumnName, int sortingRule) {
	return handleOrderBy(queryStringFrom(query), sortColumnName, sortingRule);
    }

    public static String handleOrderBy(String queryStr, String sortColumnName, int sortingRule) {
	if (StringUtils.isNotBlank(sortColumnName)) {
	    String sql = removeOrderBy(queryStr);
	    StringBuilder sqlSB = new StringBuilder(sql);
	    sqlSB.append(" ORDER BY ").append(toCamelCase(sortColumnName));
	    if (SortingRule.ASC == sortingRule) {
		sqlSB.append(" ASC ");
	    } else {
		sqlSB.append(" DESC ");
	    }
	    return sqlSB.toString();
	}
	return queryStr;
    }

    public static String toCamelCase(String value) {
	String[] strings = StringUtils.split(value.toLowerCase(), "_");
	for (int i = 1; i < strings.length; i++) {
	    strings[i] = StringUtils.capitalize(strings[i]);
	}
	return StringUtils.join(strings);
    }

    public static void copyParameters(final Query from, final Query to) {
	from.getParameters().stream().map(Parameter::getName).forEach(
		paramName -> to.setParameter(paramName, from.getParameterValue(paramName)));
    }

    public static Map<String, Object> extractParameters(final Query from) {
	Map<String, Object> params = new HashMap<>();
	from.getParameters().stream().map(Parameter::getName)
		.forEach(paramName -> params.put(paramName, from.getParameterValue(paramName)));
	return params;
    }

}
