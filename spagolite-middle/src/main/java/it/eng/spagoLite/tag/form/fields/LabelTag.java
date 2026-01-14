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

package it.eng.spagoLite.tag.form.fields;

import it.eng.spagoCore.util.JavaScript;
import it.eng.spagoLite.form.fields.Field;
import it.eng.spagoLite.form.fields.impl.Button;
import it.eng.spagoLite.tag.form.BaseFormTag;
import it.eng.spagoLite.tag.form.formLayout.ContainerTag;

import javax.servlet.jsp.JspException;

public class LabelTag extends BaseFormTag<Field> {

    private static final long serialVersionUID = 1L;

    private String labelWidth;
    private String labelPosition;

    public String getLabelWidth() {
        if (labelWidth == null || "".equals(labelWidth)) {
            if (ContainerTag.LEFT.equalsIgnoreCase(getLabelPosition())) {
                return ContainerTag.WIDTH_40;
            } else {
                return ContainerTag.WIDTH_60;
            }
        } else {
            return labelWidth;
        }
    }

    public void setLabelWidth(String labelWidth) {
        this.labelWidth = labelWidth;
    }

    public String getLabelPosition() {
        if (labelPosition != null && labelPosition.equalsIgnoreCase(ContainerTag.RIGHT)) {
            return ContainerTag.RIGHT;
        } else {
            return ContainerTag.LEFT;
        }
    }

    public void setLabelPosition(String labelPosition) {
        this.labelPosition = labelPosition;
    }

    @Override
    public int doStartTag() throws JspException {
        writeln(Factory.htmlLabel(getComponent(), getLabelWidth(), getLabelPosition()));

        return SKIP_BODY;
    }

    public static class Factory {

        public static final String htmlLabel(Field field, String width, String position) {
            String className = ContainerTag.LEFT.equalsIgnoreCase(position) ? "slLabel " + width
                    : "slLabelRight " + width;
            if (field instanceof Button) {
                return "";
            }
            if (field.isHidden()) {
                className += " displayNone ";
            }

            String name = JavaScript.stringToHTMLString(field.getName());
            // String label = /*ContainerTag.LEFT.equalsIgnoreCase(position) ?
            // JavaScript.stringToHTMLString(field.getDescription()) + ":&nbsp;" :*/
            // JavaScript.stringToHTMLString(field.getDescription());
            String label = field.isViewMode()
                    ? JavaScript.stringToHTMLString(field.getDescription()) + ":&nbsp;"
                    : JavaScript.stringToHTMLString(field.getDescription());
            label = field.isRequired() && field.isEditMode() ? " * " + label : label;

            return "<label for=\"" + name + "\" class=\"" + className + "\">" + label + "</label>";
        }

    }
}
