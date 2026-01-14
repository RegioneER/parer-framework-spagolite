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

/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package it.eng.paginator.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.container.annotation.ArquillianTest;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;

import it.eng.spagoLite.db.base.sorting.SortingRule;
import it.eng.spagoLite.db.base.table.Param;

/**
 *
 * @author manuel.bertuzzi@eng.it
 */
@ArquillianTest
public class CriteriaQueryUtilsTest {
    @PersistenceContext
    private EntityManager em;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap
                .create(WebArchive.class,
                        CriteriaQueryUtilsTest.class.getSimpleName() + "Tests.war")
                .addAsResource(CriteriaQueryUtilsTest.class.getClassLoader()
                        .getResource("persistence.xml"), "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addClass(org.apache.commons.lang3.StringUtils.class)
                .addPackages(false, "org.springframework.beans", "org.springframework.util")
                .addPackages(true, "it.eng.spagoLite.db.base", "it.eng.paginator.util");
    }

    @Test
    public void entityManagerIsHere() {
        assertNotNull(em);
    }

    @Test
    public void countFromCriteriaQuery_noParams() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PigAmbienteVers> query = cb.createQuery(PigAmbienteVers.class);
        Root<PigAmbienteVers> pav = query.from(PigAmbienteVers.class);
        query.select(pav);
        List<PigAmbienteVers> resultList = em.createQuery(query).getResultList();
        int result = CriteriaQueryUtils.count(em, query);
        assertEquals(resultList.size(), result);
    }

    @Test
    public void countFromCriteriaQuery_withParams() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PigAmbienteVers> query = cb.createQuery(PigAmbienteVers.class);
        Root<PigAmbienteVers> pav = query.from(PigAmbienteVers.class);
        List<Predicate> condition = new ArrayList<>();
        condition.add(cb.equal(pav.get("idAmbienteVers"), cb.parameter(Long.class, "id")));
        query.select(pav);
        query.where(condition.toArray(new Predicate[] {}));
        Set<Param> params = new HashSet<>();
        params.add(new Param("id", 3l));
        int result = CriteriaQueryUtils.count(em, query, params);
        assertEquals(1, result);
    }

    @Test
    public void testHandleOrderBy() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PigAmbienteVers> query = cb.createQuery(PigAmbienteVers.class);
        Root<PigAmbienteVers> pav = query.from(PigAmbienteVers.class);
        query.select(pav);
        CriteriaQueryUtils.handleOrderBy(query, "NM_AMBIENTE_VERS", SortingRule.DESC, cb);
        List<PigAmbienteVers> resultList = em.createQuery(query).getResultList();
        assertEquals("prova", resultList.get(0).getNmAmbienteVers());
    }

}
