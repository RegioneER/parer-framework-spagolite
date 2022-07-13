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
