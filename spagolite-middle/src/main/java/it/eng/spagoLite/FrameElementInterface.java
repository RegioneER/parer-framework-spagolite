package it.eng.spagoLite;

import java.io.Serializable;

import org.codehaus.jettison.json.JSONObject;
import org.dom4j.Element;

import it.eng.spagoCore.error.EMFError;

public interface FrameElementInterface extends Serializable {

    public Element asXml();

    public JSONObject asJSON() throws EMFError;

}
