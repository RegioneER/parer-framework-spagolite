package it.eng.spagoLite.tag.form.wizard;

import it.eng.spagoLite.form.wizard.EndPage;
import it.eng.spagoLite.form.wizard.Wizard;
import it.eng.spagoLite.tag.form.BaseFormTag;

import javax.servlet.jsp.JspException;

public class EndPageTag extends BaseFormTag<EndPage> {

    private static final long serialVersionUID = 1L;

    @Override
    public int doStartTag() throws JspException {
        writeln("<div class=\"endPage\">");

        EndPage endPage = getComponent();
        writeln("  <!-- EndPage " + endPage.getDescription() + "-->");

        if (AbstractWizardTag.showContent((Wizard) getComponent().getParent(), endPage)) {
            return EVAL_BODY_INCLUDE;
        } else {
            writeln("  <!-- EndPage " + endPage.getName() + " non visible-->");
            return SKIP_BODY;
        }

    }

    @Override
    public int doEndTag() throws JspException {
        writeln("</div>");
        return EVAL_PAGE;
    }
}
