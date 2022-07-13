package it.eng.spagoLite.tag;

import javax.servlet.jsp.JspException;

public class BodyTag extends BaseSpagoLiteTag {

    private static final long serialVersionUID = 1L;

    @Override
    public int doStartTag() throws JspException {
        writeln("<body>");
        writeln("<div id=\"helpDialog\"></div>");

        return EVAL_BODY_INCLUDE;

    }

    @Override
    public int doEndTag() throws JspException {

        writeln("</body>");
        return EVAL_PAGE;
    }

}
