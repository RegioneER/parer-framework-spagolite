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
