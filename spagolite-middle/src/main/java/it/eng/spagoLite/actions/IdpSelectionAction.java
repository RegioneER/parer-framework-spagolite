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

package it.eng.spagoLite.actions;

import it.eng.spagoLite.spring.RefreshableRelyingPartyRegistrationRepository;
import java.util.ArrayList;
import java.util.Comparator;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author S257421
 */
@Controller
public class IdpSelectionAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdpSelectionAction.class);

    @Autowired
    RefreshableRelyingPartyRegistrationRepository refreshableRelyingPartyRegistrationRepository;

    @Autowired
    public String nomeApplicazione;

    @GetMapping(path = "/discovery")
    public String handler(Model model, HttpServletRequest request) {
        /*
         * Anche questa è un workaround perché all'atto del logout da keycloak il logout veniva
         * fatto ma mi reindirizzava dono un http 302 verso /discovery?error !!!
         *
         * Quindi intercettando il parametro che contiene "" capisco che devo mandarlo nella pagina
         * finale del logout effettuato correttamente. E funziona.
         */
        String paramError = request.getParameter("error");
        if (paramError != null && paramError.equals("")) {
            return "/login/logout";
        } else {
            if (refreshableRelyingPartyRegistrationRepository == null) {
                LOGGER.warn("Non ci sono IDP nel repository!");
            } else {
                ArrayList<IdpBean> al = new ArrayList<>();

                refreshableRelyingPartyRegistrationRepository.forEach(r -> {
                    LOGGER.info("reg id {}, entity id {},idp {}.", r.getRegistrationId(),
                            r.getEntityId(), r.getAssertingPartyDetails().getEntityId());
                    IdpBean b = new IdpBean();
                    String ap = r.getAssertingPartyDetails().getEntityId();
                    if (ap.contains("lepida.it")) {
                        b.setLinkDescription("Accedi con SPID");
                    } else if (ap.contains("sso.")) {
                        b.setLinkDescription("Accedi con l'IDP");
                    } else {
                        b.setLinkDescription(ap);
                    }
                    b.setRegistrationId(r.getRegistrationId());
                    b.setLink("/saml2/authenticate/" + r.getRegistrationId());
                    b.setIdpEntityId(r.getAssertingPartyDetails().getEntityId());
                    al.add(b);
                });
                al.sort(Comparator.comparing(idpBean -> idpBean.getIdpEntityId()));
                request.setAttribute("idps", al);
            }
            // Se il discovery è disabilitato va direttamente all'idp con il registrationId
            // configurato con "nomeApplicazione" (es.: saceriam)
            if (System.getProperty("discovery-service-enabled", "true").equals("false")) {
                return "redirect:/saml2/authenticate/" + nomeApplicazione;
            }
            return "login/selezioneIdp";
        }
    }

    public class IdpBean {

        private String registrationId;
        private String link;
        private String linkDescription;
        private String tipo;
        private String idpEntityId;

        public String getRegistrationId() {
            return registrationId;
        }

        public void setRegistrationId(String registrationId) {
            this.registrationId = registrationId;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getLinkDescription() {
            return linkDescription;
        }

        public void setLinkDescription(String linkDescription) {
            this.linkDescription = linkDescription;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getIdpEntityId() {
            return idpEntityId;
        }

        public void setIdpEntityId(String idpEntityId) {
            this.idpEntityId = idpEntityId;
        }

    }

}
