/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

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

    protected abstract BaseTableInterface<?> invoke(LazyListInterface llBean);
}
