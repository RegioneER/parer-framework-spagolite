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

package it.eng.spagoLite.form.base;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.Elements;
import it.eng.spagoLite.form.Form;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class BaseForm implements Form {

    Map<String, Component> map;

    String description;

    public BaseForm(String description) {
	map = new LinkedHashMap<String, Component>();
	this.description = description;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    // @Override
    @SuppressWarnings("rawtypes")
    public Component getComponent(String name) {
	int indexOf = name.indexOf(".");
	if (indexOf > 0) {
	    return ((Elements) map.get(name.toLowerCase().substring(0, indexOf)))
		    .getComponent(name.substring(indexOf + 1));
	} else {
	    return map.get(name.toLowerCase());
	}
    }

    // @Override
    public Component addComponent(Component component) {
	map.put(component.getName().toLowerCase(), component);
	return component;
    }

    // @Override
    public List<Component> getComponentList(String name) {
	return new ArrayList<Component>(map.values());
    }

    public String asXml() {
	StringBuffer stringBuffer = new StringBuffer();
	stringBuffer.append("<form description=\"" + getDescription() + "\">\n");
	for (Component component : map.values()) {
	    stringBuffer.append(component.asXml());
	}
	stringBuffer.append("</form>\n");

	return stringBuffer.toString();
    }

    public JSONObject asJSON() throws EMFError {
	JSONObject result = new JSONObject();
	try {
	    result.put("name", "Form");
	    result.put("description", getDescription());

	    JSONArray sons = new JSONArray();
	    for (Component component : map.values()) {
		sons.put(component.asJSON());
	    }
	    result.put("map", sons);
	    result.put("type", "Form");
	} catch (JSONException e) {
	    throw new EMFError(EMFError.ERROR, "Eccezione nella crezione dell'oggetto JSON", e);
	}
	return result;

    }

}
