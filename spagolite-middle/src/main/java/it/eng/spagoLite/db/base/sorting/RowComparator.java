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

import it.eng.spagoLite.db.base.BaseRowInterface;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class RowComparator implements Comparator, Serializable {
    private List<SortingRule> list;

    public RowComparator() {
        list = new ArrayList<SortingRule>();
    }

    public void clearRule() {
        list.clear();
    }

    public void addRule(String columnName, int sortType) {
        list.add(new SortingRule(columnName, sortType));
    }

    public void addRule(SortingRule sortingRule) {
        list.add(sortingRule);
    }

    public SortingRule[] getSortingRules() {
        return (SortingRule[]) list.toArray(new SortingRule[list.size()]);
    }

    public SortingRule getLastSortingRule() {
        if (list.size() == 0) {
            return null;
        } else {
            return list.get(list.size() - 1);
        }
    }

    public int compare(Object o1, Object o2) {
        BaseRowInterface row1 = (BaseRowInterface) o1;
        BaseRowInterface row2 = (BaseRowInterface) o2;

        Iterator<SortingRule> iterator = list.iterator();
        while (iterator.hasNext()) {
            SortingRule sortingRule = (SortingRule) iterator.next();

            Object value1 = row1.getObject(sortingRule.getColumnName());
            Object value2 = row2.getObject(sortingRule.getColumnName());

            if (value1 == null && value2 == null)
                continue;

            if (value1 != null && value2 == null)
                return -1 * sortingRule.getSortType();

            if (value1 == null && value2 != null)
                return sortingRule.getSortType();

            if (value1 instanceof BigDecimal) {
                BigDecimal bigDecimal1 = (BigDecimal) value1;
                BigDecimal bigDecimal2 = (BigDecimal) value2;

                if (bigDecimal1.equals(bigDecimal2)) {
                    continue;
                }

                return bigDecimal1.compareTo(bigDecimal2) * sortingRule.getSortType();
            }

            if (value1 instanceof Timestamp) {
                Timestamp timestamp1 = (Timestamp) value1;
                Timestamp timestamp2 = (Timestamp) value2;

                if (timestamp1.equals(timestamp2)) {
                    continue;
                }

                return timestamp1.compareTo(timestamp2) * sortingRule.getSortType();
            }

            if (value1 instanceof String) {
                String string1 = (String) value1;
                String string2 = (String) value2;

                if (string1.equalsIgnoreCase(string2)) {
                    continue;
                }

                return string1.compareToIgnoreCase(string2) * sortingRule.getSortType();
            }
        }

        return 0;
    }
}
