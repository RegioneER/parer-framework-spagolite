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
import it.eng.spagoCore.util.JavaScript;
import it.eng.spagoLite.db.decodemap.DecodeMapIF;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.fields.IMappableField;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.util.Casting.Casting;
import it.eng.spagoLite.xmlbean.form.Field.Type.Enum;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComboBox<O> extends SingleValueField<O> implements IMappableField {

    private static final long serialVersionUID = 1L;

    static Logger log = LoggerFactory.getLogger(ComboBox.class);
    private DecodeMapIF decodeMap;
    private int maxLength;
    private boolean addBlank;
    private boolean withSearchComp;

    public ComboBox(Component parent, String name, String description, String alias, Enum type,
            String format, boolean required, boolean hidden, boolean readonly, boolean trigger,
            int maxLength, boolean addBlank, boolean withSearchComp) {
        super(parent, name, description, alias, type, format, required, hidden, readonly, trigger);
        this.maxLength = maxLength;
        this.addBlank = addBlank;
        this.withSearchComp = withSearchComp;
    }

    public DecodeMapIF getDecodeMap() {
        return decodeMap;
    }

    public void setDecodeMap(DecodeMapIF decodeMap) {
        try {
            this.decodeMap = decodeMap;
            if (StringUtils.isNotBlank(this.getValue()) && decodeMap != null
                    && !decodeMap.containsKey(parse())) {
                setValue(null);
            }
        } catch (EMFError ex) {
            log.error("DECODE MAP " + getName() + "ERRORE di conversione valore : "
                    + ex.getDescription(), ex);
        }
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public boolean isAddBlank() {
        return addBlank;
    }

    public void setAddBlank(boolean addBlank) {
        this.addBlank = addBlank;
    }

    @Override
    public String getDecodedValue() throws EMFError {
        if (check()) {
            Object object = parse();
            if (check() && object != null && !object.equals("")) {
                if (getDecodeMap() == null) {
                    return (String) object;
                }
                return getDecodeMap().getDescrizione(parse());
            }
        }
        return "";
    }

    @Override
    public String getHtmlDecodedValue() throws EMFError {
        String htmlDecodedValue = JavaScript.stringToHTMLString(getDecodedValue());
        String htmlIcon = getIconUrl() == null ? ""
                : "<img src=\"" + getIconUrl() + "\" alt=\"" + htmlDecodedValue + "\" title=\""
                        + htmlDecodedValue + "\" />&nbsp;";

        return htmlIcon + htmlDecodedValue;
    }

    public String getValue(Object key) {
        if (getDecodeMap() != null && getDecodeMap().containsKey(key)) {
            return getDecodeMap().getDescrizione(key);
        }
        return (String) key;
    }

    @Override
    public JSONObject asJSON() throws EMFError {
        JSONObject json = super.asJSON();
        try {
            JSONArray sons = new JSONArray();
            if (decodeMap != null) {
                sons.put(decodeMap.asJSON());
            }
            json.put("map", sons);
            json.put("withEmptyVal", isAddBlank());
            json.put("type", "ComboBox");
        } catch (JSONException e) {
            throw new EMFError(EMFError.ERROR, "Eccezione nella crezione dell'oggetto JSON", e);
        }

        return json;
    }

    @Override
    public void reset() {
        super.reset();
        setDecodeMap(null);
    }

    @Override
    public void setValue(String value) {
        try {
            if (getDecodeMap() != null) {
                Object val = Casting.parse(value, getType());
                if (val == null || getDecodeMap().containsKey(val)) {
                    super.setValue(value);
                }
            } else {
                if (StringUtils.isNotBlank(value)) {
                    log.warn(
                            "ATTENZIONE: \u00E8 stato impostato un valore per la combo senza DecodeMap presente.");
                }
                super.setValue(value);
            }
        } catch (EMFError ex) {
            log.error("DECODE MAP " + getName() + "ERRORE di conversione valore : "
                    + ex.getDescription(), ex);
        }
    }

    public boolean isWithSearchComp() {
        return withSearchComp;
    }

    public void setWithSearchComp(boolean withSearchComp) {
        this.withSearchComp = withSearchComp;
    }
}
