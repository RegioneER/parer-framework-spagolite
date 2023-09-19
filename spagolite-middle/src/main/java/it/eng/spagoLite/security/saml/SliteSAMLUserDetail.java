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

package it.eng.spagoLite.security.saml;

import it.eng.spagoLite.security.IUser;
import java.util.ArrayList;
import java.util.List;

import it.eng.spagoLite.security.User;
import java.util.Date;
import org.apache.commons.lang3.math.NumberUtils;

import org.opensaml.saml2.core.Attribute;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

public class SliteSAMLUserDetail implements SAMLUserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(SliteSAMLUserDetail.class.getName());
    // Costanti attributi SAML valide per PUGLIA E VECCHIO IDP INTERNO PARER
    protected static final String USERID = "urn:oid:0.9.2342.19200300.100.1.1";
    protected static final String COGNOME = "urn:oid:2.5.4.4";
    protected static final String NOME = "urn:oid:2.5.4.42";
    protected static final String USERNAME = "urn:oid:1.3.6.1.4.1.5923.1.1.1.6";
    // private static final String LISTA_APPLICAZIONI = "urn:oid:2.9.3.2.7.24";

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
    protected static final String SPID_PUGLIA_EMAIL = "email"; // DA VERIFICARE !!! INserito leggendo il documento di
                                                               // specifiche di integrazione SPID Puglia

    // Messaggi utilizzabili nelle classi derivate nella funziona verificaEsistenzaUtente()
    protected static final String MSG_TROPPE_OCCORRENZE_UTENTE = "Gentile %s %s, non è autorizzato ad accedere ai nostri sistemi tramite utente SPID con codice fiscale %s, sono state rilevate più occorrenze nel database";
    protected static final String MSG_UTENTE_NON_AUTORIZZATO = "Gentile %s %s, non è autorizzato ad accedere ai nostri sistemi tramite utente SPID con codice fiscale %s";
    protected static final String MSG_UTENTE_LIVELLO_AUTH_INADEGUATO = "Gentile %s %s, non è autorizzato ad accedere ai nostri sistemi con codice fiscale %s e livello di autorizzazione %s";
    protected static final String MSG_UTENTE_SPID_NON_VALIDO = "Gentile %s %s, non è autorizzato ad accedere ai nostri sistemi con questo utente SPID senza codice fiscale";
    protected static final String MSG_UTENTE_AGGIUNTIVO_RER = ", contatti helpdeskparer@regione.emilia-romagna.it per accedere.";
    // Inizializza la proprietà  di sistema abilita-livello-1-spid sull AppServer
    protected static boolean abilitaLivello1Spid = System.getProperty("abilita-livello-1-spid", "false").equals("true");

    @Override
    public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {

        User u = new User();
        // String codiceFiscaleSpid = getValue(credential, SPID_CODICE_FISCALE);
        if (verificaSeUtenteSpidValido(credential, u)) {
            /**
             * * SI TRATTA DI UN UTENTE SPID VALIDO! Ed ho già determinato se RER o PUGLIA *
             */
            List<UtenteDb> l = null;
            if (u.getUserType().equals(IUser.UserType.SPID_PUGLIA)) {
                l = findUtentiPerUsernameCaseInsensitive(u.getCodiceFiscale());
            } else {
                l = findUtentiPerCodiceFiscale(u.getCodiceFiscale());
            }
            if (l.size() > 1) {
                String msg = String.format(getMessaggioUtente(MSG_TROPPE_OCCORRENZE_UTENTE, u), u.getNome(),
                        u.getCognome(), u.getCodiceFiscale());
                logger.warn(msg);
                throw new UsernameNotFoundException(msg);
            } else if (l.size() == 0) {
                /* Si tratta di utente SPID senza utenza parer su DB */
                String msg = String.format(getMessaggioUtente(MSG_UTENTE_NON_AUTORIZZATO, u), u.getNome(),
                        u.getCognome(), u.getCodiceFiscale());
                logger.warn(msg);
                u.setUtenteDaAssociare(true); // nella prossima action si verrÃ  rediretti su IAM !
            } else {
                /*
                 * L'utente esiste sul db locale PARER recupero l'id dell'utente e lo setto nell'oggetto utente e lo
                 * metto in sessione. Modifica fatta perchè idp generici non conoscono l'id dell'utente del db di iam.
                 */
                UtenteDb ut = l.iterator().next();
                u.setUsername(ut.getUsername());
                u.setIdUtente(ut.getId());
                // Logga tutti gli attributi dell'utente SPID
                logInfoUtenteSAML(credential);
            }
        } else {
            /**
             * * UTENTE IDP PARER O PUGLIA, prende gli attributi classici dell'idp normale **
             */
            u.setCognome(getValue(credential, COGNOME));
            u.setNome(getValue(credential, NOME));
            // Al PARER l'ID_UTENTE è quello vero del DB, in PUGLIA è un altro ID che non si sa (forse il codice
            // fiscale)
            String str = getValue(credential, USERID);
            if (NumberUtils.isCreatable(str)) {
                u.setIdUtente(Integer.parseInt(getValue(credential, USERID)));
            } else {
                logger.info("Nell'attributo SAML [{}] non è presente un dato numerico", USERID);
                u.setIdUtente(0);
            }
            u.setUsername(getValue(credential, USERNAME));
            // Aggiunge all'utente arrivato da SAML i dati provenienti dal DB
            UtenteDb uDb = getUtentePerUsername(u.getUsername());
            /* nel caso di DPI il metodo non è ridefinito e la superclasse torna un null che va controllato! */
            if (uDb != null) {
                u.setIdUtente(uDb.getId());
                u.setScadenzaPwd(uDb.getDataScadenzaPassword());
                u.setCodiceFiscale(uDb.getCodiceFiscale());
            }
        }
        String messaggioDiLog = "L'utente " + u.getUsername() + " ha effettuato un login";
        if (getValue(credential, SPID_CODE) != null) {
            messaggioDiLog += " con SPID_CODE: " + getValue(credential, SPID_CODE) + " e CF: " + u.getCodiceFiscale()
                    + " [" + u.getUserType().name() + "]";
        }
        if (getValue(credential, SPID_AUTHENTICATION_METHOD) != null) {
            messaggioDiLog += " abilita-livello-1-spid=" + isAbilitaLivello1Spid();
        }
        logger.info(messaggioDiLog);
        return u;
    }

    protected String getValue(SAMLCredential credential, String key) {
        // Attribute accountID = credential.getAttributeByName(key);
        Attribute accountID = credential.getAttribute(key);
        if (accountID == null || accountID.getAttributeValues() == null || accountID.getAttributeValues().size() == 0) {
            return null;
        }
        XSString valore = (XSString) accountID.getAttributeValues().iterator().next();
        // XMLObject attributeValue = accountID.getAttributeValues().iterator().next();
        // return attributeValue.getDOM().getTextContent();
        return valore.getValue();
    }

    protected List<String> getValues(SAMLCredential credential, String key) {
        List<String> values = new ArrayList<String>();
        // Attribute accountID = credential.getAttributeByName(key);
        Attribute accountID = credential.getAttribute(key);
        if (accountID == null || accountID.getAttributeValues() == null || accountID.getAttributeValues().size() == 0) {
            return null;
        }
        for (XMLObject attributeValue : accountID.getAttributeValues()) {
            // values.add(attributeValue.getDOM().getTextContent());
            XSString valore = (XSString) attributeValue;
            values.add(valore.getValue());
        }
        return values;
    }

    protected void logUtenteSAML(SAMLCredential credential, boolean isDebug) {
        // Logga tutti gli attributi dell'utente SPID
        List<org.opensaml.saml2.core.Attribute> att = credential.getAttributes();
        StringBuilder str = new StringBuilder();
        for (Attribute attribute : att) {
            // attribute.getFriendlyName();
            str.append(attribute.getName()).append(": ");
            if (attribute.getAttributeValues() != null && attribute.getAttributeValues().size() > 0) {
                XSString valore = (XSString) attribute.getAttributeValues().iterator().next();
                str.append(valore.getValue());
            }
            str.append(" ");
        }
        if (isDebug) {
            logger.debug("Utente SAML loggato con i seguenti attributi: " + str.toString());
        } else {
            logger.info("Utente SAML loggato con i seguenti attributi: " + str.toString());
        }
    }

    protected void logInfoUtenteSAML(SAMLCredential credential) {
        logUtenteSAML(credential, false);
    }

    protected void logDebugUtenteSAML(SAMLCredential credential) {
        logUtenteSAML(credential, true);
    }

    protected static boolean isAbilitaLivello1Spid() {
        return abilitaLivello1Spid;
    }

    /*
     * Determina se un utente è un utente SPID oppure un utente proveniente da un classico IDP normale Inoltre verifica
     * che se si tratta di un utente RER o PUGLIA e controlla che esista il codice fiscale. Se esiste il codice fiscale
     * in caso di RER verifica i livelli di autenticazione 1 o 2 e solleva un'eccezione opportuna nel caso in cui non
     * sia autorizzata. Nel caso di Puglia...
     */
    protected boolean verificaSeUtenteSpidValido(SAMLCredential credential, User u) throws UsernameNotFoundException {
        if (getValue(credential, SPID_CODE) != null) {
            // SE PROVIENE DA UN IDP SPID...
            String codiceFiscaleSpid = getValue(credential, SPID_CODICE_FISCALE);
            String codiceFiscaleSpidPuglia = getValue(credential, SPID_PUGLIA_CODICE_FISCALE);
            String emailSpid = getValue(credential, SPID_EMAIL);
            String emailSpidPuglia = getValue(credential, SPID_PUGLIA_EMAIL);
            if (codiceFiscaleSpid != null) {
                // RAMO RER
                // MAC#26002 - Correzione gestione proprieta di sistema relativa al livello di autenticazione spid
                String metodoAutenticazioneSpid = getValue(credential, SPID_AUTHENTICATION_METHOD);
                u.setCognome(getValue(credential, SPID_COGNOME));
                u.setNome(getValue(credential, SPID_NOME));
                u.setCodiceFiscale(codiceFiscaleSpid);
                u.setEmail(emailSpid);
                u.setUserType(User.UserType.SPID_FEDERA);
                /*
                 * Se è stato abilitato il livello uno allora autorizza uno dei tre metodi di autenticazione 1-2-3
                 * oppure se non è abilitato il livello uno permette solo i livelli 2-3
                 */
                if (metodoAutenticazioneSpid != null && ((isAbilitaLivello1Spid()
                        && (metodoAutenticazioneSpid.equalsIgnoreCase(SPID_AUTHENTICATION_METHOD_ENABLED_1)
                                || metodoAutenticazioneSpid.equalsIgnoreCase(SPID_AUTHENTICATION_METHOD_ENABLED_2)
                                || metodoAutenticazioneSpid.equalsIgnoreCase(SPID_AUTHENTICATION_METHOD_ENABLED_3)))
                        || (!isAbilitaLivello1Spid() && (metodoAutenticazioneSpid
                                .equalsIgnoreCase(SPID_AUTHENTICATION_METHOD_ENABLED_2)
                                || metodoAutenticazioneSpid.equalsIgnoreCase(SPID_AUTHENTICATION_METHOD_ENABLED_3))))) {
                    // va avanti
                } else {
                    /*
                     * Se l'utente non ha scelto secondo livello SPID ma un'altro metodo di autenticazione (federa passa
                     * il codice fiscale) Il sistema impedisce di entrare!
                     */
                    String msg = String.format(getMessaggioUtente(MSG_UTENTE_LIVELLO_AUTH_INADEGUATO, u), u.getNome(),
                            u.getCognome(), codiceFiscaleSpid, metodoAutenticazioneSpid);
                    logger.warn(msg);
                    throw new UsernameNotFoundException(String.format(msg));
                }
            } else if (codiceFiscaleSpidPuglia != null) {
                // RAMO PUGLIA, toglie il prefizzo di PUGLIA "TINIT-" dal codice fiscale
                u.setCodiceFiscale(codiceFiscaleSpidPuglia.substring(6));
                u.setCognome(getValue(credential, SPID_PUGLIA_COGNOME));
                u.setNome(getValue(credential, SPID_PUGLIA_NOME));
                u.setEmail(emailSpidPuglia);
                u.setUserType(User.UserType.SPID_PUGLIA);
            } else { // Interrompe il flusso con un'eccezione!
                String msg = String.format(getMessaggioUtente(MSG_UTENTE_SPID_NON_VALIDO, u), u.getNome(),
                        u.getCognome());
                logger.warn(msg);
                throw new UsernameNotFoundException(msg);
            }
            u.setExternalId(getValue(credential, SPID_CODE));
            // USERNAME E ID UTENTE NON CI SONO!!
            return true;
        } else {
            return false;
        }
    }

    /* Restituisce u messaggio con accodato l'indirizzo mail RER a cui scrivere in caso di utente SPID_FEDERA */
    protected String getMessaggioUtente(String mes, User u) {
        if (u.getUserType() != null && u.getUserType().equals(User.UserType.SPID_FEDERA)) {
            return mes + MSG_UTENTE_AGGIUNTIVO_RER;
        } else {
            return mes + ".";
        }
    }

    /*
     * Da ridefinire nelle classi derivate per cercare tutte le occorrenze di utenti nel db locale con lo stesso codice
     * fiscale.
     */
    protected List<UtenteDb> findUtentiPerCodiceFiscale(String codiceFiscale) {
        return null;
    }

    /*
     * Da ridefinire nelle classi derivate per cercare tutte le occorrenze di utenti nel db locale con lo stesso codice
     * fiscale ignorando il case dei caratteri.
     */
    protected List<UtenteDb> findUtentiPerUsernameCaseInsensitive(String username) {
        return null;
    }

    /*
     * Da ridefinire nelle classi derivate per cercare L'utente nel DB locale con un determinato username.
     */
    protected UtenteDb getUtentePerUsername(String username) {
        return null;
    }

    /*
     * Classe Utente da utilizzare nelle classi derivate per restituire occorrenze di utenti dal DB locale
     */
    protected class UtenteDb {

        private String username;
        private long id;
        private Date dataScadenzaPassword;
        private String codiceFiscale;

        public UtenteDb() {
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
}
