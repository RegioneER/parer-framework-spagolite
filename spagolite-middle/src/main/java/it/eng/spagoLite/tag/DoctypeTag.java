package it.eng.spagoLite.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;

public class DoctypeTag extends BaseSpagoLiteTag {
    private static final long serialVersionUID = 1L;

    private String type = "xhtml1t";
    /*
     * html4t: HTML 4.01 Transitional html4f: HTML 4.01 Frameset html4s: HTML 4.01 Strict xhtml1t: XHTML 1.0
     * Transitional xhtml1f: XHTML 1.0 Frameset xhtml1s: XHTML 1.0 Strict xhtml11: XHTML 1.1
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
