/**
 *
 * Copyright 2004, 2007 Engineering Ingegneria Informatica S.p.A.
 *
 * This file is part of Spago.
 *
 * Spago is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * any later version.
 *
 * Spago is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Spago; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * */
package it.eng.spagoCore.util;

import it.eng.spagoCore.base.Constants;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringUtils.class);

    private static SimpleDateFormat getFormatterGGMM() {
        return new SimpleDateFormat("dd/MM", Locale.ITALY);
    }

    private static SimpleDateFormat getFormatter() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        formatter.setLenient(false);
        return formatter;
    }

    private static SimpleDateFormat getFormatterOra() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ITALY);
    }

    private static NumberFormat getNumberFormat() {
        return getNumberFormat(2, 2);
    }

    private static NumberFormat getNumberFormat(int numCifreDecimaliMin, int numCifreDecimaliMax) {
        NumberFormat myDecFormat = NumberFormat.getInstance(Locale.ITALY);
        myDecFormat.setMinimumFractionDigits(numCifreDecimaliMin);
        myDecFormat.setMaximumFractionDigits(numCifreDecimaliMax);
        return myDecFormat;
    }

    /**
     * Method getCurrentDate. Il metodo restituisce la data corrente con ore,minuti,sec,e millesec. a zero
     *
     * @return Date
     */
    public static Date getCurrentDate() {
        Date today = null;
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.set(GregorianCalendar.HOUR_OF_DAY, 0);
            // calendar.set(GregorianCalendar.HOUR,0);
            calendar.set(GregorianCalendar.MINUTE, 0);
            calendar.set(GregorianCalendar.SECOND, 0);
            calendar.set(GregorianCalendar.MILLISECOND, 0);
            today = calendar.getTime();

        } catch (Exception ex) {
            LOGGER.warn("Utils. getCurrentDate", ex);
        }
        return today;
    }

    /**
     * Method stringToDateGGMM. Converte una stringa in data.
     *
     * @param stringToFormat
     *            Stringa da convertire: deve essere nel formato dd/MM.
     * 
     * @return Date La data convertita o null se il formato della stringa non era valido.
     */
    public static Date stringToDateGGMM(String stringToFormat) {
        try {
            if (stringToFormat == null) {
                return null;
            }
            return getFormatterGGMM().parse(stringToFormat);
        } catch (ParseException ex) {
            LOGGER.warn("Utils.stringToDateGGMM", ex);
            return null;
        }
    }

    /**
     * Method stringToDate. Converte una stringa in data.
     *
     * @param stringToFormat
     *            Stringa da convertire: deve essere nel formato dd/MM/yyyy.
     * 
     * @return Date La data convertita o null se il formato della stringa non era valido.
     */
    public static Date stringToDate(String stringToFormat) {
        try {
            if (stringToFormat == null) {
                return null;
            }
            return getFormatter().parse(stringToFormat);
        } catch (ParseException ex) {
            LOGGER.warn("Utils.stringToDate", ex);
            return null;
        }
    }

    /**
     * Method dateToString. Converte una data in stringa.
     *
     * @param dateToFormat
     *            Data da convertire
     * 
     * @return String Stringa nel formato dd/MM/yyyy
     */
    public static String dateToString(Date dateToFormat) {
        return getFormatter().format(dateToFormat);
    }

    public static String dateToStringBlankIfNull(Date dateToFormat) {
        if (dateToFormat == null) {
            return "";
        } else {
            return dateToString(dateToFormat);
        }
    }

    /**
     * Method dateToString. Converte una data in stringa.
     *
     * @param dateToFormat
     *            Data da convertire
     * @param dateFormat
     *            Formato della stringa risultante
     * 
     * @return String Stringa nel formato stabilito
     */
    public static String dateToString(final Date dateToFormat, final String dateFormat) {
        SimpleDateFormat customFormatter = new SimpleDateFormat(dateFormat, Locale.ITALY);
        return customFormatter.format(dateToFormat);
    }

    /**
     * Method dateToString. Converte una data in stringa.
     *
     * @param dateToFormat
     *            Data da convertire
     * 
     * @return String Stringa nel formato dd/MM/yyyy
     */
    public static String dateOraToString(Date dateToFormat) {
        return getFormatterOra().format(dateToFormat);
    }

    /**
     * Method stringToDate. Converte una stringa in data con orario.
     *
     * @param stringToFormat
     *            Stringa da convertire: deve essere nel formato dd/MM/yyyy HH:mm:ss.
     * 
     * @return Date La data convertita o null se il formato della stringa non era valido.
     */
    public static Date stringToDateOra(String stringToFormat) {
        try {
            if (stringToFormat == null) {
                return null;
            }
            return getFormatterOra().parse(stringToFormat);
        } catch (ParseException ex) {
            LOGGER.warn("Utils.stringToDateOra", ex);
            return null;
        }
    }

    /**
     * Data Una stringa sorgente, una stringa search e una replace, sostituisce nella sorgente ogni occorrenza di search
     * con replace
     *
     * @param source
     *            stringa sorgente
     * @param search
     *            stringa da cercare
     * @param replace
     *            stringa da sostituire
     * 
     * @return stringa modificata
     */
    public static String replace(String source, String search, String replace) {
        if (source == null || search == null || replace == null) {
            return null;
        }
        String ritorna = "";
        String appoggio = source;
        while (appoggio.length() >= 0) {
            if (appoggio.indexOf(search) != -1) {
                if (appoggio.indexOf(search) == 0) {
                    ritorna += replace;
                } else {
                    ritorna += appoggio.substring(0, appoggio.indexOf(search)) + replace;
                }
                appoggio = appoggio.substring(appoggio.indexOf(search) + search.length());
            } else {
                ritorna += appoggio;
                break;
            }
        }
        return ritorna;
    }

    /**
     * Method bigDecimalToInteger. Converte un BigDecimal in un Integer.
     *
     * @param bdNumber
     *            BigDecimal da convertire.
     * 
     * @return Integer L'Integer convertito o null se l'input è null
     */
    public static Integer bigDecimalToInteger(BigDecimal bdNumber) {
        return bdNumber != null ? (new Integer(bdNumber.intValue())) : null;
    }

    /**
     * Method bigDecimalToInteger. Converte un BigDecimal in un Long.
     *
     * @param bdNumber
     *            BigDecimal da convertire.
     * 
     * @return Integer Il Long convertito o null se l'input è null
     */
    public static Long bigDecimalToLong(BigDecimal bdNumber) {
        return bdNumber != null ? (new Long(bdNumber.longValue())) : null;
    }

    /**
     * Method bigDecimalToDouble. Converte un BigDecimal in un Double.
     *
     * @param bdNumber
     *            BigDecimal da convertire.
     * 
     * @return Double Il Double convertito o null se l'input è null
     */
    public static Double bigDecimalToDouble(BigDecimal bdNumber) {
        return bdNumber != null ? (new Double(bdNumber.doubleValue())) : null;
    }

    /**
     * Method dateToCalendar. Converte un java.util.Date in Calendar
     *
     * @param date
     *            da convertire.
     * 
     * @return Calendar Calendar convertito o null se l'input è null
     */
    public static Calendar dateToCalendar(Date date) {
        Calendar cal = null;
        if (date != null) {
            cal = Calendar.getInstance();
            cal.setTime(date);
        }
        return cal;
    }

    /**
     * Method stringToDouble.Converte una Stringa in Double
     *
     * @param string
     *            da convertire.
     * @param separatoreDecimale
     *            separatore decimale
     * 
     * @return il valore double della string
     */
    public static double stringToDouble(String string, char separatoreDecimale) {
        double d = 0.0;

        try {
            if (separatoreDecimale == ',') {
                // Elimino i punti
                string = replace(string, ".", "");
                // Sostituisco virgola con punto
                string = string.replace(',', '.');
            }
            d = Double.parseDouble(string);
        } catch (Exception ex) {
        }
        return d;
    }

    /**
     * Method doubleToString. Converte un Double in una Stringa
     *
     * @param d
     *            da convertire.
     * 
     * @return il valore string del double
     */
    public static String doubleToString(double d) {
        return getNumberFormat().format(d);
    }

    /**
     * Method doubleToString.Converte un Double in una Stringa
     *
     * @param d
     *            da convertire.
     * @param numCifreDecimaliMin
     * @param numCifreDecimaliMax
     * 
     * @return il valore string del double
     */
    public static String doubleToString(double d, int numCifreDecimaliMin, int numCifreDecimaliMax) {
        return getNumberFormat(numCifreDecimaliMin, numCifreDecimaliMax).format(d);
    }

    /**
     * Method isNumberValue.Indica se un valore è un numero o un carattere
     *
     * @param s
     *            stringa che dovrebbe rappresentare un numero
     * 
     * @return restituisce true se il valore passato è un numero
     */
    public static boolean isNumberValue(String s) {
        try {
            if (s == null) {
                return false;
            }
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Method isNumberValue.Indica se un valore è un numero o un carattere
     *
     * @param s
     *            stringa che dovrebbe rappresentare un double
     * 
     * @return restituisce true se il valore passato è un numero
     */
    public static boolean isDoubleValue(String s) {
        try {
            if (s == null) {
                return false;
            }
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Method isNumberLongValue.Indica se un valore è un numero o un carattere
     *
     * @param s
     *            stringa che dovrebbe rappresentare un long
     * 
     * @return restituisce true se il valore passato è un numero
     */
    public static boolean isNumberLongValue(String s) {
        try {
            if (s == null) {
                return false;
            }
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Method isNumberDoubleValue.Indica se un valore nel formato ###.###,## (con punti e virgola) è un numero double o
     * un carattere
     *
     * @param s
     *            stringa che dovrebbe rappresentare un double
     * 
     * @return restituisce true se il valore passato è un numero double
     */
    public static boolean isNumberDoubleValue(String s) {
        try {
            if (s == null) {
                return false;
            }

            // Elimino i punti
            s = replace(s, ".", "");

            // Sostituisco virgola con punto
            s = s.replace(',', '.');

            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Method doubleToString. Formatta un numerico con una stringa aggiungendo come carattere di riempimento il
     * parametro passato
     *
     * @param value
     *            da convertire.
     * @param carattere
     *            per il concatenamento.
     * @param len
     *            dimensione.
     *
     * @return il valore string del double
     */
    public static String formatSNtoString(long value, char carattere, int len) {
        String result = null;
        int nCicli = 0;
        // Converto intero passato in una stringa
        result = String.valueOf(value);
        // Calcolo numero di caratteri da concatenare a sinistra
        nCicli = len - result.length();
        for (int i = 0; i < nCicli; i++) {
            result = carattere + result;
        }
        return result;
    }

    /**
     * Formatta una stringa aggiungendo come carattere di riempimento il parametro passato
     *
     * @param value
     *            da convertire.
     * @param carattere
     *            per il concatenamento.
     * @param len
     *            dimensione.
     *
     * @return il valore string
     */
    public static String fillString(String value, char carattere, int len) {
        int nCicli = 0;
        // Calcolo numero di caratteri da concatenare a sinistra
        if (len > value.length()) {
            nCicli = len - value.length();
        }
        for (int i = 0; i < nCicli; i++) {
            value = carattere + value;
        }
        return value;
    }

    /**
     * Formatta una stringa aggiungendo come carattere di riempimento il parametro passato nella pos indicata
     *
     * @param value
     *            da convertire.
     * @param carattere
     *            per il concatenamento.
     * @param pos
     *            posizione.
     *
     * @return il valore string
     */
    public static String fillString(String value, char carattere, int len, String pos) {
        int nCicli = 0;
        // Calcolo numero di caratteri da concatenare a sinistra
        if (len > value.length()) {
            nCicli = len - value.length();
        }
        for (int i = 0; i < nCicli; i++) {
            if (pos != null && pos.equalsIgnoreCase("D")) {
                value += carattere;
            } else {
                value = carattere + value;
            }
        }
        return value;
    }

    /**
     * Formatta un numerico che prevede la parte decimale. Per la parte intera aggiunge tanti caratteri di riempimento a
     * sinistra, al massimo lenPrecision, per la parte decimale riempie per al massimo lenScale a destra
     *
     * @param value
     *            da riempire.
     * @param carattere
     *            per il concatenamento.
     * @param lenPrecision
     *            precisione.
     * @param lenScale
     *            scala.
     *
     * @return il valore string
     */
    public static String fillNumDec(String value, char carattere, int lenPrecision, int lenScale) {
        String parteInt = value;
        String parteDec = "0";

        if (value.indexOf(".") != -1) {
            parteInt = value.substring(0, value.indexOf("."));
            parteDec = value.substring(value.indexOf(".") + 1, value.length());
        }
        value = fillString(parteInt, carattere, lenPrecision);
        value += "." + fillString(parteDec, carattere, lenScale, "D");

        return value;
    }

    /**
     * Method stringToInteger. Converte una stringa in integer.
     *
     * @param string
     *            Stringa da convertire
     * 
     * @return Integer corrispondente alla stringa oppure null
     */
    public static Integer stringToInteger(String string) {

        return isNumberValue(string) ? Integer.valueOf(string) : null;

    }

    /**
     * Method stringToInteger. Converte una stringa in integer.
     *
     * @param string
     *            Stringa da convertire
     * 
     * @return Integer corrispondente alla stringa oppure null
     */
    public static Long stringToLong(String string) {

        return isNumberLongValue(string) ? Long.valueOf(string) : null;

    }

    /**
     * Method formatString. Formatta una Stringa secondo le cifre decimali impostate.
     *
     * @param d
     *            Stringa da formattare.
     * @param numCifreDecimaliMin
     *            minimo numero di cifre decimali.
     * @param numCifreDecimaliMax
     *            massimo numero di cifre decimali.
     * 
     * @return la stringa formattata
     */
    public static String formatDouble(Double d, int numCifreDecimaliMin, int numCifreDecimaliMax) {
        String newValue = null;
        NumberFormat decFormat = NumberFormat.getInstance(Locale.US);
        try {
            decFormat.setMinimumFractionDigits(numCifreDecimaliMin);
            decFormat.setMaximumFractionDigits(numCifreDecimaliMax);
            newValue = decFormat.format(d);
            newValue = replace(newValue, ",", "");
        } catch (Exception ex) {
            newValue = "0.";
            for (int i = 0; i < numCifreDecimaliMin; i++) {
                newValue += "0";
            }
        }
        return newValue;
    }

    /**
     * Method isTimeValue. Indica se un valore stringa rappresenta correttamente un orario nella forma hh:mm
     *
     * @return restituisce true se il valore passato è una stringa in formato orario corretto
     */
    public static boolean isTimeValue(String s) {

        if (s == null) {
            return false;
        }
        if (s.trim().equals("")) {
            return false;
        }
        String tmCh = ":";
        int pos = s.indexOf(tmCh);
        if (pos == -1) {
            return false;
        }
        if (s.length() != 5) {
            return false;
        }

        StringTokenizer stk = new StringTokenizer(s, tmCh);
        String strHour = null;
        String strMinute = null;
        int hour = 0;
        int minute = 0;

        try {
            if (stk.hasMoreElements()) {
                strHour = stk.nextToken();
            } else {
                return false;
            }
            if (stk.hasMoreElements()) {
                strMinute = stk.nextToken();
            } else {
                return false;
            }

            if (strHour.charAt(0) == '0' && strHour.length() > 1) {
                strHour = strHour.substring(1);
            }
            if (strMinute.charAt(0) == '0' && strMinute.length() > 1) {
                strMinute = strMinute.substring(1);
            }
            if (isNumberValue(strHour)) {
                hour = Integer.parseInt(strHour);
            } else {
                return false;
            }
            if (isNumberValue(strMinute)) {
                minute = Integer.parseInt(strMinute);
            } else {
                return false;
            }
            if (hour < 0 || hour > 23) {
                return false;
            }
            if (minute < 0 || minute > 59) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Method isDateGiornoMese. Indica se un valore stringa rappresenta correttamente una data nel formato gg/mm
     *
     * @param dataggmm
     *            anno/giorno/mese
     *
     * @return restituisce true se il valore passato è una stringa in formato gg/mm corretto
     */
    public static boolean isDateGiornoMese(String dataggmm) {
        if (dataggmm == null) {
            return false;
        }
        // caso particolare 29/02 deve essere sempre permesso
        if (dataggmm.equals("29/02")) {
            return true;
        }
        // usiamo l'anno corrente per verificare che sia una data valida
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        if (stringToDate(dataggmm + "/" + year) == null) {
            return false;
        }
        return true;
    }

    /**
     * Funzione di conversione dei seriali biglietti.
     *
     * @param serial
     *            seriale
     *
     * @return restituisce il seriale calcolato
     *
     */
    public static long convertSerial(String serial) {
        long result;
        int i = 0;

        while (serial.charAt(i) == '0') {
            i++;
        }

        result = Long.valueOf(serial.substring(i, serial.length())).longValue();
        return result;
    }

    /**
     *
     * Funzione di formattazione di una data da "01/12" a "0112"
     *
     * @param data
     *            da formattare
     *
     * @return restituisce la data formattata
     *
     */
    public static String formatDataGGMM(String data) {
        String result = data;

        if (data != null && !data.equals("")) {
            result = data.substring(0, data.indexOf("/")) + data.substring(data.indexOf("/") + 1, data.length());
        }
        return result;
    }

    /**
     * Method getMinuteFromTimeValue. Estra le ore dalla string in formato orario hh:mm
     *
     * @param s
     *            valore
     *
     * @return ore calcolate
     */
    public static Integer getHourFromTimeValue(String s) {
        if (!isTimeValue(s)) {
            return null;
        }
        String tmCh = ":";
        String strHour = null;
        Integer hour = null;
        try {
            strHour = s.substring(0, s.indexOf(tmCh));
            if (strHour.charAt(0) == '0' && strHour.length() > 1) {
                strHour = strHour.substring(1);
            }
            hour = Integer.valueOf(strHour);
        } catch (Exception e) {
            return null;
        }
        return hour;
    }

    /**
     * Method getMinuteFromTimeValue. Estra i minuti dalla string in formato orario hh:mm
     *
     * @param s
     *            valore
     * 
     * @return minuti calcolati
     */
    public static Integer getMinuteFromTimeValue(String s) {
        if (!isTimeValue(s)) {
            return null;
        }
        String tmCh = ":";
        String strMinute = null;
        Integer minute = null;
        try {
            strMinute = s.substring(s.indexOf(tmCh) + 1);
            if (strMinute.charAt(0) == '0' && strMinute.length() > 1) {
                strMinute = strMinute.substring(1);
            }
            minute = Integer.valueOf(strMinute);
        } catch (Exception e) {
            return null;
        }
        return minute;
    }

    /**
     * Counts the occurrence of the given char in the string.
     *
     * @param str
     *            The string to be tested
     * @param c
     *            the char to be counted
     * 
     * @return the occurrence of the character in the string.
     */
    public static int count(String str, char c) {
        int index = 0;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == c) {
                index++;
            }
        }
        return index;
    }

    public static int unique_id() {
        return new Object().hashCode();
    }

    // public static String notNull(SourceBean cbSb, String string) {
    // // TODO Auto-generated method stub
    // if(cbSb==null){
    // return "";
    // }
    // return notNull(cbSb.getAttribute(string));
    // }
    public static String notNull(Object input) {
        if (input == null) {
            return "";
        } else {
            return input.toString();
        }
    }

    public static String notNullAndTrim(String input) {
        if (input == null) {
            return "";
        } else {
            return input.toString().trim();
        }
    }

    public static boolean getBooleanFromStringSN(String input) {
        if (input != null && "S".equalsIgnoreCase(input)) {
            return true;
        } else {
            return false;
        }
    }
}
