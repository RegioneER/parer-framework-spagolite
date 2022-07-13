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

        writeln("<div class=\"sintTitolo\">Utente: " + utente.getNome() + " " + utente.getCognome() + "</div>");
        writeln("<div class=\"sintRiga\">Username: " + utente.getUsername() + "</div>");
        // writeln("<div class=\"sintRiga\"> Email: "+utente.getMailRif() +"</div>");
        writeln("<div class=\"sintRiga\"> Organizzazione: " + utente.getOrganizzazioneMap() + "</div>");
        long diffDays = (utente.getScadenzaPwd().getTime() - new Date().getTime()) / (24 * 60 * 60 * 1000);
        writeln("<div class=\"sintRiga\"> La password scade tra " + diffDays + " giorni. ");
        write(" <a href=\""
                + ((HttpServletResponse) pageContext.getResponse()).encodeURL("Home.html?operation=changePwd")
                + "\">Cambio password</a></div> ");
        return EVAL_PAGE;
    }

}
