package it.eng.spagoLite.db.base.sorting;

import java.io.Serializable;

public class SortingRule implements Serializable {

    public static final int ASC = 1;
    public static final int DESC = -1;

    private String columnName;
    private int sortType;

    public SortingRule(String columnName, int sortType) {
        super();
        this.columnName = columnName;
        this.sortType = sortType;
    }

    public SortingRule(String columnName) {
        super();
        this.columnName = columnName;
        this.sortType = ASC;
    }

    public SortingRule() {
        super();
    }

    /**
     * @return the columnName
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * @param columnName
     *            the columnName to set
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * @return the sortType
     */
    public int getSortType() {
        return sortType;
    }

    /**
     * @param sortType
     *            the sortType to set
     */
    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public static SortingRule getAscending(String columnName) {
        return new SortingRule(columnName, ASC);
    }

    public static SortingRule getDescending(String columnName) {
        return new SortingRule(columnName, DESC);
    }

}
