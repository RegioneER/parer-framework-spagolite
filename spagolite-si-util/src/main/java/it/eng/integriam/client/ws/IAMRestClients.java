package it.eng.integriam.client.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class IAMRestClients {

    private static final Logger log = LoggerFactory.getLogger(IAMRestClients.class);
    private static RestTemplate restTemplate = null;

    static {
        restTemplate = new RestTemplate();
    }

    public static String recuperoHelpClient(String username, String password, String serviceURL, long idApplic,
            String tiHelpOnLine, String nmPaginaWeb) {
        // RestTemplate restTemplate=new RestTemplate();
        String oggettoJson = null;
        URI targetUrl = UriComponentsBuilder.fromHttpUrl(serviceURL)
                // .path("/saceriam/rest/recuperoHelp.html")
                .queryParam("idApplic", idApplic).queryParam("tiHelpOnLine", tiHelpOnLine)
                .queryParam("nmPaginaWeb", nmPaginaWeb).build().toUri();
        try {
            oggettoJson = restTemplate.getForObject(targetUrl, String.class);
            return oggettoJson;
        } catch (RuntimeException ex) {
            log.error("Errore durante l'invocazione del WS Rest per l'Help Online.", ex);
            return null;
        }
    }

}
