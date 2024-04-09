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

package it.eng.spagoLite.security.saml;

import org.opensaml.common.SAMLException;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.common.impl.ExtensionsBuilder;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.metadata.AssertionConsumerService;
import org.opensaml.saml2.metadata.SingleSignOnService;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.schema.XSAny;
import org.opensaml.xml.schema.impl.XSAnyBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.security.saml.processor.SAMLProcessor;
import org.springframework.security.saml.websso.WebSSOProfileImpl;
import org.springframework.security.saml.websso.WebSSOProfileOptions;

/**
 *
 * @author MIacolucci
 */
public class SliteWebSSOProfileImpl extends WebSSOProfileImpl {

    @Autowired
    private ApplicationContext ctx;

    public static final String PARAMETRO_PURPOSE_SPID = "SlitePur";

    public SliteWebSSOProfileImpl() {
        super();
    }

    public SliteWebSSOProfileImpl(SAMLProcessor processor, MetadataManager manager) {
        super(processor, manager);
    }

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

    @Override
    protected AuthnRequest getAuthnRequest(SAMLMessageContext context, WebSSOProfileOptions options,
            AssertionConsumerService assertionConsumer, SingleSignOnService bindingService)
            throws SAMLException, MetadataProviderException {
        AuthnRequest authnRequest = super.getAuthnRequest(context, options, assertionConsumer, bindingService);
        String parametro = (String) context.getInboundMessageTransport().getAttribute(PARAMETRO_PURPOSE_SPID);
        if (parametro != null) {
            authnRequest.setExtensions(buildExtensions(parametro));
        }
        return authnRequest;
    }

    protected Extensions buildExtensions(String parametroPurposeSpid) {
        XSAny extraElement = new XSAnyBuilder().buildObject("https://spid.gov.it/saml-extensions", "Purpose", "spid");

        extraElement.setTextContent(parametroPurposeSpid);
        Extensions extensions = new ExtensionsBuilder().buildObject();
        extensions.getUnknownXMLObjects().add(extraElement);
        return extensions;
    }

}
