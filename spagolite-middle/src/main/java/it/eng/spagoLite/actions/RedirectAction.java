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

package it.eng.spagoLite.actions;

import static it.eng.spagoCore.ConfigProperties.StandardProperty.DISABLE_SECURITY;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.spagoCore.ConfigSingleton;
import it.eng.spagoIFace.BaseController;
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

    private final Logger logger = LoggerFactory.getLogger(RedirectAction.class);

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
        if (ConfigSingleton.getInstance().getBooleanValue(DISABLE_SECURITY.name())) {
            return true;
        }
        IUser user = SessionManager.getUser(getSession());
        if (pub.contains("?"))
            pub = pub.substring(0, pub.indexOf("?"));
        if (user != null && user.getProfile() != null && user.getProfile().getChild(pub) == null) {
            logger.debug("Utente " + user.getUsername()
                    + " non autorizzato alla visualizzazione della pagina " + pub);
            return false;
        }
        return true;

    }

}
