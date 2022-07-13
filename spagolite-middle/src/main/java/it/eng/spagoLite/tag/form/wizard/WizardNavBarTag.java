package it.eng.spagoLite.tag.form.wizard;

import javax.servlet.jsp.JspException;

public class WizardNavBarTag extends AbstractWizardTag {

    private static final long serialVersionUID = 1L;

    private String insertAction = "toolbar/insert";
    private String editAction = "toolbar/edit";

    @Override
    public int doStartTag() throws JspException {
        if (getComponent().isEndPage() && getComponent().getEndPage().isHideBar()) {
            return SKIP_BODY;
        }

        writeln("<div class=\"wizardBar\">");
        calculateAuthorization();

        writeCancel();
        if (isEditAction() || isInsertAction()) {
            debugAuthorization(insertAction);
            debugAuthorization(editAction);
            writeSave();
        }
        writePrev();
        writeNext();

        writeln("&nbsp;</div>");

        return SKIP_BODY;
    }

    private void calculateAuthorization() {
        setEditAction(isUserAuthorized(editAction));
        setInsertAction(isUserAuthorized(insertAction));
    }
}
