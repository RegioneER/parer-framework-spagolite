package it.eng.spagoLite.security.saml;

import org.opensaml.saml2.core.AuthnRequest;
import org.springframework.security.saml.websso.WebSSOProfileImpl;

/**
 *
 * @author MIacolucci
 */
public class SliteWebSSOProfileImpl extends WebSSOProfileImpl {

    @Override
    protected void buildAuthnContext(AuthnRequest request,
            org.springframework.security.saml.websso.WebSSOProfileOptions options) {
        /*
         * LO SO! E' una porcheria, ma è l'unico modo per non far generare le RequestedAuthnContext che servono per SPID
         * di FEDERA. Infatti se l'utente sta facendo una richiesta ad un IDP che nell'indirizzo contiene la parola
         * ".lepida.it" il sistema genera tutte le RequestedAuthnContext configurate nel bean "samlEntryPoint" alla
         * proprietà "authnContexts" altrimenti no.
         */
        String des = request.getDestination();
        if (des != null && des.contains(".lepida.it")) {
            super.buildAuthnContext(request, options);
        }
    }

}
