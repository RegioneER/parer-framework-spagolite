package it.eng.spagoCore.util;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.keycloak.adapters.servlet.KeycloakOIDCFilter;

/**
 *
 * @author gpiccioli
 */
public class CustomKeycloakFilter extends KeycloakOIDCFilter {

    public CustomKeycloakFilter() throws IOException {
        super();
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequestWrapper wrap = new HttpServletRequestWrapper((HttpServletRequest) req);

        Enumeration<String> enumer = wrap.getHeaders("Authorization");

        List<String> authHeaders = Collections.list(enumer);

        if (authHeaders == null || authHeaders.size() == 0) {
            chain.doFilter(wrap, res);
            return;
        } else {
            super.doFilter(wrap, res, chain);
        }
    }
}
