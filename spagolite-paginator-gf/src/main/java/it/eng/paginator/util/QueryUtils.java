/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package it.eng.paginator.util;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Parameter;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import it.eng.spagoLite.db.base.sorting.SortingRule;

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
            return normalizeQueryString("SELECT COUNT(*)" + countAlias + " FROM (" + subQuery + ")");
        } else {
            return queryString;
        }
    }

    public static String selectToCount(final String selectQuery) {
        return selectToCount(selectQuery, null);
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

    public static String selectToCount(Query query, String distinctField) {
        return selectToCount(queryStringFrom(query), distinctField);
    }

    public static String selectToCount(String selectQuery, String distinctField) {
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

    public static String queryStringFrom(Query query) {
        final org.hibernate.query.Query hibernateQuery = query.unwrap(org.hibernate.query.Query.class);
        return hibernateQuery.getQueryString();
    }

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
        from.getParameters().stream().map(Parameter::getName)
                .forEach(paramName -> to.setParameter(paramName, from.getParameterValue(paramName)));
    }

    public static Map<String, Object> extractParameters(final Query from) {
        Map<String, Object> params = new HashMap<>();
        from.getParameters().stream().map(Parameter::getName)
                .forEach(paramName -> params.put(paramName, from.getParameterValue(paramName)));
        return params;
    }

}
