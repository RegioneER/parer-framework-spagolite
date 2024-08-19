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

package it.eng.spagoIFace.session;

import javax.servlet.http.HttpSession;

public abstract class SessionCoreManager {

    protected static final String ACTION_CONTAINER = "###_ACTION_CONTAINER";
    protected static final String MESSAGE_CONTAINER = "###_MESSAGE_CONTAINER";
    protected static final String FORM_CONTAINER = "###_FORM_CONTAINER";
    protected static final String NAVHIS_CONTAINER = "###_NAVHIS_CONTAINER";
    public static final String LAST_PUBLISHER = "###_LAST_PUBLISHER";

    protected static final String USER_CONTAINER = "###_USER_CONTAINER";
    protected static final String VIEW_REDIRECT = "###_PUBLISHER_REDIRECT";

    /**
     * Ritorna l'url dell'action corrente
     *
     * @param httpSession
     *            sessione http
     *
     * @return resituisce l'action corrente
     */
    public static String getCurrentActionUrl(HttpSession httpSession) {
        return (String) httpSession.getAttribute(ACTION_CONTAINER);
    }

    public static String getRedirectView(HttpSession httpSession) {
        return (String) httpSession.getAttribute(VIEW_REDIRECT);
    }

    public static void setRedirectView(HttpSession httpSession, String redirectView) {
        httpSession.setAttribute(VIEW_REDIRECT, redirectView);
    }

    public static void setLastPublisher(HttpSession httpSession, String lastPublisher) {
        httpSession.setAttribute(LAST_PUBLISHER, lastPublisher);
    }

    public static String getLastPublisher(HttpSession httpSession) {
        return (String) httpSession.getAttribute(LAST_PUBLISHER);
    }

    /**
     * Ritorna l'url dell'operazione indicata
     *
     * @param httpSession
     *            sessione http
     * @param operation
     *            nome operazione
     * @param additionalInfo
     *            informazioni aggiuntive
     *
     * @return restituisce l'URL con query parameters
     */
    public static String getOperationUrl(HttpSession httpSession, String operation, String additionalInfo) {
        String actionUrl = getCurrentActionUrl(httpSession);
        if (additionalInfo != null && !additionalInfo.isEmpty()) {
            return actionUrl + "?operation=" + operation + "&amp;" + additionalInfo;
        } else {
            return actionUrl + "?operation=" + operation;
        }
    }

}
