/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

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
