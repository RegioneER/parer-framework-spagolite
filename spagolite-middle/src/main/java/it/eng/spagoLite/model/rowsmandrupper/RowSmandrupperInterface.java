package it.eng.spagoLite.model.rowsmandrupper;

import it.eng.spagoLite.db.base.BaseRowInterface;

import java.io.Serializable;

public interface RowSmandrupperInterface extends Serializable {

    public boolean isEditable();

    public boolean isDeletable();

    public boolean isVisible();

    public boolean isInsertable();

    public void smandruppRow(BaseRowInterface row);

}