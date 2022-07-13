package it.eng.spagoLite.actions.security;

import javax.annotation.Resource;
import javax.xml.ws.soap.SOAPFaultException;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.SessionManager;
import it.eng.spagoLite.actions.ActionBase;
import it.eng.spagoLite.security.IUser;
import it.eng.spagoLite.security.User;
import it.eng.spagoLite.security.auth.Authenticator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Quaranta_M
 *
 *         1 - process() 2 - passIn() 3 - doLogin()
 *
 */
public class LoginAction extends ActionBase {

    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);

    @Resource
    private Authenticator authenticator;

    @Override
    public String getControllerName() {
        return "Login.html";
    }

    @Override
    protected String getDefaultPublsherName() {
        return "/home";
    }

    @Override
    public void process() throws EMFError {
        User utente = (User) SessionManager.getUser(getSession());
        if (utente != null) {
            logger.info("Login già effettuato per l'utente " + utente.getUsername());
            if (!getRequest().getServletPath().startsWith("/detail")) {
                redirectToAction("SceltaOrganizzazione.html?clearhistory=true");
            }
        } else {
            try {
                utente = authenticator.doLogin(getSession());
                if (utente != null && !getRequest().getServletPath().startsWith("/detail")) {
                    redirectToAction("SceltaOrganizzazione.html?clearhistory=true");
                } else if (utente == null) {
                    forwardToPublisher("/login/notAuthorized");
                }
            } catch (SOAPFaultException ex) {
                getMessageBox().addFatal(ex.getMessage());
            }
        }
    }

    public void logout() {
        IUser utente = SessionManager.currentUserDetails();
        if (utente != null) {
            getMessageBox().addInfo("L'utente " + utente.getUsername() + " ha richiesto il logout");
            if (utente.getUserType() != null && utente.getUserType().equals(IUser.UserType.SPID_FEDERA)) {
                logger.debug("Richiesto SAML LOCAL LOGOUT per l'utente " + IUser.UserType.SPID_FEDERA.name());
                redirectToAction("saml/logout?local=true");
            } else {
                logger.debug("Richiesto SAML GLOBAL LOGOUT");
                redirectToAction("saml/logout/");
            }
        }
    }

    @Override
    protected boolean isAuthorized(String destination) {
        return true;
    }

    @Override
    public void reloadAfterGoBack(String publisherName) {
    }
}
