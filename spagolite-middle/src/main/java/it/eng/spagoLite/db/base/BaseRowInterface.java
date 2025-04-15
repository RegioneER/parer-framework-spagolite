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

package it.eng.spagoLite.db.base;

import it.eng.spagoLite.FrameElementInterface;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;

public interface BaseRowInterface extends FrameElementInterface {

    public BigDecimal getBigDecimal(String name);

    public void setBigDecimal(String name, BigDecimal bigDecimal);

    public Timestamp getTimestamp(String name);

    public void setTimestamp(String name, Timestamp timestamp);

    public String getString(String name);

    public void setString(String name, String string);

    public Object getObject(String name);

    public void setObject(String name, Object object);

    public Object getOldObject(String name);

    public void setOldObject(String name, Object object);

    void copyFromBaseRow(BaseRowInterface inRow);

    public void loadFromResultSet(ResultSet resultSet) throws SQLException;

    public Iterator<String> getIteratorColumnContained();

}
