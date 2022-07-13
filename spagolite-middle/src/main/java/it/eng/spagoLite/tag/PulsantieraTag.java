package it.eng.spagoLite.tag;

import javax.servlet.jsp.JspException;

public class PulsantieraTag extends BaseSpagoLiteTag {

    private static final long serialVersionUID = 1L;

    @Override
    public int doStartTag() throws JspException {
        writeln("<div class=\"pulsantiera\">");

        return EVAL_BODY_INCLUDE;

    }

    @Override
    public int doEndTag() throws JspException {
        writeln("</div>");
        writeln(NewLineTag.Factory.htmlNewLine(false));

        return EVAL_PAGE;
    }

}
