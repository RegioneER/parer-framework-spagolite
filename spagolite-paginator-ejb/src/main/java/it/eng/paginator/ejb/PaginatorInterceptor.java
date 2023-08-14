package it.eng.paginator.ejb;

import it.eng.spagoLite.db.base.table.LazyListBean;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class PaginatorInterceptor {

    private static ThreadLocal<LazyListBean> tLocalLazyList = new ThreadLocal<>();

    @AroundInvoke
    public Object storeInvocation(InvocationContext inv) throws Exception {
        return invoke(inv);
    }

    private Object invoke(InvocationContext inv) throws Exception {
        return inv.proceed();
    }

    public static LazyListBean getLazyListBean() {
        return tLocalLazyList.get();
    }

    public static void setLazyListBean(LazyListBean llBean) {
        tLocalLazyList.set(llBean);
    }
}
