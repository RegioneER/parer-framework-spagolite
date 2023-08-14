package it.eng.parer.sacerlog.entity.constraint;

/**
 *
 * @author Bonora_L
 */
public class ConstLogEventoLoginUser {

    public enum TipoEvento {
        BAD_PASS, BAD_USER, EXPIRED, LOCKED, LOGIN_OK, SET_PSW, BAD_USER_ASSOCIATION, BAD_CF
    }

    public static final String NM_ATTORE_ONLINE = "SacerIam OnLine";
    public static final String NM_ATTORE_WS = "SacerIam WS";

    public static final String DS_EVENTO_UNKNOWN_USER = "Unknown user";
    public static final String DS_EVENTO_ACCOUNT_DISABLED = "Account disabled";
    public static final String DS_EVENTO_ACCOUNT_EXPIRED_ON = "Account expired on {0}";
    public static final String DS_EVENTO_BAD_PSW = "Bad password";
    public static final String DS_EVENTO_RESET_PSW = "Reset password utente";
    public static final String DS_EVENTO_EDIT_PSW = "Modifica password da parte dell'utente";
    public static final String DS_EVENTO_BAD_PSW_EDIT_PSW = "Comunicazione della vecchia password errata nell'ambito della funzione di \"Modifica password\"";
    public static final String DS_EVENTO_USR_LOCKED_EDIT_PSW = "Comunicazione di utente disabilitato nell'ambito della funzione di \"Modifica password\"";
    public static final String DS_EVENTO_UNKNOWN_USR_EDIT_PSW = "Comunicazione di utente inesistente nell'ambito della funzione di \"Modifica password\"";
    public static final String DS_EVENTO_UNKNOWN_USR_ASSOCIA_CF = "Comunicazione di utente inesistente nell'ambito della funzione di \"Associazione codice fiscale\"";
    public static final String DS_EVENTO_BAD_PSW_ASSOCIA_CF = "Comunicazione di password errata nell'ambito della funzione di \"Associazione codice fiscale\"";
    public static final String DS_EVENTO_BAD_CF_SPID = "SPID - Codice fiscale non trovato nella base dati";
    // public static final String DS_EVENTO_BAD_PSW_DISABLE_EDIT_PSW = "Comunicazione della vecchia password errata
    // nell'ambito della funzione di \"Modifica password\"; l'utente viene disabilitato";
    public static final String DS_EVENTO_INSERT_PSW = "Inserimento nuova password utente";
    public static final String DS_EVENTO_RIATTIVA_USER = "Riattivazione utente";
    public static final String DS_EVENTO_INSERT_USER = "Inserimento utente";
}
