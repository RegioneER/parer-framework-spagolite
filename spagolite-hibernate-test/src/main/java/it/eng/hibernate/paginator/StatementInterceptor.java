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
