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
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Bertuzzi_M
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CascadeMergeTests {

    private EJBContainer ejbContainer = null;

    @PersistenceContext
    public EntityManager em;
    @Resource
    private UserTransaction tx;

    @Test
    public void mergeParentWithTransientChild() throws Exception {
        tx.begin();
        SequenceEntity toPersist = new SequenceEntity();
        // toPersist.setId(111L);
        em.persist(toPersist);
        em.flush();
        long id = toPersist.getId();
        // --------------------------------------------------------
        SequenceEntity parent = em.find(SequenceEntity.class, id);

        SequenceChildEntity child = new SequenceChildEntity();
        // child.setId(222L);
        child.setTestSequenceEntity(parent);

        SequenceChild2Entity nephew = new SequenceChild2Entity();
        // nephew.setId(333L);
        nephew.setSequenceChildEntity(child);

        SequenceChild3Entity grandnephew = new SequenceChild3Entity();
        // nephew.setId(333L);
        grandnephew.setSequenceChild2Entity(nephew);

        nephew.setChild3Entitys(new ArrayList<>());
        nephew.getChild3Entitys().add(grandnephew);

        child.setChild2Entitys(new ArrayList<>());
        child.getChild2Entitys().add(nephew);

        parent.setTestSequenceChildEntities(new ArrayList<>());
        parent.getTestSequenceChildEntities().add(child);

        final SequenceEntity merge = em.merge(parent);
        em.flush();

        tx.commit();
    }

    @Before
    public void setUp() throws NamingException, FileNotFoundException, IOException {
        Properties props = new Properties();
        String locationPath = CascadeMergeTests.class.getProtectionDomain().getCodeSource().getLocation().getFile();
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
