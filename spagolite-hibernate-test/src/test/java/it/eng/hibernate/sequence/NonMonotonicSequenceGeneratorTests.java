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
package it.eng.hibernate.sequence;

import it.eng.hibernate.sequence.SequenceEntity;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author Bertuzzi_M
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NonMonotonicSequenceGeneratorTests {

    private EJBContainer ejbContainer = null;

    @PersistenceContext
    public EntityManager em;
    @Resource
    private UserTransaction tx;

    @Test
    public void insertNuoviRecord() throws Exception {
        tx.begin();
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
        tx.rollback();
    }

    @Test
    public void aggiornaUnCampo() throws Exception {
        tx.begin();
        SequenceEntity entity = new SequenceEntity();
        entity.setNote("First JUnit insertRecordConSequence");
        em.persist(entity);
        em.flush();
        long id = entity.getId();

        em.detach(entity);

        final String newNote = "Updated";
        SequenceEntity entityFromDb = em.find(SequenceEntity.class, id);
        entityFromDb.setNote(newNote);

        SequenceEntity entityMerged = em.merge(entityFromDb);
        em.flush();

        Assert.assertEquals(id, entityMerged.getId());
        Assert.assertEquals(newNote, entityMerged.getNote());

        tx.rollback();
    }

    @Test
    public void aggiungiUnRecordFiglio() throws Exception {
        tx.begin();
        SequenceEntity entity = new SequenceEntity();
        entity.setNote("First JUnit insertRecordConSequence");
        em.persist(entity);
        em.flush();
        long id = entity.getId();
        em.detach(entity);

        SequenceEntity parent = em.find(SequenceEntity.class, id);
        SequenceChildEntity child = new SequenceChildEntity();
        child.setTestSequenceEntity(parent);
        child.setCampo("No matter");

        parent.setTestSequenceChildEntities(new ArrayList<>());
        parent.getTestSequenceChildEntities().add(child);

        SequenceEntity merged = em.merge(parent);
        em.flush();

        Assert.assertEquals(id, merged.getId());
        Assert.assertEquals(1, merged.getTestSequenceChildEntities().size());

        tx.rollback();
    }

    @Test
    public void aggiungiPiuRecordFigli() throws Exception {
        tx.begin();
        SequenceEntity entity = new SequenceEntity();
        entity.setNote("First JUnit insertRecordConSequence");
        em.persist(entity);
        em.flush();
        long id = entity.getId();
        em.detach(entity);

        SequenceEntity parent = em.find(SequenceEntity.class, id);
        SequenceChildEntity child = new SequenceChildEntity();
        child.setTestSequenceEntity(parent);
        child.setCampo("No matter");
        SequenceChildEntity child2 = new SequenceChildEntity();
        child2.setTestSequenceEntity(parent);
        child2.setCampo("No matter2");

        parent.setTestSequenceChildEntities(new ArrayList<>());
        parent.getTestSequenceChildEntities().add(child);
        parent.getTestSequenceChildEntities().add(child2);

        SequenceEntity merged = em.merge(parent);
        em.flush();

        Assert.assertEquals(id, merged.getId());
        Assert.assertEquals(2, merged.getTestSequenceChildEntities().size());

        tx.rollback();
    }

    @Test
    public void threeLevelsChildTest() throws Exception {
        tx.begin();
        SequenceEntity entity = new SequenceEntity();
        entity.setId(99999999999L);
        entity.setNote("parent");
        em.persist(entity);
        em.flush();
        long id = entity.getId();
        em.detach(entity);
        SequenceEntity parent = em.find(SequenceEntity.class, id);

        SequenceChildEntity firstChild = new SequenceChildEntity();
        firstChild.setTestSequenceEntity(parent);
        firstChild.setCampo("child_1_1");
        SequenceChildEntity secondChild = new SequenceChildEntity();
        secondChild.setTestSequenceEntity(parent);
        secondChild.setCampo("child_1_2");

        SequenceChild2Entity firstChild2 = new SequenceChild2Entity();
        firstChild2.setSequenceChildEntity(firstChild);
        firstChild2.setCampo("child_2_1");
        SequenceChild2Entity secondChild2 = new SequenceChild2Entity();
        secondChild2.setSequenceChildEntity(firstChild);
        secondChild2.setCampo("child_2_2");

        SequenceChild3Entity child3 = new SequenceChild3Entity();
        child3.setCampo("child_3_1");
        child3.setSequenceChild2Entity(firstChild2);

        firstChild2.setChild3Entitys(new ArrayList<>());
        firstChild2.getChild3Entitys().add(child3);

        firstChild.setChild2Entitys(new ArrayList<>());
        firstChild.getChild2Entitys().add(firstChild2);
        firstChild.getChild2Entitys().add(secondChild2);

        parent.setTestSequenceChildEntities(new ArrayList<>());
        parent.getTestSequenceChildEntities().add(firstChild);
        parent.getTestSequenceChildEntities().add(secondChild);

        SequenceEntity merged = em.merge(parent);
        em.flush();

        Assert.assertEquals(id, merged.getId());

        tx.rollback();
    }

    @Before
    public void setUp() throws NamingException, FileNotFoundException, IOException {
        Properties props = new Properties();
        String locationPath = NonMonotonicSequenceGeneratorTests.class.getProtectionDomain().getCodeSource()
                .getLocation().getFile();
        String fileProperties = locationPath + "jndi.properties";

        try (FileInputStream in = new FileInputStream(fileProperties)) {
            props.load(in);
        }

        ejbContainer = EJBContainer.createEJBContainer(props);
        ejbContainer.getContext().bind("inject", this);
    }

    @After
    public void tearDown() {
        if (ejbContainer != null) {
            ejbContainer.close();
        }
    }

}
