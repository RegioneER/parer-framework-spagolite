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
