package it.eng.spagoIFace.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface BaseControllerIFace {

    /**
     * Ritorna la request
     *
     * @return HttpServletRequest {@link HttpServletRequest}
     */
    public HttpServletRequest getRequest();

    /**
     * Ritorna la response
     *
     * @return HttpServletResponse {@link HttpServletResponse}
     */
    public HttpServletResponse getResponse();

    /**
     * Ritorna la sessione
     *
     * @return HttpSession {@link HttpSession}
     */
    public HttpSession getSession();
}
