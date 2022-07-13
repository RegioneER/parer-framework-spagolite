package it.eng.spagoLite.tag.form.pageLayout;

import javax.servlet.jsp.JspException;

import it.eng.spagoLite.SessionManager;
import it.eng.spagoLite.message.MessageBox.ViewMode;
import it.eng.spagoLite.tag.MessageBoxTag;

public class SimpleFormTag extends ContentTag {

    private static final long serialVersionUID = 1L;

    @Override
    public int doStartTag() throws JspException {
        String actionUrl = SessionManager.getCurrentActionUrl(pageContext.getSession());

        if (isMultipartForm()) {
            writeln("  <form id=\"" + getId() + "\" action=\"" + actionUrl + super.getCsrfQueryToken()
                    + "\" method=\"post\" enctype=\"multipart/form-data\">");
        } else {
            writeln("  <form id=\"" + getId() + "\" action=\"" + actionUrl + "\" method=\"post\">");
            writeln(getCsrfToken());
        }

        if (getMessageBox() != null && getMessageBox().hasFatal()) {
            getMessageBox().setViewMode(ViewMode.plain);
            writeln(MessageBoxTag.Factory.drawMessageBox(getMessageBox()));
            return SKIP_BODY;
        } else {
            return EVAL_BODY_INCLUDE;
        }

    }

    @Override
    public int doEndTag() throws JspException {
        writeln("  </form>\n");
        return EVAL_PAGE;
    }

    /**
     * @return the id
     */
    @Override
    public String getId() {
        return id == null ? "spagoLiteAppForm" : id;
    }

}
