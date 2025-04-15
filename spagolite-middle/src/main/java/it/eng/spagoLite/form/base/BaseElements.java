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

package it.eng.spagoLite.form.base;

import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

public class BaseElements<T extends Component> extends BaseComponent implements Elements<T> {

    private static final long serialVersionUID = 1L;

    private Map<String, T> map;

    public enum Status {
	view, update, insert, delete
    };

    public BaseElements(Component parent, String name, String description) {
	super(parent, name, description);
	map = new LinkedHashMap<String, T>();
    }

    // @Override
    @SuppressWarnings("unchecked")
    public T getComponent(String name) {
	int indexOf = name.indexOf(".");
	if (indexOf > 0) {
	    return ((Elements<T>) map.get(name.substring(0, indexOf)))
		    .getComponent(name.substring(indexOf + 1));
	} else {
	    return map.get(name.toLowerCase());
	}
    }

    // @Override
    public T addComponent(T element) {
	map.put(element.getName().toLowerCase(), element);
	return element;
    }

    // @Override
    public List<T> getComponentList() {
	return new ArrayList<T>(map.values());
    }

    public Iterator<T> iterator() {
	return map.values().iterator();
    }

    @Override
    public Element asXml() {
	Element element = super.asXml();
	for (T baseElement : this) {
	    element.add(baseElement.asXml());
	}

	return element;
    }

    // Delegate method
    public int size() {
	return map.size();
    }
}
