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

package it.eng.spagoCore.util;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.keycloak.KeycloakSecurityContext;

/**
 *
 * @author gpiccioli
 */
public class Oauth2Srvlt extends HttpServlet {

    public KeycloakSecurityContext session;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	KeycloakSecurityContext session = getKeycloakSession(request);

	this.session = session;
    }

    public boolean isOauth2Request(HttpServletRequest request) {
	Enumeration<String> enumer = request.getHeaders("Authorization");

	List<String> authHeaders = Collections.list(enumer);

	if (authHeaders == null || authHeaders.size() == 0) {
	    return false;
	} else {
	    return true;
	}
    }

    /**
     * Get keycloak session from request
     *
     * @param req
     *
     * @return
     */
    private KeycloakSecurityContext getKeycloakSession(HttpServletRequest req) {
	return (KeycloakSecurityContext) req.getAttribute(KeycloakSecurityContext.class.getName());
    }

}
