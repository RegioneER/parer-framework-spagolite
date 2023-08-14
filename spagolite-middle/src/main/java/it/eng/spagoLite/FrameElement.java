package it.eng.spagoLite;

import java.io.IOException;
import java.io.StringWriter;

import org.codehaus.jettison.json.JSONObject;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import it.eng.spagoCore.error.EMFError;

public class FrameElement implements FrameElementInterface {

    protected static final long serialVersionUID = 1L;

    public String toString() {
        StringWriter stringWriter = new StringWriter();
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(stringWriter, format);
            writer.write(asXml());

        } catch (IOException e) {
        }

        return stringWriter.toString();
    }

    public Element asXml() {
        return DocumentHelper.createElement(getClass().getSimpleName());
    }

    public JSONObject asJSON() throws EMFError {
        return null;
    }

}
