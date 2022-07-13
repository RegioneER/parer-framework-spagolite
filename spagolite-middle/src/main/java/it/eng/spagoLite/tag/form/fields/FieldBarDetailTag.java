package it.eng.spagoLite.tag.form.fields;

import it.eng.spagoIFace.Values;
import it.eng.spagoLite.actions.form.ListAction;
import it.eng.spagoLite.form.base.BaseElements.Status;
import it.eng.spagoLite.form.fields.Field;
import it.eng.spagoLite.form.fields.Fields;
import it.eng.spagoLite.tag.form.BaseFormTag;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

public class FieldBarDetailTag extends BaseFormTag<Fields<Field>> {

    private static final long serialVersionUID = 1L;

    private boolean hideOperationButton;

    private boolean hideDetailButton;
    private boolean hideDeleteButton;
    private boolean hideUpdateButton;
    private boolean hideInsertButton;
    private boolean hideBackButton;

    private boolean editAction;
    private boolean deleteAction;
    private boolean insertAction;

    protected static final String CANCEL_STYLE = "crud cancel";
    protected static final String INSERT_STYLE = "crud insert";
    protected static final String UPDATE_STYLE = "crud update";
    protected static final String DELETE_STYLE = "crud delete";
    protected static final String SELECT_STYLE = "crud select";
    protected static final String DESELECT_STYLE = "crud deselect";
    protected static final String BACK_STYLE = "backButtton";
    protected static final String SAVE_STYLE = "crud save";

    private String insertActionString = "toolbar/insert";
    private String editActionString = "toolbar/edit";
    private String deleteActionString = "toolbar/delete";

    @Override
    public int doStartTag() throws JspException {

        Fields<Field> fields = getComponent();

        if (fields != null) {
            if (fields.size() == 0) {
                return SKIP_BODY;
            }
            writeln("<div class=\"listToolBar\">&nbsp;");
            calculateAuthorization();
            // Pulsanti di modifica/salvataggio
            if (!isHideOperationButton()) {
                if ((isEditAction() && !isHideUpdateButton()) || (isInsertAction() && !isHideInsertButton())) {
                    debugAuthorization(insertActionString);
                    debugAuthorization(editActionString);
                    writeDetailSave();
                }
                if (isEditAction() && !isHideUpdateButton()) {
                    debugAuthorization(editActionString);
                    writeDetailUpdate();
                    writeDetailCancel();
                }
                if (isDeleteAction() && !isHideDeleteButton()) {
                    debugAuthorization(deleteActionString);
                    writeDetailDelete();
                }
            }

            if (!isHideBackButton()) {
                // Pulsante di back
                writeElenco();
            }

        }

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        writeln("</div>");
        return super.doEndTag();
    }

    @Override
    public int doAfterBody() throws JspException {
        // TODO Auto-generated method stub
        return super.doAfterBody();
    }

    public boolean isHideOperationButton() {
        return hideOperationButton;
    }

    public void setHideOperationButton(boolean hideOperationButton) {
        this.hideOperationButton = hideOperationButton;
    }

    protected void writeDetailUpdate() throws JspException {
        Fields<Field> fields = getComponent();
        if (fields != null && fields.size() > 0 && !modificainCorso()) {
            writeln("<span class=\"floatRight\">" + getSubmit(ListAction.NE_DETTAGLIO_UPDATE,
                    FieldBarDetailTag.UPDATE_STYLE, "Modifica", "Modifica", false) + "</span>");
        }
    }

    protected void writeDetailInsert() throws JspException {
        Fields<Field> fields = getComponent();
        if (fields != null && fields.size() > 0 && !modificainCorso()) {
            writeln("<span class=\"floatRight\">" + getSubmit(ListAction.NE_DETTAGLIO_INSERT,
                    FieldBarDetailTag.INSERT_STYLE, "Inserisci", "Inserisci", false) + "</span>");
        }
    }

    protected void writeDetailDelete() throws JspException {
        Fields<Field> fields = getComponent();
        if (fields != null && fields.size() > 0 && !modificainCorso()) {
            writeln("<span class=\"floatRight\">" + getSubmit(ListAction.NE_DETTAGLIO_DELETE,
                    FieldBarDetailTag.DELETE_STYLE, "Elimina", "Elimina", false) + "</span>");
        }
    }

    protected void writeDetailSave() throws JspException {
        Fields<Field> fields = getComponent();
        if (fields != null && fields.size() > 0 && modificainCorso()) {
            writeln("<span class=\"floatRight\">"
                    + getSubmit(ListAction.NE_DETTAGLIO_SAVE, FieldBarDetailTag.SAVE_STYLE, "Salva", "Salva", false)
                    + "</span>");
        }
    }

    protected void writeDetailCancel() throws JspException {
        Fields<Field> fields = getComponent();
        if (fields != null && fields.size() > 0 && modificainCorso()) {
            writeln("<span class=\"floatRight\">" + getSubmit(ListAction.NE_DETTAGLIO_CANCEL,
                    FieldBarDetailTag.CANCEL_STYLE, "Annulla", "Annulla", false) + "</span>");
        }
    }

    protected void writeElenco() throws JspException {
        Fields<Field> fields = getComponent();
        if (fields != null) {

            // writeLink(ListAction.NE_ELENCO, ELENCO);
            // writeLink(ListAction.NE_ELENCO, BACK_URL_IMG, "Indietro");
            writeln(getLink("navBarBackLink", ListAction.NE_ELENCO, "", "Indietro", "Indietro", -1, false, true));

        }
    }

    protected String getSubmit(String navigationEvent, String style, String title, String value, boolean forceReload)
            throws JspException {
        return getSubmit(navigationEvent, style, title, value, -1, forceReload, true);
    }

    protected String getSubmit(String navigationEvent, String style, String title, String value, int riga,
            boolean forceReload, boolean isMasterList) throws JspException {
        if (navigationEvent != null) {
            String remoteMethod = "fieldNavigationOnClick";

            StringBuilder submitName = new StringBuilder("operation" + Values.OPERATION_SEPARATOR + remoteMethod
                    + Values.OPERATION_SEPARATOR + getName() + Values.OPERATION_SEPARATOR + navigationEvent);
            submitName.append(Values.OPERATION_SEPARATOR + ((riga < 0) ? "-1" : String.valueOf(riga)));
            submitName.append(Values.OPERATION_SEPARATOR + String.valueOf(forceReload));

            String button = "<input type=\"submit\" name=\"" + submitName + "\" title=\"" + title + "\" value=\""
                    + value + "\" class=\"" + style + "\"/>\n";
            return button;
        } else {
            return "";
        }
    }

    protected String getLink(String linkClass, String navigationEvent, String img, String text, String title, int riga,
            boolean forceReload, boolean isMasterList) throws JspException {

        String styleClass = StringUtils.isBlank(linkClass) ? "" : " class=\"" + linkClass + "\" ";
        if (navigationEvent != null) {
            String tagContent = (img != null ? img : "") + (text != null ? text : "");
            tagContent = tagContent.equals("") ? "&nbsp;" : tagContent;

            StringBuilder params = new StringBuilder("");
            params.append("riga=" + ((riga < 0) ? "-1" : String.valueOf(riga)));
            params.append("&amp;forceReload=" + String.valueOf(forceReload));

            String remoteMethod = "fieldNavigationOnClick";

            return "<a " + styleClass + " " + (title != null ? "title=\"" + title + "\"" : "") + " href=\""
                    + getOperationUrl(remoteMethod,
                            "table=" + getName() + "&amp;navigationEvent=" + navigationEvent + "&amp;" + params)
                    + "\">" + tagContent + "</a>&nbsp;";

        } else {
            return "<span " + styleClass + ">" + (img != null ? img : "") + "&nbsp;</span>";
        }
    }

    public boolean isHideDetailButton() {
        return hideDetailButton;
    }

    public void setHideDetailButton(boolean hideDetailButton) {
        this.hideDetailButton = hideDetailButton;
    }

    public boolean isHideUpdateButton() {
        return hideUpdateButton;
    }

    public void setHideUpdateButton(boolean hideUpdateButton) {
        this.hideUpdateButton = hideUpdateButton;
    }

    public boolean isHideInsertButton() {
        return hideInsertButton;
    }

    public void setHideInsertButton(boolean hideInsertButton) {
        this.hideInsertButton = hideInsertButton;
    }

    public boolean isHideDeleteButton() {
        return hideDeleteButton;
    }

    public void setHideDeleteButton(boolean hideDeleteButton) {
        this.hideDeleteButton = hideDeleteButton;
    }

    public boolean isHideBackButton() {
        return hideBackButton;
    }

    public void setHideBackButton(boolean hideBackButton) {
        this.hideBackButton = hideBackButton;
    }

    private boolean modificainCorso() {
        Fields<Field> fields = getComponent();
        if (Status.insert.equals(fields.getStatus()) || Status.delete.equals(fields.getStatus())
                || Status.update.equals(fields.getStatus())) {
            return true;
        } else {
            return false;
        }
    }

    private void calculateAuthorization() {

        setEditAction(isUserAuthorized(editActionString));
        setDeleteAction(isUserAuthorized(deleteActionString));
        setInsertAction(isUserAuthorized(insertActionString));
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

}
