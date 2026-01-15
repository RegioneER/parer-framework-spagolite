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

package it.eng.spagoLite.security.auth;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.SubnetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import java.sql.Date;
import it.eng.spagoLite.security.exception.AuthWSException;

@SuppressWarnings({
        "rawtypes", "unchecked" })
public class WSLoginHandler {

    private static Logger log = LoggerFactory.getLogger(WSLoginHandler.class);

    private static final String LOGIN_FALLITO_MSG = "Username e/o password errate/a";
    private static final String LOGIN_FALLITO_NO_PASSWORD = "Valorizzare il campo password";
    private static final String PROBLEMA_ESTRAZIONE_APPLICAZIONE_MSG = "Problema nell'estrazione dei dati dell'applicazione";

    private static final String LOGIN_IP_FALLITO_MSG = "Indirizzo IP dell'utente che ha originato la richiesta non autorizzato";

    private static final String LOGIN_QUERY = " SELECT iu.cdPsw, iu.cdSalt, iu.flAttivo, iu.dtScadPsw, iu.flContrIp  FROM IamUser iu  WHERE iu.nmUserid = :username";
    private static final String LOGIN_QUERY_CERT_COMMON_NAME = " SELECT iu.cdPsw, iu.cdSalt, iu.flAttivo, iu.dtScadPsw, iu.flContrIp  FROM IamUser iu  WHERE iu.nmUserid = :username AND iu.tipoAuth='AUTH_CERT'";
    private static final String IAM_LOGIN_QUERY = "SELECT iu.cdPsw, iu.cdSalt, iu.flAttivo, iu.dtScadPsw, iu.flContrIp  FROM UsrUser iu  WHERE iu.nmUserid = :username";
    private static final String AUTH_QUERY = " SELECT 1 FROM IamUser iu JOIN iu.iamAbilOrganizs iao JOIN iao.iamAutorServs ias  WHERE iu.nmUserid = :username "
            + " AND iao.idOrganizApplic = :idOrganiz AND ias.nmServizioWeb = :servizioWeb";
    private static final String AT_LEAST_ONE_AUTH_QUERY = " SELECT 1 FROM IamUser iu JOIN iu.iamAbilOrganizs iao JOIN iao.iamAutorServs ias WHERE iu.nmUserid = :username "
            + " AND ias.nmServizioWeb = :servizioWeb";
    private static final String IAM_AUTH_QUERY = " SELECT 1 FROM UsrUser iu JOIN iu.usrUsoUserApplics uuua JOIN uuua.usrUsoRuoloUserDefaults uurud "
            + " JOIN uurud.prfRuolo pr JOIN pr.prfUsoRuoloApplics pura JOIN pura.prfAutors autors JOIN autors.aplServizioWeb asw "
            + " WHERE iu.nmUserid = :username AND uuua.aplApplic.nmApplic = 'SACER_IAM' AND pura.aplApplic.nmApplic = 'SACER_IAM' "
            + " AND asw.nmServizioWeb = :servizioWeb";
    private static final String IP_LIST_QUERY = " SELECT iiiu.cdIndIpUser FROM IamIndIpUser iiiu WHERE iiiu.iamUser.nmUserid = :username";
    private static final String IAM_IP_LIST_QUERY = " SELECT uiiu.cdIndIpUser FROM UsrIndIpUser uiiu WHERE uiiu.usrUser.nmUserid = :username";

    private WSLoginHandler() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Controllo di autorizzazione di un utente, per un servizio in una organizzazione
     *
     * @param username    nome utente
     * @param idOrganiz   id dell'organizzazione
     * @param servizioWeb nome del servizio web
     * @param em          EntityManager
     *
     * @return true/false con verifica autorizzazione
     *
     * @throws AuthWSException eccezione lanciata se l'utente non è autorizzato
     */
    public static boolean checkAuthz(String username, Integer idOrganiz, String servizioWeb,
            EntityManager em) throws AuthWSException {
        Query q2 = em.createQuery(AUTH_QUERY);
        q2.setParameter("username", username);
        q2.setParameter("idOrganiz", new BigDecimal(idOrganiz));
        q2.setParameter("servizioWeb", servizioWeb);
        return doCheckAuthz(username, idOrganiz, servizioWeb, q2);
    }

    /**
     * Controllo di autenticazione per un utente tramite la password. TODO: aggiungere anche
     * l'autenticazione tramite IP se necessaria
     *
     * @param username  nome utente
     * @param password  password
     * @param ipAddress indirizzo ip
     * @param em        EntityManager
     *
     * @return true/false con verifica autorizzazione
     *
     * @throws AuthWSException eccezione lanciata se l'utente non è autorizzato
     */
    public static boolean login(String username, String password, String ipAddress,
            EntityManager em) throws AuthWSException {
        Query q1 = em.createQuery(LOGIN_QUERY);
        q1.setParameter("username", username);
        Query ipListQuery = em.createQuery(IP_LIST_QUERY);
        ipListQuery.setParameter("username", username);
        return doLogin(username, password, ipAddress, q1, ipListQuery, null, false);
    }

    public static boolean login(String username, String password, String ipAddress,
            EntityManager em, boolean accessToken) throws AuthWSException {
        Query q1 = em.createQuery(LOGIN_QUERY);
        q1.setParameter("username", username);
        Query ipListQuery = em.createQuery(IP_LIST_QUERY);
        ipListQuery.setParameter("username", username);
        return doLogin(username, password, ipAddress, q1, ipListQuery, null, accessToken);
    }

    /**
     * Controllo di autenticazione per un utente tramite il common name del certificato.
     *
     * @param certCommonName nome utente
     * @param em             EntityManager
     */
    public static boolean login(String certCommonName, EntityManager em) throws AuthWSException {
        Query q1 = em.createQuery(LOGIN_QUERY_CERT_COMMON_NAME);
        q1.setParameter("username", certCommonName);
        // Nel parametro username passa il common name che eventualmente viene usato
        // nel messaggio di errore nel caso l'utente non esistesse
        return doLogin(certCommonName, null, null, q1, null, certCommonName, false);
    }

    /**
     * Autenticazione e controllo di autorizzazioni ad eseguire un servizio per almeno
     * un'organizzazione
     *
     * @param username    nome utente
     * @param password    password
     * @param servizioWeb nome servizio
     * @param em          EntityManager
     * @param ipAddress   indirizzo ip
     *
     * @return true se l'utente ha inserito le credenziali corrette ed è autorizzato ad eseguire il
     *         servizioWeb per almeno un'organizzazione
     *
     * @throws AuthWSException eccezione lanciata se l'utente non è autorizzato
     */
    public static boolean loginAndCheckAuthzAtLeastOneOrganiz(String username, String password,
            String servizioWeb, String ipAddress, EntityManager em, boolean accessToken)
            throws AuthWSException {
        Query q1 = em.createQuery(LOGIN_QUERY);
        q1.setParameter("username", username);
        Query q2 = em.createQuery(AT_LEAST_ONE_AUTH_QUERY);
        q2.setParameter("username", username);
        q2.setParameter("servizioWeb", servizioWeb);
        Query ipListQuery = em.createQuery(IP_LIST_QUERY);
        ipListQuery.setParameter("username", username);
        return doLoginAndCheckAuthz(username, password, null, servizioWeb, ipAddress, q1, q2,
                ipListQuery, accessToken);
    }

    /**
     * Autenticazione e controllo di autorizzazioni ad eseguire un servizio di Sacer IAM
     *
     * @param username    nome utente
     * @param password    password
     * @param servizioWeb nome servizio
     * @param ipAddress   indirizzo ip
     * @param em          EntityManager
     *
     * @return true/false con verifica login su iam
     *
     * @throws AuthWSException eccezione lanciata se l'utente non è autorizzato
     */
    public static boolean loginAndCheckAuthzIAM(String username, String password,
            String servizioWeb, String ipAddress, EntityManager em, boolean accessToken)
            throws AuthWSException {
        Query q1 = em.createQuery(IAM_LOGIN_QUERY);
        q1.setParameter("username", username);
        Query q2 = em.createQuery(IAM_AUTH_QUERY);
        q2.setParameter("username", username);
        q2.setParameter("servizioWeb", servizioWeb);
        Query ipListQuery = em.createQuery(IAM_IP_LIST_QUERY);
        ipListQuery.setParameter("username", username);
        return doLoginAndCheckAuthz(username, password, null, servizioWeb, ipAddress, q1, q2,
                ipListQuery, accessToken);
    }

    private static boolean doCheckAuthz(String username, Integer idOrganiz, String servizioWeb,
            Query q2) throws AuthWSException {
        List result = q2.getResultList();
        if (result == null || result.isEmpty()) {
            String logMessage = "User " + username + " not authorized to execute " + servizioWeb;
            String exceptionMessage = "Utente " + username
                    + " non autorizzato ad eseguire il servizio " + servizioWeb;
            if (idOrganiz != null) {
                logMessage = logMessage + " on organization with ID " + idOrganiz;
                exceptionMessage = exceptionMessage + " all'interno dell'organizzazione con ID "
                        + idOrganiz;
            }
            log.warn(logMessage);
            throw new AuthWSException(AuthWSException.CodiceErrore.UTENTE_NON_AUTORIZZATO,
                    exceptionMessage);
        }
        return true;
    }

    private static boolean doLogin(String username, String password, String ipAddress, Query q1,
            Query ipListQuery, String commonName, boolean accessToken) throws AuthWSException {
        Object res[];
        try {
            res = (Object[]) q1.getSingleResult();
        } catch (NoResultException e) {
            throw new AuthWSException(AuthWSException.CodiceErrore.LOGIN_FALLITO,
                    LOGIN_FALLITO_MSG);
        }
        if (res != null) {
            /*
             * Se nel commonName non viene passato nulla si fanno tutti i vecchi controlli sulla
             * password ecc. altrimenti se il record esiste significa che l'utente entrato col
             * common name del certificato può entrare.
             */
            if (commonName == null) {
                String salt = (String) res[1];
                String dbPwd = (String) res[0];
                boolean userAttivo = ((String) res[2]).equals("1");
                Date expDate = (Date) res[3];
                boolean ipCheck = ((String) res[4]).equals("1");
                Date today = new Date();
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                if (!userAttivo) {
                    log.warn("Account suspended for user {}", username);

                    throw new AuthWSException(AuthWSException.CodiceErrore.UTENTE_NON_ATTIVO,
                            "Utente non attivo");
                }
                if (today.after(expDate)) {
                    log.warn("Account expired on {} for user {}", expDate, username);
                    throw new AuthWSException(AuthWSException.CodiceErrore.UTENTE_SCADUTO,
                            "Credenziali scadute in data " + formatter.format(expDate));
                }

                // in caso di autenticazione con AccessToken non viene controllata la password
                if (!accessToken) {
                    if (StringUtils.isNotBlank(password)) {
                        if (StringUtils.isEmpty(salt)) {
                            if (!PwdUtil.encodePassword(password).equals(dbPwd)) {
                                log.warn("Login failed for user: {}", username);
                                throw new AuthWSException(
                                        AuthWSException.CodiceErrore.LOGIN_FALLITO,
                                        LOGIN_FALLITO_MSG);
                            }
                        } else {
                            if (!PwdUtil.encodePBKDF2Password(PwdUtil.decodeUFT8Base64String(salt),
                                    password).equals(dbPwd)) {
                                log.warn("Login failed for user: {}", username);
                                throw new AuthWSException(
                                        AuthWSException.CodiceErrore.LOGIN_FALLITO,
                                        LOGIN_FALLITO_MSG);
                            }
                        }
                    } else {
                        log.warn("Login failed for user: {}", username);
                        throw new AuthWSException(AuthWSException.CodiceErrore.LOGIN_FALLITO,
                                LOGIN_FALLITO_NO_PASSWORD);
                    }
                }

                if (ipCheck) {
                    List<String> list = ipListQuery.getResultList();
                    for (String ipAddr : list) {
                        if (ipAddr.contains("/")) {
                            SubnetUtils utils = new SubnetUtils(ipAddr);
                            if (utils.getInfo().isInRange(ipAddress)) {
                                return true;
                            }
                        } else {
                            if (ipAddr.equals(ipAddress)) {
                                return true;
                            }
                        }
                    }
                    throw new AuthWSException(AuthWSException.CodiceErrore.LOGIN_FALLITO,
                            LOGIN_IP_FALLITO_MSG);
                }
            }
            return true;
        } else {
            log.warn("Login failed for user: {}", username);
            throw new AuthWSException(AuthWSException.CodiceErrore.LOGIN_FALLITO,
                    LOGIN_FALLITO_MSG);
        }
    }

    private static boolean doLoginAndCheckAuthz(String username, String password, Integer idOrganiz,
            String servizioWeb, String ipAddress, Query q1, Query q2, Query ipListQuery,
            boolean accessToken) throws AuthWSException {
        doLogin(username, password, ipAddress, q1, ipListQuery, null, accessToken);
        doCheckAuthz(username, idOrganiz, servizioWeb, q2);
        return true;

    }

    public static void throwSOAPFault(SOAPMessageContext msgCtx, AuthWSException e) {
        try {
            SOAPFault sfault = msgCtx.getMessage().getSOAPBody().addFault();
            sfault.setFaultCode(e.getCodiceErrore().name());
            sfault.setFaultString(e.getDescrizioneErrore());
            throw new SOAPFaultException(sfault);
        } catch (SOAPException e1) {
            throw new ProtocolException(e1);
        }

    }

    public static void throwSOAPFault(AuthWSException e) {
        try {
            SOAPFactory fac = SOAPFactory.newInstance();
            SOAPFault sfault = fac.createFault();
            sfault.setFaultCode(e.getCodiceErrore().name());
            sfault.setFaultString(e.getDescrizioneErrore());
            throw new SOAPFaultException(sfault);
        } catch (SOAPException e1) {
            throw new ProtocolException(e1);
        }
    }

    public static boolean appExists(String nmApplic, EntityManager em) throws AuthWSException {
        try {
            String queryStr = "SELECT 1 FROM AplApplic a WHERE a.nmApplic = :nmApplic";
            Query query = em.createQuery(queryStr.toString());
            query.setParameter("nmApplic", nmApplic);
            List list = query.getResultList();
            return !list.isEmpty();
        } catch (RuntimeException ex) {
            throw new AuthWSException(AuthWSException.CodiceErrore.PROBLEMA_ESTRAZIONE_APPLICAZIONE,
                    PROBLEMA_ESTRAZIONE_APPLICAZIONE_MSG);
        }
    }
}
