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

package it.eng.spagoLite.security;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;

public abstract class KeycloakAuthorizationServlet extends HttpServlet {

    private static final long serialVersionUID = -5553758874312199121L;

    protected AccessToken getAccessToken(HttpServletRequest request) {
        KeycloakSecurityContext session = getKeycloakSession(request);
        return session != null ? session.getToken() : null;
    }

    protected boolean hasAuthorizationHeader(HttpServletRequest request) {
        return getAccessToken(request) != null;
    }

    private KeycloakSecurityContext getKeycloakSession(HttpServletRequest request) {
        return (KeycloakSecurityContext) request
                .getAttribute(KeycloakSecurityContext.class.getName());
    }

}
