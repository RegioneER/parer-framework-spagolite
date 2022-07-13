package it.eng.spagoLite.tag.form.formLayout;

import it.eng.spagoLite.tag.BaseSpagoLiteTag;
import it.eng.spagoLite.tag.NewLineTag;

import javax.servlet.jsp.JspException;

public class FilterBoxTag extends BaseSpagoLiteTag {
    private static final long serialVersionUID = 1L;

    @Override
    public int doStartTag() throws JspException {
        writeln("<div class=\"filterBox\">");

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {

        writeln("</div>");
        writeln(NewLineTag.Factory.htmlNewLine(true));

        return EVAL_PAGE;
    }

}
