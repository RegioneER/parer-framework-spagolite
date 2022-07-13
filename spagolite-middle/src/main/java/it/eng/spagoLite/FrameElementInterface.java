package it.eng.spagoLite;

import it.eng.spagoCore.error.EMFError;

import java.io.Serializable;

import org.codehaus.jettison.json.JSONObject;
import org.dom4j.Element;

public interface FrameElementInterface extends Serializable {

    public Element asXml();

    public JSONObject asJSON() throws EMFError;

}
