package it.eng.spagoLite.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class Format {

    private static Map<BigDecimal, String> mapMesi = new HashMap<BigDecimal, String>() {
        {
            put(BigDecimal.valueOf(0), "Gennaio");
            put(BigDecimal.valueOf(1), "Febbraio");
            put(BigDecimal.valueOf(2), "Marzo");
            put(BigDecimal.valueOf(3), "Aprile");
            put(BigDecimal.valueOf(4), "Maggio");
            put(BigDecimal.valueOf(5), "Giugno");
            put(BigDecimal.valueOf(6), "Luglio");
            put(BigDecimal.valueOf(7), "Agosto");
            put(BigDecimal.valueOf(8), "Settembre");
            put(BigDecimal.valueOf(9), "Ottobre");
            put(BigDecimal.valueOf(10), "Novembre");
            put(BigDecimal.valueOf(11), "Dicembre");
        }
    };

    public static String getDescrizioneItaMese(Calendar cal) {
        return (String) mapMesi.get(BigDecimal.valueOf(cal.get(Calendar.MONTH)));
    }

    public static String getDescrizioneItaMese(Timestamp t) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(t.getTime());
        return getDescrizioneItaMese(cal);
    }

    // Formati - Numeri
    public final static String INTEGER_FORMAT = "#";
    public final static String DECIMAL_FORMAT = "#.##";
    public final static String INTEGER_FORMAT_WITH_THOUSANDS_SEPARATOR = "#,###";
    public final static String CURRENCY_FORMAT = "#,##0.00 " + (char) 164;

    // Formati - Date
    public enum DATE_FORMAT {
        MILLIS_FORMAT("dd/MM/yyyy HH:mm:ss.SSS"), SECOND_FORMAT("dd/MM/yyyy HH:mm:ss"),
        MINUTE_FORMAT("dd/MM/yyyy HH:mm"), HOUR_FORMAT("dd/MM/yyyy HH"), DAY_FORMAT_YY("dd/MM/yy"),
        DAY_FORMAT_NO_SLASH("ddMMyyyy"), DAY_FORMAT_NO_SLASH_YY("ddMMyy"), DAY_FORMAT_MINUS("dd-MM-yyyy"),
        DAY_FORMAT_MINUS_YY("dd-MM-yy"), DAY_FORMAT("dd/MM/yyyy"), WEEK_FORMAT("EEE dd MMM"), MONTH_FORMAT("MM/yyyy"),
        YEAR_FORMAT("yyyy"), TIME_FORMAT("HH:mm");

        private String format;

        DATE_FORMAT(String format) {
            this.format = format;
        }

        public static DATE_FORMAT parse(String parsed) throws ParseException {
            for (DATE_FORMAT format : DATE_FORMAT.values()) {
                if (StringUtils.equals(format.format(), parsed)) {
                    return format;
                }
            }
            throw new ParseException("Formato data non corretto", 0);
        }

        public String format() {
            return this.format;
        }
    }

    // Costanti di comodo
    public final static String CURRENCY_SYMBOL = "EUR";
    public final static String CURRENCY_LIT_FORMAT = "#,##0 " + (char) 164;
    public final static DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols(Locale.ITALY);

    static {
        SYMBOLS.setCurrencySymbol(CURRENCY_SYMBOL);
        SYMBOLS.setDecimalSeparator(',');
        SYMBOLS.setGroupingSeparator('.');
    }

    /**
     * FOrmatta una stringa
     *
     * @param string
     *            valore
     * 
     * @return la stringa formattata
     */
    public static String formatString(String string) {
        if (string == null) {
            return "";
        }

        return string;
    }

    /**
     * Formatta un generico numero (intero, decimale e valuta)
     *
     * @param bigDecimal
     *            valore
     * @param format
     *            formato conversione
     * @param symbols
     *            simbolo decimale
     * 
     * @return la stringa formattata
     */
    private static String formatNumber(BigDecimal bigDecimal, String format, DecimalFormatSymbols symbols) {
        if (bigDecimal == null) {
            return "";
        }
        if (format == null) {
            format = DECIMAL_FORMAT;
        }
        if (symbols == null) {
            symbols = SYMBOLS;
        }

        DecimalFormat decimalFormat = new DecimalFormat(format, symbols);
        return decimalFormat.format(bigDecimal);
    }

    /**
     * Formatta un numero decimale secondo il formato fornito
     *
     * @param bigDecimal
     *            valore
     * @param format
     *            formato
     * 
     * @return la stringa formattata
     */
    public static String formatDecimal(BigDecimal bigDecimal, String format) {
        return formatNumber(bigDecimal, format, SYMBOLS);
    }

    /**
     * Formatta un numero decimale secondo il formato standard
     *
     * @param bigDecimal
     *            valore
     * 
     * @return la stringa formattata
     */
    public static String formatDecimal(BigDecimal bigDecimal) {
        return formatDecimal(bigDecimal, DECIMAL_FORMAT);
    }

    /**
     * Formatta un numero intero secondo il formato standard
     *
     * @param bigDecimal
     *            valore
     * 
     * @return la stringa formattata
     */
    public static String formatInteger(BigDecimal bigDecimal) {
        return formatDecimal(bigDecimal, INTEGER_FORMAT);
    }

    public static String formatInteger(String bigDecimal) {
        BigDecimal value = new BigDecimal(0);
        if (StringUtils.isNotBlank(bigDecimal)) {
            try {
                value = new BigDecimal(bigDecimal);
                return formatInteger(value);
            } catch (NumberFormatException e) {
                // soffoco l'eccezione
            }
        }
        return "0";

    }

    /**
     * Formatta un numero intero secondo il formato specificato
     *
     * @param bigDecimal
     *            valore
     * @param formato
     *            formato
     * 
     * @return la stringa formattata
     */
    public static String formatInteger(BigDecimal bigDecimal, String formato) {
        return formatDecimal(bigDecimal, formato);
    }

    /**
     * Formatta un numero secondo il formato e il simbolo di valuta forniti
     *
     * @param bigDecimal
     *            valore
     * @param format
     *            formato
     * @param currencySymbol
     *            simbolo valuta
     * 
     * @return la stringa formattata
     */
    public static String formatCurrency(BigDecimal bigDecimal, String format, String currencySymbol) {
        if (format == null) {
            format = CURRENCY_FORMAT;
        }
        DecimalFormatSymbols decimalFormatSymbols = (DecimalFormatSymbols) SYMBOLS.clone();
        if (currencySymbol != null) {
            decimalFormatSymbols.setCurrencySymbol(currencySymbol);
        }

        return formatNumber(bigDecimal, format, decimalFormatSymbols);
    }

    /**
     * Formatta un numero secondo il formato standard e il simbolo di valuta fornito
     *
     * @param bigDecimal
     *            valore
     * @param currencySymbol
     *            simbolo valuta
     * 
     * @return la stringa formattata
     */
    public static String formatCurrency(BigDecimal bigDecimal, String currencySymbol) {
        return formatCurrency(bigDecimal, CURRENCY_FORMAT, currencySymbol);
    }

    /**
     * @param valore
     *            in inpunt
     * 
     * @return la stringa formattata
     */
    public static String formatItalianCurrency(String valore) {
        String result = "0,00";
        if (StringUtils.isBlank(valore)) {
            return result;
        }
        valore = StringUtils.replace(valore, ",", ".");
        try {

            BigDecimal currency = new BigDecimal(valore);

            result = Format.formatCurrency(currency, Format.DECIMAL_FORMAT, "");

        } catch (NumberFormatException e) {
            // soffoco l'eccezione e restituisco 0
        }

        return result;

    }

    /**
     * Formatta un numero secondo il formato e il simbolo di valuta standard
     *
     * @param bigDecimal
     *            valore
     * 
     * @return la stringa formattata
     */
    public static String formatCurrency(BigDecimal bigDecimal) {
        return formatNumber(bigDecimal, CURRENCY_FORMAT, SYMBOLS);
    }

    /**
     * Formatta la data secondo il formato indicato
     *
     * @param date
     *            in formato timestamp
     * @param format
     *            formato di conversione
     * 
     * @return la data formattata
     */
    public static String formatDate(Timestamp date, String format) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                format != null ? format : DATE_FORMAT.DAY_FORMAT.format(), Locale.ITALY);
        return simpleDateFormat.format(date);
    }

    /**
     * Formatta la data secondo il formato di default
     *
     * @param date
     *            timestamp in input
     * 
     * @return la data formattata
     */
    public static String formatDate(Timestamp date) {
        return formatDate(date, DATE_FORMAT.DAY_FORMAT.format());
    }

    /**
     * Formatta la data secondo il formato DATE_TIME
     *
     * @param date
     *            timestamp in input
     * 
     * @return la data formattata
     */
    public static String formatDateTime(Timestamp date) {
        return formatDate(date, DATE_FORMAT.SECOND_FORMAT.format());
    }

    /**
     * Formatta la data secondo il formato TIME
     *
     * @param date
     *            timestamp in input
     * 
     * @return la data formattata
     */
    public static String formatTime(Timestamp date) {
        return formatDate(date, DATE_FORMAT.TIME_FORMAT.format());
    }

    /**
     * Formatta la data secondo il formato WEEK
     *
     * @param date
     * 
     * @return la data formattata
     */
    public static String formatWeek(Timestamp date) {
        return formatDate(date, DATE_FORMAT.WEEK_FORMAT.format());
    }

    /**
     * Formatta la data secondo il formato MONTH
     *
     * @param date
     *            timestamp in input
     * 
     * @return la data formattata
     */
    public static String formatMonth(Timestamp date) {
        return formatDate(date, DATE_FORMAT.MONTH_FORMAT.format());
    }

    /**
     * Formatta la data secondo il formato YEAR
     *
     * @param date
     *            timestamp in input
     * 
     * @return la data formattata
     */
    public static String formatYear(Timestamp date) {
        return formatDate(date, DATE_FORMAT.YEAR_FORMAT.format());
    }

    public static String formatCodiceFiscale(String codiceFiscale) {
        if (codiceFiscale == null) {
            return "";
        }

        return codiceFiscale.toUpperCase();
    }

    public static String formatFile(String file) {
        String fileName = "";
        if (file != null) {
            fileName = file.substring(file.lastIndexOf('\\') + 1);
        }
        return fileName;
    }

    public static Date parseDate(String a) throws ParseException {
        Date result = null;

        int i = 0;
        for (Format.DATE_FORMAT pattern : Format.DATE_FORMAT.values()) {
            try {
                SimpleDateFormat df = new SimpleDateFormat(pattern.format());
                // lenient aggettivo [person] indulgente, clemente; [punishment] clemente.
                df.setLenient(false);
                result = df.parse(a);
                if (result != null) {
                    break;
                }
            } catch (ParseException e) {
                i++;
            }
        }
        if (i == Format.DATE_FORMAT.values().length) {
            throw new ParseException("Non posso convertire la data", i);
        }
        return result;
    }

}
