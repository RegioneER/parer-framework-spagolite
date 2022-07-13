package it.eng.spagoLite.util;

/**
 * Classe contenitore dei codici di messaggi o errori che si trovano nel file ...WEB-INF\classes\messages_x_Y.properties
 * dove x e Y indica language e country scelte.
 *
 * @author Corrado Vaccari
 */
public abstract class MessageCodes {
    // Quando si aggiungono nuove sezioni, tipo "errori sul lavoratore",
    // partire sempre dal 10000 superiore,
    // Es. ultimo messaggio : 30103, cominciare la nuova sezione da 40000
    // per non rischiare di utilizzare id già usati
    public static abstract class General {
        public static final int NO_ROWS_IN_LIST = 10001;
        public static final int DELETE_SUCCESS = 10002;
        public static final int DELETE_FAIL = 10003;
        public static final int UPDATE_SUCCESS = 10004;
        public static final int UPDATE_FAIL = 10005;
        public static final int GET_ROW_FAIL = 10006;
        public static final int INSERT_SUCCESS = 10007;
        public static final int INSERT_FAIL = 10008;
        public static final int OPERATION_SUCCESS = 10009;
        public static final int OPERATION_FAIL = 10010;
        public static final int ELEMENT_DUPLICATED = 10011;
        public static final int DELETE_FAILED_FK = 10012;
        public static final int UNEXPECTED_INTERRUPT = 10014;
        public static final int ERR_AGGIORNAMETO_AMDOCCOLL = 10015;
        public static final int ERR_NO_DB_CONNECTION = 10016;
        public static final int ERR_IN_WRITING_LOG = 10017;
        public static final int DUPLICATE_ROW = 10018;
        public static final int CONVERSION_TYPE = 10019;
        public static final int DEPENDENT_FIELD_MIN_CHAR_ERROR = 10020;
        public static final int DEPENDENT_FIELD_ERROR = 10021;
        public static final int FORMATO_FILE_NON_AMMESSO = 10022;
        public static final int FILE_TROOOOPPO_GRANDE = 10023;
        public static final int FILE_CARATTERI_NON_AMMESSI = 10024;
        public static final int IMPOSTARE_REGIONE_O_AZIENDA = 10025;
        public static final int IMPOSTARE_REGIONE = 10026;

    }

    public static abstract class Validation {
        public static final int ERRORE_VALIDAZIONE = 20000;
        public static final int OBBLIGATORIO = 20001;
        public static final int NUMERICO = 20002;
        public static final int FORMATO_DATA_NON_CORRETTO = 20003;
        public static final int POSITIVO = 20004;
    }

}
