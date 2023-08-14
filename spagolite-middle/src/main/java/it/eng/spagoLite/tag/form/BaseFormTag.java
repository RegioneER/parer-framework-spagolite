package it.eng.spagoLite.tag.form;

import static it.eng.spagoCore.configuration.ConfigProperties.StandardProperty.DEBUG_AUTHORIZATION;
import static it.eng.spagoCore.configuration.ConfigProperties.StandardProperty.DISABLE_SECURITY;

import it.eng.spagoCore.configuration.ConfigSingleton;
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
