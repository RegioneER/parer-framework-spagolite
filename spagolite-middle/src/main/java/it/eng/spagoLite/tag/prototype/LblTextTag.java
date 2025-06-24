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

package it.eng.spagoLite.tag.prototype;

import it.eng.spagoCore.util.JavaScript;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

public class LblTextTag extends AbstractLblTag {

    private static final long serialVersionUID = 1L;

    private String text;
    private String maxLength;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    protected void writeControl() throws JspException {
        String text = JavaScript.stringToHTMLString(getText());
        String controlWidth = getControlwidth();
        String maxLength = StringUtils.isNotBlank(getMaxLength()) ? " maxlength=\"" + getMaxLength() + "\" " : "";

        if (isEditable()) {
            writeln(" <input class=\"slText " + controlWidth + "\" type=\"text\" value=\"" + getText() + "\" "
                    + maxLength + " />");
        } else {
            writeln(" <div class=\"slText " + controlWidth + "\" >" + text + "</div>");
        }
    }
}
