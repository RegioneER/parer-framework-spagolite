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

package it.eng.spagoLite.form.grid;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.db.base.BaseTableInterface;
import it.eng.spagoLite.db.base.row.BaseRow;
import it.eng.spagoLite.db.base.table.BaseTable;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.base.BaseElements;
import it.eng.spagoLite.form.fields.Field;
import it.eng.spagoLite.form.fields.SingleValueField;

import java.util.ArrayList;
import java.util.List;

public class Grid<T extends SingleValueField> extends BaseElements<T> {

    private BaseTableInterface<?> table;

    private BaseElements<T> rowsHierachy;
    private BaseElements<T> columnsHierachy;
    private BaseElements<T> valuesHierachy;

    private List<List<Object>> packTable;
    private List<Object> packColumn;

    private List<GridRow> rowList;

    public Grid(Component parent, String name, String description) {
	super(parent, name, description);
	table = new BaseTable();
    }

    public BaseElements<T> getRowsHierachy() {
	return rowsHierachy;
    }

    public void setRowsHierachy(BaseElements<T> rowsHierachy) {
	this.rowsHierachy = rowsHierachy;
    }

    public BaseElements<T> getColumnsHierachy() {
	return columnsHierachy;
    }

    public void setColumnsHierachy(BaseElements<T> columnsHierachy) {
	this.columnsHierachy = columnsHierachy;
    }

    public BaseElements<T> getValuesHierachy() {
	return valuesHierachy;
    }

    public void setValuesHierachy(BaseElements<T> valuesHierachy) {
	this.valuesHierachy = valuesHierachy;
    }

    public BaseTableInterface<?> getTable() {
	return table;
    }

    public void setTable(BaseTableInterface table) {
	this.table = table;
    }

    private int checkRottura(BaseRowInterface oldRow, BaseRowInterface newRow) {
	int size = getRowsHierachy().getComponentList().size();

	for (Field field : getRowsHierachy().getComponentList()) {
	    Object oldValue = oldRow.getObject(field.getName());
	    Object newValue = newRow.getObject(field.getName());

	    if (oldValue == null && newValue != null) {
		return size;
	    } else if (oldValue != null && newValue == null) {
		return size;
	    } else if (oldValue == null && newValue == null) {
		size--;
	    } else if (!oldValue.equals(newValue)) {
		return size;
	    } else {
		size--;
	    }
	}

	return 0;
    }

    public List<List<Object>> getPackTable() {
	return packTable;
    }

    public List<Object> getPackColumn() {
	return packColumn;
    }

    public void pack() throws EMFError {
	packTable = new ArrayList<List<Object>>();
	packColumn = null;

	List<Object> packRow = null;
	if (table.size() > 0) {
	    table.first();
	    BaseRowInterface oldRow = table.getCurrentRow();

	    packRow = new ArrayList<Object>();
	    packColumn = new ArrayList<Object>();
	    packTable.add(packRow);

	    for (SingleValueField field : getRowsHierachy()) {
		packRow.add(oldRow.getObject(field.getName()));
	    }

	    for (BaseRowInterface row : table) {
		int livello = checkRottura(oldRow, row);

		if (livello != 0) {
		    packRow = new ArrayList<Object>();
		    packColumn = new ArrayList<Object>();
		    packTable.add(packRow);

		    for (SingleValueField field : getRowsHierachy()) {
			packRow.add(row.getObject(field.getName()));
		    }
		}

		for (SingleValueField field : getColumnsHierachy()) {
		    packColumn.add(row.getObject(field.getName()));
		}

		for (SingleValueField field : getValuesHierachy()) {
		    packRow.add(row.getObject(field.getName()));
		}

		oldRow = row;
	    }

	}

	// RowList
	int numLivelli = getRowsHierachy().getComponentList().size();

	rowList = new ArrayList<GridRow>();
	if (table.size() > 0) {
	    table.first();
	    BaseRowInterface oldRow = new BaseRow();
	    for (BaseRowInterface row : table) {
		int livelloRottura = checkRottura(oldRow, row);
		GridRow gridRow = null;

		if (livelloRottura != 0) {
		    for (int i = (numLivelli - livelloRottura); i < numLivelli; i++) {
			GridRow gridRow2 = new GridRow(numLivelli - i);
			gridRow.addCell(getRowsHierachy().getComponentList().get(i), row);

			// if (i == grid.getRowsHierachy().getComponentList().size() - 1) {
			// writeln(" <tr>");
			// writeln(" <td class=\"livello0\">" + FieldTag.Factory.htmlField(field,
			// null,
			// null, null) +
			// "</td>");
			// } else if (row.getObject(field.getName()) != null) {
			// writeln(" <tr>");
			// writeln(" <td class=\"livello" + (3- i) + "\" colspan=\"" +
			// (grid.getPackColumn().size() + 1)
			// + "\">" + field.getHtmlDecodedValue() + "</td>");
			// writeln(" </tr>");
			// }
		    }

		}

		int livello = checkRottura(oldRow, row);

		if (livello != 0) {
		    packRow = new ArrayList<Object>();
		    packColumn = new ArrayList<Object>();
		    packTable.add(packRow);

		    for (SingleValueField field : getRowsHierachy()) {
			packRow.add(row.getObject(field.getName()));
		    }
		}

		for (SingleValueField field : getColumnsHierachy()) {
		    packColumn.add(row.getObject(field.getName()));
		}

		for (SingleValueField field : getValuesHierachy()) {
		    packRow.add(row.getObject(field.getName()));
		}

		oldRow = row;
	    }

	}

    }

    @Override
    public String toString() {
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append("<grid>");

	for (GridRow gridRow : rowList) {
	    stringBuilder.append(gridRow.toString());
	}

	stringBuilder.append("</grid>");

	return super.toString();
    }
}
