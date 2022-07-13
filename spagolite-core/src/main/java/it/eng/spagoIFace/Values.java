package it.eng.spagoIFace;

public class Values {
    public final static String LOGGED_USER = "LOGGED_USER";

    public static final String MENU_STATUS = "MENU_STATUS";
    public static final int MENU_OPEN = 0;
    public static final int MENU_CLOSED = 1;
    public final static String CURRENT_ACTION = "CURRENT_ACTION";
    public final static int LIST_DEFAULT_PAGE_SIZE = 10;
    public final static String OPERATION = "operation";
    public final static String PUBLISHER_REDIRECT = "REDIRECT";
    public final static String VOID_SESSION = "VOID_SESSION";
    public final static String SESSION_ID = "SESSION_ID";
    public static final String OPERATION_SEPARATOR = "__";
    public final static int SELECT_LIST_MAX_ROW = 20;
    public final static String SUB_LIST = "TABELLA";

    // public static abstract class Authentication {
    // private final static String LOCALHOST = "Security.AUTHENTICATION.localhost";
    // private final static String AUTENTICAZIONE_CENTRALIZZATA =
    // "Security.AUTHENTICATION.autenticazione_centralizzata";
    //
    // public static boolean isLocalHost() {
    // return "true".equalsIgnoreCase((String) ConfigSingleton.getInstance().getAttribute(LOCALHOST));
    // }
    //
    // public static String autenticazioneCentralizzata() {
    // return (String) ConfigSingleton.getInstance().getAttribute(AUTENTICAZIONE_CENTRALIZZATA);
    // }
    // }

    // public static abstract class Login {
    // private final static String LOGIN_CLASS = "Security.LOGIN.loginServiceClassname";
    // private final static String CODE = "Security.LOGIN.cdnApplication";
    // private final static String NAME = "Security.LOGIN.appName";
    // private final static String SALT = "Security.LOGIN.appSalt";
    // private final static String MENU_ROOT = "Security.LOGIN.menuRoot";
    // private final static String HOME_ACTION = "Security.LOGIN.actionHome";
    //
    // public static String loginClass() {
    // return (String) ConfigSingleton.getInstance().getAttribute(LOGIN_CLASS);
    // }
    //
    // public static String homeAction() {
    // return (String) ConfigSingleton.getInstance().getAttribute(HOME_ACTION);
    // }
    //
    // public static String menuRoot() {
    // return (String) ConfigSingleton.getInstance().getAttribute(MENU_ROOT);
    // }
    //
    // public static BigDecimal code() {
    // return new BigDecimal((String) ConfigSingleton.getInstance().getAttribute(CODE));
    // }
    //
    // public static String name() {
    // return (String) ConfigSingleton.getInstance().getAttribute(NAME);
    // }
    //
    // public static String salt() {
    // return (String) ConfigSingleton.getInstance().getAttribute(SALT);
    // }
    // }

}
