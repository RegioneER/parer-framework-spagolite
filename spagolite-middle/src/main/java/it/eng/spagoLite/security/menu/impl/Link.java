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

package it.eng.spagoLite.security.menu.impl;

import it.eng.spagoLite.security.menu.MenuEntry;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

/**
 * 
 * @author Enrico Grillini
 * 
 */
public class Link extends MenuEntry {

    private static final long serialVersionUID = 1L;

    private String url;
    private boolean selected;

    public Link(String codice, String descr, String url) {
        super(codice, descr);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void reset() {
        this.selected = false;
    }

    @Override
    public boolean select(String codice) {
        this.selected = codice.equals(getCodice());
        return this.selected;
    }

    @Override
    public List<MenuEntry> getSelectedPath(String codice) {
        List<MenuEntry> result = new ArrayList<MenuEntry>();
        if (isSelected()) {
            result.add(this);
        }

        return result;
    }

    @Override
    public Element asXml() {
        Element element = super.asXml();
        element.addAttribute("url", getUrl());
        element.addAttribute("selected", isSelected() + "");

        return element;
    }

}
