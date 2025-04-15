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

import java.util.Date;

import it.eng.spagoLite.security.IUser;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

public class UserInfoTag extends BaseSpagoLiteTag {

    private static final long serialVersionUID = 1L;

    @Override
    public int doStartTag() throws JspException {
	IUser utente = getUser();

	writeln("<div class=\"sintTitolo\">Utente: " + utente.getNome() + " " + utente.getCognome()
		+ "</div>");
	writeln("<div class=\"sintRiga\">Username: " + utente.getUsername() + "</div>");
	// writeln("<div class=\"sintRiga\"> Email: "+utente.getMailRif() +"</div>");
	writeln("<div class=\"sintRiga\"> Organizzazione: " + utente.getOrganizzazioneMap()
		+ "</div>");
	long diffDays = (utente.getScadenzaPwd().getTime() - new Date().getTime())
		/ (24 * 60 * 60 * 1000);
	writeln("<div class=\"sintRiga\"> La password scade tra " + diffDays + " giorni. ");
	write(" <a href=\"" + ((HttpServletResponse) pageContext.getResponse())
		.encodeURL("Home.html?operation=changePwd") + "\">Cambio password</a></div> ");
	return EVAL_PAGE;
    }

}
