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

package it.eng.spagoLite.form.fields;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.message.Message;
import it.eng.spagoLite.message.Message.MessageLevel;
import it.eng.spagoLite.util.Casting.Casting;
import it.eng.spagoLite.xmlbean.form.Field.Type.Enum;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.dom4j.Element;

public abstract class MultiValueField<O> extends Field {

    private static final long serialVersionUID = 1L;

    private LinkedHashSet<String> values;

    public MultiValueField(Component parent, String name, String description, String alias,
	    Enum type, String format, boolean required, boolean hidden, boolean readonly,
	    boolean trigger) {
	super(parent, name, description, alias, type, format, required, hidden, readonly, trigger);
	values = new LinkedHashSet<String>();
    }

    public Set<String> getValues() {
	return values;
    }

    public void setValues(String values[]) {
	this.values.clear();

	if (values != null) {
	    CollectionUtils.addAll(this.values, values);
	}
    }

    public Set<String> getDecodedValues() throws EMFError {
	return getValues();
    }

    @Override
    public boolean check() {
	return Casting.check(getValues(), getType());
    }

    @Override
    public Message validate() {
	if (!check()) {
	    return new Message(MessageLevel.ERR,
		    "Campo '" + getDescription() + "' formalmente errato");
	}
	if (isRequired() && (getValues() == null || getValues().isEmpty())) {
	    return new Message(MessageLevel.ERR, "Campo  '" + getDescription() + "' obbligatorio");
	}

	return null;
    }

    @SuppressWarnings("unchecked")
    public List<O> parse() throws EMFError {
	if (check() && getValues() != null) {
	    List<O> list = new ArrayList<O>();
	    for (String value : getValues()) {
		list.add((O) Casting.parse(value, getType()));
	    }
	    // return (O[]) list.toArray();
	    return list;
	}
	return null;
    }

    @Override
    public void post(HttpServletRequest servletRequest) {
	if (!isReadonly() && isEditMode()) {
	    String[] postedValues = servletRequest.getParameterValues(getName());
	    setValues(postedValues);
	}
    }

    @Override
    public Element asXml() {
	return super.asXml().addAttribute("values",
		getValues() == null ? "" : getValues().toString());
    }

    @Override
    public JSONObject asJSON() throws EMFError {
	JSONObject result = new JSONObject();
	try {
	    result.put("name", getName());
	    result.put("description", getDescription());
	    String state;
	    if (isHidden()) {
		state = "hidden";
	    } else if (isReadonly()) {
		state = "readonly";
	    } else if (isViewMode()) {
		state = "view";
	    } else {
		state = "edit";
	    }
	    result.put("required", isRequired());
	    result.put("state", state);
	    if (getValues() != null) {
		JSONArray sons = new JSONArray();
		for (String value : getValues()) {
		    sons.put(value);
		}
		result.put("values", sons);
	    }
	} catch (JSONException e) {
	    throw new EMFError(EMFError.ERROR, "Eccezione nella crezione dell'oggetto JSON", e);
	}
	return result;
    }

    @Override
    public void clear() {
	setValues(null);
    }

}
