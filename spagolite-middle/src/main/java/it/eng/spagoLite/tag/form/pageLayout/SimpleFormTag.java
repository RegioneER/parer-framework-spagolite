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
