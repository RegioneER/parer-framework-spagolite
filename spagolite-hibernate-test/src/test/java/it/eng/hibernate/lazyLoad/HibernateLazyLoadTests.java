/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.hibernate.lazyLoad;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Bertuzzi_M
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HibernateLazyLoadTests {

    private EJBContainer ejbContainer = null;

    @PersistenceContext
    public EntityManager em;

    @Test
    public void retrieveIdFromHibernateLazyProxy() {
        ChildEntity entity = em.find(ChildEntity.class, 10L);
        Assert.assertNotNull(entity.getParentEntity());
        Assert.assertEquals(1L, entity.getParentEntity().getIdTestParent());
    }

    @Test
    public void retrieveNullFromHibernateLazyProxy() {
        ChildEntity entity = em.find(ChildEntity.class, 20L);
        Assert.assertNull(entity.getParentEntity());
    }

    @Before
    public void setUp() throws NamingException, FileNotFoundException, IOException {
        Properties props = new Properties();
        String locationPath = HibernateLazyLoadTests.class.getProtectionDomain().getCodeSource().getLocation()
                .getFile();
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
