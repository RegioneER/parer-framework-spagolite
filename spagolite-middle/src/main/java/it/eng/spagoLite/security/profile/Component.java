package it.eng.spagoLite.security.profile;

public class Component extends ProfileElement<Attribute> {

    private static final long serialVersionUID = 1L;

    private boolean checked;

    public Component(String name, String description) {
        super(name, description);
        this.checked = false;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
