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

package it.eng.spagoLite.security.profile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Pagina extends ProfileElement<Azione> {

    private static final long serialVersionUID = 1L;

    private boolean checked;
    private boolean helpAvailable;
    private List<MenuDips> menuDips;

    public Pagina(String name, String description) {
        super(name, description);
        this.checked = false;
        this.helpAvailable = false;
        this.menuDips = new ArrayList<>();
    }

    public Pagina(String name, String description, List<MenuDips> menuDips) {
        super(name, description);
        this.checked = false;
        this.helpAvailable = false;
        this.menuDips = menuDips;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isHelpAvailable() {
        return helpAvailable;
    }

    public void setHelpAvailable(boolean helpAvailable) {
        this.helpAvailable = helpAvailable;
    }

    public List<MenuDips> getMenuDips() {
        return menuDips;
    }

    public void setMenuDips(List<MenuDips> menuDips) {
        this.menuDips = menuDips;
    }

    /*
     * Torna TRUE se il menu passato come parametro esiste tra i menu associati alla pagina.
     */
    public boolean containsMenu(String nmMenu) {
        boolean esiste = false;
        Iterator<MenuDips> it = menuDips.iterator();
        while (it.hasNext()) {
            MenuDips next = it.next();
            if (next.getName().equals(nmMenu)) {
                esiste = true;
                break;
            }
        }
        return esiste;
    }

}
