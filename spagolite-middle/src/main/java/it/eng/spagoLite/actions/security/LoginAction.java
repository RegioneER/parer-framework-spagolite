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

package it.eng.spagoLite.actions.security;

import javax.annotation.Resource;
import javax.xml.ws.soap.SOAPFaultException;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.SessionManager;
import it.eng.spagoLite.actions.ActionBase;
import it.eng.spagoLite.security.IUser;
import it.eng.spagoLite.security.User;
import it.eng.spagoLite.security.auth.Authenticator;
import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Quaranta_M
 *
 *         1 - process() 2 - passIn() 3 - doLogin()
 *
 */
public class LoginAction extends ActionBase {

    private static final Logger logger = LoggerFactory.getLogger(LoginAction.class);
    private static final String JSESSIONID = "JSESSIONID";
    @Autowired
    public String nomeApplicazione;

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
	    if (utente.getUserType() != null
		    && utente.getUserType().equals(IUser.UserType.SPID_FEDERA)) {
		logger.debug("Richiesto SAML LOCAL LOGOUT per l'utente "
			+ IUser.UserType.SPID_FEDERA.name());
		Cookie cookie = new Cookie("shib_idp_session", "");
		cookie.setMaxAge(0);
		cookie.setPath("/idp");
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		getResponse().addCookie(cookie);

		cookie = new Cookie(JSESSIONID, "");
		cookie.setMaxAge(0);
		cookie.setPath("/wayf");
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		getResponse().addCookie(cookie);

		cookie = new Cookie(JSESSIONID, "");
		cookie.setMaxAge(0);
		cookie.setPath("/idp");
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		getResponse().addCookie(cookie);

		cookie = new Cookie(JSESSIONID, "");
		cookie.setMaxAge(0);
		cookie.setPath("/" + nomeApplicazione);
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		getResponse().addCookie(cookie);
		logger.info("Nome applicazione {}", nomeApplicazione);

		cookie = new Cookie("_saml_wayf_idp_", "");
		cookie.setMaxAge(0);
		cookie.setPath("/");
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		getResponse().addCookie(cookie);

		cookie = new Cookie("USENAV", "");
		cookie.setMaxAge(0);
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		getResponse().addCookie(cookie);

		redirectToAction("/Logout.html?operation=success");
	    } else {
		logger.debug("Richiesto SAML GLOBAL LOGOUT");
		forwardToPublisher("/login/logoutGlobale");
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
