/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.paginator.util;

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
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.eng.spagoLite.db.base.sorting.SortingRule;
import it.eng.spagoLite.db.base.table.Param;

/**
 *
 * @author manuel.bertuzzi@eng.it
 */
@RunWith(Arquillian.class)
public class CriteriaQueryUtilsTest {
    @PersistenceContext
    private EntityManager em;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, CriteriaQueryUtilsTest.class.getSimpleName() + "Tests.war")
                .addAsResource(CriteriaQueryUtilsTest.class.getClassLoader().getResource("persistence.xml"),
                        "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addClass(org.apache.commons.lang3.StringUtils.class)
                .addPackages(false, "org.springframework.beans", "org.springframework.util")
                .addPackages(true, "it.eng.spagoLite.db.base", "it.eng.paginator.util");
    }

    @Test
    public void entityManagerIsHere() {
        Assert.assertNotNull(em);
    }

    @Test
    public void countFromCriteriaQuery_noParams() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PigAmbienteVers> query = cb.createQuery(PigAmbienteVers.class);
        Root<PigAmbienteVers> pav = query.from(PigAmbienteVers.class);
        query.select(pav);
        List<PigAmbienteVers> resultList = em.createQuery(query).getResultList();
        int result = CriteriaQueryUtils.count(em, query);
        Assert.assertEquals(resultList.size(), result);
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
        Assert.assertEquals(1, result);
    }

    @Test
    public void testHandleOrderBy() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PigAmbienteVers> query = cb.createQuery(PigAmbienteVers.class);
        Root<PigAmbienteVers> pav = query.from(PigAmbienteVers.class);
        query.select(pav);
        CriteriaQueryUtils.handleOrderBy(query, "NM_AMBIENTE_VERS", SortingRule.DESC, cb);
        List<PigAmbienteVers> resultList = em.createQuery(query).getResultList();
        Assert.assertEquals("prova", resultList.get(0).getNmAmbienteVers());
    }

}
