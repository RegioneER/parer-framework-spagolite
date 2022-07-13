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
