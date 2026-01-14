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

/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package it.eng.spagoLite.actions.application;

/**
 *
 * @author Iacolucci_M
 *
 *         Oggetto contenente gli attributi di base di ogni applicazione che usa spagolite. Ad
 *         esempio serve per poter far funzionare il motore di richiesta HelpOnline che richiede il
 *         nome dell'applicazione e l'utenet e la password applicativa per comunicare con IAM.
 */
public class ApplicationBaseProperties {

    public static final String CONST_HELP_RICERCA_DIPS = "HELP_RICERCA_DIPS";
    public static final String CONST_HELP_PAGINA = "HELP_PAGINA";
    public static final String CONST_INFO_PRIVACY = "INFO_PRIVACY";

    private String applicationName;
    private String applicationUserName;
    private String applicationPassword;
    private String urlHelp;

    public ApplicationBaseProperties(String applicationName, String applicationUserName,
            String applicationPassword, String urlHelp) {
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
