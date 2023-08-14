package it.eng.spagoLite.tag.form.list;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoIFace.Values;
import it.eng.spagoLite.actions.form.ListAction;
import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.db.base.BaseTableInterface;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.form.list.List;
import it.eng.spagoLite.tag.form.fields.FieldTag;
import it.eng.spagoLite.tag.form.formLayout.ContainerTag;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

public class EditableListTag extends AbstractListTag {

    private static final long serialVersionUID = 1L;

    private boolean hideOperationButton = false;

    // private String getSortLink(SingleValueField<?> field) {
    // String image = "";
    //
    // SortingRule sortingRule = getComponent().getTable().getLastSortingRule();
    // String colummName = StringUtils.isEmpty(field.getAlias()) ? field.getName() : field.getAlias();
    // if (sortingRule != null && sortingRule.getSortType() == SortingRule.ASC &&
    // sortingRule.getColumnName().equalsIgnoreCase(colummName)) {
    // image = ASC_IMG;
    // } else if (sortingRule != null && sortingRule.getSortType() == SortingRule.DESC &&
    // sortingRule.getColumnName().equalsIgnoreCase(colummName)) {
    // image = DESC_IMG;
    // }
    //
    // return "<a href=\"" + getOperationUrl("listSortOnClick", "table=" + getName() + "&amp;column=" + field.getName())
    // + "\">" + field.getHtmlDescription() + image + "</a>";
    // }
    @Override
    public int doStartTag() throws JspException {
        try {
            List<?> fields = getComponent();
            BaseTableInterface<?> table = getComponent().getTable();

            if (table != null) {
                if (table.size() == 0) {
                    writeln("  <div class=\"slLabel\" ><i>Nessun elemento trovato</i></div>");
                    return SKIP_BODY;
                }

                calculateOperations(table);
                calculateAuthorization();

                writeln("");
                writeln("<!-- Lista: " + getName() + " -->");
                String title = "";
                if (StringUtils.isNotBlank(getTitle())) {
                    title = " title=\"" + getTitle() + "\"";
                }
                String styleClass = "list";
                if (fields.isHidden()) {
                    styleClass += " displayNone";
                }

                // FIXME: l'idea dovrebbe essere giusta, l'implementazione fa schifo
                StringBuilder head = new StringBuilder();
                StringBuilder body = new StringBuilder();

                int rowEditable = drawBody(table, fields, body);
                drawHead(styleClass, title, fields, head, rowEditable);

                writeln(head);
                writeln(body);
                writeln("</table>");
            }

        } catch (EMFError e) {
            throw new JspException(e);
        }

        return SKIP_BODY;
    }

    private void drawHead(String styleClass, String title, List<?> fields, StringBuilder head, int rowEditable) {
        head.append("<table id=\"" + getName() + "\" class=\"" + styleClass + "\" summary=\"" + fields.getDescription()
                + "\" " + title + ">");
        head.append(" <thead>");
        head.append("  <tr>");

        // colonne per l'immagine di visualizzazione
        if (isViewAction() && isContentVisible() && !fields.isHideDetailButton()) {
            head.append("   <th></th>");
        }

        // Intestazione
        for (SingleValueField<?> field : fields) {
            String headerContent = isSortable() ? getSortLink(field) : field.getHtmlDescription();
            if (!field.isHidden()) {
                head.append("    <th>" + headerContent + "</th>");
            } else {
                head.append("    <th class=\"displayNone\">" + headerContent + "</th>");
            }
        }

        // colonne per la modifica, l'eliminazione e l'inserimento:
        if (isEditAction() && isContentEditable() && !fields.isHideUpdateButton()) {
            head.append("   <th></th>");
        }
        if (isDeleteAction() && isContentDeletable() && !fields.isHideDeleteButton()) {
            head.append("   <th></th>");
        }
        // for (int i = 0; i < rowEditable; i++) {
        // head.append(" <th></th>");
        // }

        head.append("  </tr>");
        head.append(" </thead>");
    }

    private int drawBody(BaseTableInterface<?> table, List<?> fields, StringBuilder body)
            throws JspException, EMFError {
        body.append(" <tbody>");
        int saveRow = 0;
        // Righe
        int i = 0;
        String detailAuth = getDetailAuth();
        String viewAction = detailAuth + "/view";
        String editAction = detailAuth + "/edit";
        String deleteAction = detailAuth + "/delete";
        for (BaseRowInterface row : table) {
            if (table.getPageSize() <= 0 || (i >= table.getFirstRowPageIndex() && i <= table.getLastRowPageIndex())) {
                body.append("  <tr>");

                String className = "";
                if (i == table.getCurrentRowIndex() && !isMultiRowEdit()) {
                    className = (i % 2 == 0) ? "rigaPari rigaCorrente" : "rigaDispari rigaCorrente";
                } else {
                    className = (i % 2 == 0) ? "rigaPari" : "rigaDispari";
                }
                if (isViewAction() && !fields.isHideDetailButton()) {
                    debugAuthorization(body, viewAction);
                    if (isRowVisible(row)) {
                        body.append("   <td class=\"" + className + "\">" + getLink("", ListAction.NE_DETTAGLIO_VIEW,
                                DETTAGLIO, null, null, i, false, getComponent().isMasterList()) + "</td>");
                    } else {
                        body.append("   <td class=\"" + className + "\"></td>");
                    }
                }

                for (SingleValueField<?> aafield : fields) {

                    SingleValueField<?> field = (SingleValueField<?>) aafield.clone();

                    field.format(row);

                    String strValue = field.getHtmlDecodedValue();

                    if (aafield.isEditMode() && (i == table.getCurrentRowIndex() || isMultiRowEdit())) {
                        className += " editable ";
                        saveRow++;
                        body.append(
                                "   <td title=\"Seleziona la riga da modificare\" class=\""
                                        + className + "\">" + FieldTag.Factory.htmlField(field, " w70",
                                                ContainerTag.LEFT, getActionName(), i, false, StringUtils.EMPTY)
                                        + "\n");
                        body.append("   <input type=\"hidden\" value=\"" + strValue + "\" name=\"" + field.getName()
                                + Values.OPERATION_SEPARATOR + i + "\" />\n");
                        body.append("   </td>");

                    } else {
                        String styleClass = field.isHidden() ? " displayNone " : "";
                        body.append("   <td class=\"" + className + styleClass + "\">\n");
                        body.append("     " + strValue + "\n");
                        body.append("     <input type=\"hidden\" value=\"" + strValue + "\" name=\"" + field.getName()
                                + Values.OPERATION_SEPARATOR + i + "\" />\n");
                        body.append("   </td>");
                    }

                }

                if (isEditAction() && !fields.isHideUpdateButton()) {
                    if (isRowEditable(row) && !getComponent().isHideUpdateButton()) {
                        debugAuthorization(body, editAction);
                        if (getComponent().isEditable()) {
                            if (i != table.getCurrentRowIndex() && !isMultiRowEdit()) {
                                body.append("   <td class=\"" + className + "\">\n");
                                body.append("     " + getSubmit(ListAction.NE_DETTAGLIO_UPDATE, UPDATE_STYLE,
                                        "Modifica", "", i, false, getComponent().isMasterList()) + "\n");
                                body.append("   </td>");
                            } else {
                                body.append("   <td class=\"" + className + "\"></td>\n");
                            }
                        } else {
                            body.append(
                                    "   <td class=\""
                                            + className + "\">" + getLink("", ListAction.NE_DETTAGLIO_UPDATE,
                                                    UPDATE_IMG, null, null, i, false, getComponent().isMasterList())
                                            + "</td>");
                        }

                    } else {
                        body.append("   <td class=\"" + className + "\"></td>");
                    }
                }
                if (isDeleteAction() && !fields.isHideDeleteButton()) {
                    if (isRowDeletable(row)) {
                        body.append(
                                "   <td class=\""
                                        + className + "\">" + getLink("", ListAction.NE_DETTAGLIO_CONFIRM_DELETE,
                                                DELETE_IMG, null, null, i, false, getComponent().isMasterList())
                                        + "</td>");
                    } else {
                        body.append("   <td class=\"" + className + "\"></td>");
                    }
                }

                body.append("  </tr>");
            }

            i++;
        }

        body.append(" </tbody>");
        return saveRow;
    }

    /**
     * Calcola le operazioni disponibili su questa tabella. Utilizzato per generare le colonne necessarie da presentare
     * su interfaccia
     *
     * @param table
     */
    private void calculateOperations(BaseTableInterface<?> table) {
        boolean viewCheck = false;
        boolean editCheck = false;
        boolean deleteCheck = false;

        for (BaseRowInterface row : table) {

            if (!viewCheck && isRowVisible(row)) {
                viewCheck = true;
            }

            if (!editCheck && isRowEditable(row)) {// se almeno una riga è modificabile
                editCheck = true;
            }

            if (!deleteCheck && isRowDeletable(row)) {
                deleteCheck = true;
            }
        }
        setContentVisible(viewCheck);
        setContentEditable(editCheck);
        setContentDeletable(deleteCheck);

    }

    private void calculateAuthorization() {
        String detailAuth = getDetailAuth();
        String viewAction = detailAuth + "/view";
        String editAction = detailAuth + "/edit";
        String deleteAction = detailAuth + "/delete";
        String addAction = detailAuth + "/add";
        String removeAction = detailAuth + "/remove";
        setViewAction(isUserAuthorized(viewAction));
        setEditAction(isUserAuthorized(editAction));
        setDeleteAction(isUserAuthorized(deleteAction));
        setAddAction(isUserAuthorized(addAction));
        setRemoveAction(isUserAuthorized(removeAction));
    }

    private String getDetailAuth() {
        return "detail/" + getForm().getClass().getSimpleName() + "#" + getName();
    }

    public boolean isHideOperationButton() {
        return hideOperationButton;
    }

    public void setHideOperationButton(boolean hideOperationButton) {
        this.hideOperationButton = hideOperationButton;
    }

}
