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
 */
public class ProfileElement<T extends ProfileElement<?>> extends FrameElement implements Iterable<T> {

    private static final long serialVersionUID = 1L;
    private String name;
    private String description;

    private Map<String, T> childs;

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
