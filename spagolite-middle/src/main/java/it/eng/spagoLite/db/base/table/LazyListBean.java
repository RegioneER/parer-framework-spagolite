package it.eng.spagoLite.db.base.table;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 
 * @author Quaranta_M
 */
public class LazyListBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4568691707917060936L;
    private int countResultSize;
    public static final int MAX_RESULT = 300;
    public static final int HYSTERESIS = 5;
    private int firstResult;
    private int maxResult;
    private Class helperEJB;
    private Method helperMethod;
    private Object[] methodParameter;
    private boolean queryAlreadyExecuted = false;
    private String countSelectList;
    private String orderByColumnName;
    private int orderBySortingRule;
    private boolean isSortQuery;

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

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public Class getHelperEJB() {
        return helperEJB;
    }

    public void setHelperEJB(Class helperEJB) {
        this.helperEJB = helperEJB;
    }

    public Method getHelperMethod() {
        return helperMethod;
    }

    public void setHelperMethod(Method helperMethod) {
        this.helperMethod = helperMethod;
    }

    public Object[] getMethodParameter() {
        return methodParameter;
    }

    public void setMethodParameter(Object[] methodParameter) {
        this.methodParameter = methodParameter;
    }

    public boolean isQueryAlreadyExecuted() {
        return queryAlreadyExecuted;
    }

    public void setQueryAlreadyExecuted(boolean queryAlreadyExecuted) {
        this.queryAlreadyExecuted = queryAlreadyExecuted;
    }

    public int getMaxResult() {
        return maxResult > 0 ? maxResult : MAX_RESULT;
    }

    /**
     * Setta il numero massimo di record da selezionare nella query
     * 
     * @param maxResult
     *            deve essere un intero divisibile per 100, altrimenti la paginazione delle liste non funziona
     *            correttamente
     */
    public void setMaxResult(int maxResult) {
        if (maxResult % 100 != 0)
            throw new IllegalArgumentException(
                    "Il parametro maxResult " + maxResult + " deve essere un multiplo di 100");
        this.maxResult = maxResult;
    }

    public String getCountSelectList() {
        return countSelectList;
    }

    public void setCountSelectList(String countSelectList) {
        this.countSelectList = countSelectList;
    }

    public String getOrderByColumnName() {
        return orderByColumnName;
    }

    public void setOrderByColumnName(String orderByColumnName) {
        this.orderByColumnName = orderByColumnName;
    }

    public int getOrderBySortingRule() {
        return orderBySortingRule;
    }

    public void setOrderBySortingRule(int orderBySortingRule) {
        this.orderBySortingRule = orderBySortingRule;
    }

    public boolean isSortQuery() {
        return isSortQuery;
    }

    public void setSortQuery(boolean isSortQuery) {
        this.isSortQuery = isSortQuery;
    }
}
