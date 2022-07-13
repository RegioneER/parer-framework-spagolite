package it.eng.paginator.ejb;

import it.eng.spagoLite.db.base.table.LazyListBean;

import java.lang.reflect.Method;

import javax.ejb.Local;

@Local
public interface InvokerLocal {

    public <T> Object invokeLazyPagination(Class<T> helperEJB, Method method, Object[] parameterValue,
            LazyListBean llBean);
}
