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
