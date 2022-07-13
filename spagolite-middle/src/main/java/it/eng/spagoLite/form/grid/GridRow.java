package it.eng.spagoLite.form.grid;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.form.fields.SingleValueField;

import java.util.ArrayList;
import java.util.List;

public class GridRow {
    private List<GridCell> cellList;
    private int livello;

    public GridRow(int livello) {
        this.cellList = new ArrayList<GridCell>();
        this.livello = livello;
    }

    public List<GridCell> getCellList() {
        return cellList;
    }

    public GridCell addCell(GridCell gridCell) {
        cellList.add(gridCell);
        return gridCell;
    }

    public GridCell addCell(SingleValueField field, BaseRowInterface row) throws EMFError {
        GridCell gridCell = new GridCell(field, row);
        cellList.add(gridCell);
        return gridCell;
    }

    public int getLivello() {
        return livello;
    }

    public void setLivello(int livello) {
        this.livello = livello;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(" <row livello=\"" + getLivello() + "\">\n");

        for (GridCell gridCell : cellList) {
            stringBuilder.append(gridCell.toString());
        }

        stringBuilder.append(" </row>\n");
        return stringBuilder.toString();
    }

}
