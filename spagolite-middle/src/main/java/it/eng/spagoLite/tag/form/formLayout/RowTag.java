package it.eng.spagoLite.tag.form.formLayout;

import it.eng.spagoLite.tag.BaseSpagoLiteTag;
import it.eng.spagoLite.tag.NewLineTag;

import javax.servlet.jsp.JspException;

public class RowTag extends BaseSpagoLiteTag {

    private static final long serialVersionUID = 1L;

    @Override
    public int doStartTag() throws JspException {
        return EVAL_BODY_INCLUDE;

    }

    @Override
    public int doEndTag() throws JspException {
        writeln(NewLineTag.Factory.htmlNewLine());
        return EVAL_PAGE;
    }

}
