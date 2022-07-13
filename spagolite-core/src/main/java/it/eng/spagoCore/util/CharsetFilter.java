package it.eng.spagoCore.util;

import java.io.*;
import javax.servlet.*;

/**
 * This class filters user http-request setting UTF-8 character set encoding
 */
public class CharsetFilter implements Filter {

    /**
     * The encoding.
     */
    private String encoding;

    /*
     * (non-Javadoc) @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig config) throws ServletException {
        encoding = config.getInitParameter("requestEncoding");

        if (encoding == null) {
            encoding = "UTF-8";
        }
    }

    /*
     * (non-Javadoc) @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
     * javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next)
            throws IOException, ServletException {
        // Respect the client-specified character encoding
        // (see HTTP specification section 3.4.1)
        if (null == request.getCharacterEncoding()) {
            request.setCharacterEncoding(encoding);
        }
        next.doFilter(request, response);
    }

    /*
     * (non-Javadoc) @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
    }
}
