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
