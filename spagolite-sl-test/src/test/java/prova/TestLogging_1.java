/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prova;

import it.eng.parer.sacerlog.ejb.SacerLogEjb;
import it.eng.parer.sacerlog.ejb.helper.SacerLogHelper;
import it.eng.parer.sacerlog.ejb.util.ObjectsToLogBefore;
import it.eng.parer.sacerlog.job.SacerLogJob;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Iacolucci_M
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestLogging_1 {

    private EJBContainer ejbContainer = null;

    @EJB
    private SacerLogEjb sacerLog = null;
    @EJB
    private SacerLogHelper sacerLogHelper = null;
    @EJB
    private SacerLogJob sacerLogJob = null;

    @Resource
    private UserTransaction userTransaction;
    @PersistenceContext
    public EntityManager entityManager;

    private static final String NOME_APPLICAZIONE = "SACER_IAM";
    private static final String NOME_UTENTE = "admin_generale";
    private static final String NOME_FINESTRA = "/amministrazioneUtenti/dettaglioUtente";
    private static final String NOME_AZIONE = "button/AmministrazioneUtentiForm#DettaglioUtente/attivaUtente";
    private static final String NOME_TIPO_OGGETTO = "Utente";
    private static final String NOME_TIPO_EVENTO = "Modifica utente";

    private static final BigDecimal ID_QUERY_TIPO_OGGETTO_UTENTE = new BigDecimal(1);
    private static final BigDecimal ID_TIPO_OGGETTO_UTENTE = new BigDecimal(1);
    private static final BigDecimal ID_TIPO_EVENTO_UTENTE = new BigDecimal(1);
    private static final BigDecimal ID_EVENTO_OGGETTO = new BigDecimal(1);
    private static final BigDecimal ID_OGGETTO_PRINCIPALE = new BigDecimal(5000);

    private static final Logger log = LoggerFactory.getLogger(TestLogging.class);
    // private Connection con = null;

    public TestLogging_1() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws NamingException {
        FileInputStream in = null;
        try {

            Properties props = new Properties();
            String locationPath = TestLogging.class.getProtectionDomain().getCodeSource().getLocation().getFile();
            String fileProperties = locationPath + "jndi.properties";

            in = new FileInputStream(fileProperties);
            props.load(in);
            in.close();
            // String driver = props.getProperty("saceriamDs.JdbcDriver");
            // if (driver != null) {
            // Class.forName(driver);
            // }
            // String url = props.getProperty("saceriamDs.JdbcUrl");
            // String username = props.getProperty("saceriamDs.UserName");
            // String password = props.getProperty("saceriamDs.Password");
            // con = DriverManager.getConnection(url, username, password);

            // eseguiBatch(fileCaricamento);
            // EJB
            ejbContainer = EJBContainer.createEJBContainer(props);
            ejbContainer.getContext().bind("inject", this);

            // DECOMMENTARE PER PULIRE TUTTE LE TABELLE DEL LOG CON LE FOTO

            /*
             * userTransaction.begin(); Query q = entityManager.createNamedQuery("LogFotoOggettoEvento.deleteAll",
             * LogFotoOggettoEvento.class); q.executeUpdate(); q =
             * entityManager.createNamedQuery("LogChiaveAccessoEvento.deleteAll", LogChiaveAccessoEvento.class);
             * q.executeUpdate(); q = entityManager.createNamedQuery("LogOggettoEvento.deleteAll",
             * LogOggettoEvento.class); q.executeUpdate(); q = entityManager.createNamedQuery("LogEvento.deleteAll",
             * LogEvento.class); q.executeUpdate(); q = entityManager.createNamedQuery("LogAgenteEvento.deleteAll",
             * LogAgenteEvento.class); q.executeUpdate(); q = entityManager.createNamedQuery("LogDeltaFoto.deleteAll",
             * LogDeltaFoto.class); q.executeUpdate(); userTransaction.commit();
             */
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(null, ex);
        } finally {
            /*
             * try { in.close(); } catch (IOException ex) { log.debug(null, ex); }
             */
        }

    }

    @After
    public void tearDown() {
        ejbContainer.close();
    }

    /*
     * @Test public void inizializzazioneLog() throws Exception { sacerLogJob.inizializzaLog("SACER_IAM");
     * log.info("TEST Ejb LOG JOB."); Assert.assertTrue(true); }
     */
    /*
     * @Test public void logAgenteTest() throws Exception { LogVLogAgente agente =
     * sacerLogHelper.getAgenteByNomeAgente(NOME_UTENTE); log.info("Chiamata Ejb lettura agente.");
     * Assert.assertEquals(NOME_UTENTE, agente.getNmAgente()); }
     * 
     * @Test public void logTipoEventoFinestraAzioneTest() throws Exception { AplVLogTiEvnConOrigine tipo =
     * sacerLogHelper.getTipoEventoByApplicFinestraAzione(NOME_APPLICAZIONE, NOME_FINESTRA, NOME_AZIONE);
     * log.info("Chiamata Ejb lettura tipoEvento by applic, finestra e azione."); Assert.assertEquals(NOME_APPLICAZIONE,
     * tipo.getNmApplic()); Assert.assertEquals(NOME_FINESTRA, tipo.getNmPaginaCompSw());
     * Assert.assertEquals(NOME_AZIONE, tipo.getNmAzionePaginaCompSw()); }
     * 
     * @Test public void logTipoEventoNometipoEventoTest() throws Exception { AplVLogTiEvn tipo =
     * sacerLogHelper.getTipoEventoByApplicTipoEvento(NOME_APPLICAZIONE, NOME_TIPO_EVENTO);
     * log.info("Chiamata Ejb lettura tipoEvento by applic, nome tipo evento."); Assert.assertEquals(NOME_APPLICAZIONE,
     * tipo.getNmApplic()); Assert.assertEquals(NOME_TIPO_EVENTO, tipo.getNmTipoEvento()); }
     */

    /*
     * 
     * @Test public void logTipoEventoJobAzioneTest() throws Exception { AplVLogTiEvnConOrigine tipo =
     * sacerLogHelper.getTipoEventoByApplicFinestraAzione(NOME_APPLICAZIONE, "JOB_ATTIVAZIONE_UTENTE",
     * "ATTIVAZIONE_UTENTE"); log.info("Chiamata Ejb lettura tipoEvento by applic, JOB e AZIONE JOB.");
     * Assert.assertEquals(NOME_APPLICAZIONE, tipo.getNmApplic()); Assert.assertEquals("JOB_ATTIVAZIONE_UTENTE",
     * tipo.getNmPaginaCompSw()); Assert.assertEquals("ATTIVAZIONE_UTENTE", tipo.getNmAzionePaginaCompSw()); }
     */
    /*
     * @Test public void logTipoOggettoByNomeTest() throws Exception { AplVLogTiOgg tipo =
     * sacerLogHelper.getTipoOggettoByNome(NOME_APPLICAZIONE, NOME_TIPO_OGGETTO);
     * log.info("Chiamata Ejb lettura tipoOggetto by applic e tipo Oggetto."); Assert.assertEquals(NOME_APPLICAZIONE,
     * tipo.getNmApplic()); Assert.assertEquals(NOME_TIPO_OGGETTO, tipo.getNmTipoOggetto()); }
     * 
     * @Test public void logFotoByEventoOggettoTest() throws Exception { AplVLogFotoTiEvnOgg foto =
     * sacerLogHelper.getFotoByEventoOggetto(ID_TIPO_EVENTO_UTENTE, ID_TIPO_OGGETTO_UTENTE);
     * log.info("Chiamata Ejb lettura FOTO by evento e tipo Oggetto."); Assert.assertEquals(new BigDecimal(1),
     * foto.getIdTipoEvento()); Assert.assertEquals(new BigDecimal(1), foto.getIdTipoOggetto()); }
     */

    /*
     * @Test public void logTipoOggettoByNomeTest() throws Exception { LogVVisOggetto ogg =
     * sacerLogHelper.getDettOggettoByAppTipoOggettoId(NOME_APPLICAZIONE, NOME_TIPO_OGGETTO, new BigDecimal(5914));
     * log.info("Chiamata Ejb lettura LogVVisOggetto by applic e tipo Oggetto e ID.");
     * Assert.assertEquals(NOME_APPLICAZIONE, ogg.getNmApplic()); Assert.assertEquals(NOME_TIPO_OGGETTO,
     * ogg.getNmTipoOggetto()); }
     */

    /*
     * 
     * @Test public void logOggettiPadriByIdTipoOggettoTest() throws Exception { List<AplVLogChiaveTiOgg> l =
     * sacerLogHelper.getChiaviAccessoByIdTipoOggetto(ID_TIPO_OGGETTO_UTENTE); List<BigDecimal> lChiavi = null;
     * log.info("Chiamata Ejb lettura Chiavi Accesso tipo Oggetto."); if (l != null && l.size() > 0) { for
     * (Iterator<AplVLogChiaveTiOgg> iterator = l.iterator(); iterator.hasNext();) { AplVLogChiaveTiOgg chiave =
     * iterator.next(); lChiavi = sacerLogHelper.findAllChiaviAccessoByIdOggetto(chiave.getBlQueryTipoOggetto(),
     * ID_OGGETTO_PRINCIPALE); if (lChiavi != null && lChiavi.size() > 0) { for (Iterator<BigDecimal> iterator1 =
     * lChiavi.iterator(); iterator1.hasNext();) { BigDecimal next = iterator1.next();
     * 
     * } } } } Assert.assertNotNull(lChiavi); Assert.assertNotEquals(lChiavi.size(), 0); }
     * 
     * @Test public void logFotoUtenteTest() throws Exception { AplVLogFotoTiEvnOgg foto =
     * sacerLogHelper.getFotoByEventoOggetto(ID_TIPO_EVENTO_UTENTE, ID_TIPO_OGGETTO_UTENTE); String xml =
     * sacerLogHelper.getFotoXml(ID_QUERY_TIPO_OGGETTO_UTENTE); log.info("Chiamata Ejb lettura FOTO Utente."); }
     * 
     * @Test public void logTriggerTest() throws Exception { List<AplVLogTrigTiEvnOgg> trig =
     * sacerLogHelper.getTriggersByIdEventoOggetto(ID_EVENTO_OGGETTO); log.info("Chiamata Ejb lettura triggers.");
     * Assert.assertTrue(true); }
     * 
     * @Test public void ultimoTestChiamataAllAPIDiLogging() throws Exception { userTransaction.begin();
     * ArrayList<LogOggettoRuolo> al=new ArrayList(); LogOggettoRuolo logOggettoRuolo=new
     * LogOggettoRuolo(NOME_TIPO_OGGETTO,ID_OGGETTO_PRINCIPALE, PremisEnums.TipoEventoOggetto.SOURCE);
     * al.add(logOggettoRuolo); logOggettoRuolo=new LogOggettoRuolo(NOME_TIPO_OGGETTO,ID_OGGETTO_PRINCIPALE,
     * PremisEnums.TipoEventoOggetto.OUTCOME); al.add(logOggettoRuolo); LogOggettoRuolo lor[]= new
     * LogOggettoRuolo[al.size()]; lor=al.toArray(lor); sacerLog.log(NOME_APPLICAZIONE, NOME_UTENTE, NOME_AZIONE, lor,
     * NOME_FINESTRA); // sacerLog.log(NOME_APPLICAZIONE, NOME_UTENTE, NOME_AZIONE, NOME_TIPO_OGGETTO,
     * ID_OGGETTO_PRINCIPALE, NOME_FINESTRA); userTransaction.commit(); log.debug("Chiamata Ejb e log ok.");
     * Assert.assertTrue(true); }
     * 
     */

    /*
     * @Test public void _99_chiamataAllAPIDiLogging_ModificaUtente() throws Exception { userTransaction.begin();
     * LogVLogAgente agente = sacerLogHelper.getAgenteByNomeAgente(NOME_UTENTE);
     * log.info("Chiamata Ejb lettura agente."); Assert.assertEquals(NOME_UTENTE, agente.getNmAgente());
     * sacerLog.log(NOME_APPLICAZIONE, NOME_UTENTE, NOME_AZIONE, NOME_TIPO_OGGETTO, agente.getIdUserCompSw(),
     * NOME_FINESTRA); // sacerLog.log(NOME_APPLICAZIONE, NOME_UTENTE, NOME_AZIONE, NOME_TIPO_OGGETTO,
     * ID_OGGETTO_PRINCIPALE, NOME_FINESTRA); userTransaction.commit(); log.debug("PRIMA Chiamata Ejb e log ok.");
     * 
     * userTransaction.begin(); agente = sacerLogHelper.getAgenteByNomeAgente(NOME_UTENTE);
     * log.info("Chiamata Ejb lettura agente."); Assert.assertEquals(NOME_UTENTE, agente.getNmAgente()); LogEvento
     * ev=sacerLog.log(NOME_APPLICAZIONE, NOME_UTENTE, NOME_AZIONE, NOME_TIPO_OGGETTO, agente.getIdUserCompSw(),
     * NOME_FINESTRA); // sacerLog.log(NOME_APPLICAZIONE, NOME_UTENTE, NOME_AZIONE, NOME_TIPO_OGGETTO,
     * ID_OGGETTO_PRINCIPALE, NOME_FINESTRA); userTransaction.commit(); log.debug("SECONDA: Chiamata Ejb e log ok.");
     * Assert.assertTrue(true); }
     */

    /*
     * @Test public void _99_chiamataAllAPIDiLogging_ModificaUtente() throws Exception { userTransaction.begin();
     * LogVLogAgente agente = sacerLogHelper.getAgenteByNomeAgente(NOME_UTENTE);
     * log.info("Chiamata Ejb lettura agente."); Assert.assertEquals(NOME_UTENTE, agente.getNmAgente());
     * sacerLog.log(NOME_APPLICAZIONE, NOME_UTENTE, NOME_AZIONE, NOME_TIPO_OGGETTO, agente.getIdUserCompSw(),
     * NOME_FINESTRA); // sacerLog.log(NOME_APPLICAZIONE, NOME_UTENTE, NOME_AZIONE, NOME_TIPO_OGGETTO,
     * ID_OGGETTO_PRINCIPALE, NOME_FINESTRA); userTransaction.commit(); log.debug("PRIMA Chiamata Ejb e log ok.");
     * 
     * userTransaction.begin(); agente = sacerLogHelper.getAgenteByNomeAgente(NOME_UTENTE);
     * log.info("Chiamata Ejb lettura agente."); Assert.assertEquals(NOME_UTENTE, agente.getNmAgente()); LogEvento
     * ev=sacerLog.log(NOME_APPLICAZIONE, NOME_UTENTE, NOME_AZIONE, NOME_TIPO_OGGETTO, agente.getIdUserCompSw(),
     * NOME_FINESTRA); // sacerLog.log(NOME_APPLICAZIONE, NOME_UTENTE, NOME_AZIONE, NOME_TIPO_OGGETTO,
     * ID_OGGETTO_PRINCIPALE, NOME_FINESTRA); userTransaction.commit(); log.debug("SECONDA: Chiamata Ejb e log ok.");
     * Assert.assertTrue(true); }
     */

    /*
     * Simulazione LogBefore + Loggata normale + logAfter
     */
    /*
     * @Test public void _chiamataAllalogBeforeEAfter() throws Exception { userTransaction.begin();
     * log.info("Chiamata Ejb lettura agente.");
     * 
     * List<ObjectsToLogBefore> listaOggettiDaLoggare=sacerLog.logBefore("SACER", "admin_generale", "toolbar/delete",
     * "Registro", new BigDecimal(19928), "/struttura/registroUnitaDocDetail" ); sacerLog.log("SACER", "admin_generale",
     * "toolbar/delete", "Registro", new BigDecimal(19928), "/struttura/registroUnitaDocDetail");
     * sacerLog.logAfter("SACER", "admin_generale", "toolbar/delete", listaOggettiDaLoggare,
     * "/struttura/registroUnitaDocDetail" );
     * 
     * userTransaction.commit(); log.debug("PRIMA Chiamata Ejb e log ok.");
     * 
     * Assert.assertTrue(true); }
     */

    /*
     * @Test public void _chiamataAllalogBeforeEAfter() throws Exception { userTransaction.begin();
     * log.info("Chiamata Ejb lettura agente."); List<ObjectsToLogBefore>
     * listaOggettiDaLoggare=sacerLog.logBefore("SACER", "admin_generale", "toolbar/delete", "Registro", new
     * BigDecimal(26), "/struttura/registroUnitaDocDetail" ); sacerLog.logAfter("SACER", "admin_generale",
     * "toolbar/delete", listaOggettiDaLoggare, "/struttura/registroUnitaDocDetail" ); userTransaction.commit();
     * log.debug("PRIMA Chiamata Ejb e log ok.");
     * 
     * Assert.assertTrue(true); }
     */

    /*
     * @Test public void _990_chiamataAllAPIDiLogging_isLogging() throws Exception {
     * 
     * userTransaction.begin(); sacerLogHelper.getAgenteByNomeAgente("admin_generale"); userTransaction.commit();
     * Assert.assertTrue(true); }
     */
    /*
     * @Test public void _990_chiamataAllAPIDiLogging_isLogging() throws Exception { userTransaction.begin();
     * sacerLog.logCompSoftware("SACER_IAM", "REPLICA_UTENTI", "Disattivazione automatica", "Utente", new
     * BigDecimal(5000)); userTransaction.commit(); Assert.assertTrue(true); }
     */

    /*
     * @Test public void logOggettiPadriByIdTipoOggettoTest() throws Exception { List<LogEventoByScript> l =
     * sacerLogHelper.getLogEventoScriptByTipoOggettoId(new BigDecimal(46), new BigDecimal(5632));
     * log.info("Chiamata Ejb lettura Chiavi Accesso tipo Oggetto."); if (l != null && l.size() > 0) { for
     * (LogEventoByScript logEventoByScript : l) { log.info("LogEventoByScript >>>>>>>>>>>> {}",
     * logEventoByScript.getDtRegEvento()); } } Assert.assertTrue(true); }
     */
    /*
     * @Test public void _10_InserimentoDueRecord() throws Exception { userTransaction.begin(); LogEventoByScript
     * logScript=new LogEventoByScript(); logScript.setDtRegEvento(new Timestamp((new Date()).getTime()));
     * logScript.setIdAgente(new BigDecimal(423)); logScript.setIdAzioneCompSw(new BigDecimal(64));
     * logScript.setIdOggetto(new BigDecimal(5632)); logScript.setIdTipoOggetto(new BigDecimal(46));
     * logScript.setTiRuoloAgenteEvento("implementer"); logScript.setTiRuoloOggettoEvento("MODIFICA"); // Inserisce un
     * record di script sacerLogHelper.insertEntity(logScript, true); LogEventoByScript logScript2=new
     * LogEventoByScript(); logScript2.setDtRegEvento(new Timestamp((new Date()).getTime())); logScript2.setIdAgente(new
     * BigDecimal(423)); logScript2.setIdAzioneCompSw(new BigDecimal(64)); logScript2.setIdOggetto(new
     * BigDecimal(5632)); logScript2.setIdTipoOggetto(new BigDecimal(46));
     * logScript2.setTiRuoloAgenteEvento("implementer"); logScript2.setTiRuoloOggettoEvento("MODIFICA"); // Inserisce un
     * record di script sacerLogHelper.insertEntity(logScript2, true);
     * 
     * // Logga un evento da finestra di un utente // sacerLog.log("SACER_IAM", "admin_generale", "toolbar/update",
     * "Utente", new BigDecimal(5632), "/amministrazioneUtenti/dettaglioUtenteWizard"); // cancella un record script //
     * sacerLogHelper.removeEntity(logScript, true); // sacerLogHelper.removeEntity(logScript2, true);
     * userTransaction.commit(); Assert.assertTrue(true); }
     */

    /*
     * @Test public void _10_InserimentoDueRecord() throws Exception {
     * 
     * userTransaction.begin(); LogEventoByScript logScript=new LogEventoByScript(); logScript.setDtRegEvento(new
     * Timestamp((new Date()).getTime())); logScript.setIdAgente(new BigDecimal(423)); logScript.setIdAzioneCompSw(new
     * BigDecimal(64)); logScript.setIdOggetto(new BigDecimal(5632)); logScript.setIdTipoOggetto(new BigDecimal(46));
     * logScript.setTiRuoloAgenteEvento("executing program"); logScript.setTiRuoloOggettoEvento("outcome");
     * logScript.setIdApplic(new BigDecimal(321)); // Inserisce un record di script
     * sacerLogHelper.insertEntity(logScript, true); LogEventoByScript logScript2=new LogEventoByScript();
     * logScript2.setDtRegEvento(new Timestamp((new Date()).getTime())); logScript2.setIdAgente(new BigDecimal(423));
     * logScript2.setIdAzioneCompSw(new BigDecimal(64)); logScript2.setIdOggetto(new BigDecimal(5632));
     * logScript2.setIdTipoOggetto(new BigDecimal(46)); logScript2.setTiRuoloAgenteEvento("executing program");
     * logScript2.setTiRuoloOggettoEvento("outcome"); logScript2.setIdApplic(new BigDecimal(321)); // Inserisce un
     * record di script sacerLogHelper.insertEntity(logScript2, true); userTransaction.commit();
     * 
     * // Logga un evento da finestra di un utente // sacerLog.log("SACER_IAM", "admin_generale", "toolbar/update",
     * "Utente", new BigDecimal(5632), "/amministrazioneUtenti/dettaglioUtenteWizard"); // cancella un record script
     * 
     * userTransaction.begin(); sacerLogJob.allineamentoLogByScript(new BigDecimal(321)); userTransaction.commit();
     * 
     * userTransaction.begin(); sacerLogHelper.removeEntity(logScript, true); sacerLogHelper.removeEntity(logScript2,
     * true); userTransaction.commit();
     * 
     * Assert.assertTrue(true); }
     */

    /*
     * @Test public void _20_chiamataAllAPIDiLoggingCONinserimentoDueRecord() throws Exception {
     * userTransaction.begin(); // Logga un evento da finestra di un utente List<LogEventoByScript>
     * l=sacerLogHelper.getLogEventoScriptByTipoOggettoId(new BigDecimal(5632), new BigDecimal(46));
     * sacerLog.log("SACER_IAM", "admin_generale", "toolbar/update", "Utente", new BigDecimal(5632),
     * "/amministrazioneUtenti/dettaglioUtenteWizard"); // cancella un record script //
     * sacerLogHelper.removeEntity(logScript, true); // sacerLogHelper.removeEntity(logScript2, true);
     * userTransaction.commit(); Assert.assertTrue(true); }
     */

    /*
     * @Test public void _21_testnewTransactionId() throws Exception { BigDecimal
     * id=sacerLogHelper.getNewTransactionId(); log.info("TID [{}]", id); Assert.assertTrue( (id!=null &&
     * id.intValueExact()>0) ); }
     */

    /*
     * @Test public void _20_chiamataAllAPIDiLoggingCONinserimentoDueRecord() throws Exception {
     * userTransaction.begin(); // Logga un evento da finestra di un utente List<LogEventoByScript>
     * l=sacerLogHelper.getLogEventoScriptByTipoOggettoIdLocked(new BigDecimal(6), new BigDecimal(19116)); //
     * sacerLog.log("SACER_IAM", "admin_generale", "toolbar/update", "Utente", new BigDecimal(5632),
     * "/amministrazioneUtenti/dettaglioUtenteWizard"); // cancella un record script //
     * sacerLogHelper.removeEntity(logScript, true); // sacerLogHelper.removeEntity(logScript2, true);
     * userTransaction.commit(); Assert.assertTrue(true); }
     */
    /*
     * @Test public void testLogModificaDataErogazione() throws Exception { userTransaction.begin();
     * 
     * sacerLog.log(sacerLog.getNewTransactionLogContext(),"SACER_IAM", "Job Calcolo contenuto Sacer",
     * "Set data erogazione servizi", "Ente convenzionato", new BigDecimal(372), "CALCOLO_CONTENUTO_SACER");
     * userTransaction.commit(); log.debug(">>>>>>>>>>>>>>>>>>> Log effettuato!"); Assert.assertTrue(true); }
     */

    /*
     * @Test public void testFindAllEventibyScript() throws Exception { userTransaction.begin();
     * 
     * // sacerLog.logCompSoftware("SACER_IAM", "REPLICA_UTENTI", "Disattivazione automatica", "Utente", new
     * BigDecimal(5000)); // sacerLog.log("SACER_IAM", "Job Replica utenti", "Disattivazione automatica", "Utente", new
     * BigDecimal(5000), "REPLICA_UTENTI"); // sacerLog.logCompSoftware("SACER",
     * "DUPLICAZIONE_CRITERI_RAGGR_AUTOMATICI", "Set criterio raggruppamento non automatico", "Criterio raggruppamento",
     * new BigDecimal(8103)); sacerLog.log("SACER", "Job duplicazione criteri raggruppamento automatici",
     * "Crea criterio raggruppamento automatico per nuovo anno", "Criterio raggruppamento", new BigDecimal(8103),
     * "DUPLICAZIONE_CRITERI_RAGGR_AUTOMATICI"); userTransaction.commit();
     * log.debug(">>>>>>>>>>>>>>>>>>> Log effettuato!"); Assert.assertTrue(true); }
     */

    @Test
    public void dummyTest() {

    }

    /*
     * private String leggiFile(String path) throws Exception { BufferedReader br = new BufferedReader(new
     * FileReader(path)); try { StringBuilder sb = new StringBuilder(); String line = br.readLine();
     * 
     * while (line != null) { sb.append(line); sb.append(System.lineSeparator()); line = br.readLine(); } return
     * sb.toString(); } finally { br.close(); } }
     */

}
