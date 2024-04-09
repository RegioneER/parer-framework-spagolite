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

package it.eng.parer.sacerlog.ejb;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import it.eng.parer.sacerlog.common.SacerLogEjbType;
import it.eng.parer.sacerlog.ejb.helper.SacerLogHelper;
import it.eng.parer.sacerlog.ejb.util.ObjectsToLogBefore;
import it.eng.parer.sacerlog.ejb.util.PremisEnums;
import it.eng.parer.sacerlog.entity.EntityConstraints;
import it.eng.parer.sacerlog.entity.LogAgenteEvento;
import it.eng.parer.sacerlog.entity.LogChiaveAccessoEvento;
import it.eng.parer.sacerlog.entity.LogEvento;
import it.eng.parer.sacerlog.entity.LogEventoByScript;
import it.eng.parer.sacerlog.entity.LogOggettoEvento;
import it.eng.parer.sacerlog.util.TransactionLogContext;
import it.eng.parer.sacerlog.viewEntity.AplVLogChiaveTiOgg;
import it.eng.parer.sacerlog.viewEntity.AplVLogFotoTiEvnOgg;
import it.eng.parer.sacerlog.viewEntity.AplVLogTiEvn;
import it.eng.parer.sacerlog.viewEntity.AplVLogTiEvnConOrigine;
import it.eng.parer.sacerlog.viewEntity.AplVLogTiOgg;
import it.eng.parer.sacerlog.viewEntity.AplVLogTrigTiEvnOgg;
import it.eng.parer.sacerlog.viewEntity.LogVLogAgente;

/**
 *
 * @author Iacolucci_M
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@SacerLogEjbType
public class SacerLogEjb {

    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private SacerLogHelper sacerLogHelper;
    @Resource
    private SessionContext context;

    private static final String CHIAVE_FOTO_QUERY_XPATH = "//fotoOggetto/recordMaster/keyRecord/datoKey/valoreKey/text()";
    private static final String ERR_MSG_ID_TRANSAZIONE_NULLO = "TID nullo in logAfter(): Ottenere un id transazione valido con il metodo getNewTransactionId()!";

    private static Logger log = LoggerFactory.getLogger(SacerLogEjb.class);

    /*
     * Torna un nuovo TransactionLogContext con all'interno un TransactionId da utilizzare successivamente per le varie
     * loggate "correlate"
     */
    public TransactionLogContext getNewTransactionLogContext() {
        TransactionLogContext ctx = new TransactionLogContext(sacerLogHelper.getNewTransactionId());
        log.debug("TID [{}] Nuova generazione del transaction ID", ctx);
        return ctx;
    }

    /*
     * Metodo principale per il logging che gestisce sia le loggate normali che quelle scaturite da un trigger.
     * 
     * @param isTrigger viene messo a true quando questo metodo viene chiamato da se stesso per eseguire un log da
     * trigger. In questo caso non serve passare nmComponente ma basta il nmTipoEvento e nmApplicazione che usa per fare
     * una query sulla vista APL_V_LOG_TI_EVN che va solo nella tabella APL_TIPO_EVENTO. Se viene passato false in tutti
     * gli altri casi. Con false usa la view APL_V_LOG_TI_EVN_CON_ORIGINE che mette in join e UNION le tabelle delle
     * pagine web, azioni pagine e tabelle dei tipi evento.
     * 
     * @param nmTipoEvento viene passato nel caso in cui istrigger = true in quanto serve per estrarre i dati dalla view
     * APL_V_LOG_TI_EVN
     * 
     * @param nmApplic contiene il nome dell'applicazione per cui si sta loggando
     * 
     * @param agente contiene un record di LOG_V_LOG_AGENTE che è una view che consente con una UNION e join di estrarre
     * i dati dell'utente/agente oppure del componente sw/agente.
     * 
     * @param nmAzione contiene il nome dell'azione da loggare es.: toolbar/insert (campo NM_AZIONE_PAGINA della tabella
     * APL_AZIONE_PAGINA) oppure nel caso di un componente software l'azione corrisponde al contenuto del campo
     * NM_AZIONE_COMP_SW della tabella APL_AZIONE_COMP_SW come ad esempio: "Inizializza log Sacer_iam"
     * 
     * @param nmTipoOggetto contiene il del tipo oggetto che si sta loggando
     * 
     * @param idOggetto contiene la chiave primaria dell'entità che si sta loggando
     * 
     * @param nmComponente contiene il nome del componente che sta logganto come ad esempio una pagina web (es.:
     * /amministrazione/loginUtente) oppure un nome di un componente software come un job che corrisponde al campo
     * NM_COMP_SW della tabella APL_COMP_SW
     * 
     * @param tipoRuoloPremisAgenteEvento il ruolo_agente_evento da mettere nella tabella LOG_AGENTE_EVENTO. Se non
     * viene valorizzato imposta di default "implementer"
     * 
     * @param tipoRuoloPremisEventoOggetto il ruolo_oggetto_evento da mettere nella tabella LOG_OGGETTO_EVENTO. Se non
     * viene valorizzato imposta di default "outcome"
     * 
     * @param momentoAttuale è il timestamp con cui si deve registrare l'evento di log che può essere now() nel caso di
     * loggate normale oppure deve esere presa dalla tabella LOG_EVENTO_BY_SCRIPT nel caso in cui si utilizzi uno script
     * di aggiornamento ORACLE lanciato esternamente per cui quegli eventi vanno registrati con il loro timestamp
     * originario
     * 
     * @param hasToCheckScriptExistance true nei casi di log normale, false nel caso in cui questo metodo venisse
     * chiamato da se stesso quando venissero rilevati dei log dalla tabella LOG_EVENTO_BY_SCRIPT
     *
     * @return LogEvento record LOG_EVENTO con la sua chiave primaria valorizzata
     *
     */
    protected LogEvento log(TransactionLogContext ctx, boolean isTrigger, String nmTipoEvento, String nmApplic,
            LogVLogAgente agente, String nmAzione, String nmTipoOggetto, BigDecimal idOggetto, String nmComponente,
            String tipoRuoloPremisAgenteEvento, String tipoRuoloPremisEventoOggetto, Calendar momentoAttuale,
            boolean hasToCheckScriptExistance) {

        LogEvento logEvento = null;
        /*
         * Se l'ID di transazione viene fornito dall'esterno usa quello altrimenti ne ricava uno nuovo dalla sequence su
         * DB.
         */
        if (ctx == null) {
            ctx = getNewTransactionLogContext();
        }
        // verifica se il logging e' abilitato sulla tabella dei parametri dell'applicazione
        if (sacerLogHelper.isLoggingEnabled() || sacerLogHelper.isLoggingEnabledForThisNomeAzione(nmAzione)) {
            /*
             * Gestione defaults
             */
            tipoRuoloPremisAgenteEvento = tipoRuoloPremisAgenteEvento == null ? PremisEnums.TipoAgenteEvento.IMPLEMENTER
                    : tipoRuoloPremisAgenteEvento;
            tipoRuoloPremisEventoOggetto = tipoRuoloPremisEventoOggetto == null ? PremisEnums.TipoEventoOggetto.OUTCOME
                    : tipoRuoloPremisEventoOggetto;

            AplVLogTiEvn tipo = null;
            AplVLogTiEvnConOrigine tipoConOrigine = null;
            BigDecimal idTipoEvento = null;
            BigDecimal idApplic = null;
            if (isTrigger) {
                tipo = sacerLogHelper.getTipoEventoByApplicTipoEvento(nmApplic, nmTipoEvento);
                idTipoEvento = tipo.getIdTipoEvento();
                idApplic = tipo.getIdApplic();
            } else {
                tipoConOrigine = sacerLogHelper.getTipoEventoByApplicFinestraAzione(nmApplic, nmComponente, nmAzione);
                idTipoEvento = tipoConOrigine.getAplVLogTiEvnConOrigineId().getIdTipoEvento();
                idApplic = tipoConOrigine.getAplVLogTiEvnConOrigineId().getIdApplic();
            }
            AplVLogTiOgg tiOgg = sacerLogHelper.getTipoOggettoByNome(nmApplic, nmTipoOggetto);

            // Gestisci log da script solo se non lo si sta facendo dall'interno del metodo di gestione evento script
            // stesso
            if (hasToCheckScriptExistance) {
                // Fa si che venga generato un nuovo transactionId internamente passandogli null!
                gestisciLogEventoScript(null, nmApplic, nmTipoOggetto, tiOgg.getIdTipoOggetto(), idOggetto);
            }

            AplVLogFotoTiEvnOgg evnOgg = sacerLogHelper.getFotoByEventoOggetto(idTipoEvento, tiOgg.getIdTipoOggetto());
            String foto = sacerLogHelper.getFotoXml(evnOgg.getBlQueryTipoOggetto(), idOggetto,
                    evnOgg.getNmQueryTipoOggetto());

            /*
             * Scrittuta del log sul DB
             */
            // Evento
            logEvento = new LogEvento();
            logEvento.setIdApplic(idApplic);
            logEvento.setIdTipoEvento(idTipoEvento);
            logEvento.setDtRegEvento(momentoAttuale);
            logEvento.setTipoAzione(
                    agente.getTipoOrigineAgente().equals(EntityConstraints.TipoOrigineAgente.COMPONENTE_SW.name())
                            ? EntityConstraints.TipoAzione.AZIONE_JOB.name()
                            : EntityConstraints.TipoAzione.AZIONE_PAGINA.name());
            logEvento.setNmGeneratoreAzione(nmComponente);
            logEvento.setNmAzione(nmAzione);
            logEvento.setIdTransazione(ctx.getTransactionId());

            // Agenti Evento
            ArrayList<LogAgenteEvento> agentiEvento = new ArrayList();
            LogAgenteEvento logAgenteEvento = new LogAgenteEvento();
            logAgenteEvento.setIdAgente(agente.getIdAgente());
            logAgenteEvento.setTiRuoloAgenteEvento(tipoRuoloPremisAgenteEvento);
            logAgenteEvento.setLogEvento(logEvento);
            agentiEvento.add(logAgenteEvento);
            logEvento.setLogAgenteEventos(agentiEvento);

            // Oggetto Evento
            ArrayList<LogOggettoEvento> oggettiEvento = new ArrayList();
            LogOggettoEvento logOggettoEvento = new LogOggettoEvento();
            logOggettoEvento.setIdOggetto(idOggetto);
            logOggettoEvento.setIdTipoOggetto(tiOgg.getIdTipoOggetto());
            logOggettoEvento.setTiRuoloOggettoEvento(tipoRuoloPremisEventoOggetto);

            logOggettoEvento.setDsKeyOggetto(getChiaveRecordFoto(foto));
            logOggettoEvento.setLogEvento(logEvento);
            oggettiEvento.add(logOggettoEvento);
            logEvento.setLogOggettoEventos(oggettiEvento);

            // Verifica esistenza chiavi di accesso
            ArrayList<LogChiaveAccessoEvento> aChiave = new ArrayList();
            if (tiOgg.getIdTipoOggettoPadre() != null) {
                List<AplVLogChiaveTiOgg> lChiavi = sacerLogHelper
                        .getChiaviAccessoByIdTipoOggetto(tiOgg.getIdTipoOggetto());
                if (lChiavi != null && lChiavi.size() > 0) {
                    BigDecimal idOggettoFiglio = idOggetto;
                    LogChiaveAccessoEvento logChiave = null;
                    for (AplVLogChiaveTiOgg chiave : lChiavi) {
                        logChiave = new LogChiaveAccessoEvento();
                        logChiave.setIdApplic(idApplic);
                        logChiave.setIdTipoOggetto(tiOgg.getIdTipoOggettoPadre());
                        logChiave.setLogEvento(logEvento);
                        logChiave.setDtRegEvento(momentoAttuale);
                        List<BigDecimal> lChiaviPadre = sacerLogHelper
                                .findAllChiaviAccessoByIdOggetto(chiave.getBlQueryTipoOggetto(), idOggettoFiglio);
                        if (lChiaviPadre != null && lChiaviPadre.size() > 0) {
                            logChiave.setIdOggetto(lChiaviPadre.iterator().next());
                            aChiave.add(logChiave);
                        }
                    }
                }
            }
            // Gestione della chiave accesso di se stesso creata per ridondanza
            LogChiaveAccessoEvento logChiaveRidonadante = new LogChiaveAccessoEvento();
            logChiaveRidonadante.setIdApplic(idApplic);
            logChiaveRidonadante.setIdTipoOggetto(tiOgg.getIdTipoOggetto());
            logChiaveRidonadante.setIdOggetto(idOggetto);
            logChiaveRidonadante.setDtRegEvento(momentoAttuale);
            logChiaveRidonadante.setLogEvento(logEvento);
            aChiave.add(logChiaveRidonadante);
            logEvento.setLogChiaveAccessoEventos(aChiave);

            // SCRITTURA FINALE LOG
            entityManager.persist(logEvento);
            entityManager.flush();
            BigDecimal idFoto = new BigDecimal(
                    sacerLogHelper.inserisciFoto(logOggettoEvento.getIdOggettoEvento(), foto, momentoAttuale));
            Date dataInizio = new Date();
            sacerLogHelper.scriviDelta(logOggettoEvento.getIdOggettoEvento(), momentoAttuale);
            Date dataFine = new Date();
            log.debug("TID [{}] elapsed DELTA_FOTO [{}][{}] {} millis.", ctx, nmTipoOggetto, idOggetto,
                    dataFine.getTime() - dataInizio.getTime());

            // Verifica esistenza ed esecuzione dei trigger
            List<AplVLogTrigTiEvnOgg> trigList = sacerLogHelper
                    .getTriggersByIdEventoOggetto(evnOgg.getIdTipoEventoOggetto());
            if (trigList != null && trigList.size() > 0) {
                // per ogni trigger che trova...
                for (AplVLogTrigTiEvnOgg trigger : trigList) {
                    if (sacerLogHelper.isThereATrigger(trigger.getBlQueryTipoOggettoTrig(), idFoto)) {
                        log.debug("TID [{}] Deve scattare il trigger per l'entita' [{}]", ctx, idOggetto);
                        List<BigDecimal> l = sacerLogHelper
                                .findAllObjectIdFromTrigger(trigger.getBlQueryTipoOggettoSel(), idOggetto);
                        if (l != null) {
                            for (Iterator<BigDecimal> iterator1 = l.iterator(); iterator1.hasNext();) {
                                BigDecimal oggId = iterator1.next();
                                log.debug("TID [{}] Deve scattare la nuova FOTO TRIGGERATA per l'entita' [{}]", ctx,
                                        oggId);
                                // Logga l'oggetto triggerato ricorsivamente
                                // log(ctx, true, trigger.getNmTipoEventoTrig(), nmApplic, agente, nmAzione,
                                // trigger.getNmTipoOggettoTrig(), oggId, nmComponente, tipoRuoloPremisAgenteEvento,
                                // tipoRuoloPremisEventoOggetto, momentoAttuale, true);
                                log(ctx, true, trigger.getNmTipoEventoTrig(), trigger.getNmApplicTrig(), agente,
                                        nmAzione, trigger.getNmTipoOggettoTrig(), oggId, nmComponente,
                                        tipoRuoloPremisAgenteEvento, tipoRuoloPremisEventoOggetto, momentoAttuale,
                                        true);
                            }
                        }
                    }
                }
            } else {
                log.debug("TID [{}] Nessun trigger da eseguire per l'idEventoOggetto {}", ctx,
                        evnOgg.getIdTipoEventoOggetto());
            }
            log.debug("TID [{}] OGG LOGGATO: {}-{}-{}-{}-{}-{}-{}-{}.", ctx, nmApplic, agente.getNmAgente(), nmAzione,
                    nmTipoOggetto, idOggetto, nmComponente, tipoRuoloPremisAgenteEvento, tipoRuoloPremisEventoOggetto);
            log.debug("TID [{}] FOTO LOGGATA {}", ctx, foto);
        }
        return logEvento;
    }

    /*
     * Dato un idTipoOggetto e idOggetto effettua tutte le foto che deve fare per questa chiave, mettendo un LOCK
     * PESSIMISTICO su tutti i record che trova impedendo che qualsiasi altra chiamata al logging che dovesse fare delle
     * foto pregresse di oggetti inseriti via script possa avere effetto, mettendola quindi in serie. Riceve in input
     * anche nome Applicazione e nome tipo oggetto per chiamare correttamente l'API di logging.
     */
    public void gestisciLogEventoScript(TransactionLogContext ctx, String nomeApplicazione, String nomeTipoOggetto,
            BigDecimal idTipoOggetto, BigDecimal idOggetto) {
        List<LogEventoByScript> l = sacerLogHelper.getLogEventoScriptByTipoOggettoIdLocked(idTipoOggetto, idOggetto);
        if (l != null && l.size() > 0) {
            /*
             * Se l'ID di transazione viene fornito dall'esterno usa quello altrimenti ne ricava uno nuovo dalla
             * sequence su DB.
             */
            if (ctx == null) {
                ctx = getNewTransactionLogContext();
            }
            /*
             * Prende un'altra istanza di se stesso per effettuare una loggata in una nuova transazione. Fa così perché
             * altrimenti se fa una foto delle modifiche dello script nella stessa transazione fotografa l'oggetto che è
             * già stato modificato dalla transazione attuale da interfaccia e quindi tutte le modifiche collassano
             * nell'unico evento di script.
             */
            SacerLogEjb meStesso = context.getBusinessObject(SacerLogEjb.class);

            TransactionLogContext ctxEffettivo;

            for (LogEventoByScript logEventoByScript : l) {
                LogVLogAgente logAgente = sacerLogHelper.getAgenteByIdAgente(logEventoByScript.getIdAgente());
                // 29592 - recupero l'azione da loggare, che può essere riferita ad un componente sw o ad una pagina web
                BigDecimal idAzione = logEventoByScript.getIdAzionePagina() == null
                        ? logEventoByScript.getIdAzioneCompSw() : logEventoByScript.getIdAzionePagina();
                AplVLogTiEvnConOrigine evn = sacerLogHelper
                        .getAplVLogTiEvnConOrigineByApplicIdAzioneCompSw(nomeApplicazione, idAzione);
                Calendar momentoAttuale = Calendar.getInstance();
                momentoAttuale.setTimeInMillis(logEventoByScript.getDtRegEvento().getTime());

                // 29592 - Se in logEventoByScript è presente un id transazione (relativo a logging asincrono), creo un
                // contesto con quell'id
                // per avere il riferimento alla stessa transazione nei record asincroni loggati
                if (logEventoByScript.getIdTransazione() != null) {
                    ctxEffettivo = new TransactionLogContext(logEventoByScript.getIdTransazione());
                } else {
                    ctxEffettivo = ctx;
                }

                meStesso.logInNewTransaction(ctxEffettivo, false, null, nomeApplicazione, logAgente,
                        evn.getNmAzionePaginaCompSw(), nomeTipoOggetto, idOggetto, evn.getNmPaginaCompSw(),
                        logEventoByScript.getTiRuoloAgenteEvento(), logEventoByScript.getTiRuoloOggettoEvento(),
                        momentoAttuale, false, logEventoByScript.getDsMotivoScript());
                // Cancella il record logEventoByScript appena loggato per non riprocessarlo successivamente
                sacerLogHelper.removeEntity(logEventoByScript, true);
            }
        }
    }

    /*
     * Metodo da utilizzare per le loggate da azioni tipo componente software con idTransazione
     */
    public LogEvento log(TransactionLogContext ctx, String nmApplic, String nmAgente, String nmAzione,
            String nmTipoOggetto, BigDecimal idOggetto, String nmComponente, String tipoRuoloPremisAgenteEvento,
            String tipoRuoloPremisEventoOggetto) {
        LogVLogAgente agente = sacerLogHelper.getAgenteByNomeAgente(nmAgente);
        Calendar momentoAttuale = Calendar.getInstance();
        return log(ctx, false, null, nmApplic, agente, nmAzione, nmTipoOggetto, idOggetto, nmComponente,
                tipoRuoloPremisAgenteEvento, tipoRuoloPremisEventoOggetto, momentoAttuale, true);
    }

    /*
     * Metodo da utilizzare per le loggate da azioni tipo componente software con idTransazione Il ruolo Premis Evento
     * Oggetto è a default su PremisEnums.TipoEventoOggetto.OUTCOME
     */
    public LogEvento log(TransactionLogContext ctx, String nmApplic, String nmAgente, String nmAzione,
            String nmTipoOggetto, BigDecimal idOggetto, String nmComponente, String tipoRuoloPremisAgenteEvento) {
        LogVLogAgente agente = sacerLogHelper.getAgenteByNomeAgente(nmAgente);
        Calendar momentoAttuale = Calendar.getInstance();
        return log(ctx, false, null, nmApplic, agente, nmAzione, nmTipoOggetto, idOggetto, nmComponente,
                tipoRuoloPremisAgenteEvento, PremisEnums.TipoEventoOggetto.OUTCOME, momentoAttuale, true);
    }

    /*
     * Metodo da utilizzare per le loggate da azioni tipo azione pagina con idTransazione
     */
    public LogEvento log(TransactionLogContext ctx, String nmApplic, String nmAgente, String nmAzione,
            String nmTipoOggetto, BigDecimal idOggetto, String nmComponente) {
        return log(ctx, nmApplic, nmAgente, nmAzione, nmTipoOggetto, idOggetto, nmComponente, null, null);
    }

    /*
     * Metodo equivalente a log() precedente ma eseguito in una nuova transazione
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public LogEvento logInNewTx(TransactionLogContext ctx, String nmApplic, String nmAgente, String nmAzione,
            String nmTipoOggetto, BigDecimal idOggetto, String nmComponente) {
        return log(ctx, nmApplic, nmAgente, nmAzione, nmTipoOggetto, idOggetto, nmComponente);
    }

    /*
     * Metodo di logging massivo per loggare una serie di idOggetto in un colpo solo con lo stesso idTransazione.
     */
    public List<LogEvento> log(TransactionLogContext ctx, String nmApplic, String nmAgente, String nmAzione,
            String nmTipoOggetto, Set<BigDecimal> idsOggetto, String nmComponente) {
        List<LogEvento> l = new ArrayList();
        if (idsOggetto != null && idsOggetto.size() > 0) {
            /*
             * Se l'ID di transazione viene fornito dall'esterno usa quello altrimenti ne ricava uno nuovo dalla
             * sequence su DB.
             */
            if (ctx == null) {
                ctx = getNewTransactionLogContext();
            }
            for (BigDecimal idOggetto : idsOggetto) {
                l.add(log(ctx, nmApplic, nmAgente, nmAzione, nmTipoOggetto, idOggetto, nmComponente, null, null));
            }
        }
        return l;
    }

    /*
     * Metodo equivalente a log() precedente ma eseguito in una nuova transazione
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<LogEvento> logInNewTx(TransactionLogContext ctx, String nmApplic, String nmAgente, String nmAzione,
            String nmTipoOggetto, Set<BigDecimal> idsOggetto, String nmComponente) {
        return log(ctx, nmApplic, nmAgente, nmAzione, nmTipoOggetto, idsOggetto, nmComponente);
    }

    /*
     * Nuovo metodo per loggare i trigger before. Restituisce una lista di oggetti ognuno dei quali contiene il nome
     * tipo oggetto ed una lista di ID oggetto da fotografare in futuro. Si utilizza tipicamente quando di vuole una
     * lista di oggetti da fotografare dopo che sono stati cancellati dal DB. Per fotografarli successivamente si deve
     * usare la funzione logAfter() a cui verrà passata la lista di oggetti ritornati da questa funzione.
     * 
     * @param nmApplic contiene il nome dell'applicazione per cui si sta loggando
     * 
     * @param agente contiene un record di LOG_V_LOG_AGENTE che è una view che consente con una UNION e join di estrarre
     * i dati dell'utente/agente oppure del componente sw/agente.
     * 
     * @param nmAzione contiene il nome dell'azione da loggare es.: toolbar/insert
     * 
     * @param nmTipoOggetto contiene il del tipo oggetto che si sta loggando
     * 
     * @param idOggetto contiene la chiave primaria dell'entità che si sta loggando
     * 
     * @param nmComponente contiene il nome del componente che sta logganto come ad esempio una pagina web (es.:
     * /amministrazione/loginUtente) oppure un nome di un componente software come un job
     * 
     * @param tipoRuoloPremisAgenteEvento il ruolo_agente_evento da mettere nella tabella LOG_AGENTE_EVENTO. Se non
     * viene valorizzato imposta di default "implementer"
     * 
     * @param tipoRuoloPremisEventoOggetto il ruolo_oggetto_evento da mettere nella tabella LOG_OGGETTO_EVENTO. Se non
     * viene valorizzato imposta di default "outcome"
     * 
     * @return List<ObjectsToLogBefore> Unalista di ObjectsToLogBefore. Un ObjectsToLogBefore contiene il nome del tipo
     * di oggetto ed una lista di idOggetto.
     */
    public List<ObjectsToLogBefore> logBefore(TransactionLogContext ctx, String nmApplic, String nmAgente,
            String nmAzione, String nmTipoOggetto, BigDecimal idOggetto, String nmComponente) {

        if (ctx == null) {
            throw new RuntimeException(ERR_MSG_ID_TRANSAZIONE_NULLO);
        }
        // LogVLogAgente agente = sacerLogHelper.getAgenteByNomeAgente(nmAgente);
        List<ObjectsToLogBefore> listaOggettiDaLoggare = null;
        // verifica se il logging e' abilitato sulla tabella dei parametri dell'applicazione
        if (sacerLogHelper.isLoggingEnabled() || sacerLogHelper.isLoggingEnabledForThisNomeAzione(nmAzione)) {
            // Gestione defaults
            // tipoRuoloPremisAgenteEvento = tipoRuoloPremisAgenteEvento == null ?
            // PremisEnums.TipoAgenteEvento.IMPLEMENTER : tipoRuoloPremisAgenteEvento;
            // tipoRuoloPremisEventoOggetto = tipoRuoloPremisEventoOggetto == null ?
            // PremisEnums.TipoEventoOggetto.OUTCOME : tipoRuoloPremisEventoOggetto;

            AplVLogTiEvnConOrigine tipoConOrigine = sacerLogHelper.getTipoEventoByApplicFinestraAzione(nmApplic,
                    nmComponente, nmAzione);
            BigDecimal idTipoEvento = tipoConOrigine.getAplVLogTiEvnConOrigineId().getIdTipoEvento();

            AplVLogTiOgg tiOgg = sacerLogHelper.getTipoOggettoByNome(nmApplic, nmTipoOggetto);
            AplVLogFotoTiEvnOgg evnOgg = sacerLogHelper.getFotoByEventoOggetto(idTipoEvento, tiOgg.getIdTipoOggetto());

            /*
             * Verifica esistenza ed esecuzione dei trigger BEFORE Effettua una query sulla APL_V_LOG_TRIG_TI_EVN_OGG
             * estraendo per ID_TIPO_EVENTO_OGGETTO tutti i trigger che non hanno alcuna query di verifica se scatenare
             * o no un trigger (ID_QUERY_TIPO_OGGETTO_TRIG)
             */
            List<AplVLogTrigTiEvnOgg> trigList = sacerLogHelper
                    .getTriggersBeforeByIdEventoOggetto(evnOgg.getIdTipoEventoOggetto());
            if (trigList != null && trigList.size() > 0) {
                listaOggettiDaLoggare = new ArrayList();
                // per ogni trigger che trova...
                for (AplVLogTrigTiEvnOgg trigger : trigList) {
                    /*
                     * esegue la query di selezione di tutti gli oggetti da fotografare per l'idOggetto specificato. Per
                     * esempio l'idOggetto potrebbe essere quello del TipoUD e la select degli oggetti estrae per questo
                     * TipoUD tutti i Registri collegati
                     */
                    List<BigDecimal> l = sacerLogHelper.findAllObjectIdFromTrigger(trigger.getBlQueryTipoOggettoSel(),
                            idOggetto);
                    if (l != null && l.size() > 0) {
                        // ...estrae il tipo oggetto ed il tipo evento degli oggetti i cui ID si estrarranno nella
                        // successiva query...
                        ObjectsToLogBefore obj = new ObjectsToLogBefore(trigger.getNmApplicTrig(),
                                trigger.getNmTipoOggettoTrig(), trigger.getNmTipoEventoTrig(),
                                trigger.getTipoClasseEventoTrig());
                        listaOggettiDaLoggare.add(obj);
                        log.debug("TID [{}] Deve scattare il trigger per l'entita' [{}]", ctx, obj.getTipoOggetto());
                        for (BigDecimal oggId : l) {
                            // Inserisco l'ID dell'oggetto nell'array degli oggetti da fotografare in futuro
                            obj.getIdOggetto().add(oggId);
                        }
                    }
                }
            } else {
                log.debug("TID [{}] Nessun trigger da eseguire per l'idEventoOggetto {}", ctx,
                        evnOgg.getIdTipoEventoOggetto());
            }
        }
        return listaOggettiDaLoggare;
    }

    /*
     * Metodo che stampa a posteriori tutti gli oggetti estratti dalla LogBefore da utilizzare nel caso delle
     * cancellazione di Oggetti per cui e' necessario scattare le foto degli oggetti correlati una volta che l'entita'
     * e' stata cancellata.
     */
    public void logAfter(TransactionLogContext ctx, String nmApplic, String nmAgente, String nmAzione,
            List<ObjectsToLogBefore> listaOggetti, String nmComponente) {

        if (ctx == null) {
            throw new RuntimeException(ERR_MSG_ID_TRANSAZIONE_NULLO);
        }
        if (listaOggetti != null) {
            LogVLogAgente agente = sacerLogHelper.getAgenteByNomeAgente(nmAgente);
            Calendar momentoAttuale = Calendar.getInstance();
            for (ObjectsToLogBefore objectsToLogBefore : listaOggetti) {
                log.debug("TID [{}] LOG AFTER per l'oggetto {}", ctx, objectsToLogBefore.getTipoOggetto());
                for (BigDecimal idOgg : objectsToLogBefore.getIdOggetto()) {
                    this.log(ctx, true, objectsToLogBefore.getTipoEvento(), objectsToLogBefore.getNomeApplicazione(),
                            agente, nmAzione, objectsToLogBefore.getTipoOggetto(), idOgg, nmComponente, null, null,
                            momentoAttuale, true);
                }
            }
        }
    }

    /*
     * E' una replica del metodo principale di log protetto che effettua una loggata aprendo una nuova transazione. Lo
     * si utilizza dall'interno di questo EJB per gestire l'elaborazione degli script esterni. E' stato aggiunto il
     * parametro "motivazioneScript" per generare un evento di log derivato da uno script. La motivazione viene presa
     * dalla tabella LOG_BY_SCRIPT.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public LogEvento logInNewTransaction(TransactionLogContext ctx, boolean isTrigger, String nmTipoEvento,
            String nmApplic, LogVLogAgente agente, String nmAzione, String nmTipoOggetto, BigDecimal idOggetto,
            String nmComponente, String tipoRuoloPremisAgenteEvento, String tipoRuoloPremisEventoOggetto,
            Calendar momentoAttuale, boolean hasToCheckScriptExistance, String motivazioneScript) {
        LogEvento ev = log(ctx, isTrigger, nmTipoEvento, nmApplic, agente, nmAzione, nmTipoOggetto, idOggetto,
                nmComponente, tipoRuoloPremisAgenteEvento, tipoRuoloPremisEventoOggetto, momentoAttuale,
                hasToCheckScriptExistance);
        ev.setDsMotivoScript(motivazioneScript);
        return ev;
    }

    /*
     * Costruisce la chiave del record prendendo tutti gli elementi della chiave e separandoli con DUE caratteri ~
     * (TILDE). Se nell'elemento c'è 'null' allora omette sia il contenuto della chiave che della tilde
     */
    public static String getChiaveRecordFoto(String xml) {
        if (xml == null) {
            return null;
        }
        String chiave = "";
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        InputSource source = new InputSource(new StringReader(xml));
        try {
            NodeList nodes = (NodeList) xpath.evaluate(CHIAVE_FOTO_QUERY_XPATH, source, XPathConstants.NODESET);
            if (nodes != null) {
                Node fratelloPrecedente = null;
                for (int i = 0; i < nodes.getLength(); i++) {
                    if (i == (nodes.getLength() - 1)) {
                        if (nodes.item(i) != null
                                && !nodes.item(i).equals(EntityConstraints.VALORE_NULL_DS_KEY_OGGETTO)) {
                            fratelloPrecedente = nodes.item(i).getParentNode().getPreviousSibling();
                            chiave += fratelloPrecedente.getTextContent() + "=" + nodes.item(i).getNodeValue();
                        }
                    } else {
                        if (nodes.item(i) != null
                                && !nodes.item(i).equals(EntityConstraints.VALORE_NULL_DS_KEY_OGGETTO)) {
                            fratelloPrecedente = nodes.item(i).getParentNode().getPreviousSibling();
                            chiave += fratelloPrecedente.getTextContent() + "=" + nodes.item(i).getNodeValue()
                                    + EntityConstraints.SEPARATORE_DS_KEY_OGGETTO;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Errore nel processamento dell'XML", e);
        }

        return chiave;
    }

    /*
     * public List<ObjectsToLogBefore> logBefore(String nmApplic, String nmAgente, String nmAzione, String
     * nmTipoOggetto, BigDecimal idOggetto, String nmComponente, String tipoRuoloPremisAgenteEvento, String
     * tipoRuoloPremisEventoOggetto) { LogVLogAgente agente = sacerLogHelper.getAgenteByNomeAgente(nmAgente); return
     * logBefore(nmApplic, agente, nmAzione, nmTipoOggetto, idOggetto, nmComponente, tipoRuoloPremisAgenteEvento,
     * tipoRuoloPremisEventoOggetto); }
     */
    /*
     * public List<ObjectsToLogBefore> logBefore(String nmApplic, String nmAgente, String nmAzione, String
     * nmTipoOggetto, BigDecimal idOggetto, String nmComponente) { LogVLogAgente agente =
     * sacerLogHelper.getAgenteByNomeAgente(nmAgente); return logBefore(nmApplic, agente, nmAzione, nmTipoOggetto,
     * idOggetto, nmComponente); }
     */
    /*
     * Metodo di logging massivo per loggare una serie di idOggetto in un colpo solo senza idTransazione
     */
    /*
     * public List<LogEvento> log(String nmApplic, String nmAgente, String nmAzione, String nmTipoOggetto,
     * Set<BigDecimal> idsOggetto, String nmComponente) { return log(null, nmApplic, nmAgente, nmAzione, nmTipoOggetto,
     * idsOggetto, nmComponente); }
     */
    /* Multiple logging */
    /*
     * public void log(String nmApplic, String nmAgente, String nmAzione, LogOggettoRuolo logOggettoRuolo[], String
     * nmComponente, String tipoRuoloPremisAgenteEvento) { if (logOggettoRuolo != null) { for (LogOggettoRuolo or :
     * logOggettoRuolo) { log(nmApplic, nmAgente, nmAzione, or.getNmTipoOggetto(), or.getIdOggetto(), nmComponente,
     * tipoRuoloPremisAgenteEvento, or.getTiRuoloPremis()); } } }
     * 
     * public void log(String nmApplic, String nmAgente, String nmAzione, LogOggettoRuolo logOggettoRuolo[], String
     * nmComponente) { if (logOggettoRuolo != null) { for (LogOggettoRuolo or : logOggettoRuolo) { log(nmApplic,
     * nmAgente, nmAzione, or.getNmTipoOggetto(), or.getIdOggetto(), nmComponente, null, or.getTiRuoloPremis()); } } }
     * 
     * public void log(String nmApplic, String nmAgente, String nmAzione, LogOggettoRuolo logOggettoRuolo[]) { if
     * (logOggettoRuolo != null) { for (LogOggettoRuolo or : logOggettoRuolo) { log(nmApplic, nmAgente, nmAzione,
     * or.getNmTipoOggetto(), or.getIdOggetto(), null, null, or.getTiRuoloPremis()); } } }
     */
    /*
     * Metodo da utilizzare per le loggate da azioni tipo azione pagina senza idTransazione
     */
    /*
     * public LogEvento log(String nmApplic, String nmAgente, String nmAzione, String nmTipoOggetto, BigDecimal
     * idOggetto, String nmComponente) { return log(null, nmApplic, nmAgente, nmAzione, nmTipoOggetto, idOggetto,
     * nmComponente); }
     */
    /*
     * Metodo da utilizzare per le loggate da azioni tipo azione pagina senza idTransazione
     */
    /*
     * public LogEvento log(String nmApplic, String nmAgente, String nmAzione, String nmTipoOggetto, BigDecimal
     * idOggetto, String nmComponente, String tipoRuoloPremisAgenteEvento, String tipoRuoloPremisEventoOggetto) { return
     * log(null, nmApplic, nmAgente, nmAzione, nmTipoOggetto, idOggetto, nmComponente, tipoRuoloPremisAgenteEvento,
     * tipoRuoloPremisEventoOggetto); }
     */
    /*
     * Metodo in overload che passa null al corrispondente metodo precedente
     */
    /*
     * public void gestisciLogEventoScript(String nomeApplicazione, String nomeTipoOggetto, BigDecimal idTipoOggetto,
     * BigDecimal idOggetto) { gestisciLogEventoScript(null, nomeApplicazione, nomeTipoOggetto, idTipoOggetto,
     * idOggetto); }
     */
}
