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
import it.eng.spagoLite.form.base.BaseElements.Status;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.form.fields.impl.CheckBox;
import it.eng.spagoLite.form.fields.impl.ComboBox;
import it.eng.spagoLite.form.list.List;
import it.eng.spagoLite.tag.form.fields.FieldTag;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

public class CheckBoxListTag extends AbstractListTag {

    private static final long serialVersionUID = 1L;

    private String checkBoxFieldLabel;
    private String checkBoxFieldName;
    private boolean singleSelection;

    public String getCheckBoxFieldLabel() {
        return checkBoxFieldLabel;
    }

    public void setCheckBoxFieldLabel(String checkBoxFieldLabel) {
        this.checkBoxFieldLabel = checkBoxFieldLabel;
    }

    public String getCheckBoxFieldName() {
        return checkBoxFieldName;
    }

    public void setCheckBoxFieldName(String checkBoxFieldName) {
        this.checkBoxFieldName = checkBoxFieldName;
    }

    public boolean getSingleSelection() {
        return singleSelection;
    }

    public void setSingleSelection(boolean singleSelection) {
        this.singleSelection = singleSelection;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            List<SingleValueField<?>> fields = getComponent();
            BaseTableInterface<?> table = getComponent().getTable();

            if (table != null) {
                if (table.size() == 0) {
                    writeln("  <div class=\"slLabel\" ><i>Nessun elemento trovato</i></div>");
                    return SKIP_BODY;
                }

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
                writeln("<table id=\"" + getName() + "\" class=\"" + styleClass + "\" " + title + " summary=\""
                        + fields.getDescription() + "\">");
                writeln(" <tbody>");
                writeln("  <tr>");

                // Intestazione
                for (SingleValueField<?> field : fields) {
                    if (!field.isHidden()) {
                        // FIXME: anche qui il flusso fa schifo
                        if (!field.getName().equals(getCheckBoxFieldName())) {
                            writeln("    <th>" + field.getHtmlDescription() + "</th>");
                        }
                    }
                }
                // Campo che contiene i checkBox button
                writeln("    <th>" + getCheckBoxFieldLabel() + "</th>");
                writeln("    <th></th>"); // campo che contiente il pulsante di cancellazione
                writeln("  </tr>");

                // Righe
                int i = 0;

                for (Object object : table) {
                    if (table.getPageSize() <= 0
                            || (i >= table.getFirstRowPageIndex() && i <= table.getLastRowPageIndex())) {
                        BaseRowInterface row = (BaseRowInterface) object;
                        writeln("  <tr>");

                        String className = (i % 2 == 0) ? "rigaPari" : "rigaDispari";
                        for (SingleValueField<?> field : fields) {
                            field.format(row);
                            // FIXME: il flusso fa schifo
                            String strValue = "";
                            if (field instanceof ComboBox) {
                                strValue = ((ComboBox<?>) field).getHtmlDecodedValue();
                            } else if (field instanceof CheckBox) {
                                if (field.getName().equals(getCheckBoxFieldName())) {
                                    field.setEditMode();
                                    continue;
                                }

                                String imageSrc = (((CheckBox<?>) field).isChecked()) ? FieldTag.IMG_CHECKBOX_CHECKED
                                        : FieldTag.IMG_CHECKBOX_UNCHECKED;
                                String alt = (((CheckBox<?>) field).isChecked()) ? "Selezionato" : "Non selezionato";
                                strValue = "<img src=\"" + imageSrc + "\" alt=\"" + alt + "\"/>";
                            } else if (field instanceof CheckBox<?>) {
                                if (field.getName().equals(getCheckBoxFieldName())) {
                                    field.setEditMode();
                                    continue;
                                }

                                String imageSrc = (((CheckBox<?>) field).isChecked()) ? FieldTag.IMG_CHECKBOX_CHECKED
                                        : FieldTag.IMG_CHECKBOX_UNCHECKED;
                                String alt = (((CheckBox<?>) field).isChecked()) ? "Selezionato" : "Non selezionato";
                                strValue = "<img src=\"" + imageSrc + "\" alt=\"" + alt + "\"/>";
                            } else {
                                strValue = field.getHtmlValue();
                            }

                            if (!field.isHidden()) {
                                writeln("   <td class=\"" + className + "\">" + strValue + "</td>");
                            }

                        }

                        String singleSelection = (getSingleSelection()) ? "singleSelection" : "multipleSelection";
                        // Campo che contiene i checkBox button
                        Object value = row.getObject(getCheckBoxFieldName());
                        boolean isChecked = CheckBox.valueChecked.equals((String) value);
                        if (getComponent().getStatus().equals(Status.view)) {
                            String imageSrc = (isChecked) ? FieldTag.IMG_CHECKBOX_CHECKED
                                    : FieldTag.IMG_CHECKBOX_UNCHECKED;
                            String alt = (isChecked) ? "Selezionato" : "Non selezionato";
                            writeln("   <td class=\"" + className + "\"><img src=\"" + imageSrc + "\" alt=\"" + alt
                                    + "\"/></td>");
                            writeln("   <td class=\"" + className + "\"></td>"); // cancellazione dell'elemento
                        } else {
                            getComponent().setStatus(Status.update);
                            String checked = (isChecked) ? " checked=\"checked\" " : "";
                            String checkedValue = (isChecked) ? "S" : "N";
                            writeln("   <td class=\"" + className + "\"><input type=\"checkBox\" " + checked
                                    + " name=\"" + getName() + i + "_checkBox\" value=\"S\" type=\"" + singleSelection
                                    + "\"/></td>");
                            writeln("   <td class=\"" + className + "\">" + getLink("",
                                    ListAction.NE_DETTAGLIO_CONFIRM_DELETE, DELETE_IMG, "", "", i, false, true)
                                    + "</td>"); // cancellazione dell'elemento
                        }

                        writeln("  </tr>");
                    }
                    i++;
                }

                writeln(" </tbody>");
                writeln("</table>");
            }
        } catch (EMFError e) {
            throw new JspException(e);
        }

        return SKIP_BODY;
    }
}
