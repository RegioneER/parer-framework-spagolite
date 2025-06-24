/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package it.eng.spagoLite.form.fields.impl;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.xmlbean.form.Field.Type.Enum;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class CheckBox<O> extends SingleValueField<O> {
    // public final static String valueChecked = "S";
    // public final static String valueUnchecked = "N";

    public final static String valueWarning = "2";
    public final static String valueChecked = "1";
    public final static String valueUnchecked = "0";

    private String defaultValue;
    private String tooltip;

    public CheckBox(Component parent, String name, String description, String alias, Enum type, String format,
            boolean required, boolean hidden, boolean readonly, boolean trigger, String tooltip) {
        super(parent, name, description, alias, type, format, required, hidden, readonly, trigger);
        this.tooltip = tooltip;
    }

    public CheckBox(Component parent, String name, String description, String alias, Enum type, String format,
            boolean required, boolean hidden, boolean readonly, boolean trigger, String defaultValue, String tooltip) {
        this(parent, name, description, alias, type, format, required, hidden, readonly, trigger, tooltip);
        this.defaultValue = defaultValue;
        setValue(defaultValue);
        this.tooltip = tooltip;
    }

    @Override
    public void setValue(String value) {
        if (valueChecked.equalsIgnoreCase(value)) {
            super.setValue(valueChecked);
        } else if (valueWarning.equalsIgnoreCase(value)) {
            super.setValue(valueWarning);
        } else {
            super.setValue(valueUnchecked);
        }
    }

    public boolean isChecked() {
        return valueChecked.equalsIgnoreCase(getValue());
    }

    public boolean isWarning() {
        return valueWarning.equalsIgnoreCase(getValue());
    }

    public void setChecked(boolean checked) {
        setValue(checked ? valueChecked : valueUnchecked);
    }

    public void setWarningChecked() {
        setValue(valueWarning);
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public JSONObject asJSON() throws EMFError {
        JSONObject json = super.asJSON();
        try {
            json.put("type", "CheckBox");
            JSONObject son = new JSONObject();
            son.put("valueChecked", valueChecked);
            son.put("valueUnchecked", valueUnchecked);
            son.put("valueWarning", valueWarning);
            json.put("map", son);
        } catch (JSONException e) {
            throw new EMFError(EMFError.ERROR, "Eccezione nella crezione dell'oggetto JSON", e);
        }
        return json;

    }

    @Override
    public void reset() {
        this.setChecked(false);
    }

    @Override
    public void clear() {
        if (StringUtils.isNotBlank(defaultValue)) {
            setValue(defaultValue);
        } else {
            setValue(null);
        }
    }

    @Override
    public void post(HttpServletRequest servletRequest) {
        if (!isReadonly() && isEditMode()) {
            clear();
            if (StringUtils.isNotBlank(servletRequest.getParameter(getName()))) {
                setChecked(true);
            }
        }
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

}
