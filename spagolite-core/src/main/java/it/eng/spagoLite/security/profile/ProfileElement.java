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

package it.eng.spagoLite.security.profile;

import it.eng.spagoLite.FrameElement;

import java.util.Iterator;
import java.util.Map;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.dom4j.Element;

/**
 *
 * @author Enrico Grillini
 *
 * @param <T>
 *
 */
public class ProfileElement<T extends ProfileElement<?>> extends FrameElement
	implements Iterable<T> {

    private static final long serialVersionUID = 1L;
    private String name;
    private String description;

    private final Map<String, T> childs;

    public ProfileElement(String name, String description) {
	this.name = name;
	this.description = description;
	// this.childs = new LinkedHashMap<String, T>();
	this.childs = new CaseInsensitiveMap();
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

    public void setDescription(String description) {
	this.description = description;
    }

    @Override
    public Iterator<T> iterator() {
	return childs.values().iterator();
    }

    public void addChild(T profileElement) {
	childs.put(profileElement.getName(), profileElement);
    }

    public T getChild(String name) {
	return childs.get(name);
    }

    @Override
    public Element asXml() {
	Element element = super.asXml();

	element.addAttribute("name", name);
	element.addAttribute("description", description);
	for (T profileElement : this) {
	    element.add(profileElement.asXml());
	}

	return super.asXml();
    }

}
