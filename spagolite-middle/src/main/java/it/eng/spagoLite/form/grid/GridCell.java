package it.eng.spagoLite.form.grid;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.form.fields.SingleValueField;

public class GridCell {
    private String value;

    public GridCell(SingleValueField field, BaseRowInterface row) throws EMFError {
        field.format(row);
        this.value = field.getValue();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "  <cell value=\"" + getValue() + "\"/>\n";
    }
}
