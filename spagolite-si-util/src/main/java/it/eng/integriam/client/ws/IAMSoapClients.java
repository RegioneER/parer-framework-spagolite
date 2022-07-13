package it.eng.integriam.client.ws;

import it.eng.integriam.client.ws.allenteconv.AllineamentoEnteConvenzionato;
import it.eng.integriam.client.ws.allenteconv.AllineamentoEnteConvenzionato_Service;
import it.eng.integriam.client.ws.calcoloservizierogati.CalcoloServiziErogati;
import it.eng.integriam.client.ws.calcoloservizierogati.CalcoloServiziErogati_Service;
import java.net.URL;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.integriam.client.ws.recauth.RecuperoAutorizzazioni;
import it.eng.integriam.client.ws.recauth.RecuperoAutorizzazioni_Service;
import it.eng.integriam.client.ws.reporg.ReplicaOrganizzazione;
import it.eng.integriam.client.ws.reporg.ReplicaOrganizzazione_Service;
import it.eng.integriam.client.ws.reputente.ReplicaUtente;
import it.eng.integriam.client.ws.reputente.ReplicaUtente_Service;
import it.eng.integriam.client.ws.renews.RestituzioneNewsApplicazione;
import it.eng.integriam.client.ws.renews.RestituzioneNewsApplicazione_Service;
import it.eng.spagoLite.security.auth.AuthenticationHandlerConstants;
import it.eng.spagoLite.security.auth.SOAPClientLoginHandlerResolver;

public class IAMSoapClients {

    private static final Logger LOG = LoggerFactory.getLogger(IAMSoapClients.class);

    private static RecuperoAutorizzazioni_Service recAuthService;
    private static ReplicaOrganizzazione_Service repAuthService;
    private static ReplicaUtente_Service repUtenteService;
    private static RestituzioneNewsApplicazione_Service resNewsApplicService;
    private static AllineamentoEnteConvenzionato_Service aecService;
    private static CalcoloServiziErogati_Service cseService;

    public static RecuperoAutorizzazioni recuperoAutorizzazioniClient(String username, String password,
            String serviceURL) {
        try {
            LOG.info("Connessione al WS: " + serviceURL);
            if (recAuthService == null) {
                synchronized (IAMSoapClients.class) {
                    if (recAuthService == null) {
                        URL wsdlURL = new URL(serviceURL + "?wsdl");
                        recAuthService = new RecuperoAutorizzazioni_Service(wsdlURL);
                        recAuthService.setHandlerResolver(new SOAPClientLoginHandlerResolver());
                        LOG.info("Creato il client service per il WS: " + serviceURL);
                    }
                }
            }
            RecuperoAutorizzazioni client = recAuthService.getRecuperoAutorizzazioniPort();
            Map<String, Object> requestContext = ((BindingProvider) client).getRequestContext();
            // Timeout in millis
            setTimeout(requestContext, 10000);
            // Endpoint URL
            requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceURL);
            requestContext.put(AuthenticationHandlerConstants.USER, username);
            requestContext.put(AuthenticationHandlerConstants.PWD, password);

            return client;
        } catch (Exception e) {
            LOG.error("Errore durante l'inizializzazione del bean. WS Sever offline?", e);
            return null;
        }
    }

    public static ReplicaOrganizzazione replicaOrganizzazioneClient(String username, String password,
            String serviceURL) {
        try {
            LOG.info("Connessione al WS: " + serviceURL);
            if (repAuthService == null) {
                synchronized (IAMSoapClients.class) {
                    if (repAuthService == null) {
                        URL wsdlURL = new URL(serviceURL + "?wsdl");
                        repAuthService = new ReplicaOrganizzazione_Service(wsdlURL);
                        repAuthService.setHandlerResolver(new SOAPClientLoginHandlerResolver());
                        LOG.info("Creato il client service per il WS: " + serviceURL);
                    }
                }
            }

            ReplicaOrganizzazione client = repAuthService.getReplicaOrganizzazionePort();
            Map<String, Object> requestContext = ((BindingProvider) client).getRequestContext();
            // Timeout in millis
            setTimeout(requestContext, 10000);
            // Endpoint URL
            requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceURL);
            requestContext.put(AuthenticationHandlerConstants.USER, username);
            requestContext.put(AuthenticationHandlerConstants.PWD, password);
            return client;
        } catch (Exception e) {
            LOG.error("Errore durante l'inizializzazione del bean. WS Sever offline?", e);
            return null;
        }
    }

    public static ReplicaUtente replicaUtenteClient(String username, String password, String serviceURL) {
        try {
            LOG.info("Connessione al WS: " + serviceURL);
            if (repUtenteService == null) {
                synchronized (IAMSoapClients.class) {
                    if (repUtenteService == null) {
                        URL wsdlURL = new URL(serviceURL + "?wsdl");
                        repUtenteService = new ReplicaUtente_Service(wsdlURL);
                        repUtenteService.setHandlerResolver(new SOAPClientLoginHandlerResolver());
                        LOG.info("Creato il client service per il WS: " + serviceURL);
                    }
                }
            }

            ReplicaUtente client = repUtenteService.getReplicaUtentePort();
            Map<String, Object> requestContext = ((BindingProvider) client).getRequestContext();
            // Timeout in millis
            setTimeout(requestContext, 100000);
            // Endpoint URL
            requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceURL);
            requestContext.put(AuthenticationHandlerConstants.USER, username);
            requestContext.put(AuthenticationHandlerConstants.PWD, password);
            return client;
        } catch (Exception e) {
            LOG.error("Errore durante l'inizializzazione del bean. WS Sever offline?", e);
            return null;
        }
    }

    public static RestituzioneNewsApplicazione restituzioneNewsApplicazioneClient(String username, String password,
            String serviceURL) {
        try {
            LOG.info("Connessione al WS: " + serviceURL);
            if (resNewsApplicService == null) {
                synchronized (IAMSoapClients.class) {
                    if (resNewsApplicService == null) {
                        URL wsdlURL = new URL(serviceURL + "?wsdl");
                        resNewsApplicService = new RestituzioneNewsApplicazione_Service(wsdlURL);
                        resNewsApplicService.setHandlerResolver(new SOAPClientLoginHandlerResolver());
                        LOG.info("Creato il client service per il WS: " + serviceURL);
                    }
                }
            }

            RestituzioneNewsApplicazione client = resNewsApplicService.getRestituzioneNewsApplicazionePort();
            Map<String, Object> requestContext = ((BindingProvider) client).getRequestContext();
            // Timeout in millis
            setTimeout(requestContext, 10000);
            // Endpoint URL
            requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceURL);
            requestContext.put(AuthenticationHandlerConstants.USER, username);
            requestContext.put(AuthenticationHandlerConstants.PWD, password);
            return client;
        } catch (Exception e) {
            LOG.error("Errore durante l'inizializzazione del bean. WS Sever offline?", e);
            return null;
        }
    }

    public static AllineamentoEnteConvenzionato allineamentoEnteConvenzionatoClient(String username, String password,
            String serviceURL) {
        try {
            LOG.info("Connessione al WS: " + serviceURL);
            if (aecService == null) {
                synchronized (IAMSoapClients.class) {
                    if (aecService == null) {
                        URL wsdlURL = new URL(serviceURL + "?wsdl");
                        aecService = new AllineamentoEnteConvenzionato_Service(wsdlURL);
                        aecService.setHandlerResolver(new SOAPClientLoginHandlerResolver());
                        LOG.info("Creato il client service per il WS: " + serviceURL);
                    }
                }
            }

            AllineamentoEnteConvenzionato client = aecService.getAllineamentoEnteConvenzionatoPort();
            Map<String, Object> requestContext = ((BindingProvider) client).getRequestContext();
            // Timeout in millis
            setTimeout(requestContext, 10000);
            // Endpoint URL
            requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceURL);
            requestContext.put(AuthenticationHandlerConstants.USER, username);
            requestContext.put(AuthenticationHandlerConstants.PWD, password);
            return client;
        } catch (Exception e) {
            LOG.error("Errore durante l'inizializzazione del bean. WS Sever offline?", e);
            return null;
        }
    }

    public static CalcoloServiziErogati calcoloServiziErogatiClient(String username, String password,
            String serviceURL) {
        try {
            LOG.info("Connessione al WS: " + serviceURL);
            if (cseService == null) {
                synchronized (IAMSoapClients.class) {
                    if (cseService == null) {
                        URL wsdlURL = new URL(serviceURL + "?wsdl");
                        cseService = new CalcoloServiziErogati_Service(wsdlURL);
                        cseService.setHandlerResolver(new SOAPClientLoginHandlerResolver());
                        LOG.info("Creato il client service per il WS: " + serviceURL);
                    }
                }
            }

            CalcoloServiziErogati client = cseService.getCalcoloServiziErogatiPort();
            Map<String, Object> requestContext = ((BindingProvider) client).getRequestContext();
            // Timeout in millis
            setTimeout(requestContext, 10000);
            // Endpoint URL
            requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceURL);
            requestContext.put(AuthenticationHandlerConstants.USER, username);
            requestContext.put(AuthenticationHandlerConstants.PWD, password);
            return client;
        } catch (Exception e) {
            LOG.error("Errore durante l'inizializzazione del bean. WS Sever offline?", e);
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
