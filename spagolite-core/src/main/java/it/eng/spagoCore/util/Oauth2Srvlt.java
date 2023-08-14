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
