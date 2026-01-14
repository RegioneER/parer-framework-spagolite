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

import it.eng.spagoLite.SessionManager;
import it.eng.spagoLite.security.User;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Authenticator {
    private static final Logger log = LoggerFactory.getLogger(Authenticator.class);

    public User doLogin(HttpSession httpSession) {
        User utente = (User) SessionManager.currentUserDetails();
        if (utente != null) {
            if (!isAutorized(utente)) {
                return null;
            }
            SessionManager.setUser(httpSession, utente);
        }
        return utente;
    }

    /**
     * Metodo per la verifica dell'autorizzazione ad eseguire l'applicazione corrente Metodo
     * reimplementato per bypassare la verifica.
     *
     *
     * @param utente dto
     *
     * @return true/false se autenticato
     */
    // public boolean isAutorized(User utente){
    // for(String app : utente.getApplicazioni()){
    // if(app.equalsIgnoreCase(getAppName()))
    // return true;
    // }
    // return false;
    // }
    public boolean isAutorized(User utente) {
        return true;
    }

    /**
     * Metodo che ritorna il nome dell'applicazione così come è definito in IAM
     *
     * @return nome applicazione
     */
    protected abstract String getAppName();

    public abstract User recuperoAutorizzazioni(HttpSession httpSession);
}
