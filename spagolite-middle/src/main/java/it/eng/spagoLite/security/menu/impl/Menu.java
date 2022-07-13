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
