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

package it.eng.spagoLite.form.buttonList;

import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.fields.Fields;
import it.eng.spagoLite.form.fields.impl.Button;

import javax.servlet.http.HttpServletRequest;

public class ButtonList extends Fields<Button> {

    private static final long serialVersionUID = 1L;

    public ButtonList(Component parent, String name, String description) {
        super(parent, name, description);
    }

    public ButtonList(Component parent, String name, String description, boolean hideAll) {
        super(parent, name, description);
        if (hideAll) {
            hideAll();
        }
    }

    public void post(HttpServletRequest servletRequest) {
        for (Button field : this) {
            field.post(servletRequest);
        }
    }

    public void hideAll() {
        for (Button field : this) {
            field.setHidden(true);
        }
    }

    public void showAll() {
        for (Button field : this) {
            field.setHidden(false);
        }
    }

}
