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

package it.eng.spagoIFace;

import it.eng.spagoIFace.model.PublisherInfo;
import it.eng.spagoIFace.session.SessionCoreManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * Spring Core - BaseController
 *
 * @author Enrico Grillini
 *
 */
public abstract class BaseController implements Controller, BaseControllerIFace {

    private final Logger logger = LoggerFactory.getLogger(BaseController.class);

    public static final String LAST_PUBLISHER = "###_LAST_PUBLISHER";

    private HttpServletRequest request;
    private HttpServletResponse response;

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	// Inizializzo il controller
	this.request = request;
	this.response = response;

	// Eseguo la logica di business
	service();

	// Gestico la pubblicazione
	ModelAndView mav = null;
	// Forward-to-publisher
	if (publisherInfo.getType().equals(PublisherInfo.Override.forward)
		|| publisherInfo.getType().equals(PublisherInfo.Override.forwardSkipSetLast)) {
	    // controllo le autorizzazioni di pagina
	    String codiceOrganizzazione = getNomeOrganizzazione(publisherInfo.getDestination());
	    String nuovaDestinazione = publisherInfo.getDestination();
	    if (codiceOrganizzazione != null) {
		nuovaDestinazione = "[" + codiceOrganizzazione + "]" + nuovaDestinazione;
	    }
	    if (isAuthorized(nuovaDestinazione)) {
		logger.info("Forward to: " + publisherInfo.getDestination());
		if (!publisherInfo.getType().equals(PublisherInfo.Override.forwardSkipSetLast)) {
		    setLastPublisher(publisherInfo.getDestination());
		}
		mav = new ModelAndView(publisherInfo.getDestination());
	    } else {
		mav = new ModelAndView("/login/notAuthorized");
		logger.info("Unable to forward to: " + publisherInfo.getDestination()
			+ " user not authorized");
		request.setAttribute("destination", publisherInfo.getDestination());
	    }
	    // Forward-to-action
	} else if (publisherInfo.getType().equals(PublisherInfo.Override.actionForward)) {
	    logger.info("Forward to action: " + publisherInfo.getDestination());
	    // Resetto il lastpublisher utilizzato per le autorizzazione
	    setLastPublisher("");
	    mav = new ModelAndView("forward:/" + publisherInfo.getDestination());
	    // Redirect-to-action
	} else if (publisherInfo.getType().equals(PublisherInfo.Override.actionRedirect)) {
	    logger.info("Redirect to action: " + publisherInfo.getDestination());
	    // Resetto il lastpublisher utilizzato per le autorizzazione
	    setLastPublisher("");
	    RedirectView rv = new RedirectView(publisherInfo.getDestination(), true, false);
	    mav = new ModelAndView(rv);
	    // Redirect-to-action with fake publisher
	} else if (publisherInfo.getType().equals(PublisherInfo.Override.actionProfiledRedirect)) {
	    logger.info("Redirect to action: " + publisherInfo.getDestination());
	    // il publisher gia fornito permette di simulare l'accesso con profilazione
	    RedirectView rv = new RedirectView(publisherInfo.getDestination(), true, false);
	    mav = new ModelAndView(rv);
	    // Redirect-to-publisher
	} else if (publisherInfo.getType().equals(PublisherInfo.Override.redirect)) {
	    SessionCoreManager.setRedirectView(getSession(), publisherInfo.getDestination());
	    RedirectView rv = new RedirectView("View.html", true, false);
	    mav = new ModelAndView(rv);
	    // Ajax response
	} else if (publisherInfo.getType().equals(PublisherInfo.Override.ajaxRedirect)) {
	    getResponse().setContentType("text/x-json;charset=UTF-8");
	    getResponse().setHeader("Cache-Control", "no-cache");
	    JSONObject result = new JSONObject();
	    result.put("name", "Form");
	    result.put("type", "Form");
	    result.put("description", "");
	    JSONArray sons = new JSONArray();
	    sons.put(publisherInfo.getJsonObject());
	    result.put("map", sons);
	    getResponse().getOutputStream().write(result.toString().getBytes("UTF-8"));
	    getResponse().getOutputStream().flush();
	    // reimposto il publisher a forward con l'ultima destination
	    forwardToPublisher(getLastPublisher());
	    // Freeze
	} else if (publisherInfo.getType().equals(PublisherInfo.Override.freeze)) {
	    mav = null;
	    // reimposto il publisher a forward con l'ultima destination
	    forwardToPublisher(getLastPublisher());
	}

	return mav;
    }

    public HttpServletRequest getRequest() {
	return request;
    }

    public HttpServletResponse getResponse() {
	return response;
    }

    public HttpSession getSession() {
	return getRequest().getSession();
    }

    public abstract String getControllerName();

    public abstract void service() throws Exception;

    protected abstract boolean isAuthorized(String destination);

    private PublisherInfo publisherInfo;

    protected void forwardToPublisher(String publisherName) {
	publisherInfo = new PublisherInfo(PublisherInfo.Override.forward, publisherName, null);
    }

    protected void forwardToPublisherSkipSetLast(String publisherName) {
	publisherInfo = new PublisherInfo(PublisherInfo.Override.forwardSkipSetLast, publisherName,
		null);
    }

    protected void forwardToAction(String publisherName) {
	publisherInfo = new PublisherInfo(PublisherInfo.Override.actionForward, publisherName,
		null);
    }

    protected void redirectToAction(String publisherName) {
	publisherInfo = new PublisherInfo(PublisherInfo.Override.actionRedirect, publisherName,
		null);
    }

    protected void redirectToActionProfiled(String publisherName) {
	publisherInfo = new PublisherInfo(PublisherInfo.Override.actionProfiledRedirect,
		publisherName, null);
    }

    protected void redirectToPublisher(String publisherName) {
	publisherInfo = new PublisherInfo(PublisherInfo.Override.redirect, publisherName, null);
    }

    protected void redirectToAjax(JSONObject jsonObject) {
	publisherInfo = new PublisherInfo(PublisherInfo.Override.ajaxRedirect, null, jsonObject);
    }

    protected void freeze() {
	publisherInfo = new PublisherInfo(PublisherInfo.Override.freeze, null, null);
    }

    public void setLastPublisher(String lastPublisher) {
	getSession().setAttribute(LAST_PUBLISHER, lastPublisher);
    }

    public String getLastPublisher() {
	return (String) getSession().getAttribute(LAST_PUBLISHER);
    }

    /*
     * Metodo che se ridefinito nelle classi derivate consente di tornare il nome
     * dell'organizzazione (es. DIPS (LUM, Pievesestina ecc.))
     */
    protected String getNomeOrganizzazione(String destination) {
	return null;
    }

}
