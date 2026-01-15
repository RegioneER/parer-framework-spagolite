/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 */

package it.eng.spagoLite.form.fields.impl;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.xmlbean.form.Field.Type.Enum;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Radio<O> extends SingleValueField<O> {

    private String elementGroup;

    public Radio(Component parent, String name, String description, String alias, Enum type,
            String format, boolean required, boolean hidden, boolean readonly, boolean trigger,
            String elementGroup) {
        super(parent, name, description, alias, type, format, required, hidden, readonly, trigger);
        this.elementGroup = elementGroup;
    }

    @Override
    public void setValue(String value) {
        if (CheckBox.valueChecked.equalsIgnoreCase(value)) {
            super.setValue(CheckBox.valueChecked);
        } else {
            super.setValue(CheckBox.valueUnchecked);
        }
    }

    public boolean isChecked() {
        return CheckBox.valueChecked.equalsIgnoreCase(getValue());
    }

    public void setChecked(boolean checked) {
        setValue(checked ? CheckBox.valueChecked : CheckBox.valueUnchecked);
    }

    @Override
    public JSONObject asJSON() throws EMFError {
        String value = ((getValue() == null) ? "" : getValue());
        JSONObject json = super.asJSON();
        try {
            json.put("value", value);
            json.put("elementGroup", getElementGroup());
            json.put("type", "Radio");
        } catch (JSONException e) {
            throw new EMFError(EMFError.ERROR, "Eccezione nella crezione dell'oggetto JSON", e);
        }
        return json;
    }

    @Override
    public void post(HttpServletRequest servletRequest) {
        if (!isReadonly() && isEditMode()) {
            String elementGroupValue = servletRequest.getParameter(getElementGroup());
            setChecked(getName().equalsIgnoreCase(elementGroupValue));
        }
    }

    public String getElementGroup() {
        return elementGroup;
    }

    public void setElementGroup(String elementGroup) {
        this.elementGroup = elementGroup;
    }

    @Override
    public void reset() {
        this.setChecked(false);
    }

}
