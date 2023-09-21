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

package it.eng.spagoLite.tag.prototype;

import it.eng.spagoLite.tag.form.formLayout.ContainerTag;

import javax.servlet.jsp.JspException;

/**
 * Stampa una Combo statica
 */
public class LblSelectTag extends AbstractLblSelectTag {

    private static final long serialVersionUID = 1L;

    protected void writeControl() throws JspException {
        String className = ContainerTag.LEFT.equalsIgnoreCase(getLabelPosition()) ? "slText " + getControlwidth()
                : "slTextRight " + getControlwidth();

        if (isEditable()) {
            writeln(" <select id=\"" + getId() + "\" name=\"" + getId() + "\" class=\"" + className + "\">"
                    + getOption() + "</select>");
        } else {
            writeln(" <div class=\"" + className + "\" ></div>");
        }
    }
}
