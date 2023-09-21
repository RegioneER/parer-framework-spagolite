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

package it.eng.spagoLite.security.auth;

import javax.xml.namespace.QName;

public class AuthenticationHandlerConstants {

    public final static String WSSE_PREFIX = "wsse";
    public final static String WSU_PREFIX = "wsu";
    public final static String WSSE_LNAME = "Security";
    public final static String WSSE_XSD_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    public final static String WSU_SXD_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
    public final static String AUTHN_STAUTS = "authnStatus";
    public final static QName QNAME_WSSE_HEADER = new QName(WSSE_XSD_URI, WSSE_LNAME, WSSE_PREFIX);

    public final static String USER = "user";
    public final static String PWD = "pass";
}
