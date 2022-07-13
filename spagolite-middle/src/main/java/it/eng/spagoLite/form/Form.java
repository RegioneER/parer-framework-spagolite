package it.eng.spagoLite.form;

import it.eng.spagoCore.error.EMFError;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;

public interface Form extends Serializable {

    public String getDescription();

    public Component getComponent(String name);

    public Component addComponent(Component component);

    public List<Component> getComponentList(String name);

    public JSONObject asJSON() throws EMFError;

}
