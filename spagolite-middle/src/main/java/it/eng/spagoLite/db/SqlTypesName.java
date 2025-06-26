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

package it.eng.spagoLite.db;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

public final class SqlTypesName {

    private static final String BIGINT = "BIGINT";
    private static final String ARRAY = "ARRAY";
    private static final String BINARY = "BINARY";
    private static final String BIT = "BIT";
    private static final String BLOB = "BLOB";
    private static final String BOOLEAN = "BOOLEAN";
    private static final String CHAR = "CHAR";
    private static final String CLOB = "CLOB";
    private static final String DATALINK = "DATALINK";
    private static final String DECIMAL = "DECIMAL";
    private static final String DATE = "DATE";
    private static final String DISTINCT = "DISTINCT";
    private static final String DOUBLE = "DOUBLE";
    private static final String FLOAT = "FLOAT";
    private static final String INTEGER = "INTEGER";
    private static final String JAVA_OBJECT = "JAVA_OBJECT";
    private static final String LONGVARBINARY = "LONGVARBINARY";
    private static final String LONGVARCHAR = "LONGVARCHAR";
    private static final String NULL = "NULL";
    private static final String NUMERIC = "NUMERIC";
    private static final String OTHER = "OTHER";
    private static final String REF = "REF";
    private static final String SMALLINT = "SMALLINT";
    private static final String STRUCT = "STRUCT";
    private static final String TIMESTAMP = "TIMESTAMP";
    private static final String TINYINT = "TINYINT";
    private static final String VARBINARY = "VARBINARY";
    private static final String VARCHAR = "VARCHAR";
    private static final String TIME = "TIME";
    private static final String REAL = "REAL";

    private SqlTypesName() {

    }

    public static Set<BigDecimal> arrayString = new HashSet<BigDecimal>() {
        private static final long serialVersionUID = 1L;
        {
            add(BigDecimal.valueOf(Types.VARCHAR));
            add(BigDecimal.valueOf(Types.CHAR));
            add(BigDecimal.valueOf(Types.LONGVARCHAR));
        }
    };
    public static Set<BigDecimal> dateString = new HashSet<BigDecimal>() {
        private static final long serialVersionUID = 1L;
        {
            add(BigDecimal.valueOf(Types.DATE));
            add(BigDecimal.valueOf(Types.TIMESTAMP));
        }
    };
    public static Set<BigDecimal> decimalString = new HashSet<BigDecimal>() {
        private static final long serialVersionUID = 1L;
        {
            add(BigDecimal.valueOf(Types.DECIMAL));
            add(BigDecimal.valueOf(Types.DOUBLE));
            add(BigDecimal.valueOf(Types.FLOAT));
        }
    };
    public static Set<BigDecimal> integerString = new HashSet<BigDecimal>() {
        private static final long serialVersionUID = 1L;
        {
            add(BigDecimal.valueOf(Types.NUMERIC));
            add(BigDecimal.valueOf(Types.BIGINT));
            add(BigDecimal.valueOf(Types.INTEGER));
            add(BigDecimal.valueOf(Types.SMALLINT));
            add(BigDecimal.valueOf(Types.TINYINT));
            add(BigDecimal.valueOf(Types.DECIMAL));
            add(BigDecimal.valueOf(Types.DOUBLE));
            add(BigDecimal.valueOf(Types.FLOAT));
        }
    };

    public static String getName(int sqlType) {
        switch (sqlType) {
        case Types.ARRAY:
            return ARRAY;
        case Types.BIGINT:
            return BIGINT;
        case Types.BINARY:
            return BINARY;
        case Types.BIT:
            return BIT;
        case Types.BLOB:
            return BLOB;
        case Types.BOOLEAN:
            return BOOLEAN;
        case Types.CHAR:
            return CHAR;
        case Types.CLOB:
            return CLOB;
        case Types.DATALINK:
            return DATALINK;
        case Types.DATE:
            return DATE;
        case Types.DECIMAL:
            return DECIMAL;
        case Types.DISTINCT:
            return DISTINCT;
        case Types.DOUBLE:
            return DOUBLE;
        case Types.FLOAT:
            return FLOAT;
        case Types.INTEGER:
            return INTEGER;
        case Types.JAVA_OBJECT:
            return JAVA_OBJECT;
        case Types.LONGVARBINARY:
            return LONGVARBINARY;
        case Types.LONGVARCHAR:
            return LONGVARCHAR;
        case Types.NULL:
            return NULL;
        case Types.NUMERIC:
            return NUMERIC;
        case Types.OTHER:
            return OTHER;
        case Types.REAL:
            return REAL;
        case Types.REF:
            return REF;
        case Types.SMALLINT:
            return SMALLINT;
        case Types.STRUCT:
            return STRUCT;
        case Types.TIME:
            return TIME;
        case Types.TIMESTAMP:
            return TIMESTAMP;
        case Types.TINYINT:
            return TINYINT;
        case Types.VARBINARY:
            return VARBINARY;
        case Types.VARCHAR:
            return VARCHAR;

        default:
            break;
        }
        throw new IllegalArgumentException("Tipo sql non gestito: " + sqlType);

    }
}
