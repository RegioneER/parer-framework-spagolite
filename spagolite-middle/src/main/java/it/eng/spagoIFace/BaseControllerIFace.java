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
