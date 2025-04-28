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
