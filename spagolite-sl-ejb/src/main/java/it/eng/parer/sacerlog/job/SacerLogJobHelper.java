/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 */

/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package it.eng.parer.sacerlog.job;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.parer.sacerlog.ejb.common.AppServerInstance;
import it.eng.spagoCore.util.JpaUtils;

/**
 *
 * @author Iacolucci_M
 */
@Stateless(mappedName = "SacerLogJobHelper")
@LocalBean
public class SacerLogJobHelper implements Serializable {

    @PersistenceContext
    private EntityManager em;
    @EJB
    private AppServerInstance appServerInstance;

    private static Logger logger = LoggerFactory.getLogger(SacerLogJobHelper.class);

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void writeAtomicLogJob(String jobName, String opType) {
	writeLogJob(jobName, opType, null);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void writeAtomicLogJob(String jobName, String opType, String descr) {
	writeLogJob(jobName, opType, descr);
    }

    public void writeLogJob(String jobName, String opType, String descr) {
	Date now = new Date();
	Timestamp date = new Timestamp(now.getTime());
	PreparedStatement ps = null;
	Connection con = null;
	try {
	    con = JpaUtils.provideConnectionFrom(em);
	    ps = con.prepareStatement(
		    "INSERT INTO APL_V_LOG_JOB (ID_LOG_JOB, NM_JOB, TI_REG_LOG_JOB, DT_REG_LOG_JOB, DL_MSG_ERR, CD_IND_SERVER)"
			    + "VALUES (SSLOG_JOB.nextVal, ?, ?, ?, ?, ?)");
	    ps.setString(1, jobName);
	    ps.setString(2, opType);
	    ps.setTimestamp(3, date);
	    ps.setString(4, descr);
	    ps.setString(5, appServerInstance.getName());
	    ps.executeUpdate();
	} catch (Exception ex) {
	    // ex.printStackTrace();
	    logger.debug("Errore inserimento nella tabella di LOG del JOB", ex);
	} finally {
	    try {
		if (ps != null) {
		    ps.close();
		}

		if (con != null) {
		    con.close();
		}

	    } catch (Exception ex) {

	    }
	}
    }
}
