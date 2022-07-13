package it.eng.parer.sacerlog.util.web;

import it.eng.parer.sacerlog.util.LogParam;
import it.eng.spagoIFace.model.BaseController;
import it.eng.spagoLite.ExecutionHistory;
import it.eng.spagoLite.SessionManager;
import it.eng.spagoLite.actions.form.FormAction;
import it.eng.spagoLite.form.Form;
import it.eng.spagoLite.form.base.BaseComponent;
import it.eng.spagoLite.form.base.BaseForm;
import java.math.BigDecimal;

/**
 *
 * @author Iacolucci_M
 */
public class SpagoliteLogUtil {

    private final static String SEPARATORE = "#";
    private final static String SLASH = "/";

    private final static String VIEW = "/view";
    private final static String EDIT = "/edit";
    private final static String DELETE = "/delete";
    private final static String INSERT = "/insert";

    private final static String DETAIL = "detail/";
    private final static String BUTTON = "button/";

    private final static String TOOLBAR_EDIT = "toolbar/edit";
    private final static String TOOLBAR_DELETE = "toolbar/delete";
    private final static String TOOLBAR_INSERT = "toolbar/insert";
    private final static String TOOLBAR_UPDATE = "toolbar/update";

    public static String getDetailActionNameView(BaseForm form, BaseComponent component) {
        return DETAIL + getActionName(form, component) + VIEW;
    }

    public static String getDetailActionNameEdit(BaseForm form, BaseComponent component) {
        return DETAIL + getActionName(form, component) + EDIT;
    }

    public static String getDetailActionNameDelete(BaseForm form, BaseComponent component) {
        return DETAIL + getActionName(form, component) + DELETE;
    }

    public static String getDetailActionNameInsert(BaseForm form, BaseComponent component) {
        return DETAIL + getActionName(form, component) + INSERT;
    }

    public static String getButtonActionName(BaseForm form, BaseComponent component, String nomeMetodo) {
        String metodoCorretto = null;
        if (nomeMetodo != null && nomeMetodo.length() > 0) {
            String primoCarattere = nomeMetodo.substring(0, 1).toLowerCase();
            String resto = nomeMetodo.substring(1);
            metodoCorretto = primoCarattere + resto;
        }
        return BUTTON + getActionName(form, component) + SLASH + metodoCorretto;
    }

    public static String getDetailActionName(BaseForm form, BaseComponent component, String nomeMetodo) {
        String metodoCorretto = null;
        if (nomeMetodo != null && nomeMetodo.length() > 0) {
            String primoCarattere = nomeMetodo.substring(0, 1).toLowerCase();
            String resto = nomeMetodo.substring(1);
            metodoCorretto = primoCarattere + resto;
        }
        return DETAIL + getActionName(form, component) + SLASH + metodoCorretto;
    }

    private static String getActionName(BaseForm form, BaseComponent component) {
        return form.getClass().getSimpleName() + SEPARATORE + component.getName();
    }

    public static String getToolbarEdit() {
        return TOOLBAR_EDIT;
    }

    public static String getToolbarSave(boolean isModify) {
        return isModify ? TOOLBAR_UPDATE : TOOLBAR_INSERT;
    }

    public static String getToolbarDelete() {
        return TOOLBAR_DELETE;
    }

    public static String getToolbarInsert() {
        return TOOLBAR_INSERT;
    }

    public static String getToolbarUpdate() {
        return TOOLBAR_UPDATE;
    }

    public static String getPageName(BaseController action) {
        String nomePagina = action.getLastPublisher();
        if (nomePagina == null || nomePagina.trim().equals("")) {
            ExecutionHistory storia = SessionManager.getLastExecutionHistory(action.getSession());
            nomePagina = storia.getPublisherName();
        }
        return nomePagina;
    }

    public static Form getForm(FormAction action) {
        Form form = null;
        String nomePagina = action.getLastPublisher();
        if (nomePagina == null || nomePagina.trim().equals("")) {
            ExecutionHistory storia = SessionManager.getLastExecutionHistory(action.getSession());
            form = storia.getForm();
            if (form == null) {
                form = action.getForm();
            }
        } else {
            form = action.getForm();
        }
        return form;
    }

    public static LogParam getLogParam(String nomeApplicazione, String nomeUtente, String nomePagina, String nomeAzione,
            BigDecimal idOggetto) {
        return new LogParam(nomeApplicazione, nomeUtente, nomePagina, nomeAzione, idOggetto);
    }

    public static LogParam getLogParam(String nomeApplicazione, String nomeUtente, String nomePagina,
            String nomeAzione) {
        return new LogParam(nomeApplicazione, nomeUtente, nomePagina, nomeAzione);
    }

    public static LogParam getLogParam(String nomeApplicazione, String nomeUtente, String nomePagina) {
        return new LogParam(nomeApplicazione, nomeUtente, nomePagina);
    }

    public static LogParam getLogParam(String nomeApplicazione, String nomeUtente) {
        return new LogParam(nomeApplicazione, nomeUtente);
    }

}
