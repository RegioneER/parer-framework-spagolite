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

package it.eng.parer.sacerlog.ejb.helper;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.parer.sacerlog.common.SacerLogEjbType;
import it.eng.parer.sacerlog.ejb.common.AppServerInstance;
import it.eng.parer.sacerlog.entity.LogEventoByScript;
import it.eng.parer.sacerlog.entity.LogEventoLoginUser;
import it.eng.parer.sacerlog.exceptions.SacerLogRuntimeException;
import it.eng.parer.sacerlog.exceptions.SacerLogRuntimeException.SacerLogErrorCategory;
import it.eng.parer.sacerlog.viewEntity.AplVLogChiaveTiOgg;
import it.eng.parer.sacerlog.viewEntity.AplVLogFotoTiEvnOgg;
import it.eng.parer.sacerlog.viewEntity.AplVLogInit;
import it.eng.parer.sacerlog.viewEntity.AplVLogTiEvn;
import it.eng.parer.sacerlog.viewEntity.AplVLogTiEvnConOrigine;
import it.eng.parer.sacerlog.viewEntity.AplVLogTiOgg;
import it.eng.parer.sacerlog.viewEntity.AplVLogTrigTiEvnOgg;
import it.eng.parer.sacerlog.viewEntity.AplVParamApplic;
import it.eng.parer.sacerlog.viewEntity.LogVLisAsserzioniDati;
import it.eng.parer.sacerlog.viewEntity.LogVLisEventoOggetto;
import it.eng.parer.sacerlog.viewEntity.LogVLogAgente;
import it.eng.parer.sacerlog.viewEntity.LogVRicEventi;
import it.eng.parer.sacerlog.viewEntity.LogVRicEventiOrganiz;
import it.eng.parer.sacerlog.viewEntity.LogVUsrAbilOrganiz;
import it.eng.parer.sacerlog.viewEntity.LogVVisEventoPrincTx;
import it.eng.parer.sacerlog.viewEntity.LogVVisOggetto;
import it.eng.spagoCore.util.JpaUtils;

/**
 *
 * @author Iacolucci_M
 */
@SuppressWarnings("unchecked")
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@SacerLogEjbType
public class SacerLogHelper {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private AppServerInstance appServerInstance;

    private static final String TRUE = "1";

    private static Logger log = LoggerFactory.getLogger(SacerLogHelper.class);

    public LogVLogAgente getAgenteByNomeAgente(String nomeAgente) {
        try {
            Query query = entityManager.createNamedQuery("LogVLogAgente.getByNomeAgente",
                    LogVLogAgente.class);
            query.setParameter("nomeAgente", nomeAgente);
            return (LogVLogAgente) query.getSingleResult();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message("Errore nell'estrazione dell'agente [{0}]", nomeAgente).build();
        }
    }

    public LogVLogAgente getAgenteByIdAgente(BigDecimal idAgente) {
        try {
            Query query = entityManager.createNamedQuery("LogVLogAgente.getByIdAgente",
                    LogVLogAgente.class);
            query.setParameter("idAgente", idAgente);
            return (LogVLogAgente) query.getSingleResult();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message("Errore nell'estrazione dell'agente con id [{0,number,#}]", idAgente)
                    .build();
        }
    }

    public AplVLogTiEvnConOrigine getAplVLogTiEvnConOrigineByApplicIdAzioneCompSw(
            String nomeApplicazione, BigDecimal idAzionePaginaCompSw) {
        try {
            Query query = entityManager.createNamedQuery(
                    "AplVLogTiEvnConOrigine.findByApplicIdAzioneCompSw",
                    AplVLogTiEvnConOrigine.class);
            query.setParameter("nmApplic", nomeApplicazione);
            query.setParameter("idAzionePaginaCompSw", idAzionePaginaCompSw);
            return (AplVLogTiEvnConOrigine) query.getSingleResult();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message(
                            "Errore nell'estrazione di AplVLogTiEvnConOrigine con idAzionePaginaCompSw [{0,number,#}] e applic [{1}]",
                            idAzionePaginaCompSw, nomeApplicazione)
                    .build();
        }
    }

    public boolean isLoggingEnabled() {
        try {
            Query query = entityManager.createNamedQuery("AplVParamApplic.findByNmParamApplic",
                    AplVParamApplic.class);
            query.setParameter("nmParamApplic", "LOG_ATTIVO");
            AplVParamApplic p = (AplVParamApplic) query.getSingleResult();
            return p.getDsValoreParamApplic().equalsIgnoreCase("true");
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message("Errore nell'estrazione del parametro applicativo [LOG_ATTIVO]")
                    .build();
        }
    }

    public boolean isLoggingEnabledForThisNomeAzione(String nomeAzione) {
        try {
            Query query = entityManager.createNamedQuery("AplVParamApplic.findByNmParamApplic",
                    AplVParamApplic.class);
            query.setParameter("nmParamApplic", "AZIONE_INIZIALIZZAZIONE_LOG");
            AplVParamApplic p = (AplVParamApplic) query.getSingleResult();

            return p.getDsValoreParamApplic().equalsIgnoreCase(nomeAzione);
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message(
                            "Errore nell'estrazione del parametro applicativo [AZIONE_INIZIALIZZAZIONE_LOG]")
                    .build();
        }
    }

    public LogVLogAgente getAgenteByNomeCompSoftware(String nmNomeUserCompSw) {
        try {
            Query query = entityManager.createNamedQuery("LogVLogAgente.getByNomeCompSoftware",
                    LogVLogAgente.class);
            query.setParameter("nmNomeUserCompSw", nmNomeUserCompSw);
            return (LogVLogAgente) query.getSingleResult();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message("Errore nell'estrazione di LogVLogAgente con nmNomeUserCompSw [{0}]",
                            nmNomeUserCompSw)
                    .build();
        }
    }

    public AplVLogTiEvnConOrigine getTipoEventoByApplicFinestraAzione(String nmApplic,
            String nmPaginaCompSw, String nmAzionePaginaCompSw) {
        try {
            Query query = entityManager.createNamedQuery(
                    "AplVLogTiEvnConOrigine.findByApplicFinestraAzione",
                    AplVLogTiEvnConOrigine.class);
            query.setParameter("nmApplic", nmApplic);
            query.setParameter("nmPaginaCompSw", nmPaginaCompSw);
            query.setParameter("nmAzionePaginaCompSw", nmAzionePaginaCompSw);
            return (AplVLogTiEvnConOrigine) query.getSingleResult();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message(
                            "Errore nell'estrazione di AplVLogTiEvnConOrigine con nmApplic [{0}] nmPaginaCompSw [{1}] nmAzionePaginaCompSw [{2}]",
                            nmApplic, nmPaginaCompSw, nmAzionePaginaCompSw)
                    .build();
        }
    }

    public AplVLogTiEvn getTipoEventoByApplicTipoEvento(String nmApplic, String nmTipoEvento) {
        try {
            Query query = entityManager.createNamedQuery("AplVLogTiEvn.findByApplicTipoEvento",
                    AplVLogTiEvn.class);
            query.setParameter("nmApplic", nmApplic);
            query.setParameter("nmTipoEvento", nmTipoEvento);
            return (AplVLogTiEvn) query.getSingleResult();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message(
                            "Errore nell'estrazione di AplVLogTiEvn con nmApplic [{0}] nmTipoEvento [{1}]",
                            nmApplic, nmTipoEvento)
                    .build();
        }
    }

    public AplVLogTiOgg getTipoOggettoByNome(String nmApplic, String nmTipoOggetto) {
        try {
            Query query = entityManager.createNamedQuery("AplVLogTiOgg.findTipoOggettoByNome",
                    AplVLogTiOgg.class);
            query.setParameter("nmApplic", nmApplic);
            query.setParameter("nmTipoOggetto", nmTipoOggetto);
            return (AplVLogTiOgg) query.getSingleResult();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message(
                            "Errore nell'estrazione di AplVLogTiOgg con nmApplic [{0}] nmTipoOggetto [{1}]",
                            nmApplic, nmTipoOggetto)
                    .build();

        }
    }

    public AplVLogTiOgg getTipoOggettoById(BigDecimal idTipoOggetto) {
        try {
            Query query = entityManager.createNamedQuery("AplVLogTiOgg.findTipoOggettoById",
                    AplVLogTiOgg.class);
            query.setParameter("idTipoOggetto", idTipoOggetto);
            return (AplVLogTiOgg) query.getSingleResult();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message(
                            "Errore nell'estrazione di AplVLogTiOgg con idTipoOggetto [{0,number,#}]",
                            idTipoOggetto)
                    .build();
        }
    }

    public List<AplVLogChiaveTiOgg> getChiaviAccessoByIdTipoOggetto(BigDecimal idTipoOggetto) {
        try {
            Query query = entityManager.createNamedQuery("AplVLogChiaveTiOgg.findByIdTipoOggetto",
                    AplVLogChiaveTiOgg.class);
            query.setParameter("idTipoOggetto", idTipoOggetto);
            return query.getResultList();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message(
                            "Errore nell'estrazione di AplVLogChiaveTiOgg con idTipoOggetto [{0,number,#}]",
                            idTipoOggetto)
                    .build();
        }
    }

    public AplVLogFotoTiEvnOgg getFotoByEventoOggetto(BigDecimal idTipoEvento,
            BigDecimal idTipoOggetto) {
        try {
            Query query = entityManager.createNamedQuery("AplVLogFotoTiEvnOgg.findByEventoOggetto",
                    AplVLogFotoTiEvnOgg.class);
            query.setParameter("idTipoEvento", idTipoEvento);
            query.setParameter("idTipoOggetto", idTipoOggetto);
            return (AplVLogFotoTiEvnOgg) query.getSingleResult();
        } catch (NoResultException ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message("Errore di configurazione del sistema di log").build();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message(
                            "Errore nell'estrazione di AplVLogFotoTiEvnOgg con idTipoEvento [{0,number,#}] idTipoOggetto [{1,number,#}]",
                            idTipoEvento, idTipoOggetto)
                    .build();
        }
    }

    public List<AplVLogTrigTiEvnOgg> getTriggersByIdEventoOggetto(BigDecimal idEventoOggetto) {
        try {
            Query query = entityManager.createNamedQuery("AplVLogTrigTiEvnOgg.findTrigger",
                    AplVLogTrigTiEvnOgg.class);
            query.setParameter("idTipoEventoOggetto", idEventoOggetto);
            return query.getResultList();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message(
                            "Errore nell'estrazione di AplVLogChiaveTiOgg con idEventoOggetto [{0,number,#}]",
                            idEventoOggetto)
                    .build();
        }
    }

    /*
     * Estrae tutti i trigger di tipo BEFORE dalla tabella dei trigger
     */
    public List<AplVLogTrigTiEvnOgg> getTriggersBeforeByIdEventoOggetto(
            BigDecimal idEventoOggetto) {
        try {
            Query query = entityManager.createNamedQuery("AplVLogTrigTiEvnOgg.findTriggerBefore",
                    AplVLogTrigTiEvnOgg.class);
            query.setParameter("idTipoEventoOggetto", idEventoOggetto);
            return query.getResultList();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message(
                            "Errore nell'estrazione di AplVLogTrigTiEvnOgg con idEventoOggetto [{0,number,#}]",
                            idEventoOggetto)
                    .build();
        }
    }

    public String getFotoXml(String query, BigDecimal idOggetto, String nmQueryTipoOggetto) {
        Clob clob = null;
        String risultato = null;

        try (Connection con = JpaUtils.provideConnectionFrom(entityManager);
                PreparedStatement ps = con
                        .prepareStatement("SELECT GET_XML_FOTO_AS_CLOB(?,?) AS FOTO FROM DUAL")) {
            ps.setClob(1, getStringAsClob(con, query));
            ps.setBigDecimal(2, idOggetto);

            try (ResultSet rs = ps.executeQuery();) {
                if (rs != null && rs.next()) {
                    clob = rs.getClob("FOTO");
                    risultato = clob == null ? null : getClobAsString(clob);
                }
            }
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message("Errore lettura foto per la query [{0}] per l'oggetto [{1,number,#}]",
                            nmQueryTipoOggetto, idOggetto)
                    .build();
        }
        return risultato;
    }

    public String getDeltaFotoAsClob(String xml1, String xml2) {
        Clob clob = null;
        String risultato = null;

        try (Connection con = JpaUtils.provideConnectionFrom(entityManager);
                PreparedStatement ps = con.prepareStatement(
                        "SELECT SACER_LOG.GET_DELTA_FOTO_AS_CLOB(?,?) AS DELTA FROM DUAL")) {
            Clob par1 = con.createClob();
            Clob par2 = con.createClob();
            par1.setString(1, xml1);
            par2.setString(1, xml2);
            ps.setClob(1, par1);
            ps.setClob(2, par2);

            try (ResultSet rs = ps.executeQuery();) {
                if (rs != null && rs.next()) {
                    clob = rs.getClob("DELTA");
                    risultato = clob == null ? null : getClobAsString(clob);
                }
            }
        } catch (Exception ex) {
            log.error("Errore lettura foto dalla funzione oracle GET_DELTA_FOTO_AS_CLOB()", ex);
        }
        return risultato;
    }

    public String getDeltaFotoAssertAsClob(String xml1, String xml2) {
        Clob clob = null;
        String risultato = null;

        try (Connection con = JpaUtils.provideConnectionFrom(entityManager);
                PreparedStatement ps = con.prepareStatement(
                        "SELECT SACER_LOG.GET_DELTA_ASSERT_FOR_TEST(?,?) AS DELTA FROM DUAL")) {
            Clob par1 = con.createClob();
            Clob par2 = con.createClob();
            par1.setString(1, xml1);
            par2.setString(1, xml2);
            ps.setClob(1, par1);
            ps.setClob(2, par2);

            try (ResultSet rs = ps.executeQuery();) {
                if (rs != null && rs.next()) {
                    clob = rs.getClob("DELTA");
                    risultato = clob == null ? null : getClobAsString(clob);
                }
            }
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message(
                            "Errore lettura foto dalla funzione oracle GET_DELTA_ASSERT_FOR_TEST()")
                    .build();
        }
        return risultato;
    }

    public boolean isThereATrigger(String sqlTrigger, BigDecimal idFoto) {
        boolean risultato = false;
        String campo = null;

        try (Connection con = JpaUtils.provideConnectionFrom(entityManager);
                PreparedStatement ps = con.prepareStatement(sqlTrigger);) {
            ps.setBigDecimal(1, idFoto);

            try (ResultSet rs = ps.executeQuery();) {
                if (rs != null && rs.next()) {
                    campo = rs.getString("FLAG");
                    risultato = campo != null && campo.equals(TRUE);
                }
            }
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message("Errore verifica esistenza trigger").build();
        }
        return risultato;

    }

    /*
     * Esegue dinamicamente una query contenente un parametro di input "ID" che andrà valorizzato
     * con il valore di idEntity. La query estrae una lista di id degli oggetti estratti.
     */
    public List<BigDecimal> findAllObjectIdFromTrigger(String sqlTriggerAllObjectId,
            BigDecimal idEntity) {
        List<BigDecimal> risultato = null;

        try (Connection con = JpaUtils.provideConnectionFrom(entityManager);
                PreparedStatement ps = con.prepareStatement(sqlTriggerAllObjectId);) {
            ps.setBigDecimal(1, idEntity);

            try (ResultSet rs = ps.executeQuery();) {
                if (rs != null) {
                    risultato = new ArrayList<>();
                    while (rs.next()) {
                        risultato.add(rs.getBigDecimal("ID"));
                    }
                }
            }
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message("Errore estrazione ID scatenati dal trigger").build();
        }
        return risultato;
    }

    /*
     * Estrae una lista di ID di oggetti da inizializzare secondo la query passata come parametro.
     * Usato tipicamente dal job di inizializzazione delle foto.
     */
    public List<BigDecimal> findAllObjectIdToInitialize(String sqlFindAllObjectId) {
        List<BigDecimal> risultato = null;

        try (Connection con = JpaUtils.provideConnectionFrom(entityManager);
                PreparedStatement ps = con.prepareStatement(sqlFindAllObjectId);) {

            try (ResultSet rs = ps.executeQuery();) {
                if (rs != null) {
                    risultato = new ArrayList<>();
                    while (rs.next()) {
                        risultato.add(rs.getBigDecimal(1));
                    }
                }
            }
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message("Errore estrazione ID scatenati dalla funzione init log").build();
        }
        return risultato;
    }

    /*
     * A fronte dell'ID di un oggetto di cui si quole sapere qual'è il padre, restituisce una lista
     * di ID di oggetti padre rispetto a idOggettoFiglio. Tipicamente ne viene fuori uno soltanto.
     */
    public List<BigDecimal> findAllChiaviAccessoByIdOggetto(String sqlAllChiaviAccesso,
            BigDecimal idOggettoFiglio) {
        List<BigDecimal> risultato = null;

        try (Connection con = JpaUtils.provideConnectionFrom(entityManager);
                PreparedStatement ps = con.prepareStatement(sqlAllChiaviAccesso);) {
            ps.setBigDecimal(1, idOggettoFiglio);

            try (ResultSet rs = ps.executeQuery();) {
                if (rs != null) {
                    risultato = new ArrayList<>();
                    while (rs.next()) {
                        risultato.add(rs.getBigDecimal("ID"));
                    }
                }
            }
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message("Errore Lettura degli oggetti padre").build();
        }
        return risultato;
    }

    /*
     * Estrae il dettaglio da visualizzare nella form di visualizzazione log per oggetto
     */
    public LogVVisOggetto getDettOggettoByAppTipoOggettoId(String nomeApplicazione,
            String nomeTipoOggetto, BigDecimal idOggetto) {
        try {
            Query query = entityManager.createNamedQuery("LogVVisOggetto.findByAppTipoOggettoId",
                    LogVVisOggetto.class);
            query.setParameter("nmApplic", nomeApplicazione);
            query.setParameter("nmTipoOggetto", nomeTipoOggetto);
            query.setParameter("idOggetto", idOggetto);
            List<LogVVisOggetto> result = query.getResultList();
            if (!result.isEmpty()) {
                return result.get(0);
            } else {
                return null;
            }
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message(
                            "Errore nell'estrazione di LogVVisOggetto con nmApplic [{0}] nmTipoOggetto [{1}] idOggetto [{2,number,#}]",
                            nomeApplicazione, nomeTipoOggetto, idOggetto)
                    .build();
        }
    }

    /*
     * Estrae il dettaglio da visualizzare nella form di visualizzazione log per oggetto
     */
    public List<LogVLisEventoOggetto> getEventsByAppTipoOggettoId(String nomeApplicazione,
            String nomeTipoOggetto, BigDecimal idOggetto) {
        try {
            Query query = entityManager.createNamedQuery(
                    "LogVLisEventoOggetto.findEventsByAppTipoOggNameId",
                    LogVLisEventoOggetto.class);
            query.setParameter("nmApplic", nomeApplicazione);
            query.setParameter("nmTipoOggetto", nomeTipoOggetto);
            query.setParameter("idOggetto", idOggetto);
            return query.getResultList();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message(
                            "Errore nell'estrazione di LogVLisEventoOggetto con nmApplic [{0}] nmTipoOggetto [{1}] idOggetto [{2,number,#}]",
                            nomeApplicazione, nomeTipoOggetto, idOggetto)
                    .build();
        }
    }

    /*
     * Estrae il dettaglio da visualizzare nella form di visualizzazione log per oggetto
     */
    public List<LogVLisAsserzioniDati> getAsserzioniDatiByIdOggettoEvento(
            BigDecimal idOggettoEvento) {
        try {
            Query query = entityManager.createNamedQuery(
                    "LogVLisAsserzioniDati.findByIdOggettoEvento", LogVLisAsserzioniDati.class);
            query.setParameter("idOggettoEvento", idOggettoEvento);
            return query.getResultList();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message(
                            "Errore nell'estrazione di LogVLisAsserzioniDati con idOggettoEvento [{0,number,#}]",
                            idOggettoEvento)
                    .build();
        }
    }

    /*
     * Estrae i record della tabella con gli oggetti da fotografare nel caso in cui siano stati
     * modificati mediante script Oracle esterni all'applicazione.
     */
    public List<LogEventoByScript> getLogEventoScriptByTipoOggettoIdLocked(BigDecimal idTipoOggetto,
            BigDecimal idOggetto) {
        try {
            Query query = entityManager.createNamedQuery("LogEventoByScript.findByTipoOggettoId",
                    LogEventoByScript.class);
            query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
            query.setParameter("idTipoOggetto", idTipoOggetto);
            query.setParameter("idOggetto", idOggetto);
            return query.getResultList();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message(
                            "Errore nell'estrazione di LogEventoByScript con idTipoOggetto [{0,number,#}] e idOggetto [{1,number,#}]",
                            idTipoOggetto, idOggetto)
                    .build();
        }
    }

    /*
     * Estrae i record della tabella con gli oggetti da Inizializzare nell'appisito JOB.
     */
    public List<AplVLogInit> findAplVLogInitByAppName(String nmApplic) {
        try {
            Query query = entityManager.createNamedQuery("AplVLogInit.findByAppName",
                    AplVLogInit.class);
            query.setParameter("nmApplic", nmApplic);
            return query.getResultList();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message("Errore nell'estrazione di AplVLogInit con nmApplic [{0}]", nmApplic)
                    .build();
        }
    }

    /*
     * Estrae i record della tabella con gli oggetti da Inizializzare nell'appisito JOB.
     */
    public List<AplVLogTiOgg> findAplVLogTiOggByAppName(String nmApplic) {
        return findAplVLogTiOggByAppName(nmApplic, null);
    }

    /*
     * Estrae i record della tabella con gli oggetti da Inizializzare nell'appisito JOB.
     */
    public List<AplVLogTiOgg> findAplVLogTiOggByAppName(String nmApplic, String nmTipoOggetto) {
        try {
            Query query = null;
            if (nmTipoOggetto == null) {
                query = entityManager.createNamedQuery("AplVLogTiOgg.findByAppName",
                        AplVLogTiOgg.class);
            } else {
                query = entityManager.createNamedQuery(
                        "AplVLogTiOgg.findByAppNameExcludingTipoOggetto", AplVLogTiOgg.class);
                query.setParameter("nmTipoOggetto", nmTipoOggetto);
            }
            query.setParameter("nmApplic", nmApplic);
            return query.getResultList();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message("Errore nell'estrazione di AplVLogTiOgg con nmApplic [{0}]", nmApplic)
                    .build();
        }
    }

    public List<LogVUsrAbilOrganiz> findUsrVAbilOrganizByApplicAndUser(String nmApplic,
            BigDecimal idUserIam) {
        try {
            Query query = entityManager.createNamedQuery("LogVUsrAbilOrganiz.findByApplicAndUser",
                    LogVUsrAbilOrganiz.class);
            query.setParameter("nmApplic", nmApplic);
            query.setParameter("idUserIam", idUserIam);
            return query.getResultList();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message(
                            "Errore nell'estrazione di UsrVAbilOrganiz con nmApplic [{0}] e idUserIam [{1,number,#}]",
                            nmApplic, idUserIam)
                    .build();
        }
    }

    public CriteriaQuery findEventiByOrgTipoClasseDates(String nomeApplicazione, String nmOggetto,
            BigDecimal idOrganizzazione, BigDecimal idTipoOggetto, String classeEvento, Date dataDa,
            Date dataA) {

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery cq = null;
            Root logVRicEventi = null;

            if (idOrganizzazione == null) {
                cq = cb.createQuery(LogVRicEventi.class);
                logVRicEventi = cq.from(LogVRicEventi.class);
            } else {
                cq = cb.createQuery(LogVRicEventiOrganiz.class);
                logVRicEventi = cq.from(LogVRicEventiOrganiz.class);
            }
            cq.select(logVRicEventi);
            cq.orderBy(cb.desc(logVRicEventi.get("dtRegEvento")));
            Predicate predicate = cb.equal(logVRicEventi.get("nmApplic"), nomeApplicazione);
            if (idOrganizzazione != null) {
                predicate = cb.and(predicate,
                        cb.equal(logVRicEventi.get("idOrganizApplic"), idOrganizzazione));
            }
            if (idTipoOggetto != null) {
                predicate = cb.and(predicate,
                        cb.equal(logVRicEventi.get("idTipoOggetto"), idTipoOggetto));
            }
            if (classeEvento != null) {
                predicate = cb.and(predicate,
                        cb.equal(logVRicEventi.get("tipoClasseEvento"), classeEvento));
            }
            Path<Date> val = logVRicEventi.get("dtRegEvento");
            // Gestisce i casi di data between, solo "data da" e solo "data a"
            if (dataDa != null && dataA != null) {
                predicate = cb.and(predicate, cb.between(val, dataDa, dataA));
            } else if (dataDa != null && dataA == null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(val, dataDa));
            } else if (dataDa == null && dataA != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(val, dataA));
            }
            if (nmOggetto != null) {
                Path<String> nmOgg = logVRicEventi.get("nmOggetto");
                predicate = cb.and(predicate,
                        cb.like(cb.lower(nmOgg), "%" + nmOggetto.toLowerCase() + "%"));
            }
            cq.where(predicate);
            return cq;

        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message(
                            "Errore nell'estrazione di LogVRicEventi con nmApplic [{0}], idOrganizzazione [{1,number,#}], idtipoOggetto [{2,number,#}], classeEvento [{3}], dataInizio [{4,date}], dataFine [{5,date}]",
                            nomeApplicazione, idOrganizzazione, idTipoOggetto, classeEvento, dataDa,
                            dataA, ex)
                    .build();
        }
    }

    public List<LogVRicEventi> findOggettiByIdEvento(BigDecimal idEvento) {
        try {
            Query query = entityManager.createNamedQuery("LogVRicEventi.findByIdEvento",
                    LogVRicEventi.class);
            query.setParameter("idEvento", idEvento);
            return query.getResultList();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message("Errore nell'estrazione di LogVRicEventi con idEvento [{0,number,#}]",
                            idEvento, ex)
                    .build();
        }
    }

    public List<LogVRicEventi> findEventiByIdTransazioneExcludingIdEvento(BigDecimal idTransazione,
            BigDecimal idEvento) {
        try {
            Query query = entityManager.createNamedQuery(
                    "LogVRicEventi.findByIdTransazioneExcludingIdEvento", LogVRicEventi.class);
            query.setParameter("idTransazione", idTransazione);
            query.setParameter("idEvento", idEvento);
            return query.getResultList();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message(
                            "Errore nell'estrazione di LogVRicEventi con idtransazione [{0,number,#}] e idEvento [{1,number,#}]",
                            idTransazione, idEvento, ex)
                    .build();
        }
    }

    public LogVVisEventoPrincTx findTransazioneById(BigDecimal idTransazione) {
        try {
            Query query = entityManager.createNamedQuery("LogVVisEventoPrincTx.findTxById",
                    LogVVisEventoPrincTx.class);
            query.setParameter("idTransazione", idTransazione);
            query.setMaxResults(1);
            List<LogVVisEventoPrincTx> lista = query.getResultList();
            if (lista != null && !lista.isEmpty()) {
                return lista.get(0);
            }
            return null;
        } catch (RuntimeException ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message(
                            "Errore nell'estrazione di LogVVisEventoPrincTx con idtransazione [{0,number,#}]",
                            idTransazione, ex)
                    .build();
        }
    }

    /*
     * Estrae gli UsrVAbilOrganiz per nome applicazione e nome utente
     */
    public List<Object[]> getDistinctLogVLisEventoScriptByNmApplic(String nmApplic) {
        try {
            Query query = entityManager
                    .createNamedQuery("LogVLisEventoByScript.findDistinctByNmApplic");
            query.setParameter("nmApplic", nmApplic);
            return query.getResultList();
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message("Errore nell'estrazione di LogEventoByScript DISTINCT", ex).build();
        }
    }

    public <T> void insertEntity(T entity, boolean forceFlush) {
        if (entity != null) {
            log.debug("Persisting instance of class {}", entity.getClass().getSimpleName());
            entityManager.persist(entity);
            if (forceFlush) {
                entityManager.flush();
            }
        }
    }

    public void insertEventoLoginUser(String nmUserid, String cdIndIpClient, Date dtEvento,
            String tipoEvento, String dsEvento, String cognomeUser, String nomeUser, String cfUser,
            String cdIdEsterno, String emailUser) {
        LogEventoLoginUser logEventoLoginUser = new LogEventoLoginUser();
        logEventoLoginUser.setNmUserid(nmUserid);
        logEventoLoginUser.setCdIndIpClient(cdIndIpClient);
        logEventoLoginUser.setCdIndServer(appServerInstance.getName());
        logEventoLoginUser.setDtEvento(dtEvento);
        logEventoLoginUser.setTipoEvento(tipoEvento);
        logEventoLoginUser.setDsEvento(dsEvento);
        logEventoLoginUser.setNmCognomeUser(cognomeUser);
        logEventoLoginUser.setNmNomeUser(nomeUser);
        logEventoLoginUser.setCdFiscUser(cfUser);
        logEventoLoginUser.setCdIDEsterno(cdIdEsterno);
        logEventoLoginUser.setDsEmailUser(emailUser);
        insertEntity(logEventoLoginUser, true);
    }

    public <T> void removeEntity(T entity, boolean forceFlush) {
        if (entity != null) {
            log.debug("Removing instance of class {}", entity.getClass().getSimpleName());
            entityManager.remove(entity);
            if (forceFlush) {
                entityManager.flush();
            }
        }
    }

    public <T> T findById(Class<T> entityClass, BigDecimal id) {
        log.debug("Getting instance of class {} with id {}", entityClass.getSimpleName(), id);
        try {
            T instance = entityManager.find(entityClass, id);
            log.debug("Get successful");
            return instance;
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR).message("Get failed", ex).build();
        }
    }

    /*
     * Scrive la foto chiamando una stored procedure Oracle per ovviare al problema del mapping JPA
     * con i campi CLOB in scrittura
     */
    public long inserisciFoto(long idOggettoEvento, String fotoXml, Calendar momentoAttuale) {
        long risultato = -1;
        String istruzione = "{call SACER_LOG.INSERT_FOTO(?,?,?,?)}";

        try (Connection con = JpaUtils.provideConnectionFrom(entityManager);
                CallableStatement cs = con.prepareCall(istruzione);) {
            cs.registerOutParameter(4, java.sql.Types.NUMERIC);
            cs.setLong(1, idOggettoEvento);
            cs.setString(2, fotoXml);
            cs.setTimestamp(3, new Timestamp(momentoAttuale.getTime().getTime()));
            cs.executeUpdate();
            risultato = cs.getLong(4);
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR).message("Errore Scrittura Foto", ex)
                    .build();
        }
        return risultato;
    }

    /*
     * Torna un nuovo TransactionId da utilizzare successivamente per le varie loggate "correlate"
     */
    public BigDecimal getNewTransactionId() {
        BigDecimal risultato = null;

        try (Connection con = JpaUtils.provideConnectionFrom(entityManager);
                PreparedStatement ps = con.prepareStatement(
                        "SELECT SACER_LOG.slog_evento_transazione.NEXTVAL AS TRANSACTION_ID FROM DUAL");) {

            try (ResultSet rs = ps.executeQuery();) {
                if (rs != null && rs.next()) {
                    risultato = rs.getBigDecimal("TRANSACTION_ID");
                }
            }
        } catch (Exception ex) {
            log.error(null, ex);
        }
        return risultato;
    }

    /*
     * Scrive il delta della foto attuale rispetto alla precedente. Se non esiste la precedente non
     * scrive nulla.
     */
    public long scriviDelta(long idOggettoEvento, Calendar momentoAttuale) {
        long risultato = -1;
        String istruzione = "{call SACER_LOG.SCRIVI_DELTA(?,?,?)}";

        try (Connection con = JpaUtils.provideConnectionFrom(entityManager);
                CallableStatement cs = con.prepareCall(istruzione);) {

            cs.registerOutParameter(3, java.sql.Types.NUMERIC);
            cs.setLong(1, idOggettoEvento);
            cs.setTimestamp(2, new Timestamp(momentoAttuale.getTime().getTime()));
            cs.executeUpdate();
            risultato = cs.getLong(3);
        } catch (Exception ex) {
            throw SacerLogRuntimeException.builder().cause(ex)
                    .category(SacerLogErrorCategory.SQL_ERROR)
                    .message("Errore nella chiamata alla procedura SCRIVI_DELTA", ex).build();
        }
        return risultato;
    }

    private String getClobAsString(Clob clob) throws SQLException, IOException {
        StringBuilder sb = new StringBuilder((int) clob.length());
        Reader r = clob.getCharacterStream();
        char[] cbuf = new char[2048];
        int n;
        while ((n = r.read(cbuf, 0, cbuf.length)) != -1) {
            sb.append(cbuf, 0, n);
        }
        return sb.toString();
    }

    private Clob getStringAsClob(Connection con, String str) throws SQLException {
        Clob c = con.createClob();
        c.setString(1, str);
        return c;
    }

    public String getXmlFotoByIdEventoOggettoAsString(BigDecimal idOggettoEvento) {
        Clob clob = null;
        String risultato = null;

        try (Connection con = JpaUtils.provideConnectionFrom(entityManager);
                PreparedStatement ps = con.prepareStatement(
                        "SELECT SACER_LOG.GET_XML_FOTO_BY_ID_AS_CLOB(?) AS FOTO FROM DUAL");) {

            ps.setLong(1, idOggettoEvento.longValueExact());

            try (ResultSet rs = ps.executeQuery();) {
                if (rs != null && rs.next()) {
                    clob = rs.getClob("FOTO");
                    risultato = clob == null ? null : getClobAsString(clob);
                }
            }
        } catch (Exception ex) {
            log.error(null, ex);
        }
        return risultato;
    }

    /*
     * Query applicative NATIVE sullo storico delle FOTO XML *
     */

    /*
     * Torna i ruoli che l'utente aveva in una determinata data
     */
    public List<String> findRuoliUtenteAllaData(String nomeApplicazioneIam, String nomeApplicazione,
            String nomeUtente, Date dataRiferimento) {
        List<String> l = null;
        try {
            Query q = entityManager.createNamedQuery("xmldb.findRuoliUtenteAllaData");
            q.setParameter(1, nomeApplicazioneIam);
            q.setParameter(2, nomeUtente);
            q.setParameter(3, dataRiferimento);
            q.setParameter(4, nomeApplicazione);
            l = q.getResultList();
        } catch (Exception ex) {
            log.error(null, ex);
        }
        return l;
    }

}
