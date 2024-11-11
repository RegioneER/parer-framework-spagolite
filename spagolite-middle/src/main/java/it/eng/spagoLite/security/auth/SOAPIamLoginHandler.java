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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import static it.eng.spagoLite.security.auth.AuthenticationHandlerConstants.*;
import it.eng.spagoLite.security.exception.AuthWSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Handler SOAP specifico per Sacer IAM (Lato SERVER): i controlli sono effettuati su tabelle specifiche, che in IAM
 * sono diverse dagli altri applicativi Viene effettuato anche il controllo delle autorizzazioni.
 *
 * @author Quaranta_M
 *
 */

public class SOAPIamLoginHandler implements SOAPHandler<SOAPMessageContext> {

    private static Logger log = LoggerFactory.getLogger(SOAPIamLoginHandler.class);

    private static final String X_FORWARDED_UNDEFINED = "l'header http x-forwarded non è definito";

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Override
    public boolean handleMessage(SOAPMessageContext msgCtx) {
        Boolean outbound = (Boolean) msgCtx.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        Object obj = msgCtx.get(MessageContext.SERVLET_REQUEST);
        String ipAddress = X_FORWARDED_UNDEFINED;
        if (obj != null && obj instanceof HttpServletRequest) {
            ipAddress = ((HttpServletRequest) obj).getHeader("X-FORWARDED-FOR");
        }
        log.debug("SOAPServerLoginHandler attivato. Client IP Address: " + ipAddress);
        if (!outbound) {
            // Validate the message.
            EntityManager em = null;
            String username = null;
            String password = null;
            String servizioWeb = null;
            try {
                em = emf.createEntityManager();
                QName svcn = (QName) msgCtx.get(MessageContext.WSDL_SERVICE);
                NodeList usernameEl = (NodeList) msgCtx.getMessage().getSOAPHeader()
                        .getElementsByTagNameNS(WSSE_XSD_URI, "Username");
                NodeList passwordEl = (NodeList) msgCtx.getMessage().getSOAPHeader()
                        .getElementsByTagNameNS(WSSE_XSD_URI, "Password");
                Node userNode = null;
                Node passNode = null;
                if (usernameEl != null && passwordEl != null && svcn != null && (userNode = usernameEl.item(0)) != null
                        && (passNode = passwordEl.item(0)) != null && (servizioWeb = svcn.getLocalPart()) != null) {
                    username = userNode.getFirstChild().getNodeValue();
                    password = passNode.getFirstChild().getNodeValue();
                    WSLoginHandler.loginAndCheckAuthzIAM(username, password, servizioWeb, ipAddress, em, false);
                    msgCtx.put(AuthenticationHandlerConstants.AUTHN_STAUTS, java.lang.Boolean.TRUE);
                    msgCtx.put(AuthenticationHandlerConstants.USER, username);
                    msgCtx.put(AuthenticationHandlerConstants.PWD, password);
                } else
                    throw new ProtocolException("Username e password sono obbligatorie");
            } catch (AuthWSException e) {
                WSLoginHandler.throwSOAPFault(msgCtx, e);
            } catch (DOMException | SOAPException e) {
                throw new ProtocolException(e);
            } finally {
                if (em != null) {
                    em.close();
                }
            }
            msgCtx.setScope(AuthenticationHandlerConstants.AUTHN_STAUTS, MessageContext.Scope.APPLICATION);
            msgCtx.setScope(AuthenticationHandlerConstants.USER, MessageContext.Scope.APPLICATION);
            msgCtx.setScope(AuthenticationHandlerConstants.PWD, MessageContext.Scope.APPLICATION);
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {

        return true;
    }

    @Override
    public void close(MessageContext context) {
        // TODO Auto-generated method stub

    }

    @Override
    public Set<QName> getHeaders() {

        HashSet<QName> headers = new HashSet<QName>();
        headers.add(QNAME_WSSE_HEADER);
        return headers;
    }

}
