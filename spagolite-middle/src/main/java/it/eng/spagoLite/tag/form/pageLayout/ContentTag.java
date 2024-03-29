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

import it.eng.spagoLite.SessionManager;
import it.eng.spagoLite.message.MessageBox.ViewMode;
import it.eng.spagoLite.security.CsrfHelper;
import it.eng.spagoLite.tag.BaseSpagoLiteTag;
import it.eng.spagoLite.tag.MessageBoxTag;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

public class ContentTag extends BaseSpagoLiteTag {

    private static final long serialVersionUID = 1L;

    protected boolean multipartForm = false;

    protected String action;

    @Override
    public int doStartTag() throws JspException {
        String actionUrl = SessionManager.getCurrentActionUrl(pageContext.getSession());

        if (StringUtils.isNotBlank(action)) {
            actionUrl = action;
        }

        writeln("");
        writeln(" <div id=\"content\">");
        if (multipartForm) {
            writeln("  <form id=\"spagoLiteAppForm\" action=\"" + actionUrl + getCsrfQueryToken()
                    + "\" method=\"post\" enctype=\"multipart/form-data\">");
        } else {
            writeln("  <form id=\"spagoLiteAppForm\" action=\"" + actionUrl + "\" method=\"post\">");
            writeln(getCsrfToken());
        }
        writeln("    <div class=\"displayNone\">");
        writeln("    </div>");

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
        writeln(" </div>\n");
        return EVAL_PAGE;
    }

    public boolean isMultipartForm() {
        return multipartForm;
    }

    public void setMultipartForm(boolean multipartForm) {
        this.multipartForm = multipartForm;
    }

    protected String getCsrfToken() {
        return CsrfHelper.getCsrfInputToken((HttpServletRequest) this.pageContext.getRequest());
    }

    protected String getCsrfQueryToken() {
        return CsrfHelper.hasTokenFromRequest((HttpServletRequest) this.pageContext.getRequest())
                ? "?" + CsrfHelper.getCsrfQueryStringToken((HttpServletRequest) this.pageContext.getRequest())
                : StringUtils.EMPTY;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
