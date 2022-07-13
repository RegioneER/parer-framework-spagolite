package it.eng.spagoLite.form.fields.impl;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.xmlbean.form.Field.Type.Enum;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class TextArea<O> extends SingleValueField<O> {

    private int rows;
    private int cols;
    private int maxLength;
    private String tooltip;

    public TextArea(Component parent, String name, String description, String alias, Enum type, String format,
            boolean required, boolean hidden, boolean readonly, boolean trigger, int rows, int cols, int maxLength,
            String tooltip) {
        super(parent, name, description, alias, type, format, required, hidden, readonly, trigger);
        this.rows = rows;
        this.cols = cols;
        this.maxLength = maxLength;
        this.tooltip = tooltip;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void reset() {
        this.setValue(null);
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public void setValue(String value) {
        if (value != null && value.length() > getMaxLength()) {
            value = value.substring(0, getMaxLength());
        }
        super.setValue(value);
    }

    @Override
    public JSONObject asJSON() throws EMFError {
        String value = ((getValue() == null) ? "" : getValue());
        JSONObject json = super.asJSON();
        try {
            json.put("value", value);
            json.put("type", "TextArea");
        } catch (JSONException e) {
            throw new EMFError(EMFError.ERROR, "Eccezione nella crezione dell'oggetto JSON", e);
        }
        return json;
    }

}
