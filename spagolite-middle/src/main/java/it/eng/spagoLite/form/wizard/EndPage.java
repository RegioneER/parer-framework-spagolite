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

package it.eng.spagoLite.form.wizard;

import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.base.BaseComponent;

public class EndPage extends BaseComponent implements WizardElement {

    private static final long serialVersionUID = 1L;

    private boolean hideBar;
    private boolean current;

    public EndPage(Component parent, String name, String description, boolean hideBar) {
        super(parent, name, description);
        this.hideBar = hideBar;
    }

    public boolean isHideBar() {
        return hideBar;
    }

    public void setHideBar(boolean hideBar) {
        this.hideBar = hideBar;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}
