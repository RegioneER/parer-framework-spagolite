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

package it.eng.spagoLite.db.oracle.bean.column;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public abstract class TableDescriptor {

    // private static final SortingRule[] EMPTY_SORTINGRULE = new SortingRule[0];

    private final Map map;

    public TableDescriptor() {
        this.map = null;
    }

    public TableDescriptor(Map map) {
        this.map = new HashMap();
        for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
            Map.Entry e = (Entry) iter.next();
            this.map.put(((String) e.getKey()).toLowerCase(), e.getValue());
        }
    }

    public ColumnDescriptor get(String name) {
        return (ColumnDescriptor) getColumnMap().get(name.toLowerCase());
    }

    public Map getColumnMap() {
        return this.map;
    }

    public abstract String getTableName();

    public abstract String getStatement();

    // public FilteredQuery getFilteredQuery(BaseRowInterface rowBean) {
    // return getFilteredQuery(rowBean, getStatement());
    // }
    //
    // public FilteredQuery getFilteredQuery(BaseRowInterface rowBean, SortingRule[] sortingRules) {
    // return getFilteredQuery(rowBean, getStatement(), sortingRules);
    // }
    //
    // public FilteredQuery getFilteredQuery(BaseRowInterface rowBean, String statement) {
    // return getFilteredQuery(rowBean, statement, EMPTY_SORTINGRULE);
    // }
    //
    // public FilteredQuery getFilteredQuery(BaseRowInterface rowBean, String statement, SortingRule[] sortingRules) {
    // FilteredQuery filteredQuery = new FilteredQuery();
    // String finalStatement = statement;
    //
    // if (sortingRules.length > 0) {
    // finalStatement += "\n order by ";
    // String[] orderBy = new String[sortingRules.length];
    // for (int i = 0; i < sortingRules.length; i++) {
    // SortingRule sortingRule = sortingRules[i];
    // orderBy[i] = QueryUtil.addOrderBy("", sortingRule.getColumnName(), sortingRule.getSortType());
    // }
    //
    // // FIXME: DA CONTROLLARE (MA DOVREBBE FUNZIONARE)
    // finalStatement += StringUtils.join(orderBy, ", ");
    // }
    //
    // filteredQuery.setStatement(finalStatement);
    //
    // Map map = getColumnMap();
    // Iterator iterator = map.values().iterator();
    // while (iterator.hasNext()) {
    // ColumnDescriptor columnDescriptor = (ColumnDescriptor) iterator.next();
    //
    // if (columnDescriptor.getType() != Types.CLOB && columnDescriptor.getType() != Types.BLOB) {
    // filteredQuery.addFiltro(columnDescriptor.getBaseFilter(), columnDescriptor.getType(), rowBean == null ? null :
    // rowBean.getObject(columnDescriptor.getName()));
    // }
    // }
    //
    // return filteredQuery;
    // }

    public final boolean existColumn(String column) {
        return getColumnMap().containsKey(column);
    }

}
