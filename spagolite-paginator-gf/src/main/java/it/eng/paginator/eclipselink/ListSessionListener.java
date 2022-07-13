package it.eng.paginator.eclipselink;

import it.eng.paginator.ejb.PaginatorInterceptor;
import it.eng.spagoLite.db.base.table.LazyListBean;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;

/**
 *
 * @author Quaranta_M
 */
public class ListSessionListener extends SessionEventAdapter {

    // @Resource
    // private TransactionSynchronizationRegistry transactionCtx;

    // private static final String FL_COUNT_QUERY = "fl_count_query";

    @Override
    public void preExecuteQuery(SessionEvent event) {
        LazyListBean llBean = PaginatorInterceptor.getLazyListBean();
        if (llBean != null && !llBean.isQueryAlreadyExecuted()) {
            event.getQuery().setRedirector(new CountQueryRedirector());
        }
    }

    /**
     * Setta la query come già eseguita per evitare che query successive alla prima, presenti all'interno di uno stesso
     * metodo, siano paginate a loro volta. Solo la prima query infatti deve essere paginata.
     * 
     * @param event
     */
    @Override
    public void postExecuteQuery(SessionEvent event) {
        LazyListBean llBean = PaginatorInterceptor.getLazyListBean();
        if (llBean != null && !llBean.isQueryAlreadyExecuted()) {
            llBean.setQueryAlreadyExecuted(true);
        }
    }
}