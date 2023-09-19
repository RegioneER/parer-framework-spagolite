/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

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
