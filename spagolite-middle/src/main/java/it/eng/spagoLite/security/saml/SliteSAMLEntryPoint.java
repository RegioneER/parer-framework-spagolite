package it.eng.spagoLite.security.saml;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.opensaml.common.SAMLException;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.context.SAMLMessageContext;

/**
 *
 * @author S257421
 * 
 *         Classe con metodo ridefinito per poter intercettato il parametro nostro della request atto a gestire il
 *         purpose di SPID e per metterlo tra gli attributi della request, altrimenti non è più possibile leggerlo in
 *         alcun modo nelle fari successive negli oggetti SPRING SAML perché completamente incapsulati.
 */
public class SliteSAMLEntryPoint extends SAMLEntryPoint {

    public SliteSAMLEntryPoint() {
        super();
    }

    /*
     * MEtodo ridefinito e copiato dalla superclasse per intercettare e copiare il parametro del purpose SPID
     * proveniente dalla form di selezione IDP nel processo di discovery locale.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {

        try {

            SAMLMessageContext context = contextProvider.getLocalAndPeerEntity(request, response);

            if (isECP(context)) {
                initializeECP(context, e);
            } else if (isDiscovery(context)) {
                initializeDiscovery(context);
            } else {
                String parametro = request.getParameter(SliteWebSSOProfileImpl.PARAMETRO_PURPOSE_SPID);
                if (parametro != null) {
                    request.setAttribute(SliteWebSSOProfileImpl.PARAMETRO_PURPOSE_SPID, parametro);
                }
                initializeSSO(context, e);
            }

        } catch (SAMLException e1) {
            log.debug("Error initializing entry point", e1);
            throw new ServletException(e1);
        } catch (MetadataProviderException e1) {
            log.debug("Error initializing entry point", e1);
            throw new ServletException(e1);
        } catch (MessageEncodingException e1) {
            log.debug("Error initializing entry point", e1);
            throw new ServletException(e1);
        }

    }
}
