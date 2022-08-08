package it.eng.spagoLite.util.Casting;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.util.Format;
import it.eng.spagoLite.xmlbean.form.Field.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;

public class Casting {

    private static final String CAP_REGEXP = "^[0-9]{5}$";
    private static final String EMAIL_REGEXP = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$";
    private static final String COD_FISCALE_REGEXP = "^[A-Z]{6}[0-9]{2}[ABCDEHLMPRST][0-9]{2}[A-Z][0-9]{3}[A-Z,0-9]$";
    private static final String TELEFONO_REGEXP = "^[0-9]+((/|-| )|( / | - |))?[0-9]+$";
    private static final String DECIMALE_REGEXP = "^(-?0[,]\\d+)$|^(-?[1-9]+\\d*([,]\\d+)?)$|^0$";

    /**
     * Formatta la data passata
     *
     * @param date
     *            timestamp in input
     * 
     * @return formattazione date
     */
    public static String formatDate(Timestamp date) {
        return formatDate(date, Format.DATE_FORMAT.DAY_FORMAT);
    }

    /**
     * Formatta il data-time passato
     *
     * @param date
     *            timestamp in input
     * 
     * @return formattazione date
     */
    public static String formatDateTime(Timestamp date) {
        return formatDate(date, Format.DATE_FORMAT.MINUTE_FORMAT);
    }

    public static String formatDate(Timestamp date, Format.DATE_FORMAT format) {
        if (date == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat(format.format());
        return df.format(date);
    }

    public static String formatBigDecimal(BigDecimal integer) {
        if (integer == null) {
            return "";
        } else {
            return String.valueOf(integer);
        }
    }

    /**
     * Verifica che la stringa passata sia una data
     *
     * @param strDate
     *            data in formato string
     * 
     * @return true/false con risultato verifica
     */
    public static boolean checkDate(String strDate) {
        try {
            parseDate(strDate);
            return true;
        } catch (EMFError e) {
            return false;
        }
    }

    /**
     * Trasforma la stringa passati in Timestamp
     *
     * @param strDate
     *            data in formato string
     * 
     * @return Timestamp della data in ingresso
     * 
     * @throws ParseException
     */
    public static Timestamp parseDate(String strDate) throws EMFError {
        Timestamp result = null;
        if (!StringUtils.isBlank(strDate)) {
            try {
                java.util.Date parsedData = Format.parseDate(strDate);
                result = new Timestamp(parsedData.getTime());
            } catch (ParseException e) {
                throw new EMFError(EMFError.WARNING, "Errore nella conversione di formato", e);
            }
        }
        return result;
    }

    /**
     * Trasforma la stringa passati in Timestamp
     *
     * @param strInteger
     *            data in formato integer
     * 
     * @return BigDecimal della data in ingresso
     * 
     * @throws ParseException
     */
    public static BigDecimal parseInteger(String strInteger) throws EMFError {
        if (StringUtils.isBlank(strInteger)) {
            return null;
        }

        // Rimosso isDigits per poter gestire il "-" dei numeri negativi che altrimenti non verrebbero considerati
        // sostituito isNumber in quanto deprecato
        // if (!NumberUtils.isDigits(strInteger) || !NumberUtils.isNumber(strInteger)) {
        if (!NumberUtils.isCreatable(strInteger)) {
            throw new EMFError(EMFError.WARNING, "Errore nella conversione di formato");
        } else {
            return NumberUtils.createBigDecimal(strInteger);
        }
    }

    /**
     * Trasforma la stringa passata in numero decimale
     *
     * @param strDecimal
     *            data in formato string
     * 
     * @return BigDecimal della data in ingresso
     * 
     * @throws EMFError
     */
    public static BigDecimal parseDecimal(String strDecimal) throws EMFError {
        if (StringUtils.isBlank(strDecimal)) {
            return null;
        }

        if (!strDecimal.matches(DECIMALE_REGEXP)) {
            throw new EMFError(EMFError.WARNING, "Errore nella conversione di formato");
        } else {
            strDecimal = strDecimal.replace(",", ".");
            return NumberUtils.createBigDecimal(strDecimal);
        }

    }

    /**
     * Trasforma la stringa passata in CAP
     *
     * @param cap
     *            in ingresso
     * 
     * @return parsing del CAP fornito
     * 
     * @throws EMFError
     */
    public static String parseCAP(String cap) throws EMFError {
        if (StringUtils.isBlank(cap)) {
            return null;
        }
        if (!cap.matches(CAP_REGEXP)) {
            throw new EMFError(EMFError.WARNING, "Errore nella conversione del cap");
        } else {
            return cap;
        }
    }

    /**
     * Trasforma la stringa passata in Email
     *
     * @param email
     *            in ingresso
     * 
     * @return parsing della mail con espressione regolare
     * 
     * @throws EMFError
     */
    public static String parseEmail(String email) throws EMFError {
        if (StringUtils.isBlank(email)) {
            return null;
        }
        if (!email.toUpperCase().matches(EMAIL_REGEXP)) {
            throw new EMFError(EMFError.WARNING, "Errore nella conversione dell'email");
        } else {
            return email;
        }

    }

    /**
     * Trasforma la stringa passata in Codice fiscale
     *
     * @param codFiscale
     *            in ingresso
     * 
     * @return del codice fiscale con espressione regolare
     * 
     * @throws EMFError
     */
    public static String parseCodFiscale(String codFiscale) throws EMFError {
        if (StringUtils.isBlank(codFiscale)) {
            return null;
        }
        if (!codFiscale.toUpperCase().matches(COD_FISCALE_REGEXP)) {
            throw new EMFError(EMFError.WARNING, "Errore nella conversione del codice fiscale");
        } else {
            return codFiscale;
        }
    }

    /**
     * Trasforma la stringa passata in una partita IVA
     *
     * @param piva
     *            in ingresso
     * 
     * @return della partiva iva con espressione regolare
     * 
     * @throws EMFError
     */
    public static String parsePartitaIva(String piva) throws EMFError {
        if (StringUtils.isBlank(piva)) {
            return null;
        }

        piva = piva.trim();

        // Controllo lunghezza
        if (piva.length() < 11) {
            throw new EMFError(EMFError.WARNING, "Errore nella validazione della partita IVA");
        }

        if (!StringUtils.isNumeric(piva)) {
            throw new EMFError(EMFError.WARNING, "Errore nella validazione della partita IVA");
        }

        if (piva == "00000000000") {
            throw new EMFError(EMFError.WARNING, "Errore nella validazione della partita IVA");
        }

        // controllo check-digit
        int sumDispari = 0;
        int sumPari1 = 0;
        int sumPari2 = 0;

        for (int i = 0; i < piva.length() - 1; i++) {
            int c = piva.charAt(i) - '0';

            if ((i + 1) % 2 != 0) // numeri dispari
            {
                sumDispari += c;
            } else // numeri pari
            {
                if (c >= 0 && c <= 4) {
                    sumPari1 += c * 2;
                } else if (c >= 5 && c <= 9) {
                    sumPari2 += (c * 2) - 9;
                }
            }
        }

        int total = sumDispari + sumPari1 + sumPari2;

        String Totale = Integer.toString(total);

        char tmpCheck = Totale.charAt(Totale.length() - 1);

        int inttmpCheck = tmpCheck - '0';
        int CheckDigit = 0;

        if (inttmpCheck >= 1 && inttmpCheck <= 9) {
            CheckDigit = 10 - inttmpCheck;
        } // CheckDigit = 10 - tmpchk[0];
        else {
            CheckDigit = 0;
        }

        if (!(CheckDigit == (piva.charAt(piva.length() - 1) - '0'))) {
            throw new EMFError(EMFError.WARNING, "Errore nella validazione della partita IVA");
        }

        return piva;
    }

    /**
     * Trasforma la stringa passata in Codice fiscale o Partita iva
     *
     * @param codice
     *            in ingresso
     * 
     * @return del codice fiscale con espressione regolare
     * 
     * @throws EMFError
     */
    public static String parseCodFiscalePartitaIva(String codice) throws EMFError {
        if (isCodiceFiscale(codice) || isPartitaIva(codice)) {
            return codice;
        } else {
            throw new EMFError(EMFError.WARNING, "Errore nella validazione del codice fiscale o della partita IVA");
        }
    }

    public static boolean isCodiceFiscale(String codice) {
        String result = null;
        try {
            result = parseCodFiscale(codice);
        } catch (EMFError e) {

        }
        return result != null;
    }

    public static boolean isPartitaIva(String codice) {
        String result = null;
        try {
            result = parsePartitaIva(codice);
        } catch (EMFError e) {

        }
        return result != null;
    }

    /**
     * Trasforma la stringa passata in Numero telefonico
     *
     * @param telefono
     *            in ingresso
     * 
     * @return del telefono con espressione regolare
     * 
     * @throws EMFError
     */
    public static String parseTelefono(String telefono) throws EMFError {
        if (StringUtils.isBlank(telefono)) {
            return null;
        }
        if (!telefono.toUpperCase().matches(TELEFONO_REGEXP)) {
            throw new EMFError(EMFError.WARNING, "Errore nella conversione del telefono");
        } else {
            return telefono;
        }

    }

    public static String parsePrefissoTelFax(String prefisso) throws EMFError {
        if (StringUtils.isBlank(prefisso)) {
            return null;
        }
        boolean res = false;
        res = prefisso.matches("^[0-9]{1,4}$");
        if (prefisso.contains("0")) {
            if (prefisso.length() == 1 || prefisso.length() == StringUtils.countMatches(prefisso, "0")) {
                res = false;
            }
        }
        if (!res) {
            throw new EMFError(EMFError.WARNING, "Errore nella conversione del prefisso");
        } else {
            return prefisso;
        }

    }

    public static final String format(Object value, Type.Enum type, String format) throws EMFError {
        try {
            if (StringUtils.isNotBlank(format) && (type.equals(Type.DATE) || type.equals(Type.DATETIME))) {
                Format.DATE_FORMAT fmt = Format.DATE_FORMAT.parse(format);
                return formatDate((Timestamp) value, fmt);
            } else {
                return format(value, type);
            }
        } catch (ParseException e) {
            throw new EMFError(EMFError.ERROR, e.getMessage());
        }
    }

    /**
     * Formatta il valore passato
     *
     * @param value
     *            oggetto generico
     * @param type
     *            tipo {@link Enum}
     * 
     * @return risultato della formattazione
     * 
     * @throws ParseException
     */
    public static final String format(Object value, Type.Enum type) throws EMFError {
        if (value != null) {
            if (type.equals(Type.STRING) || type.equals(Type.CAP) || type.equals(Type.EMAIL)
                    || type.equals(Type.CODFISCALE) || type.equals(Type.TELEFONO) || type.equals(Type.PARTITAIVA)
                    || type.equals(Type.CODFISCPIVA) || type.equals(Type.PREFISSOTEL) || type.equals(Type.PASSWORD)) {
                return StringUtils.isBlank((String) value) ? "" : (String) value;
            } else if (type.equals(Type.DATE)) {
                return formatDate((Timestamp) value);
            } else if (type.equals(Type.DATETIME)) {
                return formatDateTime((Timestamp) value);
            } else if (type.equals(Type.INTEGER)) {
                return formatBigDecimal((BigDecimal) value);
            } else if (type.equals(Type.DECIMAL)) {
                return formatBigDecimal((BigDecimal) value);
            } else if (type.equals(Type.CURRENCY)) {
                throw new EMFError(EMFError.ERROR, "Cconversione di formato da implementare", null);
            }
        }
        return ("");

    }

    /**
     * Verifica il valore passato
     *
     * @param value
     *            string in ingresso
     * @param type
     *            tipo {@link Enum}
     * 
     * @return true/false con verifica
     */
    public static final boolean check(String value, Type.Enum type) {
        try {
            parse(value, type);
            return true;
        } catch (EMFError e) {
            return false;
        }
    }

    public static final boolean check(Set<String> values, Type.Enum type) {
        try {
            if (values != null) {
                for (String value : values) {
                    parse(value, type);
                }
            }
            return true;
        } catch (EMFError e) {
            return false;
        }
    }

    /**
     * Controlla il valore passato
     *
     * @param value
     *            string in ingresso
     * @param type
     *            tipo {@link Enum}
     * 
     * @return oggetto parsato
     * 
     * @throws EMFError
     */
    public static final Object parse(String value, Type.Enum type) throws EMFError {
        if (StringUtils.isNotBlank(value)) {
            if (type.equals(Type.STRING)) {
                return StringUtils.trim(value);
            } else if (type.equals(Type.PASSWORD)) {
                return value;
            } else if (type.equals(Type.DATE) || type.equals(Type.DATETIME)) {
                return parseDate(value);
            } else if (type.equals(Type.INTEGER)) {
                return parseInteger(value);
            } else if (type.equals(Type.DECIMAL)) {
                return parseDecimal(value);
            } else if (type.equals(Type.CURRENCY)) {
                throw new EMFError(EMFError.ERROR, "Conversione di formato da implementare", null);
            } else if (type.equals(Type.CAP)) {
                return parseCAP(value);
            } else if (type.equals(Type.EMAIL)) {
                return parseEmail(value);
            } else if (type.equals(Type.CODFISCALE)) {
                return parseCodFiscale(value);
            } else if (type.equals(Type.TELEFONO)) {
                return parseTelefono(value);
            } else if (type.equals(Type.PARTITAIVA)) {
                return parsePartitaIva(value);
            } else if (type.equals(Type.CODFISCPIVA)) {
                return parseCodFiscalePartitaIva(value);
            } else if (type.equals(Type.PREFISSOTEL)) {
                return parsePrefissoTelFax(value);
            } else if (type.equals(Type.FILE)) {
                return StringUtils.trim(value);
            }
        }
        return null;
    }

    public static final void setToExcel(Cell cell, String value, Type.Enum type) throws EMFError {
        if (StringUtils.isNotBlank(value)) {
            if (type.equals(Type.STRING) || type.equals(Type.CAP) || type.equals(Type.EMAIL)
                    || type.equals(Type.CODFISCALE) || type.equals(Type.TELEFONO) || type.equals(Type.PARTITAIVA)
                    || type.equals(Type.CODFISCPIVA) || type.equals(Type.PREFISSOTEL)) {
                cell.setCellValue(StringUtils.trim(value));
            } else if (type.equals(Type.DATE) || type.equals(Type.DATETIME)) {
                cell.setCellValue(parseDate(value));
            } else if (type.equals(Type.INTEGER) || type.equals(Type.DECIMAL)) {
                if (StringUtils.isNotBlank(value)) {
                    try {
                        Number num = new DecimalFormat("", new DecimalFormatSymbols(Locale.ITALIAN)).parse(value);
                        cell.setCellValue(num.doubleValue());
                    } catch (ParseException ex) {
                        cell.setCellValue("");
                    }
                    /*
                     * if (NumberUtils.isDigits(value)) { cell.setCellValue(Double.parseDouble(value)); } else {
                     * cell.setCellValue(""); }
                     */
                } else {
                    cell.setCellValue(value);
                }
            } else if (type.equals(Type.CURRENCY)) {
                // FIXME
                throw new EMFError(EMFError.ERROR, "Conversione di formato da implementare", null);
            }
        }
    }

}
