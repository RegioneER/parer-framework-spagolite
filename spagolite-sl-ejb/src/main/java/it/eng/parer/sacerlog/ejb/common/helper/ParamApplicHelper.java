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
package it.eng.parer.sacerlog.ejb.common.helper;

import it.eng.parer.sacerlog.common.Constants;
import it.eng.parer.sacerlog.ejb.helper.SacerLogHelper;
import it.eng.parer.sacerlog.viewEntity.AplVParamApplic;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Iacolucci_M
 */
@Stateless
@LocalBean
public class ParamApplicHelper {

    private static Logger log = LoggerFactory.getLogger(SacerLogHelper.class);

    @PersistenceContext
    private EntityManager entityManager;

    protected AplVParamApplic getParamApplicValue(String nmParamApplic) {
        try {
            Query query = entityManager.createNamedQuery("AplVParamApplic.findByNmParamApplic", AplVParamApplic.class);
            query.setParameter("nmParamApplic", nmParamApplic);
            // AplVParamApplic p = (AplVParamApplic) query.getSingleResult();
            AplVParamApplic p = null;
            List<AplVParamApplic> l = query.getResultList();
            if (l != null && l.size() > 0) {
                p = l.get(0);
            }
            return p;
        } catch (RuntimeException ex) {
            log.error("Errore nell'estrazione del parametro applicativo [{}]", nmParamApplic, ex);
            throw ex;
        }
    }

    public AplVParamApplic getServerNameSystemProperty() {
        return getParamApplicValue(Constants.NmParamApplic.SERVER_NAME_SYSTEM_PROPERTY.name());
    }

    public AplVParamApplic getApplicationName() {
        return getParamApplicValue(Constants.NmParamApplic.NM_APPLIC.name());
    }

    public Integer getMaxResultRicercaLogEventiValue() {
        Integer valore = null;
        AplVParamApplic apl = getParamApplicValue(Constants.NmParamApplic.MAX_RESULT_RICERCA_LOG_EVENTI.name());
        if (apl != null) {
            String val = apl.getDsValoreParamApplic();
            if (val != null) {
                valore = Integer.valueOf(val);
            }
        }
        return valore;
    }

}
