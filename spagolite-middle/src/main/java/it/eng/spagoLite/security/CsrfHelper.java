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

/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package it.eng.spagoLite.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.csrf.CsrfToken;

/**
 *
 * @author sinatti_s
 */
public class CsrfHelper {

    public static boolean hasTokenFromRequest(HttpServletRequest request) {
	return getTokenFromRequest(request) != null;
    }

    public static String getCsrfInputToken(HttpServletRequest request) {
	CsrfToken token = getTokenFromRequest(request);
	return token != null
		? "<input type=\"hidden\" name=\"" + token.getParameterName() + "\" value=\""
			+ token.getToken() + "\" /> \n"
		: StringUtils.EMPTY;
    }

    public static String getCsrfMetaDataToken(HttpServletRequest request) {
	CsrfToken token = getTokenFromRequest(request);
	return token != null
		? "  <meta name=\"_csrf_parameter\" content=\"" + token.getParameterName() + "\" />"
			+ "\n" + "  <meta name=\"_csrf_header\" content=\"" + token.getHeaderName()
			+ "\" />" + "\n" + "  <meta name=\"_csrf\" content=\"" + token.getToken()
			+ "\" /> \n"
		: StringUtils.EMPTY;
    }

    public static String getCsrfQueryStringToken(HttpServletRequest request) {
	CsrfToken token = getTokenFromRequest(request);
	return token != null ? token.getParameterName() + "=" + token.getToken()
		: StringUtils.EMPTY;
    }

    private static CsrfToken getTokenFromRequest(HttpServletRequest request) {
	return request.getAttribute(CsrfToken.class.getName()) != null
		? (CsrfToken) request.getAttribute(CsrfToken.class.getName())
		: null;
    }

}
