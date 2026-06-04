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

package it.eng.spagoLite.spring;

import it.eng.spagoLite.SessionManager;
import it.eng.spagoLite.security.IUser;
import it.eng.spagoLite.security.User;
import java.io.IOException;
import java.util.List;
import java.util.Date;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 *
 * @author Marco Iacolucci
 */
public abstract class CustomSaml2AuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory
            .getLogger(CustomSaml2AuthenticationSuccessHandler.class.getName());
    // Costanti attributi SAML valide per PUGLIA E VECCHIO IDP INTERNO PARER
    protected static final String USERID = "urn:oid:0.9.2342.19200300.100.1.1";
    protected static final String COGNOME = "urn:oid:2.5.4.4";
    protected static final String NOME = "urn:oid:2.5.4.42";
    protected static final String USERNAME = "urn:oid:1.3.6.1.4.1.5923.1.1.1.6";
    protected static final String EMAIL = "email";

    // Nuove costanti per attributi SAML valide per SPID
    protected static final String SPID_CODE = "spidCode";
    // Nuove costanti per attributi specifici di RER per SPID
    protected static final String SPID_AUTHENTICATION_METHOD = "authenticationMethod";
    protected static final String SPID_NOME = "nome";
    protected static final String SPID_COGNOME = "cognome";
    protected static final String SPID_CODICE_FISCALE = "CodiceFiscale";
    protected static final String SPID_EMAIL = "emailAddressPersonale";
    protected static final String SPID_AUTHENTICATION_METHOD_ENABLED_1 = "Primo Livello SPID";
    protected static final String SPID_AUTHENTICATION_METHOD_ENABLED_2 = "Secondo Livello SPID";
    protected static final String SPID_AUTHENTICATION_METHOD_ENABLED_3 = "Terzo Livello SPID";
    // Nuove costanti per attributi specifici di PUGLIA per SPID
    protected static final String SPID_PUGLIA_NOME = "nome";
    protected static final String SPID_PUGLIA_COGNOME = "familyName";
    protected static final String SPID_PUGLIA_CODICE_FISCALE = "fiscalNumber";
    protected static final String SPID_PUGLIA_EMAIL = "email"; // DA VERIFICARE !!! Inserito
    // Costanti per l'accesso tramite CIE -LEPIDA
    protected static final String CIE_CODICE_FISCALE_FEDERA = "CodiceFiscale";
    protected static final String CIE_NOME_FEDERA = "nome";
    protected static final String CIE_COGNOME_FEDERA = "cognome";
    // Costanti per l'accesso tramite CIE -PUGLIA
    protected static final String CIE_CODICE_FISCALE_PUGLIA = "fiscalNumber";
    protected static final String CIE_NOME_PUGLIA = "name";
    protected static final String CIE_COGNOME_PUGLIA = "familyName";
    // EXTERNAL_ID fisso per la CIE
    protected static final String CIE_EXTERNAL_ID = "CIE_ID";

    // leggendo il documento di
    // specifiche di integrazione SPID Puglia

    // Messaggi utilizzabili nelle classi derivate nella funziona
    // verificaEsistenzaUtente()
    protected static final String MSG_TROPPE_OCCORRENZE_UTENTE_SPID = "Gentile %s %s, non è autorizzato ad accedere ai nostri sistemi tramite utente SPID con codice fiscale %s, sono state rilevate piï¿½ occorrenze nel database";
    protected static final String MSG_TROPPE_OCCORRENZE_UTENTE_CIE = "Gentile %s %s, non è autorizzato ad accedere ai nostri sistemi tramite utente CIE con codice fiscale %s, sono state rilevate piï¿½ occorrenze nel database";
    protected static final String MSG_UTENTE_NON_AUTORIZZATO_SPID = "Gentile %s %s, non è autorizzato ad accedere ai nostri sistemi tramite utente SPID con codice fiscale %s";
    protected static final String MSG_UTENTE_NON_AUTORIZZATO_CIE = "Gentile %s %s, non è autorizzato ad accedere ai nostri sistemi tramite utente CIE con codice fiscale %s";
    protected static final String MSG_UTENTE_LIVELLO_AUTH_INADEGUATO = "Gentile %s %s, non è autorizzato ad accedere ai nostri sistemi con codice fiscale %s e livello di autorizzazione %s";
    protected static final String MSG_UTENTE_SPID_NON_VALIDO = "Gentile %s %s, non è autorizzato ad accedere ai nostri sistemi con questo utente SPID senza codice fiscale";
    protected static final String MSG_UTENTE_CIE_NON_VALIDO = "Gentile %s %s, non è autorizzato ad accedere ai nostri sistemi con questo utente CIE senza codice fiscale";
    protected static final String MSG_UTENTE_AGGIUNTIVO_RER = ", contatti helpdeskparer@regione.emilia-romagna.it per accedere.";
    // Inizializza la proprietà  di sistema abilita-livello-1-spid sull AppServer
    protected static boolean abilitaLivello1Spid = System
            .getProperty("abilita-livello-1-spid", "false").equals("true");

    private static final String STR_CARICATO = "Caricato attributo {} con valore {}";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        Saml2Authentication saml2Auth = (Saml2Authentication) authentication;
        Saml2AuthenticatedPrincipal principal = (Saml2AuthenticatedPrincipal) saml2Auth
                .getPrincipal();
        // Estrai gli attributi SAML
        Map<String, List<Object>> attributes = principal.getAttributes();
        log.debug("--CONTEXT--{}", request.getContextPath());
        // Logga tutti gli attributi SAML
        logInfoUtenteSAML(principal.getAttributes());
        User u = new User();
        if (verificaSeUtenteSpidValido(principal, u)) {
            /**
             * SI TRATTA DI UN UTENTE SPID VALIDO! Ed ho già  determinato se RER o PUGLIA
             */
            List<UtenteDb> l = null;
            if (u.getUserType().equals(IUser.UserType.SPID_PUGLIA)) {
                l = findUtentiPerUsernameCaseInsensitive(u.getCodiceFiscale());
            } else {
                l = findUtentiPerCodiceFiscale(u.getCodiceFiscale());
            }
            if (l.size() > 1) {
                String msg = String.format(getMessaggioUtente(MSG_TROPPE_OCCORRENZE_UTENTE_SPID, u),
                        u.getNome(), u.getCognome(), u.getCodiceFiscale());
                log.warn(msg);
                throw new UsernameNotFoundException(msg);
            } else if (l.isEmpty()) {
                /* Si tratta di utente SPID senza utenza parer su DB */
                String msg = String.format(getMessaggioUtente(MSG_UTENTE_NON_AUTORIZZATO_SPID, u),
                        u.getNome(), u.getCognome(), u.getCodiceFiscale());
                log.warn(msg);
                u.setUtenteDaAssociare(true); // nella prossima action si verÃ  rediretti su IAM !
            } else {
                /*
                 * L'utente esiste sul db locale PARER recupero l'id dell'utente e lo setto
                 * nell'oggetto utente e lo metto in sessione. Modifica fatta perchÃ© idp generici
                 * non conoscono l'id dell'utente del db di iam.
                 */
                UtenteDb ut = l.iterator().next();
                u.setUsername(ut.getUsername());
                u.setIdUtente(ut.getId());
            }
        } else if (verificaSeUtenteCieValido(principal, u)) {
            // SI TRATTA DI UN UTENTE CIE VALIDO!
            List<UtenteDb> l = findUtentiPerCodiceFiscale(u.getCodiceFiscale());
            if (l.size() > 1) {
                String msg = String.format(getMessaggioUtente(MSG_TROPPE_OCCORRENZE_UTENTE_CIE, u),
                        u.getNome(), u.getCognome(), u.getCodiceFiscale());
                log.warn(msg);
                throw new UsernameNotFoundException(msg);
            } else if (l.isEmpty()) {
                /* Si tratta di utente SPID senza utenza parer su DB */
                String msg = String.format(getMessaggioUtente(MSG_UTENTE_NON_AUTORIZZATO_CIE, u),
                        u.getNome(), u.getCognome(), u.getCodiceFiscale());
                log.warn(msg);
                u.setUtenteDaAssociare(true); // nella prossima action si verrà  rediretti su IAM !
            } else {
                /*
                 * L'utente esiste sul db locale PARER recupero l'id dell'utente e lo setto
                 * nell'oggetto utente e lo metto in sessione. Modifica fatta perchÃ© idp generici
                 * non conoscono l'id dell'utente del db di iam.
                 */
                UtenteDb ut = l.iterator().next();
                u.setUsername(ut.getUsername());
                u.setIdUtente(ut.getId());
            }
        } else {
            /**
             * * UTENTE IDP PARER O PUGLIA, prende gli attributi classici dell'idp normale **
             */
            // Inizia a riempire gli attributi SAML di interesse ricevuti
            List<Object> att = attributes.get(USERID);
            if (att != null) {
                String valore = att.get(0).toString();
                if (NumberUtils.isCreatable(valore)) {
                    u.setIdUtente(Long.parseLong(att.get(0).toString()));
                } else {
                    log.info("Nell'attributo SAML [{}] non è presente un dato numerico", USERID);
                    u.setIdUtente(0);
                }
                log.info(STR_CARICATO, USERID, u.getIdUtente());
            }
            att = attributes.get(USERNAME);
            if (att != null) {
                u.setUsername(att.get(0).toString());
                log.info(STR_CARICATO, USERNAME, u.getUsername());
            }
            att = attributes.get(COGNOME);
            if (att != null) {
                u.setCognome(att.get(0).toString());
                log.info(STR_CARICATO, COGNOME, u.getCognome());
            }
            att = attributes.get(NOME);
            if (att != null) {
                u.setNome(att.get(0).toString());
                log.info(STR_CARICATO, NOME, u.getNome());
            }
            att = attributes.get(EMAIL);
            if (att != null) {
                u.setEmail(att.get(0).toString());
                log.info(STR_CARICATO, EMAIL, u.getEmail());
            } else {
                u.setEmail("");
            }
            UtenteDb uDb = getUtentePerUsername(u.getUsername());
            /*
             * nel caso di DPI il metodo non ï¿½ ridefinito e la superclasse torna un null che va
             * controllato!
             */
            if (uDb != null) {
                u.setIdUtente(uDb.getId());
                u.setScadenzaPwd(uDb.getDataScadenzaPassword());
                u.setCodiceFiscale(uDb.getCodiceFiscale());
            }
        }
        log.info("Username: {}, id: {}, Cognome. {}, Nome: {}, email: {}", u.getUsername(),
                u.getIdUtente(), u.getCognome(), u.getNome(), u.getEmail());
        SessionManager.setUser(request.getSession(), u);
        saml2Auth.setDetails(u);
        // REDIRIGE per esempio su /verso
        response.sendRedirect(getSendRedirectForLogin(request));
    }

    /*
     * Restituisce l'url di reindirizzamento per il login. Da ridefinire nelle classi derivate
     * qualora il default non andasse bene.
     */
    protected String getSendRedirectForLogin(HttpServletRequest request) {
        return request.getContextPath() + "/Login.html";
    }

    /*
     * Determina se un utente ï¿½ un utente SPID oppure un utente proveniente da un classico IDP
     * normale Inoltre verifica che se si tratta di un utente RER o PUGLIA e controlla che esista il
     * codice fiscale. Se esiste il codice fiscale in caso di RER verifica i livelli di
     * autenticazione 1 o 2 e solleva un'eccezione opportuna nel caso in cui non sia autorizzata.
     * Nel caso di Puglia...
     */
    protected boolean verificaSeUtenteSpidValido(Saml2AuthenticatedPrincipal principal, User u)
            throws UsernameNotFoundException {
        Map<String, List<Object>> attributes = principal.getAttributes();
        if (attributes.get(SPID_CODE) != null) {
            // SE PROVIENE DA UN IDP SPID...
            String codiceFiscaleSpid = getAttribute(attributes, SPID_CODICE_FISCALE);
            String codiceFiscaleSpidPuglia = getAttribute(attributes, SPID_PUGLIA_CODICE_FISCALE);
            String emailSpid = getAttribute(attributes, SPID_EMAIL);
            String emailSpidPuglia = getAttribute(attributes, SPID_PUGLIA_EMAIL);
            if (codiceFiscaleSpid != null) {
                // RAMO RER
                // MAC#26002 - Correzione gestione proprieta di sistema relativa al livello di
                // autenticazione spid
                String metodoAutenticazioneSpid = getAttribute(attributes,
                        SPID_AUTHENTICATION_METHOD);
                u.setCognome(getAttribute(attributes, SPID_COGNOME));
                u.setNome(getAttribute(attributes, SPID_NOME));
                u.setCodiceFiscale(codiceFiscaleSpid);
                u.setEmail(emailSpid);
                u.setUserType(IUser.UserType.SPID_FEDERA);
                /*
                 * Se ï¿½ stato abilitato il livello uno allora autorizza uno dei tre metodi di
                 * autenticazione 1-2-3 oppure se non ï¿½ abilitato il livello uno permette solo i
                 * livelli 2-3
                 */
                if (metodoAutenticazioneSpid != null
                        && ((isAbilitaLivello1Spid() && (metodoAutenticazioneSpid
                                .equalsIgnoreCase(SPID_AUTHENTICATION_METHOD_ENABLED_1)
                                || metodoAutenticazioneSpid
                                        .equalsIgnoreCase(SPID_AUTHENTICATION_METHOD_ENABLED_2)
                                || metodoAutenticazioneSpid
                                        .equalsIgnoreCase(SPID_AUTHENTICATION_METHOD_ENABLED_3)))
                                || (!isAbilitaLivello1Spid() && (metodoAutenticazioneSpid
                                        .equalsIgnoreCase(SPID_AUTHENTICATION_METHOD_ENABLED_2)
                                        || metodoAutenticazioneSpid.equalsIgnoreCase(
                                                SPID_AUTHENTICATION_METHOD_ENABLED_3))))) {
                    // va avanti
                } else {
                    /*
                     * Se l'utente non ha scelto secondo livello SPID ma un'altro metodo di
                     * autenticazione (federa passa il codice fiscale) Il sistema impedisce di
                     * entrare!
                     */
                    String msg = String.format(
                            getMessaggioUtente(MSG_UTENTE_LIVELLO_AUTH_INADEGUATO, u), u.getNome(),
                            u.getCognome(), codiceFiscaleSpid, metodoAutenticazioneSpid);
                    log.warn(msg);
                    throw new UsernameNotFoundException(String.format(msg));
                }
            } else if (codiceFiscaleSpidPuglia != null) {
                // RAMO PUGLIA, toglie il prefizzo di PUGLIA "TINIT-" dal codice fiscale
                u.setCodiceFiscale(codiceFiscaleSpidPuglia.substring(6));
                u.setCognome(getAttribute(attributes, SPID_PUGLIA_COGNOME));
                u.setNome(getAttribute(attributes, SPID_PUGLIA_NOME));
                u.setEmail(emailSpidPuglia);
                u.setUserType(IUser.UserType.SPID_PUGLIA);
            } else { // Interrompe il flusso con un'eccezione!
                String msg = String.format(getMessaggioUtente(MSG_UTENTE_SPID_NON_VALIDO, u),
                        u.getNome(), u.getCognome());
                log.warn(msg);
                throw new UsernameNotFoundException(msg);
            }
            u.setExternalId(getAttribute(attributes, SPID_CODE));
            // USERNAME E ID UTENTE NON CI SONO!!
            return true;
        } else {
            return false;
        }
    }

    /*
     * Determina se un utente ï¿½ un utente CIE oppure un utente proveniente da un classico IDP
     * normale. Inoltre controlla che esista il codice fiscale.
     */
    protected boolean verificaSeUtenteCieValido(Saml2AuthenticatedPrincipal principal, User u)
            throws UsernameNotFoundException {
        Map<String, List<Object>> attributes = principal.getAttributes();
        /*
         * per determinare se si tratta di un utente CIE si verifica che NON sia presente lo
         * SPID_CODE che sia presente il codice fiscale e che NON essta un attributo tipico dell'IDP
         * interno ovvero USERNAME. Tutto questo perché non è presente lo spidCode in caso di CIE.
         */

        log.info("spidCode:{}, CF Federa:{}, CF Puglia:{}, username:{} ",
                getAttribute(attributes, SPID_CODE),
                getAttribute(attributes, CIE_CODICE_FISCALE_FEDERA),
                getAttribute(attributes, CIE_CODICE_FISCALE_PUGLIA),
                getAttribute(attributes, USERNAME));
        String codiceFiscaleLepida = getAttribute(attributes, CIE_CODICE_FISCALE_FEDERA);
        String codiceFiscalePuglia = getAttribute(attributes, CIE_CODICE_FISCALE_PUGLIA);
        if (getAttribute(attributes, SPID_CODE) == null &&
                (codiceFiscaleLepida != null || codiceFiscalePuglia != null) &&
                getAttribute(attributes, USERNAME) == null) {
            // SE PROVIENE DA UN IDP CIE...
            log.info("--- IDP CIE ---");
            if (codiceFiscaleLepida != null) {
                u.setCognome(getAttribute(attributes, CIE_COGNOME_FEDERA));
                u.setNome(getAttribute(attributes, CIE_NOME_FEDERA));
                u.setCodiceFiscale(codiceFiscaleLepida);
                u.setUserType(IUser.UserType.CIE_FEDERA);
            } else if (codiceFiscalePuglia != null) {
                u.setCognome(getAttribute(attributes, CIE_COGNOME_PUGLIA));
                u.setNome(getAttribute(attributes, CIE_NOME_PUGLIA));
                u.setCodiceFiscale(codiceFiscalePuglia.substring(6)); // TOGLIE IL "TINIT-" iniziale
                                                                      // che CIE mette davanti al CF
                u.setUserType(IUser.UserType.CIE_PUGLIA);
            } else { // Interrompe il flusso con un'eccezione!
                String msg = String.format(getMessaggioUtente(MSG_UTENTE_CIE_NON_VALIDO, u),
                        u.getNome(), u.getCognome());
                log.warn(msg);
                throw new UsernameNotFoundException(msg);
            }
            u.setExternalId(CIE_EXTERNAL_ID);
            // USERNAME E ID UTENTE NON CI SONO!!
            return true;
        } else {
            log.info("--- NON e' CIE !!!---");
            return false;
        }
    }

    protected String getAttribute(Map<String, List<Object>> attributes, String nomeAttributo) {
        List<Object> att = attributes.get(nomeAttributo);
        if (att != null) {
            return att.get(0).toString();
        } else {
            return null;
        }
    }

    protected static boolean isAbilitaLivello1Spid() {
        return abilitaLivello1Spid;
    }

    /*
     * Restituisce un messaggio con accodato l'indirizzo mail RER a cui scrivere in caso di utente
     * SPID_FEDERA
     */
    protected String getMessaggioUtente(String mes, User u) {
        if (u.getUserType() != null && u.getUserType().equals(IUser.UserType.SPID_FEDERA)) {
            return mes + MSG_UTENTE_AGGIUNTIVO_RER;
        } else {
            return mes + ".";
        }
    }

    /*
     * Classe Utente da utilizzare nelle classi derivate per restituire occorrenze di utenti dal DB
     * locale
     */
    protected class UtenteDb {

        private String username;
        private long id;
        private Date dataScadenzaPassword;
        private String codiceFiscale;

        public UtenteDb() {
            //
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public Date getDataScadenzaPassword() {
            return dataScadenzaPassword;
        }

        public void setDataScadenzaPassword(Date dataScadenzaPassword) {
            this.dataScadenzaPassword = dataScadenzaPassword;
        }

        public String getCodiceFiscale() {
            return codiceFiscale;
        }

        public void setCodiceFiscale(String codiceFiscale) {
            this.codiceFiscale = codiceFiscale;
        }

    }

    protected void logInfoUtenteSAML(Map<String, List<Object>> attributes) {
        logUtenteSAML(attributes, false);
    }

    protected void logDebugUtenteSAML(Map<String, List<Object>> attributes) {
        logUtenteSAML(attributes, true);
    }

    protected void logUtenteSAML(Map<String, List<Object>> attributes, boolean isDebug) {
        // Logga tutti gli attributi dell'utente SPID
        StringBuilder str = new StringBuilder();
        for (Map.Entry<String, List<Object>> entry : attributes.entrySet()) {
            String key = entry.getKey();
            List<Object> value = entry.getValue();
            str.append(key).append(": ");
            str.append(value);
            str.append(" ");
        }

        if (isDebug) {
            log.debug("Utente SAML loggato con i seguenti attributi: {}", str);
        } else {
            log.info("Utente SAML loggato con i seguenti attributi: {}", str);
        }
    }

    /*
     * Da ridefinire nelle classi derivate per cercare tutte le occorrenze di utenti nel db locale
     * con lo stesso codice fiscale.
     */
    protected abstract List<UtenteDb> findUtentiPerCodiceFiscale(String codiceFiscale);

    /*
     * Da ridefinire nelle classi derivate per cercare tutte le occorrenze di utenti nel db locale
     * con lo stesso codice fiscale ignorando il case dei caratteri.
     */
    protected abstract List<UtenteDb> findUtentiPerUsernameCaseInsensitive(String username);

    /*
     * Da ridefinire nelle classi derivate per cercare L'utente nel DB locale con un determinato
     * username.
     */
    protected abstract UtenteDb getUtentePerUsername(String username);

}
