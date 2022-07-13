package it.eng.spagoLite.tag.form;

import it.eng.spagoLite.SessionManager;
import it.eng.spagoLite.form.Form;
import it.eng.spagoLite.message.MessageBox;
import it.eng.spagoLite.security.IUser;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Classe base per
 * 
 * @author Grillini Enrico
 * 
 */
public abstract class BaseTag extends TagSupport {

    private static final long serialVersionUID = 1L;
    protected static Logger logger = LoggerFactory.getLogger(BaseTag.class.getName());

    protected void write(String htmlStream) throws JspException {
        try {
            pageContext.getOut().write(htmlStream);
        } catch (IOException e) {
            throw new JspException(e);
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

    public Form getForm() {
        return SessionManager.getForm(pageContext.getSession());
    }

    public MessageBox getMessageBox() {
        return SessionManager.getMessageBox(pageContext.getSession());
    }

    public String getActionName() {
        return SessionManager.getCurrentAction(pageContext.getSession());
    }

    public String getOperationUrl(String operation, String additionalInfo) {
        return SessionManager.getOperationUrl(pageContext.getSession(), operation, additionalInfo);
    }

    public IUser getUser() {
        return SessionManager.getUser(pageContext.getSession());
    }

    public String getLastPublisher() {
        return SessionManager.getLastPublisher(pageContext.getSession());
    }

}
