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
