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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.keycloak.adapters.servlet.KeycloakOIDCFilter;

/**
 *
 * @author gpiccioli
 */
public class CustomKeycloakFilter extends KeycloakOIDCFilter {

    public CustomKeycloakFilter() throws IOException {
        super();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) res;

        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(httpRequest);
        BypassErrorPageResponseWrapper responseWrapper = new BypassErrorPageResponseWrapper(
                httpResponse);

        Enumeration<String> enumer = requestWrapper.getHeaders("Authorization");
        List<String> authHeaders = Collections.list(enumer);

        if (authHeaders == null || authHeaders.isEmpty()) {
            chain.doFilter(requestWrapper, responseWrapper);
        } else {
            super.doFilter(requestWrapper, responseWrapper, chain);
        }
    }

    /*
     * Response wrapper to bypass error-page mechanism for 401 responses and write an empty body
     */
    private static class BypassErrorPageResponseWrapper extends HttpServletResponseWrapper {
        private boolean isCommitted = false;

        public BypassErrorPageResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public void sendError(int sc) throws IOException {
            if (sc == HttpServletResponse.SC_UNAUTHORIZED
                    || sc == HttpServletResponse.SC_FORBIDDEN) {
                // Set status directly without triggering error-page
                setStatus(sc);
                setContentLength(0);
                flushBuffer();
                isCommitted = true;
            } else {
                super.sendError(sc);
            }
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            if (sc == HttpServletResponse.SC_UNAUTHORIZED
                    || sc == HttpServletResponse.SC_FORBIDDEN) {
                // Set status directly without triggering error-page
                setStatus(sc);
                setContentLength(0);
                flushBuffer();
                isCommitted = true;
            } else {
                super.sendError(sc, msg);
            }
        }

        @Override
        public void setStatus(int sc) {
            if ((sc == HttpServletResponse.SC_UNAUTHORIZED
                    || sc == HttpServletResponse.SC_FORBIDDEN) && !isCommitted) {
                super.setStatus(sc);
                try {
                    setContentLength(0);
                } catch (Exception e) {
                    // Ignore
                }
            } else {
                super.setStatus(sc);
            }
        }

        @Override
        public boolean isCommitted() {
            return isCommitted || super.isCommitted();
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            if (getStatus() == HttpServletResponse.SC_UNAUTHORIZED
                    || getStatus() == HttpServletResponse.SC_FORBIDDEN) {
                return new EmptyServletOutputStream();
            }
            return super.getOutputStream();
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            if (getStatus() == HttpServletResponse.SC_UNAUTHORIZED
                    || getStatus() == HttpServletResponse.SC_FORBIDDEN) {
                return new EmptyPrintWriter();
            }
            return super.getWriter();
        }
    }

    private static class EmptyServletOutputStream extends ServletOutputStream {
        @Override
        public void write(int b) throws IOException {
            // Discard all writes
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            // No-op
        }
    }

    private static class EmptyPrintWriter extends PrintWriter {
        public EmptyPrintWriter() {
            super(new NullOutputStream());
        }

        @Override
        public void write(char[] buf, int off, int len) {
            // Discard all writes
        }

        @Override
        public void write(String s, int off, int len) {
            // Discard all writes
        }
    }

    private static class NullOutputStream extends OutputStream {
        @Override
        public void write(int b) throws IOException {
            // Discard all writes
        }
    }
}