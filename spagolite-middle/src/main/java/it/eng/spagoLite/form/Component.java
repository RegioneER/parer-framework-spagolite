package it.eng.spagoLite.form;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.FrameElementInterface;

import org.codehaus.jettison.json.JSONObject;
import org.dom4j.Element;

public interface Component extends Cloneable, FrameElementInterface {
    public Component getParent();

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public Element asXml();

    public JSONObject asJSON() throws EMFError;
}
