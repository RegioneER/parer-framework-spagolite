package it.eng.spagoLite.db.base.paging;

import it.eng.spagoLite.db.base.BaseTableInterface;
import it.eng.spagoLite.db.base.table.LazyListBean;
import it.eng.spagoLite.db.base.table.LazyListInterface;

public abstract class AbstractPaginator implements IPaginator {

    @Override
    public BaseTableInterface<?> nextPage(BaseTableInterface<?> tableBean) {
        LazyListInterface llBean = tableBean.getLazyListInterface();
        int oldFirst = llBean.getFirstResult();
        llBean.setFirstResult(oldFirst + llBean.getMaxResult());
        BaseTableInterface<?> table = invoke(llBean);
        updateTableAfterInvoke(tableBean, table);
        table.first();
        return table;
    }

    @Override
    public BaseTableInterface<?> prevPage(BaseTableInterface<?> tableBean) {
        LazyListInterface llBean = tableBean.getLazyListInterface();
        int oldFirst = llBean.getFirstResult();
        llBean.setFirstResult(oldFirst - llBean.getMaxResult());
        BaseTableInterface<?> table = invoke(llBean);
        updateTableAfterInvoke(tableBean, table);
        return table;
    }

    @Override
    public BaseTableInterface<?> firstPage(BaseTableInterface<?> tableBean) {
        LazyListInterface llBean = tableBean.getLazyListInterface();
        llBean.setFirstResult(0);
        BaseTableInterface<?> table = invoke(llBean);
        updateTableAfterInvoke(tableBean, table);
        table.first();
        return table;
    }

    @Override
    public BaseTableInterface<?> lastPage(BaseTableInterface<?> tableBean) {
        LazyListInterface llBean = tableBean.getLazyListInterface();
        int floorCount = (int) Math.floor((double) llBean.getCountResultSize() / llBean.getMaxResult())
                * tableBean.getLazyListInterface().getMaxResult();
        llBean.setFirstResult(floorCount);
        BaseTableInterface<?> table = invoke(llBean);
        updateTableAfterInvoke(tableBean, table);
        return table;
    }

    @Override
    public BaseTableInterface<?> goPage(BaseTableInterface<?> tableBean, int page) {
        LazyListInterface llBean = tableBean.getLazyListInterface();
        int pageSize = tableBean.getPageSize();
        int firstResult = (int) Math.floor((double) ((page - 1) * pageSize) / llBean.getMaxResult())
                * llBean.getMaxResult();
        llBean.setFirstResult(firstResult > 0 ? firstResult : 0);
        BaseTableInterface<?> table = invoke(llBean);
        updateTableAfterInvoke(tableBean, table);
        return table;
    }

    @Override
    public BaseTableInterface<?> sort(BaseTableInterface<?> tableBean) {
        LazyListInterface llBean = tableBean.getLazyListInterface();
        llBean.setSortQuery(true);
        llBean.setOrderBySortingRule(tableBean.getLastSortingRule().getSortType());
        llBean.setOrderByColumnName(tableBean.getLastSortingRule().getColumnName());
        BaseTableInterface<?> table = invoke(llBean);
        updateTableAfterInvoke(tableBean, table);
        // Setto la riga in cui mi trovato nel momento in cui ho ordinato la
        // lista
        table.setCurrentRowIndex(tableBean.getCurrentRowIndex());
        return table;
    }

    private void updateTableAfterInvoke(BaseTableInterface<?> oldTable, BaseTableInterface<?> newTable) {
        // dopo la chiamata "invoke" il table bean � stato rigenerato
        // Setto il page size come prima dell'invocazione
        newTable.setPageSize(oldTable.getPageSize());
        // Aggiungo tra le property della tabella la regola che ho utilizzato
        // per ordinare, sar� letta dal ListTag per il rendering della lista
        if (oldTable.getLastSortingRule() != null) {
            newTable.addSortingRule(oldTable.getLastSortingRule().getColumnName(),
                    oldTable.getLastSortingRule().getSortType());
        }

    }

    protected abstract BaseTableInterface<?> invoke(LazyListInterface llBean);
}
