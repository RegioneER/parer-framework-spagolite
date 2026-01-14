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
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Button<O> extends SingleValueField<O> {

    private static final long serialVersionUID = 1L;

    private boolean disableHourGlass;
    private boolean secure;

    public Button(Component parent, String name, String description, String alias, Enum type,
            String format, boolean required, boolean hidden, boolean readonly, boolean trigger) {
        super(parent, name, description, alias, type, format, required, hidden, readonly, trigger);
    }

    public Button(Component parent, String name, String description, String alias, Enum type,
            String format, boolean required, boolean hidden, boolean readonly, boolean trigger,
            boolean disableHourGlass, boolean secure) {
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
