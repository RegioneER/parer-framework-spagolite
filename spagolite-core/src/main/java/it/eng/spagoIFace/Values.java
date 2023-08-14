package it.eng.spagoIFace;

public final class Values {

    public static final String LOGGED_USER = "LOGGED_USER";

    public static final String MENU_STATUS = "MENU_STATUS";
    public static final int MENU_OPEN = 0;
    public static final int MENU_CLOSED = 1;
    public static final String CURRENT_ACTION = "CURRENT_ACTION";
    public static final int LIST_DEFAULT_PAGE_SIZE = 10;
    public static final String OPERATION = "operation";
    public static final String PUBLISHER_REDIRECT = "REDIRECT";
    public static final String VOID_SESSION = "VOID_SESSION";
    public static final String SESSION_ID = "SESSION_ID";
    public static final String OPERATION_SEPARATOR = "__";
    public static final int SELECT_LIST_MAX_ROW = 20;
    public static final String SUB_LIST = "TABELLA";

    private Values() {
        throw new IllegalStateException("Impossibile istanziare la classe, contiene solo costanti");
    }

}
