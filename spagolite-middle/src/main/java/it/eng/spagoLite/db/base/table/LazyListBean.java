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

package it.eng.spagoLite.db.base.table;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 *
 * @author Quaranta_M
 */
public class LazyListBean implements Serializable, LazyListInterface {

    /**
     *
     */
    private static final long serialVersionUID = 4568691707917060936L;
    private int countResultSize;
    public static final int MAX_RESULT = 300;
    public static final int HYSTERESIS = 5;
    private int firstResult;
    private int maxResult;
    private String countDistinctField;
    private String orderByColumnName;
    private int orderBySortingRule;
    private boolean isSortQuery;
    private Function<List, ? extends AbstractBaseTable> resultListToTableBeanFunc;
    private LazyQuery lazyQuery;

    @Override
    public int getCountResultSize() {
	return countResultSize;
    }

    public void setCountResultSize(int countResultSize) {
	this.countResultSize = countResultSize;
    }

    public int incCountResultSize() {
	return this.countResultSize++;
    }

    public int decCountResultSize() {
	return this.countResultSize--;
    }

    @Override
    public int getFirstResult() {
	return firstResult;
    }

    @Override
    public void setFirstResult(int firstResult) {
	this.firstResult = firstResult;
    }

    @Override
    public int getMaxResult() {
	return maxResult > 0 ? maxResult : MAX_RESULT;
    }

    /**
     * Setta il numero massimo di record da selezionare nella query
     *
     * @param maxResult deve essere un intero divisibile per 100, altrimenti la paginazione delle
     *                  liste non funziona correttamente
     */
    public void setMaxResult(int maxResult) {
	if (maxResult % 100 != 0)
	    throw new IllegalArgumentException(
		    "Il parametro maxResult " + maxResult + " deve essere un multiplo di 100");
	this.maxResult = maxResult;
    }

    public String getCountDistinctField() {
	return countDistinctField;
    }

    public void setCountDistinctField(String countDistinctField) {
	this.countDistinctField = countDistinctField;
    }

    public String getOrderByColumnName() {
	return orderByColumnName;
    }

    @Override
    public void setOrderByColumnName(String orderByColumnName) {
	this.orderByColumnName = orderByColumnName;
    }

    public int getOrderBySortingRule() {
	return orderBySortingRule;
    }

    @Override
    public void setOrderBySortingRule(int orderBySortingRule) {
	this.orderBySortingRule = orderBySortingRule;
    }

    public boolean isSortQuery() {
	return isSortQuery;
    }

    @Override
    public void setSortQuery(boolean isSortQuery) {
	this.isSortQuery = isSortQuery;
    }

    public Function<List, ? extends AbstractBaseTable> getResultListToTableBeanFunc() {
	return resultListToTableBeanFunc;
    }

    public void setResultListToTableBeanFunc(
	    Function<List, ? extends AbstractBaseTable> resultListToTableBeanFunc) {
	this.resultListToTableBeanFunc = resultListToTableBeanFunc;
    }

    public LazyQuery getLazyQuery() {
	return lazyQuery;
    }

    public void setLazyQuery(LazyQuery lazyQuery) {
	this.lazyQuery = lazyQuery;
    }
}
