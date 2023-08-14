/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.hibernate.paginator;

import it.eng.paginator.ejb.PaginatorInterceptor;
import it.eng.spagoLite.db.base.table.LazyListBean;
import java.util.Iterator;
import javax.ejb.DependsOn;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;

/**
 *
 * @author mbertuzzi
 */
public class StatementInterceptor extends EmptyInterceptor {

    @Override
    public String onPrepareStatement(String sql) {
        if (isToPaginate() && isReadQuery(sql)) {
            // gestisco la paginazione
            return sql;
        }
        return sql;
    }

    @Override
    public void postFlush(Iterator entities) {
        System.out.println("postFlush");
    }

    private boolean isReadQuery(String sql) {
        return sql != null && sql.toUpperCase().matches("\\s*SELECT.*");
    }

    private boolean isToPaginate() {
        LazyListBean llBean = PaginatorInterceptor.getLazyListBean();
        return llBean != null && !llBean.isQueryAlreadyExecuted();
    }

}