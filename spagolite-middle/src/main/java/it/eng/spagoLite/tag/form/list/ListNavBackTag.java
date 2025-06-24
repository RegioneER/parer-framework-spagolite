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

package it.eng.spagoLite.tag.form.list;

import it.eng.spagoLite.db.base.BaseTableInterface;

import javax.servlet.jsp.JspException;

/**
 * @deprecated Al momento non disegna nulla sulla pagina.
 *
 *
 */
@Deprecated
public class ListNavBackTag extends AbstractListNavBarTag {

    private static final long serialVersionUID = 1L;

    @Override
    public int doStartTag() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();

        if (table != null) {
            if (table.size() == 0) {
                return SKIP_BODY;
            }
            // writeln("<div>");
            // writeBack();
            // writeln("</div>");
        }

        return SKIP_BODY;
    }
}
