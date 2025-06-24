/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 */

package it.eng.spagoLite.tag;

import javax.servlet.jsp.JspException;

import it.eng.spagoLite.message.Message;
import it.eng.spagoLite.message.Message.MessageLevel;
import it.eng.spagoLite.message.MessageBox;
import it.eng.spagoLite.message.MessageBox.ViewMode;
import it.eng.spagoLite.tag.form.BaseTag;

public class MessageBoxTag extends BaseTag {

    private static final long serialVersionUID = 1L;

    @Override
    public int doStartTag() throws JspException {
	writeln(Factory.drawMessageBox(getMessageBox()));
	return SKIP_BODY;

    }

    private static String getStyleClass(ViewMode mode, Message.MessageLevel messageLevel) {
	boolean alertMode = ViewMode.alert.equals(mode);
	String styleClass = "";
	if (messageLevel.equals(MessageLevel.INF)) {
	    styleClass = alertMode ? " ui-state-highlight infoBox " : " plainInfo ";
	} else if (messageLevel.equals(MessageLevel.WAR)) {
	    styleClass = alertMode ? " ui-state-highlight warnBox " : " plainWarning ";
	} else if (messageLevel.equals(MessageLevel.ERR)) {
	    styleClass = alertMode ? " ui-state-error errorBox " : " plainError ";
	} else if (messageLevel.equals(MessageLevel.FATAL)) {
	    styleClass = alertMode ? " ui-state-error fatalBox " : " plainFatal ";
	}
	return styleClass;
    }

    private static String getStyleSpanClass(Message.MessageLevel messageLevel) {

	String styleClass = "";
	if (messageLevel.equals(MessageLevel.INF)) {
	    styleClass = "ui-icon-info";
	} else if (messageLevel.equals(MessageLevel.WAR)) {
	    styleClass = "ui-icon-alert";
	} else if (messageLevel.equals(MessageLevel.ERR)) {
	    styleClass = "ui-icon-alert";
	} else if (messageLevel.equals(MessageLevel.FATAL)) {
	    styleClass = "ui-icon-alert";
	}
	return styleClass;
    }

    private static MessageLevel getMaxLevel(MessageBox messageBox) {
	MessageLevel result = null;
	if (messageBox.hasFatal()) {
	    result = MessageLevel.FATAL;
	} else if (messageBox.hasError()) {
	    result = MessageLevel.ERR;
	} else if (messageBox.hasWarning()) {
	    result = MessageLevel.WAR;
	} else {
	    result = MessageLevel.INF;
	}
	return result;
    }

    public static class Factory {
	public static String drawMessageBox(MessageBox messageBox) {
	    StringBuilder result = new StringBuilder("");
	    if (!messageBox.isEmpty()) {
		result.append("\n");
		result.append("  <!-- Message Box -->\n");
		result.append("  <div class=\"messages "
			+ getStyleClass(messageBox.getViewMode(), getMaxLevel(messageBox))
			+ "\">\n");
		result.append("  <ul>\n");
		result.append("  <span class=\"ui-icon "
			+ getStyleSpanClass(getMaxLevel(messageBox)) + "\"></span>\n");
		for (Message message : messageBox) {
		    // boolean info = message.getMessageLevel().equals(Message.MessageLevel.INF) ||
		    // message.getText().startsWith(Message.MessageLevel.INF.toString());
		    boolean warning = message.getMessageLevel().equals(Message.MessageLevel.WAR)
			    || message.getText().startsWith(Message.MessageLevel.WAR.toString());
		    boolean error = message.getMessageLevel().equals(Message.MessageLevel.ERR)
			    || message.getText().startsWith(Message.MessageLevel.ERR.toString());
		    boolean fatal = message.getMessageLevel().equals(Message.MessageLevel.FATAL)
			    || message.getText().startsWith(Message.MessageLevel.FATAL.toString());
		    String liClass = "";// fatal ? " fatal " : error ? " error " : warning ? "
					// warning " : " info ";
		    result.append(
			    // "<li class=\"message " + liClass + " \">"+
			    message.getText()
				    // +"</li>
				    + "<br/>\n");

		    // 05/09/2018: eliminato stack trace errore nel sorgente pagina
		    /*
		     * if (message.getThrowable() != null) { result.append("<!-- Stack Trace:" +
		     * soundex(message.getThrowable().getStackTrace(), "\n") + "-->\n"); Throwable
		     * cause = message.getThrowable().getCause(); while(cause!=null){
		     * result.append("<!-- Cause Stack Trace:" + soundex(cause.getStackTrace(),
		     * "\n") + "-->\n"); cause = cause.getCause(); } //
		     * if(message.getThrowable().getCause() != null){ //
		     * result.append("<!-- Cause Stack Trace:" +
		     * Logger.soundex(message.getThrowable().getCause().getStackTrace(), "\n") +
		     * "-->\n"); // } }
		     */
		}
		result.append("  </ul>\n");
		result.append("</div>\n");
		// FIXME: Andrebbe del BaseAction
		messageBox.clear();
	    }
	    return result.toString();
	}

    }

    private static String soundex(StackTraceElement locations[], String lineSeparator) {
	StringBuilder stringBuilder = new StringBuilder();
	if (locations != null) {
	    for (StackTraceElement el : locations) {
		stringBuilder.append(el.toString()).append(lineSeparator);
	    }
	}
	return stringBuilder.toString();
    }

}
