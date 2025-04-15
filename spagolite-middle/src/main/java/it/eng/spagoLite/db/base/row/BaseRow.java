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

package it.eng.spagoLite.db.base.row;

import it.eng.spagoLite.FrameElement;
import it.eng.spagoLite.db.base.BaseRowInterface;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.dom4j.Element;

public class BaseRow extends FrameElement implements BaseRowInterface {

    private static final long serialVersionUID = 1L;

    protected static final String BASE_NAME = "ROW";

    protected Map<String, Object> row;

    public BaseRow() {
	this(null);
    }

    /**
     * Crea un nuovo RowBean e lo prepopola
     *
     * @param row value
     */
    public BaseRow(BaseRowInterface row) {
	this.row = new LinkedHashMap<String, Object>();

	if (row != null) {
	    this.copyFromBaseRow(row);
	}
    }

    public final Iterator<String> getIteratorColumnContained() {
	return row.keySet().iterator();
    }

    public BigDecimal getBigDecimal(String name) {
	return (BigDecimal) getObject(name);
    }

    public void setBigDecimal(String name, BigDecimal bigDecimal) {
	setObject(name, bigDecimal);
    }

    public Timestamp getTimestamp(String name) {
	return (Timestamp) getObject(name);
    }

    public void setTimestamp(String name, Timestamp timestamp) {
	setObject(name, timestamp);
    }

    public String getString(String name) {
	return (String) getObject(name);
    }

    public void setString(String name, String string) {
	setObject(name, string);
    }

    public Object getObject(String name) {
	return row.get(name.toLowerCase());
    }

    public void setObject(String name, Object object) {
	row.put(name.toLowerCase(), object);
    }

    public Object getOldObject(String name) {
	return row.get(name.toLowerCase() + "_OLD");
    }

    public void setOldObject(String name, Object object) {
	row.put(name.toLowerCase() + "_OLD", object);
    }

    public void copyFromBaseRow(BaseRowInterface row) {
	Iterator<String> iterator = row.getIteratorColumnContained();
	while (iterator.hasNext()) {
	    String column = iterator.next();
	    setObject(column, row.getObject(column));
	}
    }

    public void loadFromResultSet(ResultSet resultSet) throws SQLException {
	ResultSetMetaData rsMetaData = resultSet.getMetaData();
	int numberOfColumns = rsMetaData.getColumnCount();

	for (int i = 1; i <= numberOfColumns; i++) {
	    String name = rsMetaData.getColumnName(i);
	    Object value = resultSet.getObject(i);

	    // Trscodifica JDBC Type.
	    // Vengono gestiti String, Timestamp, BigDecimal.
	    if (value != null && value instanceof Date) {
		// Date --> Timestamp
		this.setObject(name, new Timestamp(((Date) value).getTime()));
	    } else if (value != null && value instanceof Timestamp) {
		// TIMESTAMP --> Timestamp
		this.setObject(name, resultSet.getTimestamp(i));
	    } else if (value != null && value instanceof Integer) {
		// Integer --> Timestamp
		this.setObject(name, BigDecimal.valueOf(((Integer) value).longValue()));
	    } else {
		this.setObject(name, value);
	    }
	}
    }

    public Element asXml() {
	Element element = super.asXml();
	for (Entry<String, Object> entry : row.entrySet()) {
	    Element column = element.addElement("col");
	    column.addAttribute("name", entry.getKey());
	    column.addAttribute("value",
		    entry.getValue() == null ? "" : entry.getValue().toString());
	}

	return element;
    }

}
