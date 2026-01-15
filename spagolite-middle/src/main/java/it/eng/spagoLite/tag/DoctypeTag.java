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

package it.eng.spagoLite.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;

public class DoctypeTag extends BaseSpagoLiteTag {
    private static final long serialVersionUID = 1L;

    private String type = "xhtml1t";
    /*
     * html4t: HTML 4.01 Transitional html4f: HTML 4.01 Frameset html4s: HTML 4.01 Strict xhtml1t:
     * XHTML 1.0 Transitional xhtml1f: XHTML 1.0 Frameset xhtml1s: XHTML 1.0 Strict xhtml11: XHTML
     * 1.1
     */

    private static Map<String, String> doctype = new HashMap<String, String>();
    static {

        doctype.put("html4s",
                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
        doctype.put("html4t",
                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
        doctype.put("html4f",
                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\" \"http://www.w3.org/TR/html4/frameset.dtd\">");
        doctype.put("xhtml1s",
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        doctype.put("xhtml1t",
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        doctype.put("xhtml1f",
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Frameset//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd\">");
        doctype.put("xhtml11",
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int doEndTag() throws JspException {
        writeln(renderSelectStartElement());

        return (EVAL_PAGE);
    }

    protected String renderSelectStartElement() throws JspException {
        String docType = doctype.get(this.getType());
        if (docType == null || docType.equals("")) {
            docType = doctype.get("xhtml1t");
        }
        return docType;
    }

}
