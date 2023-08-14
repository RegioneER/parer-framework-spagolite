package it.eng.spagoLite.tag.form.wizard;

import it.eng.spagoLite.form.wizard.Wizard;
import it.eng.spagoLite.tag.NewLineTag;

import it.eng.spagoLite.SessionManager;
import it.eng.spagoLite.security.IUser;
import it.eng.spagoLite.security.profile.Pagina;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

public class WizardTag extends AbstractWizardTag {

    private static final long serialVersionUID = 1L;

    protected String writeStepCounter() throws JspException {
        if (!getComponent().isHideStepCounter()) {
            return " (Passo " + (getComponent().currentStepIndex() + 1) + " di " + getComponent().stepSize() + ")";
        } else {
            return "";
        }

    }

    @Override
    public int doStartTag() throws JspException {
        Wizard wizard = getComponent();

        writeln("");
        writeln("<!-- Wizard Tag: " + wizard.getName() + "-->");
        writeln(NewLineTag.Factory.htmlNewLine());
        writeln("<div>");
        writeln(" <h2 class=\"wizardTitle\">" + wizard.getDescription() + " - " + getCurrentStep().getDescription()
                + writeStepCounter());
        IUser utente = this.getUser();
        if (utente != null) {
            String pagina = SessionManager.getLastPublisher(pageContext.getSession());
            Pagina paginaSelezionata = null;
            String href = "";
            if (utente.getProfile() != null
                    && (paginaSelezionata = (Pagina) utente.getProfile().getChild(pagina)) != null
                    && paginaSelezionata.isHelpAvailable()) {
                href = "javascript:mostraHelpPagina();";
                writeln("<a title=\"Vai alla pagina di help online\" href=\"" + href + "\">");
                writeln("<img src=\"" + ((HttpServletRequest) pageContext.getRequest()).getContextPath()
                        + "/img/help.png\" title=\"Help online\" alt=\"Help online\" />");
                writeln("</a>");
            }
        }
        writeln("</h2>");
        writeln("</div>");
        writeln("<div class=\"wizard\">");
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        writeln("</div>");
        return EVAL_PAGE;
    }

}
