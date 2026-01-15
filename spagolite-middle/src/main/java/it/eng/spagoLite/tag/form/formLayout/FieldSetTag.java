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

package it.eng.spagoLite.tag.form.formLayout;

import it.eng.spagoLite.tag.BaseSpagoLiteTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

public class FieldSetTag extends BaseSpagoLiteTag {
    private static final long serialVersionUID = 1L;

    private String legend;
    private boolean borderHidden;
    private boolean showButton;
    private boolean loadOpened;
    private String style;
    private String styleClass;

    private static final String IMG_TITLE = "clicca sul pulsante per espandere o nascondere la sezione";
    private static final String IMG_OPEN = "/img/window/aperto.gif";
    private static final String IMG_CLOSE = "/img/window/chiuso.gif";
    private static final String ALT_IMG_OPEN = " Apri ";
    private static final String ALT_IMG_CLOSE = " Chiudi ";

    @Override
    public int doStartTag() throws JspException {
        writeln("<!-- field -->");

        writeln("<fieldset " + getHtmlClass() + getHtmlInlineStyle() + ">");
        writeln(getCompleteLegend());

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        writeln("</fieldset>");
        return EVAL_PAGE;
    }

    private String getHtmlClass() {
        if (isBorderHidden()) {
            return " class=\"noborder " + getStyleClass() + "\"";
        } else {
            return " class=\"" + getStyleClass() + "\"";
        }
    }

    private String getHtmlInlineStyle() {
        if (StringUtils.isNotBlank(getStyle())) {
            return " style=\"" + getStyle() + "\" ";
        } else {
            return "";
        }
    }

    private String getCompleteLegend() {
        String completeLegend = "";
        boolean hasLegend = false;
        if (StringUtils.isNotBlank(getLegend())) {
            hasLegend = true;
            completeLegend = getLegend();
        }
        if (isShowButton()) {
            hasLegend = true;
            completeLegend += getButton(
                    ((HttpServletRequest) pageContext.getRequest()).getContextPath());
        }
        return hasLegend ? " <legend> " + completeLegend + "</legend>" : "";
    }

    private String getButton(String contextPath) {
        String openImgStyle = "";
        String closeImgStyle = "";
        String buttonClass = "class=\"windowButton ";
        if (isLoadOpened()) { // se deve essere presentato chiuso visualizza l'immagine di apertura
            closeImgStyle = " class=\"imgClose displayNone \"";
            buttonClass += " isLoadOpened\"";
        } else {
            openImgStyle = " class=\"imgOpen displayNone \"";
            buttonClass += " \"";

        }

        StringBuilder stringBuilder = new StringBuilder("<span>\n");
        stringBuilder.append("  <button " + buttonClass + " type=\"button\">\n");
        stringBuilder.append("    <img src=\"" + contextPath + IMG_OPEN + "\" title=\"" + IMG_TITLE
                + "\" alt=\"" + ALT_IMG_OPEN + "\" " + closeImgStyle + " />\n");
        stringBuilder.append("    <img src=\"" + contextPath + IMG_CLOSE + "\" title=\"" + IMG_TITLE
                + "\" alt=\"" + ALT_IMG_CLOSE + "\" " + openImgStyle + " />\n");
        stringBuilder.append("  </button>\n");
        stringBuilder.append("</span>");
        return stringBuilder.toString();
    }

    // getter and setter
    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public boolean isBorderHidden() {
        return borderHidden;
    }

    public void setBorderHidden(boolean borderHidden) {
        this.borderHidden = borderHidden;
    }

    public boolean isShowButton() {
        return showButton;
    }

    public void setShowButton(boolean showButton) {
        this.showButton = showButton;
    }

    public boolean isLoadOpened() {
        return loadOpened;
    }

    public void setLoadOpened(boolean loadOpened) {
        this.loadOpened = loadOpened;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
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
