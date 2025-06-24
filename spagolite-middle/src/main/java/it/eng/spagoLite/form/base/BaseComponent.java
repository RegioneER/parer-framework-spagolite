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

package it.eng.spagoLite.form.base;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoCore.util.JavaScript;
import it.eng.spagoLite.FrameElement;
import it.eng.spagoLite.form.Component;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.dom4j.Element;

public class BaseComponent extends FrameElement implements Component {

    private static final long serialVersionUID = 1L;

    private Component parent;
    private String name;
    private String description;

    public BaseComponent(Component parent, String name, String description) {
        this.parent = parent;
        this.name = name;
        this.description = description;
    }

    public Component getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getHtmlDescription() {
        return JavaScript.stringToHTMLString(getDescription());
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Element asXml() {
        Element element = super.asXml();
        element.addAttribute("name", getName());
        element.addAttribute("description", getDescription());

        return element;
    }

    public JSONObject asJSON() throws EMFError {
        JSONObject result = new JSONObject();
        try {
            result.put("name", getName());
            result.put("description", getDescription());
        } catch (JSONException e) {
            throw new EMFError(EMFError.ERROR, "Eccezione nella crezione dell'oggetto JSON", e);
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().getName().equals(getClass().getName())) {
            return ((Component) obj).getName().equals(getName());
        } else {
            return false;
        }
    }

}
