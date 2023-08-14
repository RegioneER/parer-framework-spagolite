/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Bertuzzi_M
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PaginatorTests {

    private EJBContainer ejbContainer = null;

    @PersistenceContext
    public EntityManager em;

    @EJB
    private SimplyEjb ejb;

    @Test
    public void dummyTest() {

    }

    // @Test
    // public void selectQuery_richiamaInterceptor() {
    // ejb.querySelect(em);
    // }

    @Test
    public void updateQuery_nonRichiamaInterceptor() {
        ejb.queryUpdate(em);
    }

    @Test
    public void tipoDiRitornoNonCompatibile_exception() {
        try {
            ejb.queryUpdateVoid(em);
            Assert.fail(
                    "Se il metoodo chiamato non ritorna un tipo compatibile con l'interceptor dev'essere sollevata un'eccezione");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws NamingException, FileNotFoundException, IOException {
        Properties props = new Properties();
        String locationPath = PaginatorTests.class.getProtectionDomain().getCodeSource().getLocation().getFile();
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
