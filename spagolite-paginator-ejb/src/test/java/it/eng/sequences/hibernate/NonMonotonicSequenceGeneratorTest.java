/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.sequences.hibernate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author manuel.bertuzzi@eng.it
 */
@RunWith(Arquillian.class)
public class NonMonotonicSequenceGeneratorTest {
    @PersistenceContext
    private EntityManager em;

    @Resource
    private UserTransaction tx;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap
                .create(WebArchive.class, NonMonotonicSequenceGeneratorTest.class.getSimpleName() + "Tests.war")
                .addAsResource(NonMonotonicSequenceGeneratorTest.class.getClassLoader().getResource("persistence.xml"),
                        "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addClasses(org.apache.commons.lang3.StringUtils.class)
                .addPackages(false, "org.springframework.beans", "org.springframework.util")
                .addPackages(true, "it.eng.sequences.hibernate");

    }

    @Test
    public void userTransactionIsHere() {
        Assert.assertNotNull(tx);
    }

    @Test
    public void entityManagerIsHere() {
        Assert.assertNotNull(em);
    }

    @Test
    public void insertNuoviRecord() throws Exception {
        tx.begin();
        try {
            SequenceEntity entity1 = new SequenceEntity();
            entity1.setNote("First JUnit insertRecordConSequence");
            em.persist(entity1);

            SequenceEntity entity2 = new SequenceEntity();
            entity2.setNote("Second JUnit insertRecordConSequence");
            em.persist(entity2);

            // se generata correttamente la seconda sequence non è il numero successivo rispetto alla prima
            assertThat(entity2.getId(), is(not(entity1.getId() + 1L)));
            assertThat(entity2.getId(), is(not(entity1.getId() - 1L)));

            Query query = em.createNamedQuery("SequenceEntity.findAll");
            List<SequenceEntity> entities = query.getResultList();
            Assert.assertEquals(2, entities.size());
        } finally {
            tx.rollback();
        }

    }

}
