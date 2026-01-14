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

import it.eng.spagoCore.ConfigSingleton;
import it.eng.spagoCore.error.EMFError;
import it.eng.spagoIFace.Values;
import it.eng.spagoIFace.BaseController;
import it.eng.spagoLite.ExecutionHistory;
import it.eng.spagoLite.FrameElement;
import it.eng.spagoLite.SessionManager;
import it.eng.spagoLite.form.Form;
import it.eng.spagoLite.message.MessageBox;
import it.eng.spagoLite.message.MessageBox.ViewMode;
import it.eng.spagoLite.security.IUser;
import it.eng.spagoLite.security.Secure;
import it.eng.spagoLite.security.SuppressLogging;
import it.eng.spagoLite.security.profile.Pagina;

import static it.eng.spagoCore.ConfigProperties.StandardProperty.DISABLE_SECURITY;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dom4j.Element;

public abstract class ActionBase<U extends IUser<?>> extends BaseController {

    private static Logger log = LoggerFactory.getLogger(ActionBase.class.getName());

    public static final String JSON_OBJECT = "###_JSON_OBJECT";
    public static final String CLEAN_HISTORY = "cleanhistory";
    private static final String DEFAULT_METHOD = "process";
    public static String DOUBLE_CLICK = "DOUBLE_CLICK";

    private boolean ajax_mode;

    protected abstract String getDefaultPublsherName();

    public abstract void process() throws EMFError;

    @SuppressWarnings("unchecked")
    public U getUser() {
        return (U) SessionManager.getUser(getSession());
    }

    public ActionBase() {
        super.forwardToPublisher(getDefaultPublsherName());
    }

    private void checkDoubleClick() {
        if (getRequest().getParameter(DOUBLE_CLICK) != null) {
            getMessageBox().addError(
                    "Il pulsante salva è stato premuto più volte: verificare il corretto inserimento dei dati ricominciando l'usuale navigazione a partire dalla Lista Lavoro");
            redirectToPublisher("DOUBLECLIK");
        }
    }

    public MessageBox getMessageBox() {
        return SessionManager.getMessageBox(getSession());
    }

    protected boolean init() {
        String sessionAction = SessionManager.getCurrentAction(getSession());
        String currentAction = getControllerName();
        String cleanHistory = getRequest().getParameter(CLEAN_HISTORY);
        if ("true".equalsIgnoreCase(cleanHistory)) {
            SessionManager.clearActionHistory(getSession());
        }
        // Se cambio action la inizializzo
        if (sessionAction == null || !sessionAction.equalsIgnoreCase(currentAction)
                || "TRUE".equalsIgnoreCase(getRequest().getParameter(Values.VOID_SESSION))) {
            SessionManager.setCurrentAction(getSession(), currentAction);
            SessionManager.initMessageBox(getSession());
            setLastPublisher("");
            return true;
        } else {
            return false;
        }
    }

    public void service() throws Exception {
        try {
            init();

        } catch (Throwable throwable) {
            log.error("Errore durante l'inizializzazione della action", throwable);
            return;
        }

        checkDoubleClick();
        check();

        if (!getMessageBox().hasFatal()) {
            try {
                new Process(this).execute();
            } catch (Throwable throwable) {
                log.error("Errore interno del server", throwable);
                getMessageBox().addFatal(
                        "Errore critico sulla funzionalita', contattare l'assistenza tecnica.",
                        throwable);
                getMessageBox().setViewMode(ViewMode.plain);
                forwardToPublisher(getDefaultPublsherName());
            }
        }
        // Se ho cambiato action ed ancora non è stato mostrato il primo publisher tra
        // poco lo sarà di certo ..
        ExecutionHistory history = SessionManager.getLastExecutionHistory(getSession());
        if (history != null && history.getName() != null
                && !history.getName().equalsIgnoreCase(getControllerName())
                && !history.isActionPublished()) {
            history.setActionPublished(true);
            SessionManager.setLastExecutionHistory(getSession(), history);
        }
    }

    /**
     * Effettua le verifiche base di sicurezza e doppio click
     *
     */
    protected void check() {

    }

    public boolean isAjaxMode() {
        return this.ajax_mode;
    }

    /**
     * Metodo da invocare per tornare alla precedente "ExcecutionHistory"
     *
     */
    protected void goBack() {
        this.goBack(false);
    }

    /**
     * Il metodo estrae dallo stack della cronologia di navigazione l'ultima operazione e la
     * ripristina: se la precedente esecuzione è stata una forwardToAction o una redirectToAction il
     * framework esegue comunque una redirect forzando la chiamata al metodo backPublisher. A volte,
     * infatti, potrebbe capitare che, forwardando indietro una request, questa possa già contenere
     * un'altra operazione che andrebbe in conflitto con la backPublisher.
     *
     * @param recoverFromError parametro che indica se la goBack è scatenata da un'errore causato da
     *                         una back del browser
     */
    protected void goBack(boolean recoverFromError) {
        ExecutionHistory history = SessionManager.removeLastExecutionHistory(getSession());
        if (history.isAction()) {
            SessionManager.setForm(getSession(), history.getForm());
            SessionManager.setCurrentAction(getSession(), history.getName());
            redirectToAction(history.getName() + "?operation=backPublisher&name="
                    + history.getPublisherName() + history.getBackParameter());
        } else {
            if (!recoverFromError) {
                reloadAfterGoBack(history.getName());
            }
            if (history.isForward()) {
                super.forwardToPublisher(history.getName());
            } else {
                super.redirectToPublisher(history.getName());
            }
        }
    }

    /**
     * Il metodo estrae dallo stack della cronologia di navigazione l'ultima operazione che ha
     * inoltrato la request alla pagina (publisherNam) passata in input e la ripristina. Se la
     * precedente esecuzione è stata una forwardToAction o una redirectToAction il framework esegue
     * comunque una redirect forzando la chiamata al metodo backPublisher. A volte, infatti,
     * potrebbe capitare che, forwardando indietro una request, questa possa già contenere un'altra
     * operazione che andrebbe in conflitto con la backPublisher
     *
     * @param publisherName parametro che indica la pagina a cui è stata forwardata l'ultima volta
     *                      la request e a cui si vuole "tornare"
     */
    protected void goBackTo(String publisherName) {
        ExecutionHistory history = SessionManager.removeLastExecutionHistory(getSession());
        String history_name = history.getName();
        String history_publisher = history.getPublisherName();
        if ((history_publisher != null && history_publisher.equals(publisherName))
                || (history_name != null && history_name.equals(publisherName))) {

            if (history.isAction()) {
                SessionManager.setForm(getSession(), history.getForm());
                SessionManager.setCurrentAction(getSession(), history.getName());
                redirectToAction(history.getName() + "?operation=backPublisher&name="
                        + history.getPublisherName() + history.getBackParameter());
            } else {
                reloadAfterGoBack(history.getName());
                if (history.isForward()) {
                    super.forwardToPublisher(history.getName());
                } else {
                    super.redirectToPublisher(history.getName());
                }
            }
        } else {
            goBackTo(publisherName);
        }
    }

    /**
     *
     * Metodo invocato a seguito di una redirect o una forward scatenata dalla goBack() Si occupa di
     * invocare la reloadAfterGoBack passandole il publisher di destinazione;
     *
     */
    public void backPublisher() {
        String publisherName = getRequest().getParameter("name");
        reloadAfterGoBack(publisherName);
        super.forwardToPublisher(publisherName);
    }

    /**
     * Metodo da implementare per il refresh delle liste dopo una goBack()
     *
     * @param publisherName nome del publisher a cui sarà forwardata la corrente request. (Indica il
     *                      publisher di destinazione)
     */
    public abstract void reloadAfterGoBack(String publisherName);

    @Override
    protected void forwardToPublisher(String publisherName) {
        ExecutionHistory history = SessionManager.getLastExecutionHistory(getSession());
        if (!publisherName.equals(getLastPublisher())
                && (history == null || history.isActionPublished())) {
            SessionManager.addPrevExecutionToHistory(getSession(), false, true);
        }
        super.forwardToPublisher(publisherName);
    }

    @Override
    protected void forwardToPublisherSkipSetLast(String publisherName) {
        ExecutionHistory history = SessionManager.getLastExecutionHistory(getSession());
        if (!publisherName.equals(getLastPublisher())
                && (history == null || history.isActionPublished())) {
            SessionManager.addPrevExecutionToHistory(getSession(), false, true);
        }
        super.forwardToPublisherSkipSetLast(publisherName);
    }

    @Override
    protected void redirectToPublisher(String publisherName) {
        ExecutionHistory history = SessionManager.getLastExecutionHistory(getSession());
        if (!publisherName.equals(getLastPublisher())
                && (history == null || history.isActionPublished())) {
            SessionManager.addPrevExecutionToHistory(getSession(), false, false);
        }

        super.redirectToPublisher(publisherName);
    }

    protected void forwardToAction(String action, String parameters, Form form) {
        if (parameters == null) {
            parameters = "";
        }
        String param = getRequest().getParameter("mainNavTable") != null
                ? "&table=" + getRequest().getParameter("mainNavTable")
                : null;
        SessionManager.addPrevExecutionToHistory(getSession(), true, true, param);
        SessionManager.setForm(getSession(), form);
        SessionManager.setCurrentAction(getSession(), action);
        forwardToAction(action + parameters);
    }

    protected void redirectToAction(String action, String parameters, Form form) {
        if (parameters == null) {
            parameters = "";
        }
        String param = getRequest().getParameter("mainNavTable") != null
                ? "&table=" + getRequest().getParameter("mainNavTable")
                : null;
        SessionManager.addPrevExecutionToHistory(getSession(), true, false, param);
        SessionManager.setForm(getSession(), form);
        SessionManager.setCurrentAction(getSession(), action);
        redirectToAction(action + parameters);
    }

    protected void redirectToAction(String action, String parameters, String fakeLastPublisher,
            Form form) {
        if (parameters == null) {
            parameters = "";
        }
        String param = getRequest().getParameter("mainNavTable") != null
                ? "&table=" + getRequest().getParameter("mainNavTable")
                : null;
        SessionManager.addPrevExecutionToHistory(getSession(), true, false, param);
        SessionManager.setForm(getSession(), form);
        SessionManager.setCurrentAction(getSession(), action);
        setLastPublisher(fakeLastPublisher);
        redirectToActionProfiled(action + parameters);
    }

    /*
     * Torna l'IP del client (se c'è quello del girato dal proxy. MEV#22913 - Logging accessi SPID
     * non autorizzati
     */
    protected String getIpClient() {
        HttpServletRequest request = getRequest();
        String ipVers = request.getHeader("X-FORWARDED-FOR");
        if (ipVers == null || ipVers.isEmpty()) {
            ipVers = request.getRemoteAddr();
        }
        return ipVers;
    }

    public class Process extends FrameElement {

        private ActionBase actionBase;
        private String processName;
        private List<String> processParameters;

        public Process(ActionBase actionBase) {
            this.actionBase = actionBase;
            this.processName = DEFAULT_METHOD;
            this.processParameters = new ArrayList<String>();

            for (Object object : actionBase.getRequest().getParameterMap().entrySet()) {
                Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) object;

                if (entry.getKey().equalsIgnoreCase(Values.OPERATION)) {
                    processName = entry.getValue()[0];
                    break;
                } else if (entry.getKey().toLowerCase().startsWith(Values.OPERATION)) {
                    String[] tokenizer = entry.getKey()
                            .substring(
                                    Values.OPERATION.length() + Values.OPERATION_SEPARATOR.length())
                            .split(Values.OPERATION_SEPARATOR);
                    for (String token : tokenizer) {
                        if (DEFAULT_METHOD.equals(processName)) {
                            processName = token;
                        } else {
                            processParameters.add(token);
                        }
                    }

                    break;
                }
            }

        }

        public void execute() throws SecurityException, NoSuchMethodException,
                IllegalArgumentException, IllegalAccessException, InvocationTargetException {

            if (processParameters.size() == 0) {
                Method method = actionBase.getClass().getMethod(processName);
                logMethod(method);
                if (isAuthorized(method, false)) {
                    method.invoke(actionBase);
                }
            } else {
                Method method = actionBase.getClass().getMethod(processName, String[].class);
                logMethod(method);
                if (isAuthorized(method, true)) {
                    method.invoke(actionBase, new Object[] {
                            processParameters.toArray(new String[] {}) });
                }
            }
        }

        private void logMethod(Method method) {
            if (method.isAnnotationPresent(SuppressLogging.class)) {
                log.debug("Eseguo il metodo: " + methodLog());
            } else {
                log.info("Eseguo il metodo: " + methodLog());
            }
        }

        private String methodLog() {
            StringBuilder sb = new StringBuilder();
            sb.append(actionBase.getClass().getName());
            sb.append(".");
            sb.append(processName);
            sb.append("(");
            if (processParameters.size() > 0) {
                String currentSeparator = "";
                for (String value : processParameters) {
                    sb.append(currentSeparator);
                    sb.append(value);
                    currentSeparator = ", ";
                }
            }
            sb.append(")");
            return sb.toString();
        }

        public Element asXml() {
            Element element = super.asXml();
            element.addAttribute("className", actionBase.getClass().getName());
            element.addAttribute("methodName", processName);

            for (String value : processParameters) {
                element.addElement("parameter").setText(value);
            }

            return element;
        }

        // Controllo delle autorizzazioni per le esecuzioni del metodo (azioni e menu)
        private boolean isAuthorized(Method method, boolean hasParams) throws SecurityException {
            if (ConfigSingleton.getInstance().getBooleanValue(DISABLE_SECURITY.name())) {
                return true;
            }
            Secure annotation = method.getAnnotation(Secure.class);
            if (annotation == null) {
                try {
                    if (hasParams) {
                        annotation = actionBase.getClass().getSuperclass()
                                .getMethod(this.processName, String[].class)
                                .getAnnotation(Secure.class);
                    } else {
                        annotation = actionBase.getClass().getSuperclass()
                                .getMethod(this.processName).getAnnotation(Secure.class);
                    }
                } catch (NoSuchMethodException ex) {
                    // il metodo non è presente nella superclass e non contiene annotation nella
                    // classe
                    return true;
                }
            }
            if (annotation != null) {
                if (getUser() != null) {
                    if (getLastPublisher().equals("")) {
                        return true;
                    }
                    String dynamicDestination = getLastPublisher();
                    String nomeOrganizzazione = getNomeOrganizzazione(dynamicDestination);
                    if (nomeOrganizzazione != null) {
                        dynamicDestination = "[" + nomeOrganizzazione + "]" + getLastPublisher();
                    }

                    Pagina p = (Pagina) getUser().getProfile().getChild(dynamicDestination);
                    if (p != null && p.getChild(annotation.action()) != null) {
                        return true;
                    } else {
                        log.debug("Utente " + getUser().getUsername()
                                + " non autorizzato all'esecuzione del metodo "
                                + annotation.action() + " in pagina " + getLastPublisher());
                        getMessageBox().addFatal("Utente " + getUser().getUsername()
                                + " non autorizzato all'esecuzione dell'azione "
                                + annotation.action() + " in pagina " + getLastPublisher());
                        return false;
                    }
                }
            }
            return true;
        }

    }

}
