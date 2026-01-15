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

package it.eng.spagoLite.security.menu.impl;

import it.eng.spagoLite.security.menu.MenuEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

/**
 *
 * @author Enrico Grillini
 *
 */
public class Menu extends MenuEntry implements Iterable<MenuEntry> {

    private static final long serialVersionUID = 1L;

    private List<MenuEntry> childs;

    public Menu(String codice, String descr) {
        super(codice, descr);
        childs = new ArrayList<MenuEntry>();
    }

    public Iterator<MenuEntry> iterator() {
        return childs.iterator();
    }

    public void add(MenuEntry menuEntry) {
        childs.add(menuEntry);
    }

    public boolean hasChild() {
        return childs.size() > 0;
    }

    @Override
    public boolean isSelected() {
        for (MenuEntry menuEntry : this) {
            if (menuEntry.isSelected()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean select(String codice) {
        for (MenuEntry menuEntry : this) {
            if (menuEntry.select(codice)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void reset() {
        for (MenuEntry child : childs) {
            child.reset();
        }
    }

    @Override
    public List<MenuEntry> getSelectedPath(String codice) {
        List<MenuEntry> result = new ArrayList<MenuEntry>();
        for (MenuEntry menuEntry : this) {
            if (menuEntry.isSelected()) {
                result.add(this);
                result.addAll(menuEntry.getSelectedPath(codice));
                return result;
            }
        }
        return result;
    }

    @Override
    public Element asXml() {
        Element element = super.asXml();
        for (MenuEntry menuEntry : this) {
            element.add(menuEntry.asXml());
        }

        return element;
    }

}
