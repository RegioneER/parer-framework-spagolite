package it.eng.spagoLite.form.wizard;

import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.base.BaseComponent;

public class Step extends BaseComponent implements WizardElement {

    private static final long serialVersionUID = 1L;

    private boolean summary;
    private boolean current;
    private boolean hidden;

    public Step(Component parent, String name, String description, boolean summary, boolean current, boolean hidden) {
        super(parent, name, description);
        this.summary = summary;
        this.current = current;
        this.hidden = hidden;
    }

    public boolean isSummary() {
        return summary;
    }

    public void setSummary(boolean summary) {
        this.summary = summary;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

}
