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
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.tag.form.BaseFormTag;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringEscapeUtils;

public class ValueTag extends BaseFormTag<SingleValueField<?>> {
    private static final long serialVersionUID = 1L;

    private boolean escapeJavaScript = false;

    @Override
    public int doStartTag() throws JspException {
        String str = getComponent().getValue();
        if (escapeJavaScript) {
            str = StringEscapeUtils.escapeJava(str);
        }

        write(JavaScript.stringToHTMLString(str));
        return SKIP_BODY;
    }

    public boolean isEscapeJavaScript() {
        return escapeJavaScript;
    }

    public void setEscapeJavaScript(boolean escapeJavaScript) {
        this.escapeJavaScript = escapeJavaScript;
    }

}
