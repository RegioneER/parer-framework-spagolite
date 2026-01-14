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

package it.eng.spagoLite.message;

import it.eng.spagoLite.FrameElement;
import it.eng.spagoLite.message.Message.MessageLevel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

public class MessageBox extends FrameElement implements Iterable<Message> {

    private static final long serialVersionUID = 1L;

    static final String ERR_ORA_MSG = "ERR";
    static final String INF_ORA_MSG = "INF";
    static final String WAR_ORA_MSG = "WAR";
    static final String SEPARATORE_ORA_MSG = "#";

    public enum ViewMode {
        alert, plain
    }

    private List<Message> list;
    private ViewMode viewMode;

    public MessageBox() {
        clear();
    }

    public ViewMode getViewMode() {
        return viewMode;
    }

    public void setViewMode(ViewMode viewMode) {
        this.viewMode = viewMode;
    }

    public Iterator<Message> iterator() {
        return list.iterator();
    }

    public void clear() {
        list = new ArrayList<Message>();
        viewMode = ViewMode.alert;
    }

    /**
     * Aggiunge messaggi {@link Message} alla propria {@link MessageBox#list} di messaggi
     * parserizzando la stringa <em>regexOracle</em> in input che deve essere del tipo
     * <b>ERRmsg#INFmsg#WARmsg</b> dove ERR INF e WAR sono i livelli del messaggio oracle.
     *
     * @param regexOracle value regular expression
     */
    public void addMessagesFromOracle(String regexOracle) {
        if (regexOracle == null || "".equals(regexOracle)) {
            return;
        }

        String[] oraMessages = StringUtils.split(regexOracle, SEPARATORE_ORA_MSG);

        for (String oraMessage : oraMessages) {
            String livello = oraMessage.substring(0, 3);
            String msg = oraMessage.substring(3);
            if (livello.equals(ERR_ORA_MSG)) {
                addError(msg);
            } else if (livello.equals(WAR_ORA_MSG)) {
                addWarning(msg);
            } else if (livello.equals(INF_ORA_MSG)) {
                addInfo(msg);
            }
        }
    }

    public void addMessage(Message message) {
        if (message != null) {
            list.add(message);
        }
    }

    public void addMessages(List<Message> messages) {
        for (Message message : messages) {
            list.add(message);
        }
    }

    public void addMessage(MessageLevel level, String text) {
        list.add(new Message(level, text));
    }

    public void addInfo(String text) {
        list.add(new Message(MessageLevel.INF, text));
    }

    public void addWarning(String text) {
        list.add(new Message(MessageLevel.WAR, text));
    }

    public void addError(String text) {
        list.add(new Message(MessageLevel.ERR, text));
    }

    public void addError(String text, Throwable throwable) {
        list.add(new Message(MessageLevel.ERR, text, throwable));
    }

    public void addFatal(String text) {
        list.add(new Message(MessageLevel.FATAL, text));
    }

    public void addFatal(String text, Throwable throwable) {
        list.add(new Message(MessageLevel.FATAL, text, throwable));
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean hasInfo() {
        for (Message message : list) {
            if (message.getMessageLevel().equals(MessageLevel.INF)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasWarning() {
        for (Message message : list) {
            if (message.getMessageLevel().equals(MessageLevel.WAR)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasError() {
        for (Message message : list) {
            if (message.getMessageLevel().equals(MessageLevel.ERR)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasFatal() {
        for (Message message : list) {
            if (message.getMessageLevel().equals(MessageLevel.FATAL)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Element asXml() {
        Element element = super.asXml();

        for (Message message : list) {
            element.add(message.asXml());
        }

        return element;
    }
}
