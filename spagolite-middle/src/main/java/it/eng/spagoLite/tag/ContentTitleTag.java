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

package it.eng.spagoLite.tag;

import it.eng.spagoLite.SessionManager;

import it.eng.spagoLite.security.IUser;
import it.eng.spagoLite.security.profile.Pagina;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

public class ContentTitleTag extends BaseSpagoLiteTag {

    private static final long serialVersionUID = 1L;
    private String title;
    private String label;
    /*
     * Questo parametro serve ad indicare che il tag deve costruire una chiamata all'help sia nel
     * caso in cui ci sia un help associato alla pagina sia se c'è un help associato al menu.
     */
    private String codiceMenu;
    private boolean showHelpBtn = true;

    @Override
    public int doStartTag() throws JspException {

	/*
	 * NUOVA GESTIONE
	 */
	writeln("<div class=\"contentTitle\">");
	writeln("<h2> " + getCompleteTitle() + "</h2>");

	String pagina = SessionManager.getLastPublisher(pageContext.getSession());
	IUser utente = this.getUser();
	/*
	 * Viene fatto un controllo che l'utente esista (nella pagina del cambio password per
	 * esempio non esiste). Se l'utente non esiste chiude il tag senza fare altro.
	 */
	if (utente != null) {
	    Pagina paginaSelezionata = null;
	    String href = "";
	    /*
	     * Nuovo comportamento per pagine dinamiche per DIPS
	     */
	    String nuovaPagina = pagina;
	    if (codiceOrganizzazione != null && !codiceOrganizzazione.trim().equals("")) {
		nuovaPagina = "[" + codiceOrganizzazione + "]" + pagina;
	    }
	    /*
	     * Nuovo comportamento dell'Help su SIAM
	     */
	    if (utente.getProfile() != null
		    && (paginaSelezionata = (Pagina) utente.getProfile()
			    .getChild(nuovaPagina)) != null
		    && paginaSelezionata.isHelpAvailable()) {
		href = "javascript:mostraHelpPagina();";
		writeln("<a title=\"Vai alla pagina di help online\" href=\"" + href + "\">");
		writeln("<img src=\"" + getContextPath()
			+ "/img/help.png\" title=\"Help online\" alt=\"Help online\" />");
		writeln("</a>");
	    } else {
		if (codiceMenu != null && !codiceMenu.trim().equals("")) {
		    /*
		     * Se il menu associato a questo tag è presente tra ii menu associati alla
		     * pagina selezionata allora attiva l'Help con il codice di menu' per dispenser.
		     */
		    if (paginaSelezionata.containsMenu(codiceMenu)) {
			href = "javascript:mostraHelpPagina('" + codiceMenu + "');";
			writeln("<a title=\"Vai alla pagina di help online\" href=\"" + href
				+ "\">");
			writeln("<img src=\"" + getContextPath()
				+ "/img/help.png\" title=\"Help online\" alt=\"Help online\" />");
			writeln("</a>");
		    }
		}
	    }
	}
	writeln("</div>");
	return EVAL_PAGE;
    }

    private String getCompleteTitle() {
	String result = getTitle();
	if (StringUtils.isNotBlank(getLabel())) {
	    result = getLabel() + ": " + result;
	}
	return result;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

    public boolean isShowHelpBtn() {
	return showHelpBtn;
    }

    public void setShowHelpBtn(boolean showHelpBtn) {
	this.showHelpBtn = showHelpBtn;
    }

    public String getCodiceMenu() {
	return codiceMenu;
    }

    public void setCodiceMenu(String codiceMenu) {
	this.codiceMenu = codiceMenu;
    }

}
