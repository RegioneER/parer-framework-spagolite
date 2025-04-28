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

package it.eng.spagoLite.db.oracle.decode;

import it.eng.spagoLite.FrameElement;
import it.eng.spagoLite.db.decodemap.DecodeMapIF;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dom4j.Element;

public abstract class DecodeMapAbs extends FrameElement implements DecodeMapIF {

    private static final long serialVersionUID = 1L;

    protected Map<Object, String> map;

    public DecodeMapAbs() {
	this.map = new LinkedHashMap<Object, String>();
    }

    public String getDescrizione(Object codice) {
	String res = this.map.get(codice);

	return res == null ? "" : res.toString();
    }

    public final boolean isEmpty() {
	return this.map.isEmpty();
    }

    public final Object firstCodice() {
	Iterator<Object> i = this.map.keySet().iterator();
	if (i.hasNext()) {
	    return i.next();
	}

	return null;
    }

    @Override
    public Element asXml() {
	Element element = super.asXml();
	for (Map.Entry<Object, String> entry : map.entrySet()) {
	    Element entryElement = element.addElement("entry");
	    entryElement.addAttribute("codice", entry.getKey().toString());
	    entryElement.addAttribute("descrizione", entry.getValue().toString());
	}
	return element;
    }

}
