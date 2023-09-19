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

package it.eng.integriam.client.ws;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.integriam.client.ws.allenteconv.AllineamentoEnteConvenzionato;
import it.eng.integriam.client.ws.allenteconv.AllineamentoEnteConvenzionato_Service;
import it.eng.integriam.client.ws.calcoloservizierogati.CalcoloServiziErogati;
import it.eng.integriam.client.ws.calcoloservizierogati.CalcoloServiziErogati_Service;
import it.eng.integriam.client.ws.recauth.RecuperoAutorizzazioni;
import it.eng.integriam.client.ws.recauth.RecuperoAutorizzazioni_Service;
import it.eng.integriam.client.ws.renews.RestituzioneNewsApplicazione;
import it.eng.integriam.client.ws.renews.RestituzioneNewsApplicazione_Service;
import it.eng.integriam.client.ws.reporg.ReplicaOrganizzazione;
import it.eng.integriam.client.ws.reporg.ReplicaOrganizzazione_Service;
import it.eng.integriam.client.ws.reputente.ReplicaUtente;
import it.eng.integriam.client.ws.reputente.ReplicaUtente_Service;
import it.eng.spagoLite.security.auth.AuthenticationHandlerConstants;
import it.eng.spagoLite.security.auth.SOAPClientLoginHandlerResolver;

/**
 * Contenitore dei client SOAP. Per garantire la thread safety nonostante il lazy loading vieniva utilizzato il pattern
 * di double-checked locking. Questo pattern è sbagliato per questo motivo:
 * https://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
 *
 * (i suggerimenti sono di SONAR).
 */
public class IAMSoapClients {

    private static final Logger LOG = LoggerFactory.getLogger(IAMSoapClients.class);

    private static final String LOG_CONNESSIONE = "Connessione al WS: {}";
    private static final String LOG_ERRORE = "Errore durante l'inizializzazione del bean. WS Sever offline?";
    private static final String WSDL = "?wsdl";
    private static final int DEFAULT_TIMEOUT = 10_000;

    private IAMSoapClients() {
        throw new IllegalStateException("Impossibile istanziare la classe. Espone solamente metodi statici.");
    }

    /**
     * Contenitore delle istanze del servizio. L'ottenimento dell'istanza del service è thread safe.
     *
     */
    public static class ServiceHolder {

        private static RecuperoAutorizzazioni_Service recAuthService;
        private static ReplicaOrganizzazione_Service repAuthService;
        private static ReplicaUtente_Service repUtenteService;
        private static RestituzioneNewsApplicazione_Service resNewsApplicService;
        private static AllineamentoEnteConvenzionato_Service aecService;
        private static CalcoloServiziErogati_Service cseService;

        private static final String LOG_CREATO = "Creato il client service identitificato dal WSDL: {}";
        private static final String ISTANZA_ERRATA = "Impossibile riutilizzare la stessa istanza, l'URL del wsdl fornita {} è diversa da quella attualmente configurata {} !";

        private ServiceHolder() {
            throw new IllegalStateException("Questa classe è pensata per esporre solamente metodi statici");
        }

        public static synchronized RecuperoAutorizzazioni_Service getRecuperoAutorizzazioniService(URL wsdlUrl) {
            if (recAuthService != null && !recAuthService.getWSDLDocumentLocation().equals(wsdlUrl)) {
                LOG.warn(ISTANZA_ERRATA, wsdlUrl, recAuthService.getWSDLDocumentLocation());
                recAuthService = null;
            }
            if (recAuthService == null) {
                recAuthService = new RecuperoAutorizzazioni_Service(wsdlUrl);
                LOG.info(LOG_CREATO, wsdlUrl);

            }

            return recAuthService;
        }

        public static synchronized ReplicaOrganizzazione_Service getReplicaOrganizzazioneService(URL wsdlUrl) {
            if (repAuthService != null && !repAuthService.getWSDLDocumentLocation().equals(wsdlUrl)) {
                LOG.warn(ISTANZA_ERRATA, wsdlUrl, repAuthService.getWSDLDocumentLocation());
                repAuthService = null;
            }
            if (repAuthService == null) {
                repAuthService = new ReplicaOrganizzazione_Service(wsdlUrl);
                LOG.info(LOG_CREATO, wsdlUrl);
            }
            return repAuthService;
        }

        public static synchronized ReplicaUtente_Service getReplicaUtenteService(URL wsdlUrl) {
            if (repUtenteService != null && !repUtenteService.getWSDLDocumentLocation().equals(wsdlUrl)) {
                LOG.warn(ISTANZA_ERRATA, wsdlUrl, repUtenteService.getWSDLDocumentLocation());
                repUtenteService = null;
            }
            if (repUtenteService == null) {
                repUtenteService = new ReplicaUtente_Service(wsdlUrl);
                LOG.info(LOG_CREATO, wsdlUrl);
            }
            return repUtenteService;
        }

        public static synchronized RestituzioneNewsApplicazione_Service getRestituzioneNewsApplicazioneService(
                URL wsdlUrl) {
            if (resNewsApplicService != null && !resNewsApplicService.getWSDLDocumentLocation().equals(wsdlUrl)) {
                LOG.warn(ISTANZA_ERRATA, wsdlUrl, resNewsApplicService.getWSDLDocumentLocation());
                resNewsApplicService = null;
            }

            if (resNewsApplicService == null) {
                resNewsApplicService = new RestituzioneNewsApplicazione_Service(wsdlUrl);
                LOG.info(LOG_CREATO, wsdlUrl);
            }
            return resNewsApplicService;
        }

        public static synchronized AllineamentoEnteConvenzionato_Service getAllineamentoEnteConvenzionatoService(
                URL wsdlUrl) {

            if (aecService != null && !aecService.getWSDLDocumentLocation().equals(wsdlUrl)) {
                LOG.warn(ISTANZA_ERRATA, wsdlUrl, aecService.getWSDLDocumentLocation());
                aecService = null;
            }
            if (aecService == null) {
                aecService = new AllineamentoEnteConvenzionato_Service(wsdlUrl);
                LOG.info(LOG_CREATO, wsdlUrl);
            }
            return aecService;
        }

        public static synchronized CalcoloServiziErogati_Service getCalcoloServiziErogatiService(URL wsdlUrl) {

            if (cseService != null && !cseService.getWSDLDocumentLocation().equals(wsdlUrl)) {
                LOG.warn(ISTANZA_ERRATA, wsdlUrl, cseService.getWSDLDocumentLocation());
                cseService = null;
            }
            if (cseService == null) {
                cseService = new CalcoloServiziErogati_Service(wsdlUrl);
                LOG.info(LOG_CREATO, wsdlUrl);
            }
            return cseService;
        }
    }

    public static RecuperoAutorizzazioni recuperoAutorizzazioniClient(String username, String password,
            String serviceURL) {
        try {
            LOG.info(LOG_CONNESSIONE, serviceURL);

            RecuperoAutorizzazioni_Service recAuthService = ServiceHolder
                    .getRecuperoAutorizzazioniService(new URL(serviceURL + WSDL));
            recAuthService.setHandlerResolver(new SOAPClientLoginHandlerResolver());
            RecuperoAutorizzazioni client = recAuthService.getRecuperoAutorizzazioniPort();

            // configura il client
            configureWsClient(client, serviceURL, username, password, DEFAULT_TIMEOUT);

            return client;
        } catch (MalformedURLException e) {
            LOG.error(LOG_ERRORE, e);
            return null;
        }
    }

    public static ReplicaOrganizzazione replicaOrganizzazioneClient(String username, String password,
            String serviceURL) {
        try {
            LOG.info(LOG_CONNESSIONE, serviceURL);
            ReplicaOrganizzazione_Service replicaOrganizzazioneService = ServiceHolder
                    .getReplicaOrganizzazioneService(new URL(serviceURL + WSDL));
            replicaOrganizzazioneService.setHandlerResolver(new SOAPClientLoginHandlerResolver());
            ReplicaOrganizzazione client = replicaOrganizzazioneService.getReplicaOrganizzazionePort();

            // configura il client
            configureWsClient(client, serviceURL, username, password, DEFAULT_TIMEOUT);

            return client;
        } catch (MalformedURLException e) {
            LOG.error(LOG_ERRORE, e);
            return null;
        }
    }

    public static ReplicaUtente replicaUtenteClient(String username, String password, String serviceURL) {
        try {
            LOG.info(LOG_CONNESSIONE, serviceURL);
            ReplicaUtente_Service repUtenteService = ServiceHolder.getReplicaUtenteService(new URL(serviceURL + WSDL));
            repUtenteService.setHandlerResolver(new SOAPClientLoginHandlerResolver());
            ReplicaUtente client = repUtenteService.getReplicaUtentePort();

            // configura il client
            configureWsClient(client, serviceURL, username, password, DEFAULT_TIMEOUT);

            return client;
        } catch (MalformedURLException e) {
            LOG.error(LOG_ERRORE, e);
            return null;
        }
    }

    public static RestituzioneNewsApplicazione restituzioneNewsApplicazioneClient(String username, String password,
            String serviceURL) {
        try {
            LOG.info(LOG_CONNESSIONE, serviceURL);
            RestituzioneNewsApplicazione_Service resNewsApplicService = ServiceHolder
                    .getRestituzioneNewsApplicazioneService(new URL(serviceURL + WSDL));
            resNewsApplicService.setHandlerResolver(new SOAPClientLoginHandlerResolver());
            RestituzioneNewsApplicazione client = resNewsApplicService.getRestituzioneNewsApplicazionePort();

            // configura il client
            configureWsClient(client, serviceURL, username, password, DEFAULT_TIMEOUT);

            return client;
        } catch (MalformedURLException e) {
            LOG.error(LOG_ERRORE, e);
            return null;
        }

    }

    public static AllineamentoEnteConvenzionato allineamentoEnteConvenzionatoClient(String username, String password,
            String serviceURL) {

        try {
            LOG.info(LOG_CONNESSIONE, serviceURL);
            AllineamentoEnteConvenzionato_Service aecService = ServiceHolder
                    .getAllineamentoEnteConvenzionatoService(new URL(serviceURL + WSDL));
            aecService.setHandlerResolver(new SOAPClientLoginHandlerResolver());
            AllineamentoEnteConvenzionato client = aecService.getAllineamentoEnteConvenzionatoPort();

            // configura il client
            configureWsClient(client, serviceURL, username, password, DEFAULT_TIMEOUT);

            return client;
        } catch (MalformedURLException e) {
            LOG.error(LOG_ERRORE, e);
            return null;
        }

    }

    public static CalcoloServiziErogati calcoloServiziErogatiClient(String username, String password,
            String serviceURL) {

        try {
            LOG.info(LOG_CONNESSIONE, serviceURL);
            CalcoloServiziErogati_Service cseService = ServiceHolder
                    .getCalcoloServiziErogatiService(new URL(serviceURL + WSDL));
            cseService.setHandlerResolver(new SOAPClientLoginHandlerResolver());
            CalcoloServiziErogati client = cseService.getCalcoloServiziErogatiPort();

            // configura il client
            configureWsClient(client, serviceURL, username, password, DEFAULT_TIMEOUT);

            return client;
        } catch (MalformedURLException e) {
            LOG.error(LOG_ERRORE, e);
            return null;
        }

    }

    /**
     * Permette di sovrascrivere i valori relativi al timeout della chiamata al ws SOAP. Attualmente sovrascrive tutti i
     * valori già impostati su :
     * {@link IAMSoapClients#replicaUtenteClient(java.lang.String, java.lang.String, java.lang.String) }
     *
     * Esempio di utilizzo:
     *
     * <pre>
     * {
     *     &#64;code
     *
     *     ReplicaOrganizzazione client = IAMSoapClients.replicaOrganizzazioneClient(pa.getNmUserid(), pa.getCdPsw(),
     *             url);
     *     IAMSoapClients.changeRequestTimeout((BindingProvider) client, 100000);
     * }
     * </pre>
     *
     * @param wsClient
     *            client del WS SOAP
     * @param newTimeout
     *            nuovo valore del timeout
     */
    public static void changeRequestTimeout(BindingProvider wsClient, int newTimeout) {
        if (wsClient != null) {
            Map<String, Object> requestContext = wsClient.getRequestContext();
            setTimeout(requestContext, newTimeout);
        }
    }

    /**
     * Configura il client del web service SOAP
     *
     * @param <T>
     *            Web service client type
     * @param wsClient
     *            Istanza client web service
     * @param serviceURL
     *            url da configurare
     * @param username
     *            username da configurare
     * @param password
     *            password da configurare
     * @param newTimeout
     *            timeout da sovrascrivere
     */
    private static <T> void configureWsClient(T wsClient, String serviceURL, String username, String password,
            int newTimeout) {
        Map<String, Object> requestContext = ((BindingProvider) wsClient).getRequestContext();

        // Timeout in millis
        setTimeout(requestContext, newTimeout);
        // Endpoint URL
        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceURL);
        requestContext.put(AuthenticationHandlerConstants.USER, username);
        requestContext.put(AuthenticationHandlerConstants.PWD, password);

    }

    /**
     * Imposta o sovrascrive il timeout.
     *
     * @param requestContext
     *            map
     * @param newTimeout
     *            timeout in ms
     */
    private static void setTimeout(Map<String, Object> requestContext, int newTimeout) {
        requestContext.put("com.sun.xml.internal.ws.connect.timeout", newTimeout);
        requestContext.put("com.sun.xml.internal.ws.request.timeout", newTimeout);
        requestContext.put("com.sun.xml.ws.connect.timeout", newTimeout);
        requestContext.put("com.sun.xml.ws.request.timeout", newTimeout);
        requestContext.put("javax.xml.ws.client.connectionTimeout", newTimeout);
        requestContext.put("javax.xml.ws.client.receiveTimeout", newTimeout);
    }

}
