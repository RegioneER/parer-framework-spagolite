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

import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

public abstract class AbstractLblSelectTag extends AbstractLblTag {

    private static final long serialVersionUID = 1L;

    private String elements;

    public String getElements() {
        return elements;
    }

    public void setElements(String elements) {
        this.elements = elements;
    }

    public int doStartTag() throws JspException {
        return EVAL_BODY_AGAIN;
    }

    protected String getOption() {
        // Scelgo il pool di option da rappresentare
        String buffer = getElements();
        if (getElements() == null || "".equals(getElements().trim())) {
            buffer = getBodyContent().getString();
        }
        buffer = buffer.replace("\r", "").replace("\t", "").replace(",", "\n");
        buffer = StringUtils.strip(buffer, " ");

        // Scrivo le Option
        StringBuffer stringBuffer = new StringBuffer();
        StringTokenizer st = new StringTokenizer(buffer, "\n");
        while (st.hasMoreTokens()) {
            String valore = JavaScript.stringToHTMLString(st.nextToken().trim());
            stringBuffer.append("<option value=\"" + valore + "\">" + valore + "</option>");
        }

        return stringBuffer.toString();
    }

}
