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

package it.eng.spagoLite.tag.form.formLayout;

import it.eng.spagoLite.form.fields.Section;
import it.eng.spagoLite.tag.form.BaseFormTag;

import javax.servlet.jsp.JspException;

public class TableSectionTag extends BaseFormTag<Section> {
    private static final long serialVersionUID = 1L;

    // private static final String IMG_TITLE = "clicca sul pulsante per espandere o nascondere la sezione";
    // private static final String IMG_OPEN = "/img/toolBar/openGreen.png";
    // private static final String IMG_CLOSE = "/img/toolBar/closeGreen.png";
    // private static final String ALT_IMG_OPEN = " Apri ";
    // private static final String ALT_IMG_CLOSE = " Chiudi ";

    private String styleClass;

    @Override
    public int doStartTag() throws JspException {
        writeln("<!-- field -->");

        Section section = getComponent();
        if (section.isViewMode() || section.isHidden()) {
            return SKIP_BODY;
        } else {
            // <td style="color: #055122;"><span class="totale"><b>Oggetti in
            // corso di versamento</b></span></td>
            writeln("<td " + getHtmlClass(section) + " id=\"" + getName() + "\">");
            writeln(getCompleteLegend(section));
        }
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    @Override
    public int doAfterBody() throws JspException {
        writeln("</td>");
        return super.doAfterBody();
    }

    private String getHtmlClass(Section section) {
        return " class=\"" + getStyleClass() + "\"";
    }

    private String getCompleteLegend(Section section) {

        // return hasLegend ? " <legend> " + completeLegend + "</legend>" : "";
        return " <span class=\"tablesection " + (section.isLoadOpened() ? "opened" : "closed") + "\"><b>"
                + section.getLegend() + "</b></span>";
    }

    public String getStyleClass() {
        if (styleClass == null) {
            return "";
        } else {
            return styleClass;
        }
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

}
