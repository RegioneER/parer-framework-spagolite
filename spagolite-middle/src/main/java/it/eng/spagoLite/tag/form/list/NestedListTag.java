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

package it.eng.spagoLite.tag.form.list;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoIFace.Values;
import it.eng.spagoLite.actions.form.ListAction;
import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.db.base.BaseTableInterface;
import it.eng.spagoLite.db.base.table.BaseTable;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.form.fields.impl.ComboBox;
import it.eng.spagoLite.form.list.List;
import it.eng.spagoLite.tag.form.fields.FieldTag;
import it.eng.spagoLite.tag.form.formLayout.ContainerTag;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

public class NestedListTag extends AbstractListTag {

    private static final long serialVersionUID = 1L;

    private boolean hideOperationButton = false;
    private String subListName;

    public boolean isHideOperationButton() {
        return hideOperationButton;
    }

    public void setHideOperationButton(boolean hideOperationButton) {
        this.hideOperationButton = hideOperationButton;
    }

    public String getSubListName() {
        return subListName;
    }

    public void setSubListName(String subListName) {
        this.subListName = subListName;
    }

    // private String getSortLink(SingleValueField<?> field) {
    // String image = "";
    //
    // SortingRule sortingRule = getComponent().getTable().getLastSortingRule();
    // String colummName = StringUtils.isEmpty(field.getAlias()) ? field.getName() : field.getAlias();
    // if (sortingRule != null && sortingRule.getSortType() == SortingRule.ASC &&
    // sortingRule.getColumnName().equalsIgnoreCase(colummName)) {
    // image = AbstractListTag.ASC_IMG;
    // } else if (sortingRule != null && sortingRule.getSortType() == SortingRule.DESC &&
    // sortingRule.getColumnName().equalsIgnoreCase(colummName)) {
    // image = AbstractListTag.DESC_IMG;
    // }
    //
    // return "<a href=\"" + getOperationUrl("listSortOnClick", "table=" + getName() + "&amp;column=" + field.getName())
    // + "\">" + field.getHtmlDescription() + image + "</a>";
    // }

    @Override
    public int doStartTag() throws JspException {
        try {
            List<?> fields = getComponent();
            List<?> secondaryFields = getComponent(getSubListName());

            BaseTableInterface<?> table = getComponent().getTable();

            if (table != null) {
                if (table.size() == 0) {
                    writeln("  <div class=\"slLabel\" ><i>Nessun elemento trovato</i></div>");
                    return SKIP_BODY;
                }

                calculateOperations(table);

                writeln("");
                writeln("<!-- Lista: " + getName() + " -->");
                String title = "";
                if (StringUtils.isNotBlank(getComponent().getTitle())) {
                    title = " title=\"" + getComponent().getTitle() + "\"";
                }
                String styleClass = "list";
                if (fields.isHidden()) {
                    styleClass += " displayNone";
                }

                // FIXME: l'idea dovrebbe essere giusta, l'implementazione fa schifo
                StringBuilder head = new StringBuilder();
                StringBuilder body = new StringBuilder();

                drawBody(table, fields, secondaryFields, body);
                drawHead(styleClass, title, fields, secondaryFields, head);

                writeln(head);
                writeln(body);
                writeln("</table>");
            }

        } catch (EMFError e) {
            throw new JspException(e);
        }

        return SKIP_BODY;
    }

    private void drawHead(String styleClass, String title, List<?> fields, List<?> secondaryFields,
            StringBuilder head) {
        head.append("<table id=\"" + getName() + "\" class=\"" + styleClass + "\" summary=\"" + fields.getDescription()
                + "\" " + title + ">");
        head.append(" <thead>");
        head.append("  <tr>");

        // colonne per l'immagine di visualizzazione
        if (isContentVisible() && !fields.isHideDetailButton()) {
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

        // Ripete le intestazioni per la nested table
        for (SingleValueField<?> field : secondaryFields) {
            if (!field.isHidden()) {
                head.append("    <th>" + getSortLink(field) + "</th>");
            } else {
                head.append("    <th class=\"displayNone\">" + getSortLink(field) + "</th>");
            }
        }

        // colonne per la modifica, l'eliminazione e l'inserimento:
        if (isContentEditable() && !fields.isHideUpdateButton() && !isHideOperationButton()) {
            head.append("   <th></th>");
        }
        if (isContentDeletable() && !fields.isHideDeleteButton() && !isHideOperationButton()) {
            head.append("   <th></th>");
        }

        head.append("  </tr>");
        head.append(" </thead>");
    }

    private void drawBody(BaseTableInterface<?> table, List<?> fields, List<?> secondaryFields, StringBuilder body)
            throws JspException, EMFError {
        body.append(" <tbody>");
        int i = 0;
        int numeroRigheNestedTable = 1;

        for (BaseRowInterface row : table) {
            if (table.getPageSize() <= 0 || (i >= table.getFirstRowPageIndex() && i <= table.getLastRowPageIndex())) {
                body.append("  <tr>");

                BaseTable nestedTable = (BaseTable) row.getObject(Values.SUB_LIST);
                if (nestedTable != null) {
                    numeroRigheNestedTable = nestedTable.size();
                } else {
                    numeroRigheNestedTable = 1;
                }

                String className = "";
                if (i == table.getCurrentRowIndex() && !isMultiRowEdit()) {
                    className = (i % 2 == 0) ? "rigaPari rigaCorrente" : "rigaDispari rigaCorrente";
                } else {
                    className = (i % 2 == 0) ? "rigaPari" : "rigaDispari";
                }

                if (!fields.isHideDetailButton()) {
                    if (isRowVisible(row)) {
                        body.append("   <td class=\"" + className + "\" rowspan=\"" + numeroRigheNestedTable + "\">"
                                + getLink("", ListAction.NE_DETTAGLIO_VIEW, DETTAGLIO, null, null, i, false, true)
                                + "</td>\n");
                    } else {
                        body.append(
                                "   <td class=\"" + className + "\" rowspan=\"" + numeroRigheNestedTable + "\"></td>");
                    }
                }

                for (SingleValueField<?> aafield : fields) {

                    SingleValueField<?> field = (SingleValueField<?>) aafield.clone();

                    field.format(row);

                    String strValue = "";
                    if (aafield.isEditMode() && (i == table.getCurrentRowIndex() || isMultiRowEdit())) {
                        className += " editable ";

                        body.append("   <td title=\"Seleziona la riga da modificare\" class=\"" + className
                                + "\"  rowspan=\"" + numeroRigheNestedTable + "\">" + FieldTag.Factory.htmlField(field,
                                        " w70", ContainerTag.LEFT, getActionName(), i, false, StringUtils.EMPTY)
                                + "</td>");

                    } else {

                        if (field instanceof ComboBox) {
                            strValue = ((ComboBox<?>) field).getHtmlDecodedValue();
                        } else {
                            strValue = field.getHtmlValue();
                        }

                        if (field.isHidden()) {
                            body.append("   <td class=\"" + className + " displayNone\"  rowspan=\""
                                    + numeroRigheNestedTable + "\"><input type=\"hidden\" value=\"" + strValue
                                    + "\" name=\"" + field.getName() + "\" /></td>");
                        } else {
                            body.append("   <td class=\"" + className + "\" rowspan=\"" + numeroRigheNestedTable + "\">"
                                    + strValue + "</td>");
                        }
                    }

                }

                boolean first = true;
                for (BaseRowInterface nestedRow : nestedTable) {
                    if (first) {
                        first = false;

                        drawNestedRow(nestedRow, secondaryFields, body, className);

                        if (!fields.isHideUpdateButton() && !isHideOperationButton()) {
                            if (isRowEditable(row)) {
                                body.append("   <td class=\""
                                        + className + "\"rowspan=\"" + numeroRigheNestedTable + "\">" + getLink("",
                                                ListAction.NE_DETTAGLIO_UPDATE, UPDATE_IMG, null, null, i, false, true)
                                        + "</td>\n");
                            } else {
                                body.append("   <td class=\"" + className + "\" rowspan=\"" + numeroRigheNestedTable
                                        + "\"></td>");
                            }
                        }
                        if (!fields.isHideDeleteButton() && !isHideOperationButton()) {
                            if (isRowDeletable(row)) {
                                body.append("   <td class=\"" + className + "\" rowspan=\"" + numeroRigheNestedTable
                                        + "\">" + getLink("", ListAction.NE_DETTAGLIO_CONFIRM_DELETE, DELETE_IMG, null,
                                                null, i, false, true)
                                        + "</td>\n");
                            } else {
                                body.append("   <td class=\"" + className + "\" rowspan=\"" + numeroRigheNestedTable
                                        + "\"></td>");
                            }
                        }
                        body.append("  </tr>\n");

                    } else {
                        body.append("   <tr>");
                        drawNestedRow(nestedRow, secondaryFields, body, className);
                        body.append("  </tr>\n");
                    }

                }

            }
            i++;
        }

        body.append(" </tbody>");
    }

    private void drawNestedRow(BaseRowInterface row, List<?> secondaryFields, StringBuilder body, String className)
            throws JspException, EMFError {

        // int i = 0;
        // body.append(" <td class=\"" + className + "\">");
        // body.append(" <table>");

        // if (table.getPageSize() <= 0 || (i >= table.getFirstRowPageIndex() && i
        // <= table.getLastRowPageIndex())) {

        //

        for (SingleValueField<?> aafield : secondaryFields) {

            SingleValueField<?> field = (SingleValueField<?>) aafield.clone();

            field.format(row);

            String strValue = "";

            if (field instanceof ComboBox) {
                strValue = ((ComboBox<?>) field).getHtmlDecodedValue();
            } else {
                strValue = field.getHtmlValue();
            }

            if (field.isHidden()) {
                body.append("   <td class=\"" + className + " displayNone\"><input type=\"hidden\" value=\"" + strValue
                        + "\" name=\"" + field.getName() + "\" /></td>");
            } else {
                body.append("   <td class=\"" + className + "\">" + strValue + "</td>");
            }

            // body.append(" </tr>");
            // }
        }
        // body.append(" </table>");
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

            if (!editCheck && isRowEditable(row)) {// se almeno una riga è
                                                   // modificabile
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

}
