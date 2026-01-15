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
        OutputFormat format = OutputFormat.createPrettyPrint();
        try (XMLWriter writer = new XMLWriter(stringWriter, format);) {
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
