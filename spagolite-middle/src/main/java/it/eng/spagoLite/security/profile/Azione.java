package it.eng.spagoLite.security.profile;

public class Azione extends ProfileElement<ProfileElement<?>> {

    private static final long serialVersionUID = 1L;

    private boolean checked;

    public Azione(String name, String description) {
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
