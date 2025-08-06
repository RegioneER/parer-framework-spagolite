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
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.xmlbean.form.Field.Type.Enum;
import it.eng.spagoLite.xmlbean.form.Semaphore.State;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Semaphore extends SingleValueField<String> {

    private static final long serialVersionUID = 1L;

    public static final String GREEN_URL = "../img/sips/green.png";
    public static final String YELLOW_URL = "../img/sips/orange.png";
    public static final String RED_URL = "../img/sips/red.png";
    public static final String GRAY_URL = "../img/sips/gray.png";

    public State.Enum state;

    public Semaphore(Component parent, String name, String description, String alias, Enum type,
	    String format, boolean required, boolean hidden, boolean readonly, boolean trigger,
	    State.Enum state) {
	super(parent, name, description, alias, type, format, required, hidden, readonly, trigger);
	this.state = state;

    }

    @Override
    public JSONObject asJSON() throws EMFError {
	JSONObject result = new JSONObject();
	try {
	    result.put("name", getName());
	    result.put("description", getDescription());
	    result.put("state", getState());
	    result.put("type", "Semaphore");
	} catch (JSONException e) {
	    throw new EMFError(EMFError.ERROR, "Eccezione nella crezione dell'oggetto JSON", e);
	}
	return result;
    }

    public String getHtmlValue() throws EMFError {
	String name = JavaScript.stringToHTMLString(getName());
	StringBuilder result = new StringBuilder("<span id=\"" + name + "\" >");
	String state = "<img src=\"" + GRAY_URL
		+ "\" alt=\"Non definito\" title=\"Non definito\" />&nbsp;";
	if (this.state.equals(State.GREEN)) {
	    state = "<img src=\"" + GREEN_URL + "\" alt=\"Valido\" title=\"Valido\" />&nbsp;";
	} else if (this.state.equals(State.YELLOW)) {
	    state = "<img src=\"" + YELLOW_URL
		    + "\" alt=\"Da validare\" title=\"Da validare\" />&nbsp;";
	} else if (this.state.equals(State.RED)) {
	    state = "<img src=\"" + RED_URL + "\" alt=\"Inserito\" title=\"Inserito\" />&nbsp;";
	}

	result.append(state);
	result.append("</span>");
	return result.toString();
    }

    @Override
    public void setValue(String value) {
	super.setValue(value);
	if ("V".equals(value))
	    this.state = State.GREEN;
	if ("G".equals(value))
	    this.state = State.YELLOW;
	if ("R".equals(value))
	    this.state = State.RED;
    }

    public State.Enum getState() {
	return state;
    }

    public void setState(State.Enum state) {
	this.state = state;
    }

    public static class Factory {

	public static String getImageUrl(String value) {
	    if ("V".equals(value)) {
		return GREEN_URL;
	    } else if ("G".equals(value)) {
		return YELLOW_URL;
	    } else if ("R".equals(value)) {
		return RED_URL;
	    } else {
		return GRAY_URL;
	    }
	}

    }

}
