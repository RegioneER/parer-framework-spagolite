/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 */

package it.eng.paginator.ejb;

import it.eng.spagoLite.db.base.table.LazyListBean;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

@Deprecated(forRemoval = true, since = "LazyListHelper")
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
