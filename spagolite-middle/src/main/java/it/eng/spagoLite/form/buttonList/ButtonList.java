package it.eng.spagoLite.form.buttonList;

import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.fields.Fields;
import it.eng.spagoLite.form.fields.impl.Button;

import javax.servlet.http.HttpServletRequest;

public class ButtonList extends Fields<Button> {

    private static final long serialVersionUID = 1L;

    public ButtonList(Component parent, String name, String description) {
        super(parent, name, description);
    }

    public ButtonList(Component parent, String name, String description, boolean hideAll) {
        super(parent, name, description);
        if (hideAll) {
            hideAll();
        }
    }

    public void post(HttpServletRequest servletRequest) {
        for (Button field : this) {
            field.post(servletRequest);
        }
    }

    public void hideAll() {
        for (Button field : this) {
            field.setHidden(true);
        }
    }

    public void showAll() {
        for (Button field : this) {
            field.setHidden(false);
        }
    }

}
