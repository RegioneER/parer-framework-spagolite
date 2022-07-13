package it.eng.spagoLite.db.base.paging;

import it.eng.spagoLite.db.base.BaseTableInterface;
import it.eng.spagoLite.db.base.table.LazyListBean;

public abstract class AbstractPaginator implements IPaginator {

    @Override
    public BaseTableInterface<?> nextPage(BaseTableInterface<?> tableBean) {
        LazyListBean llBean = tableBean.getLazyListBean();
        int oldFirst = llBean.getFirstResult();
        tableBean.getLazyListBean().setFirstResult(oldFirst + llBean.getMaxResult());
        BaseTableInterface<?> table = invoke(llBean);
        updateTableAfterInvoke(tableBean, table);
        table.first();
        return table;
    }

    @Override
    public BaseTableInterface<?> prevPage(BaseTableInterface<?> tableBean) {
        LazyListBean llBean = tableBean.getLazyListBean();
        int oldFirst = llBean.getFirstResult();
        tableBean.getLazyListBean().setFirstResult(oldFirst - llBean.getMaxResult());
        BaseTableInterface<?> table = invoke(llBean);
        updateTableAfterInvoke(tableBean, table);
        return table;
    }

    @Override
    public BaseTableInterface<?> firstPage(BaseTableInterface<?> tableBean) {
        LazyListBean llBean = tableBean.getLazyListBean();
        tableBean.getLazyListBean().setFirstResult(0);
        BaseTableInterface<?> table = invoke(llBean);
        updateTableAfterInvoke(tableBean, table);
        table.first();
        return table;
    }

    @Override
    public BaseTableInterface<?> lastPage(BaseTableInterface<?> tableBean) {
        LazyListBean llBean = tableBean.getLazyListBean();
        int floorCount = (int) Math
                .floor((double) llBean.getCountResultSize() / tableBean.getLazyListBean().getMaxResult())
                * tableBean.getLazyListBean().getMaxResult();
        tableBean.getLazyListBean().setFirstResult(floorCount);
        BaseTableInterface<?> table = invoke(llBean);
        updateTableAfterInvoke(tableBean, table);
        return table;
    }

    @Override
    public BaseTableInterface<?> goPage(BaseTableInterface<?> tableBean, int page) {
        LazyListBean llBean = tableBean.getLazyListBean();
        int pageSize = tableBean.getPageSize();
        int firstResult = (int) Math
                .floor((double) ((page - 1) * pageSize) / tableBean.getLazyListBean().getMaxResult())
                * tableBean.getLazyListBean().getMaxResult();
        tableBean.getLazyListBean().setFirstResult(firstResult > 0 ? firstResult : 0);
        BaseTableInterface<?> table = invoke(llBean);
        updateTableAfterInvoke(tableBean, table);
        return table;
    }

    @Override
    public BaseTableInterface<?> sort(BaseTableInterface<?> tableBean) {
        LazyListBean llBean = tableBean.getLazyListBean();
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
        // dopo la chiamata "invoke" il table bean è stato rigenerato
        // Setto il page size come prima dell'invocazione
        newTable.setPageSize(oldTable.getPageSize());
        // Aggiungo tra le property della tabella la regola che ho utilizzato
        // per ordinare, sarà letta dal ListTag per il rendering della lista
        if (oldTable.getLastSortingRule() != null) {
            newTable.addSortingRule(oldTable.getLastSortingRule().getColumnName(),
                    oldTable.getLastSortingRule().getSortType());
        }

    }

    protected abstract BaseTableInterface<?> invoke(LazyListBean llBean);
}
