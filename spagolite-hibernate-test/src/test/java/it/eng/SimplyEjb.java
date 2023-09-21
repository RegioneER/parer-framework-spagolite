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
package it.eng;

//import it.eng.parer.sacerlog.entity.LogEvento;
import it.eng.spagoLite.db.base.table.AbstractBaseTable;
import it.eng.spagoLite.db.base.table.BaseTable;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Iacolucci_M
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SimplyEjb {

    // public AbstractBaseTable querySelect(EntityManager em) {
    // Query query = em.createQuery("SELECT e FROM LogEvento e WHERE e.idEvento=:id");
    // query.setParameter("id", 1L);
    // List<LogEvento> eventiList = query.getResultList();
    // return new BaseTable();
    // }

    public AbstractBaseTable queryUpdate(EntityManager em) {
        Query q = em.createQuery("UPDATE LogEvento e SET nmAzione=nmAzione WHERE e.idEvento= :id");
        q.setParameter("id", 1L);
        q.executeUpdate();
        return new BaseTable();
    }

    public void queryUpdateVoid(EntityManager em) {
        Query q = em.createQuery("UPDATE LogEvento e SET nmAzione=nmAzione WHERE e.idEvento= :id");
        q.setParameter("id", 1L);
    }

}
