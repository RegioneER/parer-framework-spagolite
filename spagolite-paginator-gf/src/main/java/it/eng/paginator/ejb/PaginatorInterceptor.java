package it.eng.paginator.ejb;

import it.eng.spagoLite.db.base.table.AbstractBaseTable;
import it.eng.spagoLite.db.base.table.LazyListBean;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaginatorInterceptor {

    static Logger log = LoggerFactory.getLogger(PaginatorInterceptor.class);
    private final static Map<String, String> countQuerySelectList = new HashMap<String, String>();
    private static final String BUNDLE_NAME = "countselectlist";
    private static final ResourceBundle rb = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault(),
            Thread.currentThread().getContextClassLoader());

    static {
        // null equivale a SELECT 1 FROM ...
        if (rb != null) {
            Enumeration<String> keys = rb.getKeys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                String value = rb.getString(key);
                countQuerySelectList.put(key, ("".equals(value)) ? null : value);
            }
        }
    }

    private static ThreadLocal<LazyListBean> tLocalLazyList = new ThreadLocal<LazyListBean>();

    @AroundInvoke
    public Object storeInvocation(InvocationContext inv) throws Exception {
        try {
            pre(inv);
            Object obj = invoke(inv);
            post(obj);
            return obj;
        } catch (Exception e) {
            // Logger log = Logger.getLogger(inv.getTarget().getClass());
            log.error("Exception in LoggingInterceptor: " + e.getMessage());
            throw e;
        } finally {
            tLocalLazyList.remove();
        }
    }

    private void pre(InvocationContext inv) {
        // Recupero il bean settatomi dall'invoker(e ancor prima dal Paginator)
        // (la prima chiamata al metodo dell'helper non avrà il bean nella
        // variabile threadLocal)
        if (tLocalLazyList.get() == null) {
            LazyListBean paginator = new LazyListBean();
            Class helperEJB = inv.getTarget().getClass();
            Method method = inv.getMethod();
            // L'interceptor deve entrare in funzione solo con metodi che
            // ritornano un'istanza di AbstractLazyBaseTable; asSubclass()
            // lancia un'eccezione
            method.getReturnType().asSubclass(AbstractBaseTable.class);
            Object[] parameterValue = inv.getParameters();
            paginator.setHelperEJB(helperEJB);
            paginator.setHelperMethod(method);
            paginator.setMethodParameter(parameterValue);
            paginator.setCountSelectList(
                    countQuerySelectList != null ? countQuerySelectList.get(method.getName()) : null);
            tLocalLazyList.set(paginator);
        }
    }

    private Object invoke(InvocationContext inv) throws Exception {
        return inv.proceed();
    }

    private void post(Object obj) {
        if (obj instanceof AbstractBaseTable) {
            // Setto il lazybean nel tablebean per le successive invocazioni
            AbstractBaseTable tableBean = (AbstractBaseTable) obj;
            if (tableBean.getLazyListBean() == null) {
                tableBean.setLazyListBean(tLocalLazyList.get());
            }
        }
    }

    public static LazyListBean getLazyListBean() {
        return tLocalLazyList.get();
    }

    public static void setLazyListBean(LazyListBean llBean) {
        tLocalLazyList.set(llBean);
    }
}
