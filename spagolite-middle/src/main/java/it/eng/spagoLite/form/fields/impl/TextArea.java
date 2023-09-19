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
