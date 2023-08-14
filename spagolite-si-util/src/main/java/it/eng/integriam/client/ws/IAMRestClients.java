package it.eng.integriam.client.ws;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class IAMRestClients {

    private static final Logger log = LoggerFactory.getLogger(IAMRestClients.class);
    private static RestTemplate restTemplate = null;

    static {
        restTemplate = new RestTemplate();
    }

    private IAMRestClients() {
        throw new IllegalArgumentException("Impossibile istanziare la classe. Contiene solamente metodi statici.");
    }

    /**
     * Recupera il l'help on line per l'applicazione.
     *
     * @param serviceURL,
     *            url del servizio di helpe online
     * @param idApplic,
     *            id applicazione per cui fornire l'help online
     * @param tiHelpOnLine,
     *            tipologia help online
     * @param nmPaginaWeb.
     *            pagina web per cui cui richiedere l'help online
     * 
     * @return stringa contenente la serializzazione dell'help online.
     */
    public static String recuperoHelpClient(String serviceURL, long idApplic, String tiHelpOnLine, String nmPaginaWeb) {
        final String oggettoJson;
        URI targetUrl = UriComponentsBuilder.fromHttpUrl(serviceURL).queryParam("idApplic", idApplic)
                .queryParam("tiHelpOnLine", tiHelpOnLine).queryParam("nmPaginaWeb", nmPaginaWeb).build().toUri();
        try {
            oggettoJson = restTemplate.getForObject(targetUrl, String.class);
            return oggettoJson;
        } catch (RuntimeException ex) {
            log.error("Errore durante l'invocazione del WS Rest per l'Help Online.", ex);
            return null;
        }
    }

}
