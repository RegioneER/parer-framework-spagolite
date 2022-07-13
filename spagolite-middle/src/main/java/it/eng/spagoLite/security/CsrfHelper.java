/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        return token != null ? "<input type=\"hidden\" name=\"" + token.getParameterName() + "\" value=\""
                + token.getToken() + "\" /> \n" : StringUtils.EMPTY;
    }

    public static String getCsrfMetaDataToken(HttpServletRequest request) {
        CsrfToken token = getTokenFromRequest(request);
        return token != null ? "  <meta name=\"_csrf_parameter\" content=\"" + token.getParameterName() + "\" />" + "\n"
                + "  <meta name=\"_csrf_header\" content=\"" + token.getHeaderName() + "\" />" + "\n"
                + "  <meta name=\"_csrf\" content=\"" + token.getToken() + "\" /> \n" : StringUtils.EMPTY;
    }

    public static String getCsrfQueryStringToken(HttpServletRequest request) {
        CsrfToken token = getTokenFromRequest(request);
        return token != null ? token.getParameterName() + "=" + token.getToken() : StringUtils.EMPTY;
    }

    private static CsrfToken getTokenFromRequest(HttpServletRequest request) {
        return request.getAttribute(CsrfToken.class.getName()) != null
                ? (CsrfToken) request.getAttribute(CsrfToken.class.getName()) : null;
    }

}
