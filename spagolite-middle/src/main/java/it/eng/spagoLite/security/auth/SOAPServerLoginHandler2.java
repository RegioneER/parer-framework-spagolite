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
 * Handler SOAP per un WS Server generico: effettua il login (autenticazione con user/password) dell'utente e verifica
 * che l'utente sia autorizzato ad eseguire il servizio su cui l'handler è applicato per almeno un'organizzazione. E'
 * utile per i servizi che sono trasversali alle organizzazioni. Si aspetta che i campi user e password siano
 * nell'header come da standard ws security.
 *
 *
 * @author Quaranta_M
 *
 */
public class SOAPServerLoginHandler2 implements SOAPHandler<SOAPMessageContext> {

    private static Logger log = LoggerFactory.getLogger(SOAPServerLoginHandler2.class);

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
                    WSLoginHandler.loginAndCheckAuthzAtLeastOneOrganiz(username, password, servizioWeb, ipAddress, em);
                    msgCtx.put(AuthenticationHandlerConstants.AUTHN_STAUTS, java.lang.Boolean.TRUE);
                    msgCtx.put(AuthenticationHandlerConstants.USER, username);
                    msgCtx.put(AuthenticationHandlerConstants.PWD, password);
                } else {
                    throw new ProtocolException("Username e password sono obbligatorie");
                }
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
