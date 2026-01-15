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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

public class Parse {

    public final static String RE_CODICE_FISCALE = "[A-Za-z]{6}\\d{2}[A-Za-z]\\d{2}[A-Za-z]\\w{4}";
    // public final static String RE_MAIL_ADDRESS =
    // "(\\w+)(\\.\\w+)*@(\\w+\\.)(\\w+)(\\.\\w+)*";
    public final static String RE_MAIL_ADDRESS = "^[-_a-z0-9\\'+*$^&%=~!?{}]++(?:\\.[-_a-z0-9\\'+*$^&%=~!?{}]+)*+@(?:(?![-.])[-a-z0-9.]+(?<![-.])\\.[a-z]{2,6}|\\d{1,3}(?:\\.\\d{1,3}){3})(?::\\d++)?$";

    /**
     * Parsa una stringa
     *
     * @param string in ingresso
     *
     * @return string parsata
     */
    public static String parseString(String string) {
        if (StringUtils.isBlank(string)) {
            return null;
        }

        return string;
    }

    /**
     * Parsa una generica stringa rappresentante un numero (intero, decimale e valuta)
     *
     * @param value   in ingresso
     * @param format  formato
     * @param symbols simbolo decimale
     *
     * @return la stringa parsata
     *
     * @throws ParseException
     */
    private static BigDecimal parseNumber(String value, String format, DecimalFormatSymbols symbols)
            throws ParseException {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        if (format == null) {
            format = Format.DECIMAL_FORMAT;
        }
        if (symbols == null) {
            symbols = Format.SYMBOLS;
        }

        DecimalFormat decimalFormat = new DecimalFormat(format, symbols);

        return new BigDecimal(decimalFormat.parse(value).toString());
    }

    /**
     * Parsa un numero decimale secondo il formato fornito
     *
     * @param value  in ingresso
     * @param format formato
     *
     * @return il numero parsato
     *
     * @throws ParseException eccezione generica
     */
    public static BigDecimal parseDecimal(String value, String format) throws ParseException {
        return parseNumber(value, format, Format.SYMBOLS);
    }

    /**
     * Parsa un numero decimale secondo il formato standard
     *
     * @param value in ingresso
     *
     * @return il numero parsato
     *
     * @throws ParseException eccezione generica
     */
    public static BigDecimal parseDecimal(String value) throws ParseException {
        return parseDecimal(value, Format.DECIMAL_FORMAT);
    }

    /**
     * Parsa un numero intero secondo il formato standard
     *
     * @param value in ingresso
     *
     * @return il numero parsato
     *
     * @throws ParseException eccezione generica
     */
    public static BigDecimal parseInteger(String value) throws ParseException {
        return parseDecimal(value, Format.INTEGER_FORMAT);
    }

    /**
     * Parsa un numero secondo il formato e il simbolo di valuta forniti
     *
     * @param value          in ingresso
     * @param format         formato
     * @param currencySymbol currency
     *
     * @return il numero parsato
     *
     * @throws ParseException eccezione generica
     */
    public static BigDecimal parseCurrency(String value, String format, String currencySymbol)
            throws ParseException {
        DecimalFormatSymbols decimalFormatSymbols = (DecimalFormatSymbols) Format.SYMBOLS.clone();
        if (currencySymbol != null) {
            decimalFormatSymbols.setCurrencySymbol(currencySymbol);
        }

        return parseNumber(value, format, decimalFormatSymbols);
    }

    /**
     * Parsa un numero secondo il formato standard e il simbolo di valuta fornito
     *
     * @param value          in ingresso
     * @param currencySymbol currency
     *
     * @return il numero parsato
     *
     * @throws ParseException eccezione generica
     */
    public static BigDecimal parseCurrency(String value, String currencySymbol)
            throws ParseException {
        return parseCurrency(value, Format.CURRENCY_FORMAT, currencySymbol);
    }

    /**
     * Parsa un numero secondo il formato e il simbolo di valuta standard
     *
     * @param value in ingresso
     *
     * @return risultato parsing valuta
     *
     * @throws ParseException eccezione generica
     */
    public static BigDecimal parseCurrency(String value) throws ParseException {
        try {
            return parseDecimal(value, Format.CURRENCY_FORMAT);
        } catch (ParseException e) {
            return parseDecimal(value, Format.DECIMAL_FORMAT);
        }
    }

    /**
     * Effettua il parsing della data in input
     *
     * @param date   in ingresso
     * @param format formato
     *
     * @return la data parsata, <code>null</code> se la data è <code>null</code> o stringa vuota
     *
     * @throws ParseException eccezione generica
     */
    public static Timestamp parseDate(String date, String format) throws ParseException {
        if (StringUtils.isBlank(date)) {
            return null;
        }

        if (format == null) {
            format = Format.DATE_FORMAT.DAY_FORMAT.format();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.ITALY);
        simpleDateFormat.setLenient(false);

        Date date2 = simpleDateFormat.parse(date);
        return new Timestamp(date2.getTime());
    }

    /**
     * Parsa la data secondo il formato di default
     *
     * @param date in ingresso
     *
     * @return la data parsata, <code>null</code> se la data è <code>null</code> o stringa vuota
     *
     * @throws ParseException eccezione generica
     */
    public static Timestamp parseDate(String date) throws ParseException {
        return parseDate(date, Format.DATE_FORMAT.DAY_FORMAT.format());
    }

    /**
     * Parsa la data secondo il formato DATE_TIME
     *
     * @param date in ingresso
     *
     * @return la data parsata, <code>null</code> se la data è <code>null</code> o stringa vuota
     *
     * @throws ParseException eccezione generica
     */
    public static Timestamp parseDateTime(String date) throws ParseException {
        return parseDate(date, Format.DATE_FORMAT.SECOND_FORMAT.format());
    }

    /**
     * Parsa la data secondo il formato TIME
     *
     * @param date in ingresso
     *
     * @return la data parsata, <code>null</code> se la data è <code>null</code> o stringa vuota
     *
     * @throws ParseException eccezione generica
     */
    public static Timestamp parseTime(String date) throws ParseException {
        return parseDate(date, Format.DATE_FORMAT.TIME_FORMAT.format());
    }

    /**
     * Parsa la data secondo il formato MONTH
     *
     * @param date in ingresso
     *
     * @return la data parsata, <code>null</code> se la data è <code>null</code> o stringa vuota
     *
     * @throws ParseException eccezione generica
     */
    public static Timestamp parseMonth(String date) throws ParseException {
        return parseDate(date, Format.DATE_FORMAT.MONTH_FORMAT.format());
    }

    /**
     * Parsa la data secondo il formato YEAR
     *
     * @param date in ingresso
     *
     * @return la data parsata, <code>null</code> se la data è <code>null</code> o stringa vuota
     *
     * @throws ParseException eccezione generica
     */
    public static Timestamp parseYear(String date) throws ParseException {
        return parseDate(date, Format.DATE_FORMAT.YEAR_FORMAT.format());
    }

    public static String parseCodiceFiscale(String codiceFiscale) throws ParseException {
        if (StringUtils.isBlank(codiceFiscale)) {
            return null;
        }

        if (!checkCodiceFiscale(codiceFiscale)) {
            throw new ParseException("Codice fiscale non valido", 0);
        }

        return codiceFiscale.toUpperCase();
    }

    public static String parseMailAddress(String mailAddress) throws ParseException {
        if (StringUtils.isBlank(mailAddress)) {
            return null;
        }

        if (!checkMailAddress(mailAddress)) {
            throw new ParseException("Indirizzo mail non valido", 0);
        }

        return mailAddress;
    }

    public static Object parseObject(String value, String dataType, String format,
            String currencySymbol) throws ParseException {
        if (DataTypes.STRING.equalsIgnoreCase(dataType)) {
            return parseString(value);
        } else if (DataTypes.CODICEFISCALE.equalsIgnoreCase(dataType)) {
            return parseCodiceFiscale(value);
        } else if (DataTypes.MAIL.equalsIgnoreCase(dataType)) {
            return parseMailAddress(value);
        } else if (DataTypes.DECIMAL.equalsIgnoreCase(dataType)) {
            return parseDecimal(value, format);
        } else if (DataTypes.INTEGER.equalsIgnoreCase(dataType)) {
            return parseInteger(value);
        } else if (DataTypes.CURRENCY.equalsIgnoreCase(dataType)) {
            return parseCurrency(value, currencySymbol);
        } else if (DataTypes.DATE.equalsIgnoreCase(dataType)) {
            return parseDate(value, format);
        } else if (DataTypes.DATE_TIME.equalsIgnoreCase(dataType)) {
            return parseDateTime(value);
        } else if (DataTypes.TIME.equalsIgnoreCase(dataType)) {
            return parseTime(value);
        } else if (DataTypes.MONTH.equalsIgnoreCase(dataType)) {
            return parseMonth(value);
        } else if (DataTypes.YEAR.equalsIgnoreCase(dataType)) {
            return parseYear(value);
        } else {
            return parseString((String) value);
        }
    }

    public static Object parseObject(String value, String dataType, String format)
            throws ParseException {
        return parseObject(value, dataType, format, Format.CURRENCY_SYMBOL);
    }

    public static Object parseObject(String value, String dataType) throws ParseException {
        return parseObject(value, dataType, null);
    }

    public static boolean checkString(String string) {
        return true;
    }

    public static boolean checkCodiceFiscale(String codiceFiscale) {
        return codiceFiscale.matches(RE_CODICE_FISCALE);
    }

    public static boolean checkMailAddress(String mailAddress) {
        return mailAddress.matches(RE_MAIL_ADDRESS);
    }

    private static boolean checkNumber(String value, String format, DecimalFormatSymbols symbols) {
        try {
            parseNumber(value, format, symbols);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static boolean checkDecimal(String value, String format) {
        return checkNumber(value, format, Format.SYMBOLS);
    }

    public static boolean checkDecimal(String value) {
        return checkDecimal(value, Format.DECIMAL_FORMAT);
    }

    public static boolean checkInteger(String value) {
        return checkDecimal(value, Format.INTEGER_FORMAT);
    }

    public static boolean checkCurrency(String value, String format, String currencySymbol) {
        DecimalFormatSymbols decimalFormatSymbols = (DecimalFormatSymbols) Format.SYMBOLS.clone();
        if (currencySymbol != null) {
            decimalFormatSymbols.setCurrencySymbol(currencySymbol);
        }

        return checkNumber(value, format, decimalFormatSymbols);
    }

    public static boolean checkCurrency(String value, String currencySymbol) {
        return checkCurrency(value, Format.CURRENCY_FORMAT, currencySymbol);
    }

    public static boolean checkCurrency(String value) {
        try {
            parseDecimal(value, Format.CURRENCY_FORMAT);
        } catch (ParseException e) {
            try {
                parseDecimal(value, Format.DECIMAL_FORMAT);
            } catch (ParseException e1) {
                return false;
            }
        }

        return true;
    }

}
