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

package it.eng.spagoLite.tag.form.list;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoCore.util.JavaScript;
import it.eng.spagoLite.actions.form.ListAction;
import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.db.base.BaseTableInterface;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.form.fields.impl.CheckBox;
import it.eng.spagoLite.form.fields.impl.ComboBox;
import it.eng.spagoLite.form.fields.impl.Link;
import it.eng.spagoLite.form.list.List;
import it.eng.spagoLite.tag.form.fields.FieldTag;
import it.eng.spagoLite.tag.form.formLayout.ContainerTag;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang3.StringUtils;

public class ListTag extends AbstractListTag {

    private static final long serialVersionUID = 1L;

    private boolean hideOperationButton = false;

    // private String getSortLink(SingleValueField<?> field) {
    // String image = "";
    //
    // SortingRule sortingRule = getComponent().getTable().getLastSortingRule();
    // String colummName = StringUtils.isEmpty(field.getAlias()) ?
    // field.getName() : field.getAlias();
    // if (sortingRule != null && sortingRule.getSortType() == SortingRule.ASC
    // && sortingRule.getColumnName().equalsIgnoreCase(colummName)) {
    // image = ASC_IMG;
    // } else if (sortingRule != null && sortingRule.getSortType() ==
    // SortingRule.DESC &&
    // sortingRule.getColumnName().equalsIgnoreCase(colummName)) {
    // image = DESC_IMG;
    // }
    //
    // return "<a href=\"" + getOperationUrl("listSortOnClick", "table=" +
    // getName() + "&amp;column=" + field.getName()) + ((getMainNavTable()!=null
    // && !getMainNavTable().isEmpty()) ?"&amp;mainNavTable=" +
    // getMainNavTable():"")+ "\">" + field.getHtmlDescription() + image +
    // "</a>";
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

                // FIXME: ottimizzare implementazione
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

    private void drawHead(String styleClass, String title, List<?> fields, StringBuilder head,
            int rowEditable) {
        head.append("<table id=\"" + getName() + "\" class=\"" + styleClass + "\" summary=\""
                + fields.getDescription() + "\" " + title + ">\n");
        head.append(" <thead>\n");
        head.append("  <tr>\n");

        // colonne per l'immagine di visualizzazione
        if (isViewAction() && isContentVisible() && !fields.isHideDetailButton()) {
            head.append("   <th></th>\n");
        }

        // Intestazione
        for (SingleValueField<?> field : fields) {
            String beginth = "<th>";
            if (field instanceof CheckBox && !field.isReadonly()) {
                beginth = "<th class=\"cbth\" id=\""
                        + JavaScript.stringToHTMLString(field.getName()) + "\">";
            }
            String headerContent = isSortable() ? getSortLink(field) : field.getHtmlDescription();
            if (!field.isHidden()) {
                if (field instanceof Link) {
                    String linkAction;
                    if (((Link) field).isTargetAList()) {
                        // se il link è usato per visualizzare un dettaglio ..
                        linkAction = "detail/" + getForm().getClass().getSimpleName() + "#"
                                + ((Link) field).getTarget() + "/view";
                    } else {
                        // se il link è usato per eseguire un metodo ..
                        linkAction = "detail/" + getForm().getClass().getSimpleName() + "#"
                                + getName() + "/" + ((Link) field).getTarget();
                    }
                    head.append("    " + beginth
                            + (StringUtils.isNotBlank(field.getHtmlDescription()) ? headerContent
                                    : "")
                            + "</th>\n");
                } else {
                    head.append("    " + beginth + headerContent + "</th>\n");
                }
            } else {
                head.append("    <th class=\"displayNone\">" + headerContent + "</th>\n");
            }
        }

        if (!fields.isReadOnly()) {
            // colonne per la modifica, l'eliminazione e l'inserimento:
            if (isEditAction() && isContentEditable() && !fields.isHideUpdateButton()
                    && !isHideOperationButton()) {
                head.append("   <th></th>\n");
            }
            if (isDeleteAction() && isContentDeletable() && !fields.isHideDeleteButton()
                    && !isHideOperationButton()) {
                head.append("   <th></th>\n");
            }
        }

        head.append("  </tr>\n");
        head.append(" </thead>\n");
    }

    private int drawBody(BaseTableInterface<?> table, List<?> fields, StringBuilder body)
            throws JspException, EMFError {
        body.append(" <tbody>\n");
        int saveRow = 0;
        // Righe
        int i = 0;
        String detailAuth = getDetailAuth();
        String viewAction = detailAuth + "/view";
        String editAction = detailAuth + "/edit";
        String deleteAction = detailAuth + "/delete";
        for (BaseRowInterface row : table) {
            if (table.getPageSize() <= 0
                    || (i >= table.getFirstRowPageIndex() && i <= table.getLastRowPageIndex())) {
                body.append("  <tr>\n");

                String className = "";
                if (i == table.getCurrentRowIndex()) {
                    className = (i % 2 == 0) ? "rigaPari rigaCorrente" : "rigaDispari rigaCorrente";
                } else {
                    className = (i % 2 == 0) ? "rigaPari" : "rigaDispari";
                }
                if (isViewAction() && !fields.isHideDetailButton()) {
                    debugAuthorization(body, viewAction);
                    if (isRowVisible(row)) {
                        body.append("   <td class=\""
                                + className + "\">" + getLink("", ListAction.NE_DETTAGLIO_VIEW,
                                        DETTAGLIO, null, null, i, false, true, getMainNavTable())
                                + "</td>\n");
                    } else {
                        body.append("   <td class=\"" + className + "\"></td>\n");
                    }
                }

                for (SingleValueField<?> aafield : fields) {
                    SingleValueField<?> field = (SingleValueField<?>) aafield.clone();
                    field.format(row);
                    String strValue = "";
                    String titleValue = "";
                    if (field instanceof ComboBox) {
                        strValue = ((ComboBox<?>) field).getHtmlDecodedValue();
                        // Modifica per far apparire correttamente le
                        // checkbox in una lista
                    } else if (field instanceof CheckBox || field instanceof CheckBox<?>) {
                        aafield.setEditMode();
                        field.setEditMode();
                        strValue = FieldTag.Factory.htmlField(field, " w100 center",
                                ContainerTag.LEFT, getActionName(), i, false, StringUtils.EMPTY);
                    } else if (field instanceof Link) {
                        boolean isLinkVisible = controlVisibilityProperty(row,
                                ((Link) field).getVisibilityProperty());
                        if (isLinkVisible) {
                            String linkAction = "";
                            if (((Link) field).isTargetAList()) {
                                // se il link è usato per visualizzare un
                                // dettaglio ..
                                linkAction = "detail/" + getForm().getClass().getSimpleName() + "#"
                                        + ((Link) field).getTarget() + "/view";
                            } else {
                                // se il link è usato per eseguire un
                                // metodo ..
                                linkAction = "detail/" + getForm().getClass().getSimpleName() + "#"
                                        + getName() + "/" + ((Link) field).getTarget();
                            }
                            // Controllo che sia autorizzato a visualizzare
                            // il
                            // link
                            if (isUserAuthorized(linkAction)) {
                                debugAuthorization(body, linkAction);
                                if (field.isHidden()) {
                                    // Se il link è nascosto non genero il
                                    // link
                                    // con anchor tag
                                    strValue = field.getName();
                                } else if (((Link) field).isTargetAList()) {
                                    strValue = "<a title=\"" + ((Link) field).getTooltip()
                                            + "\" class=\"" + field.getName() + "\" href=\""
                                            + getOperationUrl("listNavigationOnClick", "table="
                                                    + ((Link) field).getTarget()
                                                    + ((getMainNavTable() != null
                                                            && !getMainNavTable().isEmpty())
                                                                    ? "&amp;mainNavTable="
                                                                            + getMainNavTable()
                                                                    : "")
                                                    + "&amp;navigationEvent="
                                                    + ListAction.NE_DETTAGLIO_VIEW + "&amp;"
                                                    + "riga=" + ((i < 0) ? "-1" : String.valueOf(i))
                                                    + "&amp;forceReload=false&amp;nomeColonna="
                                                    + field.getName())
                                            + "\">&nbsp;</a>&nbsp;";
                                } else {
                                    final Link link = (Link) field;
                                    String operationUrl;
                                    String targetAttribute = "";
                                    boolean error = false;
                                    if (StringUtils.isNotBlank(link.getExternalLinkParamApplic())) {
                                        String externalUrl = link.getExternalLinkParamApplic()
                                                .endsWith("/")
                                                        ? link.getExternalLinkParamApplic()
                                                                + "detail/"
                                                        : link.getExternalLinkParamApplic()
                                                                + "/detail/";
                                        String externalId = "";
                                        if (StringUtils.isNotBlank(link.getExternalLinkParamId())) {
                                            Object propertyValue = row
                                                    .getObject(link.getExternalLinkParamId());
                                            if (propertyValue == null) {
                                                error = true;
                                            } else {
                                                externalId = "/" + String.valueOf(propertyValue);
                                            }
                                        }
                                        operationUrl = externalUrl + link.getTarget() + externalId;
                                        targetAttribute = " target=\"_blank\"";
                                    } else {
                                        if (StringUtils.isNotBlank(link.getGenericLinkId())) {
                                            String genericLink = "";
                                            Object propertyValue = row
                                                    .getObject(link.getGenericLinkId());
                                            if (propertyValue == null) {
                                                error = true;
                                            } else {
                                                genericLink = String.valueOf(propertyValue);
                                            }
                                            operationUrl = genericLink;
                                            targetAttribute = " target=\"_blank\"";
                                        } else {
                                            operationUrl = getOperationUrl(
                                                    ((Link) field).getTarget(),
                                                    "table=" + getName() + "&riga="
                                                            + ((i < 0) ? "-1" : String.valueOf(i))
                                                            + "&amp;nomeColonna="
                                                            + field.getName());
                                        }
                                    }
                                    if (!error) {
                                        strValue = "<a title=\"" + (link).getTooltip()
                                                + "\" class=\"" + field.getName() + "\" href=\""
                                                + operationUrl + "\" " + targetAttribute + ">"
                                                + field.getHtmlValue() + "</a>&nbsp;";
                                    } else {
                                        strValue = field.getHtmlValue();
                                    }
                                }
                            } else if (!((Link) field).isTargetAList()) {
                                strValue = field.getHtmlValue();
                            }
                        }
                    } else {
                        titleValue = strValue = field.getHtmlValue();
                        strValue = strValue.replaceAll("&lt;br/&gt;", "<br/>");
                        if (abbrLongList && titleValue.length() > 100) {
                            strValue = "<span class=\"longline\">" + strValue + "</span>";
                        }
                    }

                    if (field.isHidden()) {
                        body.append("   <td class=\"" + className
                                + " displayNone\"><input type=\"hidden\" value=\"" + strValue
                                + "\" name=\"" + field.getName() + "\" /></td>\n");
                    } else {
                        body.append("   <td title=\"" + titleValue + "\" class=\"" + className
                                + "\">" + strValue + "</td>\n");
                    }
                }
                if (!fields.isReadOnly()) {
                    if (isEditAction() && !fields.isHideUpdateButton()
                            && !isHideOperationButton()) {
                        debugAuthorization(body, editAction);
                        if (isRowEditable(row)) {
                            body.append(
                                    "   <td class=\"" + className + "\">"
                                            + getLink("", ListAction.NE_DETTAGLIO_UPDATE,
                                                    UPDATE_IMG, null, null, i, false, true)
                                            + "</td>\n");
                        } else {
                            body.append("   <td class=\"" + className + "\"></td>\n");
                        }
                    }
                    if (isDeleteAction() && !fields.isHideDeleteButton()
                            && !isHideOperationButton()) {
                        debugAuthorization(body, deleteAction);
                        if (isRowDeletable(row)) {
                            body.append(
                                    "   <td class=\"" + className + "\">"
                                            + getLink("", ListAction.NE_DETTAGLIO_CONFIRM_DELETE,
                                                    DELETE_IMG, null, null, i, false, true)
                                            + "</td>\n");
                        } else {
                            body.append("   <td class=\"" + className + "\"></td>\n");
                        }
                    }
                }
                body.append("  </tr>\n");
            }
            i++;
        }

        body.append(" </tbody>\n");
        return saveRow;
    }

    /**
     * Calcola le operazioni disponibili su questa tabella. Utilizzato per generare le colonne
     * necessarie da presentare su interfaccia
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

            if (!editCheck && isRowEditable(row)) {// se almeno una riga �
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

    private void calculateAuthorization() {
        String detailAuth = getDetailAuth();
        String viewAction = detailAuth + "/view";
        String editAction = detailAuth + "/edit";
        String deleteAction = detailAuth + "/delete";
        setViewAction(isUserAuthorized(viewAction));
        setEditAction(isUserAuthorized(editAction));
        setDeleteAction(isUserAuthorized(deleteAction));

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
