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

import it.eng.spagoLite.tag.BaseSpagoLiteTag;

import javax.servlet.jsp.JspException;

public class ContainerTag extends BaseSpagoLiteTag {

    private static final long serialVersionUID = 1L;

    public static final String LEFT = "left";
    public static final String RIGHT = "right";

    public static final String WIDTH_40 = "w40";
    public static final String WIDTH_50 = "w50";
    public static final String WIDTH_60 = "w60";
    public static final String WIDTH_100 = "w100";

    public static final String WIDTH_LABEL = "wlbl";
    public static final String WIDTH_1_CONTROLLER = "w1ctr";
    public static final String WIDTH_2_CONTROLLER = "w2ctr";
    public static final String WIDTH_3_CONTROLLER = "w3ctr";
    public static final String WIDTH_4_CONTROLLER = "w4ctr";

    private String width;
    private String position;

    public String getWidth() {
        if (width == null || "".equals(width))
            return WIDTH_50;
        else
            return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public int doStartTag() throws JspException {
        writeln("");
        writeln(Factory.htmlStartContainer(getWidth(), getPosition()));

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        writeln(Factory.htmlEndContainer());
        return EVAL_PAGE;
    }

    public static class Factory {

        public static String htmlStartContainer(String width, String position) throws JspException {
            String containerClass = RIGHT.equals(position) ? "containerRight" : "containerLeft";

            return "<div class=\"" + containerClass + " " + width + "\">";
        }

        public static String htmlEndContainer() throws JspException {
            return "</div>";
        }

    }

}
