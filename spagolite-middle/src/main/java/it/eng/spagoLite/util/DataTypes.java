package it.eng.spagoLite.util;

import java.sql.Types;

public class DataTypes {

    // Stringhe
    public final static String STRING = "string";

    // Numeri
    public final static String INTEGER = "integer";
    public final static String DECIMAL = "decimal";
    public final static String CURRENCY = "currency";

    // Date
    public final static String DATE = "date";
    public static final String DATE_TIME = "dateTime";
    public final static String TIME = "time";
    public final static String MONTH = "month";
    public final static String YEAR = "year";

    // Stringhe - particolari
    public final static String CODICEFISCALE = "codiceFiscale";
    public final static String MAIL = "mail";
    public final static String BOOLEAN = "boolean";
    public final static String HTML = "html";

    public static int getSqlTypes(String dataType) {
        if (STRING.equals(dataType))
            return Types.VARCHAR;
        if (CODICEFISCALE.equals(dataType))
            return Types.VARCHAR;
        if (MAIL.equals(dataType))
            return Types.VARCHAR;
        if (BOOLEAN.equals(dataType))
            return Types.VARCHAR;
        if (HTML.equals(dataType))
            return Types.VARCHAR;

        if (INTEGER.equals(dataType))
            return Types.DECIMAL;
        if (DECIMAL.equals(dataType))
            return Types.DECIMAL;
        if (CURRENCY.equals(dataType))
            return Types.DECIMAL;

        if (DATE.equals(dataType))
            return Types.DATE;
        if (DATE_TIME.equals(dataType))
            return Types.DATE;
        if (TIME.equals(dataType))
            return Types.DATE;
        if (MONTH.equals(dataType))
            return Types.DATE;
        if (YEAR.equals(dataType))
            return Types.DATE;

        return Types.VARCHAR;
    }
}
