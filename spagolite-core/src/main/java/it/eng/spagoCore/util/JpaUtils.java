/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.spagoCore.util;

import java.sql.Connection;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

/**
 * @author Manuel Bertuzzi
 */
public class JpaUtils {

    public static Connection provideConnectionFrom(EntityManager em) throws SQLException {
        return em.unwrap(Session.class).getSessionFactory().getSessionFactoryOptions().getServiceRegistry()
                .getService(ConnectionProvider.class).getConnection();
    }
}
