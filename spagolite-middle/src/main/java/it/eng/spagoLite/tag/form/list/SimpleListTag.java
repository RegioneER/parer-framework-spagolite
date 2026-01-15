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
import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.db.base.BaseTableInterface;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.form.fields.impl.CheckBox;
import it.eng.spagoLite.form.fields.impl.ComboBox;
import it.eng.spagoLite.form.list.List;
import it.eng.spagoLite.tag.form.fields.FieldTag;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

public class SimpleListTag extends AbstractListTag {

    private static final long serialVersionUID = 1L;

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
                writeln("<table id=\"" + getName() + "\" class=\"" + styleClass + "\" " + title
                        + " summary=\"" + fields.getDescription() + "\">");
                writeln(" <tbody>");
                writeln("  <tr>");

                // Intestazione
                for (SingleValueField<?> field : fields) {
                    if (!field.isHidden()) {
                        writeln("    <th>" + field.getHtmlDescription() + "</th>");
                    }
                }
                writeln("  </tr>");

                // Righe
                int i = 0;

                for (Object object : table) {
                    if (table.getPageSize() <= 0 || (i >= table.getFirstRowPageIndex()
                            && i <= table.getLastRowPageIndex())) {
                        BaseRowInterface row = (BaseRowInterface) object;
                        writeln("  <tr>");

                        String className = (i % 2 == 0) ? "rigaPari" : "rigaDispari";
                        for (SingleValueField<?> field : fields) {
                            field.format(row);

                            String strValue = "";

                            if (field instanceof ComboBox) {
                                strValue = ((ComboBox<?>) field).getHtmlDecodedValue();
                            } else if (field instanceof CheckBox) {
                                String imageSrc = (((CheckBox<?>) field).isChecked())
                                        ? FieldTag.IMG_CHECKBOX_CHECKED
                                        : FieldTag.IMG_CHECKBOX_UNCHECKED;
                                String alt = (((CheckBox<?>) field).isChecked()) ? "Selezionato"
                                        : "Non selezionato";
                                strValue = "<img src=\"" + imageSrc + "\" alt=\"" + alt + "\"/>";
                            } else {
                                strValue = field.getHtmlValue();
                            }

                            if (!field.isHidden()) {
                                writeln("   <td class=\"" + className + "\">" + strValue + "</td>");
                            }

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
