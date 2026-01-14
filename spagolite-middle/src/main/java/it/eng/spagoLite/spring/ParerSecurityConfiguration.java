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

package it.eng.spagoLite.spring;

import org.opensaml.core.xml.schema.XSAny;
import org.opensaml.core.xml.schema.impl.XSAnyBuilder;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.saml2.core.*;
import org.opensaml.saml.saml2.core.impl.AuthnContextClassRefBuilder;
import org.opensaml.saml.saml2.core.impl.ExtensionsBuilder;
import org.opensaml.saml.saml2.core.impl.RequestedAuthnContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.saml2.provider.service.web.DefaultRelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.RelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.authentication.OpenSaml4AuthenticationRequestResolver;
import org.springframework.security.saml2.provider.service.web.authentication.Saml2AuthenticationRequestResolver;

/**
 *
 * @author Marco Iacolucci
 */
public class ParerSecurityConfiguration {

    public static final String MODALITA_SPID_PROFESSIONALE = "PX";
    public static final String PARAMETRO_MODALITA_SPID_PROFESSIONALE = "SlitePur";

    public static String nomeApplicazione;

    private static final Logger LOGGER = LoggerFactory.getLogger(ParerSecurityConfiguration.class);

    protected RequestedAuthnContext buildRequestedAuthnContext() {
        // Create AuthnContextClassRef
        AuthnContextClassRefBuilder authnContextClassRefBuilder = new AuthnContextClassRefBuilder();
        boolean abilitaLivello1Spid = Boolean
                .parseBoolean(System.getProperty("abilita-livello-1-spid", "false"));

        AuthnContextClassRef authnContextClassRef = authnContextClassRefBuilder.buildObject(
                SAMLConstants.SAML20_NS, AuthnContextClassRef.DEFAULT_ELEMENT_LOCAL_NAME,
                SAMLConstants.SAML20_PREFIX);
        authnContextClassRef.setURI(AuthnContext.PPT_AUTHN_CTX);

        AuthnContextClassRef authnContextClassRef2 = authnContextClassRefBuilder.buildObject(
                SAMLConstants.SAML20_NS, AuthnContextClassRef.DEFAULT_ELEMENT_LOCAL_NAME,
                SAMLConstants.SAML20_PREFIX);
        authnContextClassRef2.setURI(AuthnContext.SRP_AUTHN_CTX);

        AuthnContextClassRef authnContextClassRef3 = authnContextClassRefBuilder.buildObject(
                SAMLConstants.SAML20_NS, AuthnContextClassRef.DEFAULT_ELEMENT_LOCAL_NAME,
                SAMLConstants.SAML20_PREFIX);
        authnContextClassRef3.setURI(AuthnContext.SMARTCARD_AUTHN_CTX);

        // Create RequestedAuthnContext
        RequestedAuthnContextBuilder requestedAuthnContextBuilder = new RequestedAuthnContextBuilder();
        RequestedAuthnContext requestedAuthnContext = requestedAuthnContextBuilder.buildObject();
        requestedAuthnContext.setComparison(AuthnContextComparisonTypeEnumeration.EXACT);

        // Se configurato abilita il livello 1 di SPID altrimenti il 2
        if (abilitaLivello1Spid) {
            requestedAuthnContext.getAuthnContextClassRefs().add(authnContextClassRef);
        }
        requestedAuthnContext.getAuthnContextClassRefs().add(authnContextClassRef2);
        requestedAuthnContext.getAuthnContextClassRefs().add(authnContextClassRef3);

        return requestedAuthnContext;
    }

    @Bean
    public static RefreshableRelyingPartyRegistrationRepository refreshableRelyingPartyRegistrationRepository(
            ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        long fixedRate = Long.parseLong(
                System.getProperty(nomeApplicazione + "-refresh-check-interval", "600000"));
        LOGGER.info("Il federationMetadata si aggiorna ogni {} millisecondi.", fixedRate);
        RefreshableRelyingPartyRegistrationRepository r = new RefreshableRelyingPartyRegistrationRepository(
                nomeApplicazione);
        threadPoolTaskScheduler.scheduleAtFixedRate(r, fixedRate);
        LOGGER.info("Nome dell'applicazione configurato [{}]", nomeApplicazione);
        return r;
    }

    /*
     * Serve per attivare lo sheduling per il federationMetadata
     */
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(2);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }

    @Bean
    Saml2AuthenticationRequestResolver authenticationRequestResolver(
            RefreshableRelyingPartyRegistrationRepository registrations) {
        RelyingPartyRegistrationResolver registrationResolver = new DefaultRelyingPartyRegistrationResolver(
                registrations);
        OpenSaml4AuthenticationRequestResolver authenticationRequestResolver = new OpenSaml4AuthenticationRequestResolver(
                registrationResolver);
        authenticationRequestResolver.setAuthnRequestCustomizer(context -> {
            context.getAuthnRequest().setRequestedAuthnContext(buildRequestedAuthnContext());
            /*
             * Testa se l'utente ha scelto di loggarsi con lo SPID PROFESSIONALE. Se si la richiesta
             * SAML include questi tag:
             *
             * <saml2p:Extensions> <spid:Purpose
             * xmlns:spid="https://spid.gov.it/saml-extensions">PX</spid:Purpose>
             * </saml2p:Extensions>
             *
             */
            String spidProf = context.getRequest()
                    .getParameter(PARAMETRO_MODALITA_SPID_PROFESSIONALE);
            if (spidProf != null && spidProf.equalsIgnoreCase(MODALITA_SPID_PROFESSIONALE)) {
                XSAny extraElement = new XSAnyBuilder()
                        .buildObject("https://spid.gov.it/saml-extensions", "Purpose", "spid");
                extraElement.setTextContent(MODALITA_SPID_PROFESSIONALE);
                Extensions extensions = new ExtensionsBuilder().buildObject();
                extensions.getUnknownXMLObjects().add(extraElement);
                context.getAuthnRequest().setExtensions(extensions);
            }

            /*
             * Inserisce il suffisso che vuole la Puglia da accosare all'Issuer della richiesta SAML
             * Aggiunto per risolvere il problema dell'Issuer della Puglia che voleva accodato il
             * Tenant dopo l'entityId ('@conservazione' ad esempio).
             */
            String suffissoIssuerPuglia = System.getProperty("suffisso-issuer-saml-request", "");
            if (!suffissoIssuerPuglia.trim().equals("")) {
                String issuerOriginale = context.getAuthnRequest().getIssuer().getValue();
                context.getAuthnRequest().getIssuer()
                        .setValue(issuerOriginale + suffissoIssuerPuglia);
            }

        });

        return authenticationRequestResolver;
    }

}
