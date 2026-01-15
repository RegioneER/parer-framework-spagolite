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

package it.eng.parer.sacerlog.job;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.parer.sacerlog.ejb.SacerLogEjb;
import it.eng.parer.sacerlog.ejb.helper.SacerLogHelper;
import it.eng.parer.sacerlog.ejb.util.PremisEnums;
import it.eng.parer.sacerlog.viewEntity.AplVLogInit;

/**
 *
 *
 * @author Iacolucci_M
 *
 */
@Stateless(mappedName = "SacerLogJob")
@LocalBean
public class SacerLogJob {

    private Logger logger = LoggerFactory.getLogger(SacerLogJob.class.getName());

    @EJB
    private SacerLogJobHelper sacerLogJobHelper;
    @EJB
    private SacerLogHelper sacerLogHelper;
    @EJB
    private SacerLogEjb sacerLogEjb;

    @Resource
    private SessionContext context;

    /*
     * Metodo invocato dal timer per la gestione dell'inizializzazione delle entità da fotografare.
     */
    public void inizializzaLog(String nmApplic) throws Exception {
        Date dataInizio = new Date();
        String nomeEffettivoJob = Constants.NomiJob.INIZIALIZZAZIONE_LOG.name() + "_" + nmApplic;
        logger.info("{} - Inizio esecuzione job - {}", nomeEffettivoJob, dataInizio);
        SacerLogJob me = context.getBusinessObject(SacerLogJob.class);
        List<AplVLogInit> l = sacerLogHelper.findAplVLogInitByAppName(nmApplic);
        boolean logIniziofatto = false;
        ArrayList<LogJobData> risultato = new ArrayList();
        for (AplVLogInit rec : l) {
            if (logIniziofatto == false) {
                logIniziofatto = true;
                sacerLogJobHelper.writeAtomicLogJob(nomeEffettivoJob,
                        Constants.tiEvento.INIZIO_SCHEDULAZIONE.name());
            }
            BigDecimal idDaLoggare = null;
            try {
                Date dataInizioOggetto = new Date();
                List<BigDecimal> ids = sacerLogHelper
                        .findAllObjectIdToInitialize(rec.getBlQueryTipoOggetto());
                logger.info("{} - trovati {} '{}' da inizializzare", nomeEffettivoJob, ids.size(),
                        rec.getNmTipoOggetto());
                for (BigDecimal id : ids) {
                    // Scrittura transazionale
                    idDaLoggare = id;
                    me.execute(rec, id, nomeEffettivoJob);
                }
                Date dataFineOggetto = new Date();
                LogJobData jd = new LogJobData(rec.getNmTipoOggetto(), ids.size(),
                        dataFineOggetto.getTime() - dataInizioOggetto.getTime());
                risultato.add(jd);
            } catch (Exception ex) {
                String message = ExceptionUtils.getRootCauseMessage(ex);
                logger.error("{} - Errore nell'esecuzione del job di inizializzazione log",
                        nomeEffettivoJob, ex);
                String mex = "Errore sul tipo oggetto '" + rec.getNmTipoOggetto() + "' con id ["
                        + idDaLoggare + "] " + message;
                if (mex.length() > 1023) {
                    mex = mex.substring(0, 1023);
                }
                sacerLogJobHelper.writeAtomicLogJob(nomeEffettivoJob,
                        Constants.tiEvento.ERRORE.name(), mex);
                throw ex;
            }
        }
        // FINE ELABORAZIONE
        Date dataFine = new Date();
        if (logIniziofatto) {
            StringBuilder mexBuf = new StringBuilder("Oggetti inizializzati: ");
            for (LogJobData jd : risultato) {
                mexBuf.append(jd.getNome()).append(" [").append(jd.getNumFotoEseguite())
                        .append("] in ").append(jd.tempoTotale / 1000).append("s ");
            }
            long diff = dataFine.getTime() - dataInizio.getTime();
            mexBuf.append(", tempo totale: ").append(diff / 1000).append("s");
            sacerLogJobHelper.writeAtomicLogJob(nomeEffettivoJob,
                    Constants.tiEvento.FINE_SCHEDULAZIONE.name(),
                    mexBuf.length() > 1023 ? mexBuf.substring(0, 1023) : mexBuf.toString());
        }
        logger.info(
                "{} - fine esecuzione job - {}, tempo impiegato in ms: {}, elementi processati: {}",
                nomeEffettivoJob, dataFine, dataFine.getTime() - dataInizio.getTime(),
                l != null ? l.size() : 0);
    }

    /*
     * Scrittura della foto in una nuova transazione
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void execute(AplVLogInit rec, BigDecimal id, String nomeEffettivoJob) throws Exception {
        try {
            sacerLogEjb.log(null, rec.getNmApplic(), rec.getNmAgente(), rec.getNmAzioneCompSw(),
                    rec.getNmTipoOggetto(), id, rec.getNmCompSw(),
                    PremisEnums.TipoAgenteEvento.EXECUTING_PROGRAM,
                    PremisEnums.TipoEventoOggetto.OUTCOME);
        } catch (Exception ex) {
            logger.error("{} - Errore sul tipo oggetto {} con id [{}]", nomeEffettivoJob,
                    rec.getNmTipoOggetto(), id);
            throw ex;
        }
    }

    /*
     * Metodo invocato dal timer di gestione degli allineamenti delle loggate inserite mediante
     * script esterni all'applicazione.
     *
     * Apre un cursore che estrae con una DISTINCT dalla VIEW LOG_V_LIS_EVENTO_BY_SCRIPT ( legge
     * dalla tabella: LOG_EVENTO_BY_SCRIPT) tutte le coppie di valori ID_TIPO_OGGETTO e ID_OGGETTO
     * (+ nome tipo oggetto). Per ogni record estratto effettua una SELECT FOR UPDATE della coppia
     * di valori estratti loccando tutti i records estratti in modo pessimistico in modo tale da
     * bloccare un'altra eventuale select dello stesso tipo che dovesse essere fatta sugli stessi
     * oggetti dal motore di logging.
     *
     */
    public void allineamentoLogByScript(String nomeApplicazione) throws Exception {
        Date dataInizio = new Date();
        String nomeEffettivoJob = Constants.NomiJob.ALLINEAMENTO_LOG.name() + "_"
                + nomeApplicazione;
        logger.info("{} - Inizio esecuzione job - {}", nomeEffettivoJob, dataInizio);
        List<Object[]> l = sacerLogHelper
                .getDistinctLogVLisEventoScriptByNmApplic(nomeApplicazione);
        sacerLogJobHelper.writeAtomicLogJob(nomeEffettivoJob,
                Constants.tiEvento.INIZIO_SCHEDULAZIONE.name());
        // Alla posizione 0=idTipoOggetto, 1=idOggetto, 2=nome tipo oggetto
        // boolean logIniziofatto = false;
        SacerLogJob ejbPerNuoveTransazioni = context.getBusinessObject(SacerLogJob.class);
        for (Object[] rec : l) {
            // if (logIniziofatto == false) {
            // logIniziofatto = true;
            // sacerLogJobHelper.writeAtomicLogJob(nomeEffettivoJob,
            // Constants.tiEvento.INIZIO_SCHEDULAZIONE.name());
            // }
            try {
                // Esegue in una nuova transazione!
                ejbPerNuoveTransazioni.allineaOggettiEstrattiDaScriptInNuovaTransazione(
                        (BigDecimal) rec[0], (BigDecimal) rec[1], nomeApplicazione,
                        (String) rec[2]);

            } catch (Exception ex) {
                String message = ExceptionUtils.getRootCauseMessage(ex);
                logger.error("{} - Errore nell'esecuzione del job di inizializzazione log",
                        nomeEffettivoJob, ex);
                String mex = "Errore sul tipo oggetto '" + rec[0] + "' con id [" + rec[1] + "] "
                        + message;
                if (mex.length() > 1023) {
                    mex = mex.substring(0, 1023);
                }
                sacerLogJobHelper.writeAtomicLogJob(nomeEffettivoJob,
                        Constants.tiEvento.ERRORE.name(), mex);
                throw ex;
            }

        }
        // FINE ELABORAZIONE
        Date dataFine = new Date();
        // if (logIniziofatto) {
        StringBuilder mexBuf = new StringBuilder("Oggetti allineati: ");
        mexBuf.append(l.size());
        long diff = dataFine.getTime() - dataInizio.getTime();
        mexBuf.append(" TEMPO TOT ").append(diff / 1000).append("s");
        sacerLogJobHelper.writeAtomicLogJob(nomeEffettivoJob,
                Constants.tiEvento.FINE_SCHEDULAZIONE.name(),
                mexBuf.length() > 1023 ? mexBuf.substring(0, 1023) : mexBuf.toString());
        // }
        logger.info(
                "{} - fine esecuzione job - {}, tempo impiegato in ms: {}, elementi processati: {}",
                nomeEffettivoJob, dataFine, dataFine.getTime() - dataInizio.getTime(),
                l != null ? l.size() : 0);
    }

    /*
     * Metodo che apre una nuova transazione e richiama il metodo di gestione evento script che
     * mette un LOCK PESSIMISTICO sui records estratti. Quando quegli eventi sono processati la
     * transazione viene chiusa e i Lock eliminati in modo da lasciare liberi altri thread di
     * processare gli stessi oggetti.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void allineaOggettiEstrattiDaScriptInNuovaTransazione(BigDecimal idTipoOggetto,
            BigDecimal idOggetto, String nmApplic, String nmTipoOggetto) throws Exception {
        logger.info("Allineo e metto il lock su: idTipoOggetto[{}] idOggetto:[{}]", idTipoOggetto,
                idOggetto);
        sacerLogEjb.gestisciLogEventoScript(null, nmApplic, nmTipoOggetto, idTipoOggetto,
                idOggetto);
        /*
         * logger. info("[{}]>>> ATTESA SIMULATA 30sec NEL THREAD DI ALLINEAMENTO...[{}]-[{}]-[{}] "
         * , new Date(),nmApplic, nmTipoOggetto,idOggetto); try {
         * Thread.currentThread().sleep(1000*30); } catch (Exception ex) {
         *
         * } logger.
         * info("[{}]>>> USCITO DALL'ATTESA SIMULATA 30sec NEL THREAD DI ALLINEAMENTO..[{}]-[{}]-[{}] "
         * , new Date(),nmApplic, nmTipoOggetto,idOggetto);
         */
    }

    private class LogJobData {

        private String nome;
        private int numFotoEseguite;
        private long tempoTotale;

        public LogJobData(String nome) {
            this.nome = nome;
        }

        public LogJobData(String nome, int numFotoEseguite) {
            this.nome = nome;
            this.numFotoEseguite = numFotoEseguite;
        }

        public LogJobData(String nome, int numFotoEseguite, long tempoTotale) {
            this.nome = nome;
            this.numFotoEseguite = numFotoEseguite;
            this.tempoTotale = tempoTotale;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public int getNumFotoEseguite() {
            return numFotoEseguite;
        }

        public void setNumFotoEseguite(int numFotoEseguite) {
            this.numFotoEseguite = numFotoEseguite;
        }

        public long getTempoTotale() {
            return tempoTotale;
        }

        public void setTempoTotale(long tempoTotale) {
            this.tempoTotale = tempoTotale;
        }

    }

}
