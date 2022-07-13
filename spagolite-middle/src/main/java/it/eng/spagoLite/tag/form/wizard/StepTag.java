package it.eng.spagoLite.tag.form.wizard;

import it.eng.spagoLite.form.wizard.Step;
import it.eng.spagoLite.form.wizard.Wizard;
import it.eng.spagoLite.tag.form.BaseFormTag;

import javax.servlet.jsp.JspException;

public class StepTag extends BaseFormTag<Step> {

    private static final long serialVersionUID = 1L;

    @Override
    public int doStartTag() throws JspException {
        Step step = getComponent();

        if (!step.isHidden()) {

            writeln("<div class=\"step\">");
            writeln("  <!-- Step " + step.getDescription() + "-->");

            if (AbstractWizardTag.showContent((Wizard) getComponent().getParent(), step)) {
                return EVAL_BODY_INCLUDE;
            }
        }

        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        if (!getComponent().isHidden()) {
            writeln("</div>");
        }

        return EVAL_PAGE;
    }
}
