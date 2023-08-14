package it.eng.paginator.ejb;

import it.eng.paginator.helper.LazyListHelper;
import it.eng.spagoLite.db.base.BaseTableInterface;
import it.eng.spagoLite.db.base.paging.AbstractPaginator;
import it.eng.spagoLite.db.base.table.LazyListBean;
import it.eng.spagoLite.db.base.table.LazyListInterface;

import javax.ejb.EJB;

/**
 *
 * @author Quaranta_M
 */
public class PaginatorImpl extends AbstractPaginator {

    @EJB(mappedName = "java:app/paginator/LazyListHelper")
    private LazyListHelper lazyListHelper;

    @Override
    protected BaseTableInterface<?> invoke(LazyListInterface llBean) {
        return lazyListHelper.getTableBean((LazyListBean) llBean);
    }

}
