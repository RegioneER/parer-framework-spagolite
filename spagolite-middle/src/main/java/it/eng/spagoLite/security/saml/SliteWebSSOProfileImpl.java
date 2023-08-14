package it.eng.spagoLite.security.saml;

import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.Issuer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.saml.websso.WebSSOProfileImpl;

/**
 *
 * @author MIacolucci
 */
public class SliteWebSSOProfileImpl extends WebSSOProfileImpl {

    @Autowired
    private ApplicationContext ctx;

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
        /*
         * Risoluzione problema Puglia che voleva aggiungere al nome dell'Issuer (EntityId) un suffisso riguardante il
         * tenant
         */

        String suffissoIssuer = null;
        if (ctx.containsBean("suffissoIssuerSamlRequest")) {
            suffissoIssuer = (String) ctx.getBean("suffissoIssuerSamlRequest");
        }
        if (suffissoIssuer != null) {
            Issuer iss = request.getIssuer();
            iss.setValue(iss.getValue() + suffissoIssuer);
        }
    }

}
