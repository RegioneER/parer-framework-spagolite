/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

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
