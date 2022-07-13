package it.eng.spagoLite.tag.form;

import javax.servlet.jsp.JspException;

import it.eng.spagoCore.configuration.ConfigSingleton;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.security.IUser;
import it.eng.spagoLite.security.profile.Pagina;

public abstract class BaseFormTag<T extends Component> extends BaseTag {

    protected static final String CONTEXTPATH = ConfigSingleton.get_contextRoot();
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
        if (ConfigSingleton.getDebugAuthorization()) {
            body.append(" <!-- pagina: " + getLastPublisher() + " azione: " + authorization + " -->\n");
        }
    }

    public void debugAuthorization(String authorization) throws JspException {
        if (ConfigSingleton.getDebugAuthorization()) {
            writeln(" <!-- pagina: " + getLastPublisher() + " azione: " + authorization + " -->");
        }
    }

    public boolean isUserAuthorized(String action) {
        if (ConfigSingleton.getDisableSecurity()) {
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
