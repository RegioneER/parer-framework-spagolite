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
     * @param columnName the columnName to set
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
     * @param sortType the sortType to set
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
