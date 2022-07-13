package it.eng.spagoLite.form;

import java.util.List;

public interface Elements<T extends Component> extends Component, Iterable<T> {
    public T getComponent(String name);

    public T addComponent(T element);

    public List<T> getComponentList();
}
