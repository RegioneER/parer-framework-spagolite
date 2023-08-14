package it.eng.spagoLite.actions.form;

import static it.eng.spagoCore.configuration.ConfigProperties.StandardProperty.DISABLE_SECURITY;

import it.eng.spagoCore.configuration.ConfigSingleton;
import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.SessionManager;
import it.eng.spagoLite.actions.ActionBase;
import it.eng.spagoLite.actions.application.ApplicationBaseProperties;
import it.eng.spagoLite.actions.application.IApplicationBasePropertiesSevice;
import it.eng.spagoLite.form.Form;
import it.eng.spagoLite.form.base.BaseForm;
import it.eng.spagoLite.form.fields.Field;
import it.eng.spagoLite.form.fields.Fields;
import it.eng.spagoLite.form.fields.Section;
import it.eng.spagoLite.form.wizard.Wizard;
import it.eng.spagoLite.form.wizard.WizardElement;
import it.eng.spagoLite.security.IUser;
import it.eng.spagoLite.security.profile.Pagina;
import java.io.IOException;
import java.net.URI;
import javax.servlet.ServletOutputStream;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public abstract class FormAction<T extends Form, U extends IUser<?>> extends ActionBase<U> {

    private static Logger log = LoggerFactory.getLogger(FormAction.class.getName());
    private boolean viewAction;
    private boolean editAction;
    private boolean deleteAction;
    private boolean insertAction;

    private String tableName = null;
    private String navigationEvent = null;
    private String riga = null;
    private String forceReload = null;

    @Autowired(required = false)
    protected IApplicationBasePropertiesSevice applicationBasePropertiesSevice;

    @Autowired(required = false)
    protected RestTemplate restTemplate;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getNavigationEvent() {
        return navigationEvent;
    }

    public void setNavigationEvent(String navigationEvent) {
        this.navigationEvent = navigationEvent;
    }

    public String getRiga() {
        return riga;
    }

    public void setRiga(String riga) {
        this.riga = riga;
    }

    public String getForceReload() {
        return forceReload;
    }

    public void setForceReload(String forceReload) {
        this.forceReload = forceReload;
    }

    public enum BaseAction {

        NE_ELENCO("elenco"), NE_DETTAGLIO_VIEW("dettaglioView"), NE_DETTAGLIO_UPDATE("update"),
        NE_DETTAGLIO_INSERT("dettaglioInsert"), NE_DETTAGLIO_DELETE("delete"), NE_DETTAGLIO_SELECT("select"),
        NE_DETTAGLIO_SAVE("dettaglioSave"), NE_DETTAGLIO_CANCEL("dettaglioUndo");

        private final String value;

        private BaseAction(final String value) {
            this.value = value;
        }

        public java.lang.String value() {
            return this.value;
        }
    }

    /**
     * Metodo di utility che viene eseguito dopo ogni click sull'ordinamento delle colonne o la navigazione per pagine.
     */
    protected void postLoad() {
    }

    @Override
    protected boolean init() {
        if (super.init()) {
            SessionManager.setForm(getSession(), newForm());
            return true;
        } else {
            return false;
        }
    }

    protected void redirectToDefaultPublisher() {
        redirectToPublisher(getDefaultPublsherName());
    }

    protected ServletOutputStream getServletOutputStream() throws EMFError {
        ServletOutputStream res = null;
        try {
            res = getResponse().getOutputStream();
        } catch (IOException e) {
            new EMFError(EMFError.WARNING, "Errore di Ingresso/Uscita", e);

        }

        return res;
    }

    @SuppressWarnings("unchecked")
    public T getForm() {
        return (T) SessionManager.getForm(getSession());
    }

    public void setForm(T form) {
        SessionManager.setForm(getSession(), form);
    }

    public abstract T newForm();

    /**
     * Gestisce gli eventi di navigazione sui wizard delle action
     *
     * @param wizard
     *            value
     * @param element
     *            value
     * @param wizardNavigation
     *            value
     * 
     * @throws EMFError
     *             eccezione generica
     */
    public abstract void wizard(Wizard wizard, WizardElement element, Wizard.WizardNavigation wizardNavigation)
            throws EMFError;

    public void wizardNavigationOnClick() throws EMFError {
        String wizardName = getRequest().getParameter("wizard");
        String navigationEvent = getRequest().getParameter("navigationEvent");
        String stepName = getRequest().getParameter("step");

        wizardNavigationOnClick(wizardName, navigationEvent, stepName);
    }

    public void wizardNavigationOnClick(String params[]) throws EMFError {
        String wizardName = params[0];
        String navigationEvent = params[1];
        String stepName = params[2];

        wizardNavigationOnClick(wizardName, navigationEvent, stepName);
    }

    protected void wizardNavigationOnClick(String wizardName, String navigationEvent, String stepName) throws EMFError {
        Wizard wizard = (Wizard) getForm().getComponent(wizardName);

        WizardElement wizardElement = wizard.getComponent(stepName);

        if (wizardElement == null) {
            log.error("Passo non trovato");
            getMessageBox().addError("Passo non trovato");
            forwardToPublisher(getDefaultPublsherName());
        }

        wizard(wizard, wizardElement, Wizard.WizardNavigation.valueOf(navigationEvent));

    }

    public abstract void insertDettaglio() throws EMFError;

    public abstract void update(Fields<Field> fields) throws EMFError;

    public abstract void delete(Fields<Field> fields) throws EMFError;

    public abstract void loadDettaglio() throws EMFError;

    public abstract void undoDettaglio() throws EMFError;

    public abstract void saveDettaglio() throws EMFError;

    public abstract void dettaglioOnClick() throws EMFError;

    public abstract void elencoOnClick() throws EMFError;

    protected void setParameters() {
        setTableName(getRequest().getParameter("table"));
        setNavigationEvent(getRequest().getParameter("navigationEvent"));
        setRiga(getRequest().getParameter("riga"));
        setForceReload(getRequest().getParameter("forceReload"));
    }

    protected void setParameters(String param[]) {
        setTableName(param[0]);
        setNavigationEvent(param[1]);
        setRiga(param[2]);
        setForceReload(param[3]);
    }

    public void fieldNavigationOnClick() throws EMFError {
        setParameters();
        fieldNavigationOnClick(getTableName(), getNavigationEvent(), getRiga(), getForceReload());
    }

    public void fieldNavigationOnClick(String param[]) throws EMFError {

        setParameters(param);
        fieldNavigationOnClick(getTableName(), getNavigationEvent(), getRiga(), getForceReload());
    }

    protected void fieldNavigationOnClick(String tableName, String navigationEvent, String riga, String forceReload)
            throws EMFError {
        Fields<Field> list = (Fields<Field>) getForm().getComponent(tableName);
        calculateAuthorization(tableName);
        // forwardToPublisher(getLastPublisher());
        if (navigationEvent.equalsIgnoreCase(BaseAction.NE_ELENCO.value())) {
            elencoOnClick();
        } else if (navigationEvent.equalsIgnoreCase(BaseAction.NE_DETTAGLIO_UPDATE.value())) {
            // loadDettaglio();
            dettaglioOnClick();
            // Check security
            // list.getRowSmandrupper().smandruppRow(list.getTable().getCurrentRow());
            // if (list.getRowSmandrupper().isEditable()) {
            if (isEditAction()) {
                update(list);
            }
        } else if (navigationEvent.equalsIgnoreCase(BaseAction.NE_DETTAGLIO_INSERT.value())) {
            loadDettaglio();
            dettaglioOnClick();

            // Check security
            // list.getRowSmandrupper().smandruppRow(list.getTable().getCurrentRow());
            // if (list.getRowSmandrupper().isInsertable()) {
            if (isInsertAction()) {
                insertDettaglio();
            }
        } else if (navigationEvent.equalsIgnoreCase(BaseAction.NE_DETTAGLIO_DELETE.value())) {
            // list.getTable().setCurrentRowIndex(new Integer(riga));
            // loadDettaglio();
            dettaglioOnClick();

            // Check security
            // list.getRowSmandrupper().smandruppRow(list.getTable().getCurrentRow());
            // if (list.getRowSmandrupper().isDeletable()) {
            if (isDeleteAction()) {
                delete(list);
            }
        } else if (navigationEvent.equalsIgnoreCase(BaseAction.NE_DETTAGLIO_SAVE.value())) {
            saveDettaglio();
        } else if (navigationEvent.equalsIgnoreCase(BaseAction.NE_DETTAGLIO_CANCEL.value())) {
            // list.getTable().setCurrentRowIndex(new Integer(riga));
            undoDettaglio();
        }
        postLoad();
    }

    @Override
    protected void check() {
        super.check();
        if (SessionManager.getUser(getSession()) == null) {
            getMessageBox().addFatal(
                    "Utente non trovato. Si prega di eseguire la procedura di <a href=\"Login.html\" title=\"EXIT\">login</a>");
            redirectToAction("Login.html");
        }
    }

    // Controllo delle autorizzazioni per le pagine
    @Override
    public boolean isAuthorized(String destination) {
        if (ConfigSingleton.getInstance().getBooleanValue(DISABLE_SECURITY.name())) {
            return true;
        }
        IUser user = SessionManager.getUser(getSession());
        if (user != null && user.getProfile().getChild(destination) == null) {
            // getMessageBox().addFatal("Utente non autorizzato alla visualizzazione della risorsa richiesta");
            log.debug("Utente " + user.getUsername() + " non autorizzato alla visualizzazione della pagina "
                    + destination);
            return false;
        }
        return true;
    }

    /*
     * VECCHIO CODICE CHE NON GESTIVA LE FORM DINAMICHE CON UNA SOLA JSP
     * 
     * public boolean isUserAuthorized(String action) { if (ConfigSingleton.getDisableSecurity()) { return true; }
     * IUser<?> user = getUser(); if (user != null) { if (getLastPublisher().equals("")) { return true; }
     * 
     * Pagina p = (Pagina) user.getProfile().getChild(getLastPublisher()); if (p != null && p.getChild(action) != null)
     * { return true; } else { logger.debug("Utente " + user.getUsername() +
     * " non autorizzato all'esecuzione dell'azione " + action + " nella pagina " + getLastPublisher()); } }
     * 
     * return false; }
     */
    public boolean isUserAuthorized(String action) {
        if (ConfigSingleton.getInstance().getBooleanValue(DISABLE_SECURITY.name())) {
            return true;
        }
        IUser<?> user = getUser();
        if (user != null) {
            if (getLastPublisher().equals("")) {
                return true;
            }
            String dynamicDestination = getLastPublisher();
            String nomeOrganizzazione = getNomeOrganizzazione(dynamicDestination);
            if (nomeOrganizzazione != null) {
                dynamicDestination = "[" + nomeOrganizzazione + "]" + getLastPublisher();
            }

            Pagina p = (Pagina) user.getProfile().getChild(dynamicDestination);
            if (p != null && p.getChild(action) != null) {
                return true;
            } else {
                log.debug("Utente " + user.getUsername() + " non autorizzato all'esecuzione dell'azione " + action
                        + " nella pagina " + getLastPublisher());
            }
        }

        return false;
    }

    protected void calculateAuthorization(String name) {
        String viewAction = "detail/" + getForm().getClass().getSimpleName() + "#" + name + "/view";
        String editAction = "detail/" + getForm().getClass().getSimpleName() + "#" + name + "/edit";
        String deleteAction = "detail/" + getForm().getClass().getSimpleName() + "#" + name + "/delete";
        String insertAction = "detail/" + getForm().getClass().getSimpleName() + "#" + name + "/insert";
        setViewAction(isUserAuthorized(viewAction));
        setEditAction((isUserAuthorized(editAction)) || isUserAuthorized("toolbar/edit"));
        setDeleteAction((isUserAuthorized(deleteAction) || isUserAuthorized("toolbar/delete")));
        setInsertAction((isUserAuthorized(insertAction) || isUserAuthorized("toolbar/insert")));
    }

    public boolean isViewAction() {
        return viewAction;
    }

    public void setViewAction(boolean viewAction) {
        this.viewAction = viewAction;
    }

    public boolean isEditAction() {
        return editAction;
    }

    public void setEditAction(boolean editAction) {
        this.editAction = editAction;
    }

    public boolean isDeleteAction() {
        return deleteAction;
    }

    public void setDeleteAction(boolean deleteAction) {
        this.deleteAction = deleteAction;
    }

    public boolean isInsertAction() {
        return insertAction;
    }

    public void setInsertAction(boolean insertAction) {
        this.insertAction = insertAction;
    }

    public void setSectionLoadOpened() throws EMFError {
        String param = getRequest().getParameter("nomeSezione");
        Boolean openParam = ((Section) getForm().getComponent(param)).isLoadOpened();
        ((Section) getForm().getComponent(param)).setLoadOpened(!openParam);
        redirectToAjax(((Section) getForm().getComponent(param)).asJSON());
        // forwardToPublisher(getLastPublisher());
    }

    public void mostraInformativa() throws EMFError {
        redirectToAction("Home.html", "?operation=mostraInformativa", new BaseForm("Home"));
    }

    /*
     * Metodo da ridefinire nel caso in cui si volesse gestire una chiamata all'help che include anche il codice del
     * menu della pagina per cui si intende chiedere il contenuto del menu. Si utilizza per l'applicazione dispenser che
     * ha delle form generiche ma associate a piu' voci di menu (es.: pievesetstina e altre).
     */
    /*
     * protected String getCodiceMenu() { return null; }
     */
    public void mostraHelpPagina() {

        String stringaJson = null;
        ApplicationBaseProperties appProps = applicationBasePropertiesSevice.getApplicationBaseProperties();
        String nomePagina = SessionManager.getLastPublisher(getSession());
        String codiceMenu = this.getRequest().getParameter("codiceMenuHelp");
        if (codiceMenu == null) {
            codiceMenu = "";
        }
        URI targetUrl = UriComponentsBuilder.fromHttpUrl(appProps.getUrlHelp())
                .queryParam("nmUserId", appProps.getApplicationUserName())
                .queryParam("cdPwd", appProps.getApplicationPassword())
                .queryParam("nmApplic", appProps.getApplicationName())
                .queryParam("tiHelpOnLine",
                        codiceMenu.equals("") ? appProps.CONST_HELP_PAGINA : appProps.CONST_HELP_RICERCA_DIPS)
                .queryParam("nmPaginaWeb", nomePagina).queryParam("nmEntryMenu", codiceMenu).build().toUri();
        try {
            ResponseEntity<String> resp = restTemplate.postForEntity(targetUrl, null, String.class);
            stringaJson = resp.getBody();
            if (stringaJson != null) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("risposta", stringaJson);
                } catch (JSONException ex) {
                    //
                }
                redirectToAjax(jsonObject);
            } else {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("risposta", "{cdEsito:'KO',dlErr:'Nessun Help Trovato'}");
                } catch (JSONException ex) {
                    //
                }
                redirectToAjax(jsonObject);
            }
        } catch (RuntimeException ex) {
            log.error("Errore durante l'invocazione del WS Rest per l'Help Online.", ex);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("risposta",
                        "{cdEsito:'KO',dlErr:'Errore nell'invocazione del servizio di recupero Help On Line}");
            } catch (JSONException ex2) {
                //
            }
            redirectToAjax(jsonObject);
        }

    }
}
