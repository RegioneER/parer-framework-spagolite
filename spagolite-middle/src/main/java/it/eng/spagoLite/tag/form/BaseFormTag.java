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

package it.eng.spagoLite.tag.form;

import static it.eng.spagoCore.ConfigProperties.StandardProperty.DEBUG_AUTHORIZATION;
import static it.eng.spagoCore.ConfigProperties.StandardProperty.DISABLE_SECURITY;

import it.eng.spagoCore.ConfigSingleton;
import javax.servlet.jsp.JspException;

import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.security.IUser;
import it.eng.spagoLite.security.profile.Pagina;

public abstract class BaseFormTag<T extends Component> extends BaseTag {

    protected static final String CONTEXTPATH = ConfigSingleton.getInstance().getContextPath();
    private static final long serialVersionUID = 1L;

    private String name;
    protected String codiceOrganizzazione;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodiceOrganizzazione() {
        return codiceOrganizzazione;
    }

    public void setCodiceOrganizzazione(String codiceOrganizzazione) {
        this.codiceOrganizzazione = codiceOrganizzazione;
    }

    @SuppressWarnings("unchecked")
    public T getComponent() {
        return (T) getForm().getComponent(getName());
    }

    @SuppressWarnings("unchecked")
    public T getComponent(String componentName) {
        return (T) getForm().getComponent(componentName);
    }

    public void debugAuthorization(StringBuilder body, String authorization) {
        if (ConfigSingleton.getInstance().getBooleanValue(DEBUG_AUTHORIZATION.name())) {
            body.append(" <!-- pagina: ").append(getLastPublisher()).append(" azione: ").append(authorization)
                    .append(" -->\n");
        }
    }

    public void debugAuthorization(String authorization) throws JspException {
        if (ConfigSingleton.getInstance().getBooleanValue(DEBUG_AUTHORIZATION.name())) {
            writeln(" <!-- pagina: " + getLastPublisher() + " azione: " + authorization + " -->");
        }
    }

    public boolean isUserAuthorized(String action) {
        if (ConfigSingleton.getInstance().getBooleanValue(DISABLE_SECURITY.name())) {
            return true;
        }
        IUser<?> user = getUser();
        if (user != null) {
            /*
             * Nuovo comportamento per pagine dinamiche per DIPS
             */
            String nuovaPagina = getLastPublisher();
            if (codiceOrganizzazione != null && !codiceOrganizzazione.trim().equals("")) {
                nuovaPagina = "[" + codiceOrganizzazione + "]" + getLastPublisher();
            }

            Pagina p = (Pagina) user.getProfile().getChild(nuovaPagina);
            if (p != null && p.getChild(action) != null) {
                return true;
            } else {
                logger.trace("Utente " + user.getUsername() + " non autorizzato all'esecuzione dell'azione " + action
                        + " nella pagina " + getLastPublisher());
            }
        }
        return false;
    }

}
