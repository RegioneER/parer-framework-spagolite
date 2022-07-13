package it.eng.spagoLite.form.fields.impl;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.xmlbean.form.Field.Type.Enum;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Button<O> extends SingleValueField<O> {

    private static final long serialVersionUID = 1L;

    private boolean disableHourGlass;
    private boolean secure;

    public Button(Component parent, String name, String description, String alias, Enum type, String format,
            boolean required, boolean hidden, boolean readonly, boolean trigger) {
        super(parent, name, description, alias, type, format, required, hidden, readonly, trigger);
    }

    public Button(Component parent, String name, String description, String alias, Enum type, String format,
            boolean required, boolean hidden, boolean readonly, boolean trigger, boolean disableHourGlass,
            boolean secure) {
        this(parent, name, description, alias, type, format, required, hidden, readonly, trigger);
        this.disableHourGlass = disableHourGlass;
        this.secure = secure;
    }

    @Override
    public JSONObject asJSON() throws EMFError {
        JSONObject json = super.asJSON();
        try {
            json.put("type", "Button");
        } catch (JSONException e) {
            throw new EMFError(EMFError.ERROR, "Eccezione nella crezione dell'oggetto JSON", e);
        }
        return json;
    }

    public boolean isDisableHourGlass() {
        return disableHourGlass;
    }

    public void setDisableHourGlass(boolean disableHourGlass) {
        this.disableHourGlass = disableHourGlass;
    }

    @Override
    public void reset() {
        this.setValue(null);
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

}
