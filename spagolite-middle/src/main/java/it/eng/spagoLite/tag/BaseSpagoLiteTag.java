package it.eng.spagoLite.tag;

import it.eng.spagoLite.SessionManager;
import it.eng.spagoLite.message.MessageBox;
import it.eng.spagoLite.security.IUser;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.BooleanUtils;

public class BaseSpagoLiteTag extends BodyTagSupport {

    private static final long serialVersionUID = 1L;

    private boolean readonly;
    protected boolean blank;
    protected String target;
    protected String label;
    protected String classlabel;
    protected String codiceOrganizzazione;

    public String getContextPath() {
        return ((HttpServletRequest) pageContext.getRequest()).getContextPath();
    }

    public MessageBox getMessageBox() {
        return SessionManager.getMessageBox(pageContext.getSession());
    }

    public boolean isReadonly() {
        return BooleanUtils.toBoolean(readonly);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getClasslabel() {
        return classlabel;
    }

    public void setClasslabel(String classlable) {
        this.classlabel = classlable;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isBlank() {
        return blank;
    }

    public void setBlank(boolean blank) {
        this.blank = blank;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    protected void write(String htmlStream) throws JspException {
        try {
            pageContext.getOut().print(htmlStream);
        } catch (IOException e) {
        }
    }

    protected void writeln(String htmlStream) throws JspException {
        write(htmlStream + "\n");
    }

    protected void write(StringBuilder htmlStream) throws JspException {
        write(htmlStream.toString());
    }

    protected void writeln(StringBuilder htmlStream) throws JspException {
        writeln(htmlStream.toString());
    }

    public String getActionName() {
        return SessionManager.getCurrentAction(pageContext.getSession());
    }

    public IUser getUser() {
        return (IUser) SessionManager.getUser(pageContext.getSession());
    }

    public String getCodiceOrganizzazione() {
        return codiceOrganizzazione;
    }

    public void setCodiceOrganizzazione(String codiceOrganizzazione) {
        this.codiceOrganizzazione = codiceOrganizzazione;
    }

}
