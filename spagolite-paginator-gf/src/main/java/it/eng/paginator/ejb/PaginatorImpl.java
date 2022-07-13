package it.eng.paginator.ejb;

import it.eng.spagoLite.db.base.BaseTableInterface;
import it.eng.spagoLite.db.base.paging.AbstractPaginator;
import it.eng.spagoLite.db.base.table.LazyListBean;
import javax.ejb.EJB;

/**
 *
 * @author Quaranta_M
 */
public class PaginatorImpl extends AbstractPaginator {

    @EJB(mappedName = "java:app/paginator/Invoker")
    private InvokerLocal invoker;

    @SuppressWarnings("unchecked")
    @Override
    protected BaseTableInterface<?> invoke(LazyListBean llBean) {
        return (BaseTableInterface<?>) invoker.invokeLazyPagination(llBean.getHelperEJB(), llBean.getHelperMethod(),
                llBean.getMethodParameter(), llBean);
    }

}
