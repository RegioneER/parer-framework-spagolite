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

import static it.eng.spagoCore.ConfigProperties.StandardProperty.LOGO_2_ALT;
import static it.eng.spagoCore.ConfigProperties.StandardProperty.LOGO_2_TITLE;
import static it.eng.spagoCore.ConfigProperties.StandardProperty.LOGO_2_URL;
import static it.eng.spagoCore.ConfigProperties.StandardProperty.LOGO_3_ALT;
import static it.eng.spagoCore.ConfigProperties.StandardProperty.LOGO_3_TITLE;
import static it.eng.spagoCore.ConfigProperties.StandardProperty.LOGO_3_URL;
import static it.eng.spagoCore.ConfigProperties.URIProperty.LOGO_2_RELATIVE;
import static it.eng.spagoCore.ConfigProperties.URIProperty.LOGO_3_RELATIVE;

import it.eng.spagoCore.ConfigSingleton;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang3.StringUtils;

public class FooterTag extends BaseSpagoLiteTag {

    private static final long serialVersionUID = 1L;

    @Override
    public int doStartTag() throws JspException {
        writeln("<!--Footer-->");
        writeln("<div class=\"footer\">");
        writeln(renderFooterInfo(this.getContextPath()));
        return SKIP_BODY;

    }

    @Override
    public int doEndTag() throws JspException {
        writeln("</div>");
        return EVAL_PAGE;
    }

    private String renderFooterInfo(String contextPath) {
        ConfigSingleton configSingleton = ConfigSingleton.getInstance();

        StringBuilder img2 = new StringBuilder();
        img2.append("<img src=\"");
        img2.append(contextPath).append(configSingleton.getStringValue(LOGO_2_RELATIVE.name()));
        img2.append("\" alt=\"");
        img2.append(configSingleton.getStringValue(LOGO_2_ALT.name()));
        img2.append("\"/>");

        StringBuilder img3 = new StringBuilder();
        img3.append("<img src=\"");
        img3.append(contextPath).append(configSingleton.getStringValue(LOGO_3_RELATIVE.name()));
        img3.append("\" alt=\"");
        img3.append(configSingleton.getStringValue(LOGO_3_ALT.name()));
        img3.append("\"/>");

        StringBuilder footer = new StringBuilder();
        footer.append("<div class=\"left\">");
        if (StringUtils.isNotBlank(configSingleton.getStringValue(LOGO_2_URL.name()))) {
            footer.append("<a href=\"").append(configSingleton.getStringValue(LOGO_2_URL.name()))
                    .append("\"  title=\"")
                    .append(configSingleton.getStringValue(LOGO_2_TITLE.name())).append("\">");

            footer.append(img2.toString());

            footer.append("</a>");
        } else {
            footer.append(img2.toString());
        }
        footer.append("</div>\n");

        footer.append("<div class=\"right\">");
        if (StringUtils.isNotBlank(configSingleton.getStringValue(LOGO_3_URL.name()))) {
            footer.append("<a href=\"").append(configSingleton.getStringValue(LOGO_2_URL.name()))
                    .append("\"  title=\"")
                    .append(configSingleton.getStringValue(LOGO_3_TITLE.name())).append("\">");

            footer.append(img3.toString());

            footer.append("</a>");
        } else {
            footer.append(img3.toString());
        }
        footer.append("</div>");

        return footer.toString();
    }
}
