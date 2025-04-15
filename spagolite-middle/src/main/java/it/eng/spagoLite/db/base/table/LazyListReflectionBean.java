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
import java.lang.reflect.Method;

/**
 * Usato solo dal DPI. Non ha logiche di DB e serve a tracciare più in generale il metodo che ha
 * fornito i risultati per l'{@link AbstractBaseTable}
 *
 * @author mbertuzzi
 */
public class LazyListReflectionBean implements Serializable, LazyListInterface {
    public static final int MAX_RESULT = 300;

    private Class clazz;
    private Method method;
    private Object[] parameters;
    private int maxResult;
    private int countResultSize;
    private int firstResult;
    private boolean isSortQuery;
    private int orderBySortingRule;
    private String orderByColumnName;

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

    public Class getClazz() {
	return clazz;
    }

    public void setClazz(Class clazz) {
	this.clazz = clazz;
    }

    public Method getMethod() {
	return method;
    }

    public void setMethod(Method method) {
	this.method = method;
    }

    public Object[] getParameters() {
	return parameters;
    }

    public void setParameters(Object[] parameters) {
	this.parameters = parameters;
    }

    @Override
    public int getCountResultSize() {
	return countResultSize;
    }

    public void setCountResultSize(int countResultSize) {
	this.countResultSize = countResultSize;
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
    public void setSortQuery(boolean isSortQuery) {
	this.isSortQuery = isSortQuery;
    }

    @Override
    public void setOrderBySortingRule(int orderBySortingRule) {
	this.orderBySortingRule = orderBySortingRule;
    }

    @Override
    public void setOrderByColumnName(String orderByColumnName) {
	this.orderByColumnName = orderByColumnName;
    }

    public boolean isSortQuery() {
	return isSortQuery;
    }

    public int getOrderBySortingRule() {
	return orderBySortingRule;
    }

    public String getOrderByColumnName() {
	return orderByColumnName;
    }
}
