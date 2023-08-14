package it.eng.spagoLite.db.base.table;

/**
 * Fornisce le informazioni di minima necessarie a gestire la corretta paginazione delle liste
 */
public interface LazyListInterface {
    int getMaxResult();

    int getCountResultSize();

    int getFirstResult();

    void setFirstResult(int firstResult);

    void setSortQuery(boolean isSortQuery);

    void setOrderBySortingRule(int orderBySortingRule);

    void setOrderByColumnName(String orderByColumnName);
}
