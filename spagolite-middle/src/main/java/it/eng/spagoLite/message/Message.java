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

import org.dom4j.Element;

public class Message extends FrameElement {

    // con riferimento al type Oracle presente in LogMessages si sisanagstrutt
    public enum MessageLevel {
        INF, WAR, ERR, FATAL
    };

    private MessageLevel messageLevel;
    private String text;
    private Throwable throwable;

    public Message() {
        this(MessageLevel.INF, "");
    }

    public Message(MessageLevel messageLevel, String text, Throwable throwable) {
        this.messageLevel = messageLevel;
        this.text = text;
        this.throwable = throwable;
    }

    public Message(MessageLevel messageLevel, String text) {
        this(messageLevel, text, null);
    }

    public MessageLevel getMessageLevel() {
        return messageLevel;
    }

    public void setMessageLevel(MessageLevel messageLevel) {
        this.messageLevel = messageLevel;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public Element asXml() {
        Element element = super.asXml();

        element.addAttribute("messageLevel", getMessageLevel().toString());
        element.addAttribute("text", getText());

        return element;
    }

}
