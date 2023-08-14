package it.eng.integriam.client.ws;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.eng.integriam.client.ws.IAMSoapClients.ServiceHolder;
import it.eng.integriam.client.ws.allenteconv.AllineamentoEnteConvenzionato_Service;
import it.eng.integriam.client.ws.calcoloservizierogati.CalcoloServiziErogati_Service;
import it.eng.integriam.client.ws.recauth.RecuperoAutorizzazioni_Service;
import it.eng.integriam.client.ws.renews.RestituzioneNewsApplicazione_Service;
import it.eng.integriam.client.ws.reporg.ReplicaOrganizzazione_Service;
import it.eng.integriam.client.ws.reputente.ReplicaUtente_Service;

/**
 * Test per i client SOAP
 */
public class TestSoapClients {

    private static URL REC_AUTORIZZ_WSDL_SVIL;
    private static URL REC_AUTORIZZ_WSDL_TEST;
    private static URL REP_ORGANIZZ_WSDL_SVIL;
    private static URL REP_ORGANIZZ_WSDL_TEST;
    private static URL REP_UTENTE_WSDL_SVIL;
    private static URL REP_UTENTE_WSDL_TEST;
    private static URL REST_NEWS_WSDL_SVIL;
    private static URL REST_NEWS_WSDL_TEST;
    private static URL ALL_ENTE_CONVENZ_WSDL_SVIL;
    private static URL ALL_ENTE_CONVENZ_WSDL_TEST;
    private static URL CALCOLO_SERVIZI_WSDL_SVIL;
    private static URL CALCOLO_SERVIZI_WSDL_TEST;

    public TestSoapClients() {
    }

    @BeforeAll
    public static void setUpClass() throws MalformedURLException {
        REC_AUTORIZZ_WSDL_SVIL = new URL("https://parer-svil.ente.regione.emr.it/saceriam/RecuperoAutorizzazioni?wsdl");
        REC_AUTORIZZ_WSDL_TEST = new URL(
                "https://parer-test.regione.emilia-romagna.it/saceriam/RecuperoAutorizzazioni?wsdl");

        REP_ORGANIZZ_WSDL_SVIL = new URL("https://parer-svil.ente.regione.emr.it/saceriam/ReplicaOrganizzazione?wsdl");
        REP_ORGANIZZ_WSDL_TEST = new URL(
                "https://parer-test.regione.emilia-romagna.it/saceriam/ReplicaOrganizzazione?wsdl");

        REP_UTENTE_WSDL_SVIL = new URL("https://parer-svil.ente.regione.emr.it/sacer/ReplicaUtente?wsdl");
        REP_UTENTE_WSDL_TEST = new URL("https://parer-test.regione.emilia-romagna.it/sacer/ReplicaUtente?wsdl");

        REST_NEWS_WSDL_SVIL = new URL(
                "https://parer-svil.ente.regione.emr.it/saceriam/RestituzioneNewsApplicazione?wsdl");
        REST_NEWS_WSDL_TEST = new URL(
                "https://parer-test.regione.emilia-romagna.it/saceriam/RestituzioneNewsApplicazione?wsdl");

        ALL_ENTE_CONVENZ_WSDL_SVIL = new URL(
                "https://parer-svil.ente.regione.emr.it/saceriam/AllineamentoEnteConvenzionato?wsdl");
        ALL_ENTE_CONVENZ_WSDL_TEST = new URL(
                "https://parer-test.regione.emilia-romagna.it/saceriam/AllineamentoEnteConvenzionato?wsdl");

        CALCOLO_SERVIZI_WSDL_SVIL = new URL(
                "https://parer-svil.ente.regione.emr.it/saceriam/CalcoloServiziErogati?wsdl");
        CALCOLO_SERVIZI_WSDL_TEST = new URL(
                "https://parer-test.regione.emilia-romagna.it/saceriam/CalcoloServiziErogati?wsdl");

    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testIstanzeDiverseRecuperoAutorizzazioni() {
        RecuperoAutorizzazioni_Service service = ServiceHolder.getRecuperoAutorizzazioniService(REC_AUTORIZZ_WSDL_SVIL);
        RecuperoAutorizzazioni_Service service2 = ServiceHolder
                .getRecuperoAutorizzazioniService(REC_AUTORIZZ_WSDL_TEST);
        assertFalse(service == service2);

    }

    @Test
    public void testIstanzeDiverseReplicaOrganizzazione() {
        ReplicaOrganizzazione_Service service = ServiceHolder.getReplicaOrganizzazioneService(REP_ORGANIZZ_WSDL_SVIL);
        ReplicaOrganizzazione_Service service2 = ServiceHolder.getReplicaOrganizzazioneService(REP_ORGANIZZ_WSDL_TEST);
        assertFalse(service == service2);
    }

    @Test
    public void testIstanzeDiverseReplicaUtente() {
        ReplicaUtente_Service service = ServiceHolder.getReplicaUtenteService(REP_UTENTE_WSDL_SVIL);
        ReplicaUtente_Service service2 = ServiceHolder.getReplicaUtenteService(REP_UTENTE_WSDL_TEST);
        assertFalse(service == service2);
    }

    @Test
    public void testIstanzeDiverseRestituzioneNews() {
        RestituzioneNewsApplicazione_Service service = ServiceHolder
                .getRestituzioneNewsApplicazioneService(REST_NEWS_WSDL_SVIL);
        RestituzioneNewsApplicazione_Service service2 = ServiceHolder
                .getRestituzioneNewsApplicazioneService(REST_NEWS_WSDL_TEST);
        assertFalse(service == service2);
    }

    @Test
    public void testIstanzeDiverseAllineaEntiConvenzionati() {
        AllineamentoEnteConvenzionato_Service service = ServiceHolder
                .getAllineamentoEnteConvenzionatoService(ALL_ENTE_CONVENZ_WSDL_SVIL);
        AllineamentoEnteConvenzionato_Service service2 = ServiceHolder
                .getAllineamentoEnteConvenzionatoService(ALL_ENTE_CONVENZ_WSDL_TEST);
        assertFalse(service == service2);
    }

    @Test
    public void testIstanzeDiverseCalcoloServiziiErogati() {
        CalcoloServiziErogati_Service service = ServiceHolder
                .getCalcoloServiziErogatiService(CALCOLO_SERVIZI_WSDL_SVIL);
        CalcoloServiziErogati_Service service2 = ServiceHolder
                .getCalcoloServiziErogatiService(CALCOLO_SERVIZI_WSDL_TEST);
        assertFalse(service == service2);
    }

    @Test
    public void testThreadSafetyRecuperoAutorizzazioniService() {

        RecuperoAutorizzazioni_Service service = ServiceHolder.getRecuperoAutorizzazioniService(REC_AUTORIZZ_WSDL_SVIL);

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                RecuperoAutorizzazioni_Service service2 = ServiceHolder
                        .getRecuperoAutorizzazioniService(REC_AUTORIZZ_WSDL_SVIL);
                // Sono la stessa istanza (creata una volta sola)
                assertTrue(service == service2);
            }).start();

        }
    }

    @Test
    public void testThreadSafetyReplicaOrganizzazioneService() {

        ReplicaOrganizzazione_Service service = ServiceHolder.getReplicaOrganizzazioneService(REP_ORGANIZZ_WSDL_SVIL);

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                ReplicaOrganizzazione_Service service2 = ServiceHolder
                        .getReplicaOrganizzazioneService(REP_ORGANIZZ_WSDL_SVIL);
                // Sono la stessa istanza (creata una volta sola)
                assertTrue(service == service2);
            }).start();

        }
    }

    @Test
    public void testThreadSafetyReplicaUtenteService() {

        ReplicaUtente_Service service = ServiceHolder.getReplicaUtenteService(REP_UTENTE_WSDL_SVIL);

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                ReplicaUtente_Service service2 = ServiceHolder.getReplicaUtenteService(REP_UTENTE_WSDL_SVIL);
                // Sono la stessa istanza (creata una volta sola)
                assertTrue(service == service2);
            }).start();

        }
    }

    @Test
    public void testThreadSafetyRestituzioneNewsService() {

        RestituzioneNewsApplicazione_Service service = ServiceHolder
                .getRestituzioneNewsApplicazioneService(REST_NEWS_WSDL_SVIL);

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                RestituzioneNewsApplicazione_Service service2 = ServiceHolder
                        .getRestituzioneNewsApplicazioneService(REST_NEWS_WSDL_SVIL);
                // Sono la stessa istanza (creata una volta sola)
                assertTrue(service == service2);
            }).start();

        }
    }

    @Test
    public void testThreadSafetyAllineaEntiConvenzionatiService() {

        AllineamentoEnteConvenzionato_Service service = ServiceHolder
                .getAllineamentoEnteConvenzionatoService(ALL_ENTE_CONVENZ_WSDL_SVIL);

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                AllineamentoEnteConvenzionato_Service service2 = ServiceHolder
                        .getAllineamentoEnteConvenzionatoService(ALL_ENTE_CONVENZ_WSDL_SVIL);
                // Sono la stessa istanza (creata una volta sola)
                assertTrue(service == service2);
            }).start();

        }
    }

    @Test
    public void testThreadSafetyCalcoloServiziErogatiService() {

        CalcoloServiziErogati_Service service = ServiceHolder
                .getCalcoloServiziErogatiService(CALCOLO_SERVIZI_WSDL_SVIL);

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                CalcoloServiziErogati_Service service2 = ServiceHolder
                        .getCalcoloServiziErogatiService(CALCOLO_SERVIZI_WSDL_SVIL);
                // Sono la stessa istanza (creata una volta sola)
                assertTrue(service == service2);
            }).start();

        }
    }

}
