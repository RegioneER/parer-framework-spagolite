package it.eng.spagoCore.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * This class filters user http-request setting UUID on MDC Log4j
 */
public class UUIDLoggerFilter implements Filter {

    /*
     * (non-Javadoc) @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig config) throws ServletException {
    }

    /*
     * (non-Javadoc) @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
     * javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next)
            throws IOException, ServletException {
        // LOG UUID
        UUIDMdcLogUtil.genUuid();
        next.doFilter(request, response);
    }

    /*
     * (non-Javadoc) @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
    }
}
