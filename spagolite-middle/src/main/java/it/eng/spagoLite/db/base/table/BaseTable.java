package it.eng.spagoLite.db.base.table;

import it.eng.spagoLite.db.base.row.BaseRow;

public class BaseTable extends AbstractBaseTable<BaseRow> {
    private static final long serialVersionUID = 1L;

    public BaseRow createRow() {
        return new BaseRow();
    }

}
