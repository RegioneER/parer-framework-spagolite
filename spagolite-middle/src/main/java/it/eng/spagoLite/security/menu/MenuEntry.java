package it.eng.spagoLite.security.menu;

import it.eng.spagoLite.FrameElement;

import java.util.List;

import org.dom4j.Element;

/**
 *
 * @author Enrico Grillini
 *
 */
public abstract class MenuEntry extends FrameElement {

    private static final long serialVersionUID = 1L;

    private String codice;
    private String descr;
    private boolean helpAvailable;
    // private String descrLunga;

    public MenuEntry(String codice, String descr) {
        this.codice = codice;
        this.descr = descr;
        // this.descrLunga = descrLunga;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public boolean isHelpAvailable() {
        return helpAvailable;
    }

    public void setHelpAvailable(boolean helpAvailable) {
        this.helpAvailable = helpAvailable;
    }

    public abstract boolean isSelected();

    public abstract boolean select(String codice);

    public abstract List<MenuEntry> getSelectedPath(String codice);

    public abstract void reset();

    public Element asXml() {
        Element element = super.asXml();
        element.addAttribute("codice", getCodice());
        element.addAttribute("descr", getDescr());
        // element.addAttribute("descrLunga", getDescrLunga());

        return element;
    }

}
