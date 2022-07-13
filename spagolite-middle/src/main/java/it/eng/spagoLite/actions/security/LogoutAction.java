package it.eng.spagoLite.actions.security;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.actions.ActionBase;

public class LogoutAction extends ActionBase {
    // private static Logger logger = LoggerFactory.getLogger(LogoutAction.class.getName());

    @Override
    public String getControllerName() {
        return "Logout.html";
    }

    @Override
    public void process() throws EMFError {
        redirectToAction("Login.html?operation=logout");
    }

    @Override
    protected boolean isAuthorized(String destination) {
        return true;
    }

    @Override
    public void reloadAfterGoBack(String publisherName) {

    }

    public void success() {
        forwardToPublisher(getDefaultPublsherName());
    }

    /*
     * Intecettata la chiamata al logout locale che richiede eventualmente un SAML GLOBAL LOGOUT nel caso di utente con
     * login classico (IDP INTERNO, PUGLIA e tutti gli altri casi) altrimenti nel caso di IDP FEDERA SPID il Global
     * logout non è gestito e quindi fa solo un logout LOCALE
     */
    @Override
    protected String getDefaultPublsherName() {
        return "/login/logout";
    }
}
