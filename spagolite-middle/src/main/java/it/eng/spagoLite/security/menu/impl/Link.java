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
