/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prova;

import it.eng.parer.sacerlog.entity.LogEvento;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Iacolucci_M
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class EjbTest {

    @PersistenceContext
    private EntityManager entityManager;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void operazioneInNuovatgransazione(long id) {
        LogEvento e = entityManager.find(LogEvento.class, id);
    }

}
