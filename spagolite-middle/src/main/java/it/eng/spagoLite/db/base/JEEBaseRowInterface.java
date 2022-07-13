package it.eng.spagoLite.db.base;

public interface JEEBaseRowInterface {

    void entityToRowBean(Object entity);

    Object rowBeanToEntity();

}
