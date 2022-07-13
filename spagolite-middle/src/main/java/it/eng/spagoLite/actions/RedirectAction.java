package it.eng.spagoLite.actions;

import it.eng.spagoCore.configuration.ConfigSingleton;
import it.eng.spagoIFace.model.BaseController;
import it.eng.spagoIFace.model.PublisherInfo;
import it.eng.spagoLite.SessionManager;
import it.eng.spagoLite.security.IUser;

/**
 * 
 * Action standard per governare il redirect a livello applicativo
 * 
 * @author Enrico Grillini
 * 
 */
public class RedirectAction extends BaseController {

    private static final long serialVersionUID = 1L;

    @Override
    public String getControllerName() {
        return "View.html";
    }

    @Override
    public void service() throws Exception {
        forwardToPublisher(SessionManager.getRedirectView(getSession()));
    }

    // Controllo delle autorizzazioni per le pagine
    @Override
    protected final boolean isAuthorized(String pub) {
        if (ConfigSingleton.getDisableSecurity()) {
            return true;
        }
        IUser user = SessionManager.getUser(getSession());
        if (pub.contains("?"))
            pub = pub.substring(0, pub.indexOf("?"));
        if (user != null && user.getProfile() != null && user.getProfile().getChild(pub) == null) {
            logger.debug("Utente " + user.getUsername() + " non autorizzato alla visualizzazione della pagina " + pub);
            return false;
        }
        return true;

    }

}
