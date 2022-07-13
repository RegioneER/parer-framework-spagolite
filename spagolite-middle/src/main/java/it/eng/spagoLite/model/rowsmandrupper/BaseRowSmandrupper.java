package it.eng.spagoLite.model.rowsmandrupper;

import it.eng.spagoLite.db.base.BaseRowInterface;

public class BaseRowSmandrupper extends AbstractRowSmandrupper {

    public BaseRowSmandrupper() {
        super();
        setVisible(true);
        setEditable(true);
        setDeletable(true);
        setInsertable(true);
    }

    public void smandruppRow(BaseRowInterface row) {

    }

}
