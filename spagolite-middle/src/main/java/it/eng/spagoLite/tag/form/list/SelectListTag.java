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

public class SelectListTag extends AbstractListTag {

    /**
     * Implementa una lista di cui è possibile scegliere una riga ed eseguire un'azione con la riga selezionata
     */
    private static final long serialVersionUID = 1L;

    private boolean addList = true;

    private boolean hideOperationButton = false;

    @Override
    public int doStartTag() throws JspException {
        try {
            List<SingleValueField<?>> fields = getComponent();
            BaseTableInterface<?> table = getComponent().getTable();
            calculateAuthorization();
            String detailAuth = getDetailAuth();
            String viewAction = detailAuth + "/view";
            String editAction = detailAuth + "/edit";
            String deleteAction = detailAuth + "/delete";
            String addAction = detailAuth + "/add";
            String removeAction = detailAuth + "/remove";
            if (table != null) {
                if (table.size() == 0) {
                    if (addList) {
                        writeln("  <div class=\"slLabel\" ><i>Nessun elemento trovato</i></div>");
                    }
                    return SKIP_BODY;
                }
                calculateOperations(table);

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
                writeln("");
                writeln("<!-- Lista: " + getName() + " -->");
                writeln("<table id=\"" + getName() + "\" class=\"" + styleClass + "\" " + title + " summary=\""
                        + fields.getDescription() + "\">");
                writeln(" <tbody>");
                writeln("  <tr>");

                // Intestazione di add o remove
                if ((addList && isAddAction()) || (!addList && isRemoveAction())) {
                    writeln("    <th></th>");
                }
                // colonne per l'immagine di visualizzazione
                if (isViewAction() && !fields.isHideDetailButton()) {
                    writeln("   <th></th>");
                }

                for (SingleValueField<?> field : fields) {
                    String headerContent = isSortable() ? getSortLink(field) : field.getHtmlDescription();
                    if (!field.isHidden()) {
                        if (field instanceof Link) {
                            String linkAction = "";
                            if (((Link) field).isTargetAList()) {
                                // se il link è usato per visualizzare un
                                // dettaglio ..
                                linkAction = "detail/" + getForm().getClass().getSimpleName() + "#"
                                        + ((Link) field).getTarget() + "/view";
                            } else {
                                // se il link è usato per eseguire un metodo ..
                                linkAction = "detail/" + getForm().getClass().getSimpleName() + "#" + getName() + "/"
                                        + ((Link) field).getTarget();
                            }
                            // Controllo che sia autorizzato a visualizzare il
                            // link
                            if (isUserAuthorized(linkAction)) {
                                writeln("    <th></th>\n");
                            }
                        } else {
                            writeln("    <th>" + headerContent + "</th>");
                        }
                    } else {
                        writeln("    <th class=\"displayNone\">" + headerContent + "</th>\n");
                    }
                }

                if (!fields.isReadOnly()) {
                    // colonne per la modifica, l'eliminazione e l'inserimento:
                    if (isEditAction() && isContentEditable() && !fields.isHideUpdateButton()
                            && !isHideOperationButton()) {
                        writeln("   <th></th>\n");
                    }
                    if (isDeleteAction() && isContentDeletable() && !fields.isHideDeleteButton()
                            && !isHideOperationButton()) {
                        writeln("   <th></th>\n");
                    }
                }

                writeln("  </tr>");

                // Righe
                int i = 0;

                for (Object object : table) {
                    if (table.getPageSize() <= 0
                            || (i >= table.getFirstRowPageIndex() && i <= table.getLastRowPageIndex())) {
                        BaseRowInterface row = (BaseRowInterface) object;
                        String className = "";

                        if (i == table.getCurrentRowIndex()) {
                            className = (i % 2 == 0) ? "rigaPari rigaCorrente" : "rigaDispari rigaCorrente";
                        } else {
                            className = (i % 2 == 0) ? "rigaPari" : "rigaDispari";
                        }

                        writeln("  <tr>");
                        // Controllo se è presente un filtro per cui non debba inserire l'icona di aggiunta..
                        String visibilityProperty = getVisibilityProperty();
                        String sAction = null;
                        if (addList && isAddAction()) {
                            debugAuthorization(addAction);
                            sAction = "    <td class=\"" + className + "\">" + getSubmit(ListAction.NE_DETTAGLIO_SELECT,
                                    SELECT_STYLE, "Seleziona riga", "", i, false) + "</td>";
                            if (visibilityProperty != null) {
                                boolean isSelectVisible = controlVisibilityProperty(row, visibilityProperty);
                                sAction = isSelectVisible ? sAction : "<td class=\"" + className + "\"></td>";
                            }
                        } else if (!addList && isRemoveAction()) {
                            debugAuthorization(removeAction);
                            sAction = "    <td class=\"" + className + "\">" + getSubmit(ListAction.NE_DETTAGLIO_SELECT,
                                    DESELECT_STYLE, "Rimuovi riga", "", i, false) + "</td>";
                        }
                        if (sAction != null && sAction.length() > 0) {
                            writeln(sAction);
                        }
                        if (isViewAction() && !fields.isHideDetailButton()) {
                            debugAuthorization(viewAction);
                            if (isRowVisible(row)) {
                                writeln("   <td class=\"" + className + "\">" + getLink("",
                                        ListAction.NE_DETTAGLIO_VIEW, DETTAGLIO, null, null, i, false, true)
                                        + "</td>\n");
                            } else {
                                writeln("   <td class=\"" + className + "\"></td>\n");
                            }
                        }

                        for (SingleValueField<?> field : fields) {
                            field.format(row);

                            String strValue = "";
                            String titleValue = "";

                            if (field instanceof ComboBox) {
                                strValue = ((ComboBox<?>) field).getHtmlDecodedValue();
                            } else if (field instanceof CheckBox) {
                                field.setEditMode();
                                strValue = FieldTag.Factory.htmlField(field, " w100 center", ContainerTag.LEFT,
                                        getActionName(), i, false, StringUtils.EMPTY);
                            } else if (field instanceof CheckBox<?>) {
                                field.setEditMode();
                                strValue = FieldTag.Factory.htmlField(field, " w100 center", ContainerTag.LEFT,
                                        getActionName(), i, false, StringUtils.EMPTY);
                            } else if (field instanceof Link) {
                                String linkAction = "";
                                if (((Link) field).isTargetAList()) {
                                    // se il link è usato per visualizzare un
                                    // dettaglio ..
                                    linkAction = "detail/" + getForm().getClass().getSimpleName() + "#"
                                            + ((Link) field).getTarget() + "/view";
                                } else {
                                    // se il link è usato per eseguire un metodo
                                    // ..
                                    linkAction = "detail/" + getForm().getClass().getSimpleName() + "#" + getName()
                                            + "/" + ((Link) field).getTarget();
                                }
                                // Controllo che sia autorizzato a visualizzare
                                // il link
                                if (isUserAuthorized(linkAction)) {
                                    debugAuthorization(linkAction);
                                    if (((Link) field).isTargetAList()) {
                                        strValue = "<a title=\"" + ((Link) field).getTooltip() + "\" class=\""
                                                + field.getName() + "\" href=\""
                                                + getOperationUrl("listNavigationOnClick", "table="
                                                        + ((Link) field).getTarget()
                                                        + ((getMainNavTable() != null && !getMainNavTable().isEmpty())
                                                                ? "&amp;mainNavTable=" + getMainNavTable() : "")
                                                        + "&amp;navigationEvent=" + ListAction.NE_DETTAGLIO_VIEW
                                                        + "&amp;" + "riga=" + ((i < 0) ? "-1" : String.valueOf(i))
                                                        + "&amp;forceReload=false")
                                                + "\">&nbsp;</a>&nbsp;";
                                    } else {
                                        final Link link = (Link) field;
                                        String operationUrl;
                                        boolean error = false;
                                        if (StringUtils.isNotBlank(link.getExternalLinkParamApplic())) {
                                            String externalUrl = link.getExternalLinkParamApplic().endsWith("/")
                                                    ? link.getExternalLinkParamApplic() + "detail/"
                                                    : link.getExternalLinkParamApplic() + "/detail/";
                                            String externalId = "";
                                            if (StringUtils.isNotBlank(link.getExternalLinkParamId())) {
                                                Object propertyValue = row.getObject(link.getExternalLinkParamId());
                                                if (propertyValue == null) {
                                                    error = true;
                                                } else {
                                                    externalId = String.valueOf(propertyValue);
                                                }
                                            }
                                            operationUrl = externalUrl + link.getTarget() + externalId;
                                        } else {
                                            operationUrl = getOperationUrl(((Link) field).getTarget(),
                                                    "riga=" + ((i < 0) ? "-1" : String.valueOf(i)));
                                        }
                                        if (!error) {
                                            strValue = "<a title=\"" + ((Link) field).getTooltip() + "\" class=\""
                                                    + field.getName() + "\" href=\"" + operationUrl
                                                    + "\">&nbsp;</a>&nbsp;";
                                        } else {
                                            strValue = field.getHtmlValue();
                                        }
                                    }
                                } else if (!((Link) field).isTargetAList()) {
                                    strValue = field.getHtmlValue();
                                }
                            } else {
                                titleValue = strValue = field.getHtmlValue();
                                if (abbrLongList && titleValue.length() > 100) {
                                    strValue = "<span class=\"longline\">" + strValue + "</span>";
                                }
                            }

                            if (field.isHidden()) {
                                write("   <td class=\"" + className + " displayNone\"><input type=\"hidden\" value=\""
                                        + strValue + "\" name=\"" + field.getName() + "\" /></td>");
                            } else {
                                writeln("   <td  title=\"" + titleValue + "\" class=\"" + className + "\">" + strValue
                                        + "</td>");
                            }

                        }
                        if (!fields.isReadOnly()) {
                            if (isEditAction() && !fields.isHideUpdateButton()) {
                                debugAuthorization(editAction);
                                if (isRowEditable(row)) {
                                    writeln("   <td class=\"" + className + "\">" + getLink("",
                                            ListAction.NE_DETTAGLIO_UPDATE, UPDATE_IMG, null, null, i, false, true)
                                            + "</td>\n");
                                } else {
                                    writeln("   <td class=\"" + className + "\"></td>\n");
                                }
                            }
                            if (isDeleteAction() && !fields.isHideDeleteButton()) {
                                debugAuthorization(deleteAction);
                                if (isRowDeletable(row)) {
                                    writeln("   <td class=\"" + className + "\">"
                                            + getLink("", ListAction.NE_DETTAGLIO_CONFIRM_DELETE, DELETE_IMG, null,
                                                    null, i, false, true)
                                            + "</td>\n");
                                } else {
                                    writeln("   <td class=\"" + className + "\"></td>\n");
                                }
                            }
                        }
                        writeln("  </tr>");
                    }
                    i++;
                    // if (i >= Values.SELECT_LIST_MAX_ROW)
                    // break;

                }

                writeln(" </tbody>");
                writeln("</table>");

                // if (i >= Values.SELECT_LIST_MAX_ROW) {
                // writeln(" <div class=\"slLabel\" ><i>Visualizzati "
                // + i
                // + " record di "
                // + table.size()
                // +
                // ". Introdurre parametri di ricerca pi&ugrave; selettivi per affinare la ricerca.</i></div>");
                // }
            }
        } catch (EMFError e) {
            throw new JspException(e);
        }

        return SKIP_BODY;
    }

    public boolean getAddList() {
        return addList;
    }

    public void setAddList(boolean addList) {
        this.addList = addList;
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

            if (!editCheck && isRowEditable(row)) {// se almeno una riga ï¿½
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
