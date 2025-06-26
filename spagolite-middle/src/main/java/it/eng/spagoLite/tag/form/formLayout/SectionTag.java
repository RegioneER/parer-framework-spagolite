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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

public class SectionTag extends BaseFormTag<Section> {
    private static final long serialVersionUID = 1L;

    private static final String IMG_TITLE = "clicca sul pulsante per espandere o nascondere la sezione";
    private static final String IMG_OPEN = "/img/window/aperto.png";
    private static final String IMG_CLOSE = "/img/window/chiuso.png";
    private static final String ALT_IMG_OPEN = " Apri ";
    private static final String ALT_IMG_CLOSE = " Chiudi ";

    private String styleClass;

    @Override
    public int doStartTag() throws JspException {
        writeln("<!-- field -->");

        Section section = getComponent();
        if (section.isViewMode() || section.isHidden()) {
            return SKIP_BODY;
        } else {

            writeln("<fieldset " + getHtmlClass(section) + " id=\"" + getName() + "\">");
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
        writeln("</fieldset>");
        return super.doAfterBody();
    }

    private String getHtmlClass(Section section) {
        if (section.isBorderHidden()) {
            return " class=\"noborder " + getStyleClass() + "\"";
        } else {
            return " class=\"" + getStyleClass() + "\"";
        }
    }

    private String getCompleteLegend(Section section) {
        String completeLegend = "";
        boolean hasLegend = false;
        if (StringUtils.isNotBlank(section.getLegend())) {
            hasLegend = true;
            completeLegend = section.getLegend();
        }
        if (section.isShowButton()) {
            hasLegend = true;
            completeLegend += getButton(section, ((HttpServletRequest) pageContext.getRequest()).getContextPath());
        }
        // return hasLegend ? " <legend> " + completeLegend + "</legend>" : "";
        return hasLegend ? " <span class=\"legend\"> " + completeLegend + "</span>" : "";
    }

    private String getButton(Section section, String contextPath) {
        String openImgStyle = "";
        String closeImgStyle = "";
        String buttonClass = "class=\"windowButton ";
        if (section.isLoadOpened()) { // se deve essere presentato chiuso visualizza l'immagine di apertura
            closeImgStyle = " class=\"imgClose displayNone \"";
            buttonClass += " isLoadOpened\"";
        } else {
            openImgStyle = " class=\"imgOpen displayNone \"";
            buttonClass += " \"";

        }

        StringBuilder stringBuilder = new StringBuilder("<span>\n");
        stringBuilder.append("  <button " + buttonClass + " type=\"button\">\n");
        stringBuilder.append("    <img src=\"" + contextPath + IMG_OPEN + "\" title=\"" + IMG_TITLE + "\" alt=\""
                + ALT_IMG_OPEN + "\" " + closeImgStyle + " />\n");
        stringBuilder.append("    <img src=\"" + contextPath + IMG_CLOSE + "\" title=\"" + IMG_TITLE + "\" alt=\""
                + ALT_IMG_CLOSE + "\" " + openImgStyle + " />\n");
        stringBuilder.append("  </button>\n");
        stringBuilder.append("</span>");
        return stringBuilder.toString();
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
