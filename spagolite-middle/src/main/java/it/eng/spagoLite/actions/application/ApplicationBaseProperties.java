/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.spagoLite.actions.application;

/**
 *
 * @author Iacolucci_M
 * 
 *         Oggetto contenente gli attributi di base di ogni applicazione che usa spagolite. Ad esempio serve per poter
 *         far funzionare il motore di richiesta HelpOnline che richiede il nome dell'applicazione e l'utenet e la
 *         password applicativa per comunicare con IAM.
 */
public class ApplicationBaseProperties {

    public static final String CONST_HELP_RICERCA_DIPS = "HELP_RICERCA_DIPS";
    public static final String CONST_HELP_PAGINA = "HELP_PAGINA";

    private String applicationName;
    private String applicationUserName;
    private String applicationPassword;
    private String urlHelp;

    public ApplicationBaseProperties(String applicationName, String applicationUserName, String applicationPassword,
            String urlHelp) {
        this.applicationName = applicationName;
        this.applicationUserName = applicationUserName;
        this.applicationPassword = applicationPassword;
        this.urlHelp = urlHelp;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationUserName() {
        return applicationUserName;
    }

    public void setApplicationUserName(String applicationUserName) {
        this.applicationUserName = applicationUserName;
    }

    public String getApplicationPassword() {
        return applicationPassword;
    }

    public void setApplicationPassword(String applicationPassword) {
        this.applicationPassword = applicationPassword;
    }

    public String getUrlHelp() {
        return urlHelp;
    }

    public void setUrlHelp(String urlHelp) {
        this.urlHelp = urlHelp;
    }

}
