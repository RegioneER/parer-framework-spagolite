package it.eng.spagoLite.tag;

import java.util.Map;

import it.eng.spagoCore.configuration.ConfigSingleton;
import it.eng.spagoCore.util.JavaScript;
import it.eng.spagoLite.security.User;
import it.eng.spagoLite.security.menu.MenuEntry;
import it.eng.spagoLite.security.menu.impl.Link;
import it.eng.spagoLite.security.menu.impl.Menu;
import it.eng.spagoLite.util.Casting.Casting;
import it.eng.spagoLite.util.Format;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

public class MenuTag extends BaseSpagoLiteTag {

    private static final String IMG_SHOW_MENU = "/img/menu/showMenu.png";
    private static final String IMG_HIDE_MENU = "/img/menu/hideMenu.png";
    private static final String IMG_USER_ICON = "/img/menu/user.png";
    private static final String IMG_CHANGE_PWD = "/img/menu/changePwd.png";

    private static final long serialVersionUID = 1L;
    private boolean showChangePasswordBtn = true;

    @Override
    public int doStartTag() throws JspException {
        if (getMessageBox() == null || !getMessageBox().hasFatal()) {

            Menu menu = getUser().getMenu();

            writeln("<!-- Menu -->\n");
            writeln("  <div class=\"menu\" id=\"menu\">");
            writeln(" <div class=\"version\">" + ConfigSingleton.get_appName() + " v."
                    + ConfigSingleton.get_appVersion() + " | " + ConfigSingleton.get_appBuildDate() + "</div>");
            drawUserInfo();
            drawCloseMenu();
            closeUserInfo();
            // FIXME: gestire lo status
            writeln("  <span class=\"todo\"/>");
            if (menu != null) {
                writeln("    <ul>");

                for (MenuEntry menuEntry : menu) {
                    drawMenu(menuEntry);
                }

                writeln("    </ul>");
            }

            writeln("  </div>");
            drawOpenMenu();
        }
        return SKIP_BODY;
    }

    private void closeUserInfo() throws JspException {
        writeln("</div>");

    }

    private void drawUserInfo() throws JspException {

        StringBuilder infoUtente = new StringBuilder();
        infoUtente.append("<div class=\"user\">");
        infoUtente.append("<div class=\"nome\">");
        infoUtente.append("     <img src=\"" + getContextPath() + IMG_USER_ICON
                + "\" alt=\"Nome utente\" title=\"Nome utente\" />");
        infoUtente.append(JavaScript.stringToHTMLString(getUser().getNome()) + " "
                + JavaScript.stringToHTMLString(getUser().getCognome()));
        infoUtente.append(
                "<span class=\"username\">(" + JavaScript.stringToHTMLString(getUser().getUsername()) + ")</span>");
        infoUtente.append("</div>");
        if (getUser().getScadenzaPwd() != null) {
            infoUtente.append("<br />");
            infoUtente.append("<div class=\"scadenzaPassword\"><span><b>Data scadenza password: </b></span>");
            SimpleDateFormat sdf = new SimpleDateFormat(Format.DATE_FORMAT.DAY_FORMAT.format());
            infoUtente.append(JavaScript.stringToHTMLString(sdf.format(getUser().getScadenzaPwd())));
            infoUtente.append("</div>");
        }
        infoUtente.append("<br />");
        if (showChangePasswordBtn) {
            infoUtente.append("<div class=\"cambioPassword\">");
            infoUtente.append("     <img src=\"" + getContextPath() + IMG_CHANGE_PWD
                    + "\" alt=\"Cambia password\" title=\"Cambio password\" />");
            infoUtente.append("<a href=\""
                    + ((HttpServletResponse) pageContext.getResponse()).encodeURL("Home.html?operation=changePwd")
                    + "\">Cambia password</a>");
            infoUtente.append("</div>");
            infoUtente.append("<br />");
        }
        if (getUser().getOrganizzazioneMap() != null) {
            Map<String, String> map = getUser().getOrganizzazioneMap();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String nome = entry.getKey();
                String value = entry.getValue();
                infoUtente.append("<div><label class=\"slLabel\" >");
                infoUtente.append(nome + ": ");
                infoUtente.append("</label>");
                // Recupero dalla sessione il parametro linkabile da visualizzare nel menu
                String link = (String) pageContext.getSession().getAttribute(nome + "_LINK");
                if (link != null) {
                    infoUtente.append(link);
                } else {
                    infoUtente.append(value);
                }
                infoUtente.append("</div>");

            }
            // String[] nmStrutCompleto = getUser().getSceltaMap().split(",");
            // infoUtente
            // .append("<div><label class=\"slLabel\" >AMBIENTE: </label>");
            // infoUtente.append(nmStrutCompleto[0]);
            // infoUtente.append("</div>");
            // infoUtente
            // .append("<div><label class=\"slLabel\" >ENTE: </label>");
            // infoUtente.append(nmStrutCompleto[1]);
            // infoUtente.append("</div>");
            // infoUtente
            // .append("<div><label class=\"slLabel\" >STRUTTURA: </label>");
            // infoUtente.append(nmStrutCompleto[2]);
            // infoUtente.append("</div>");
        }
        writeln(infoUtente);
    }

    private void drawCloseMenu() throws JspException {

        writeln("  <!--Chiudi menu-->");
        writeln("  <div class=\"menuButtonOpen\" >");
        writeln("    <button class=\"openClose\" id=\"menuLateraleChiudi\" title=\"Chiudi il menu\">");
        writeln("     <img src=\"" + getContextPath() + IMG_HIDE_MENU
                + "\" alt=\"Chiudi il menu laterale\" title=\"Chiudi il menu laterale\" id=\"menuLateraleImgChiudi\" />");
        writeln("    </button>");
        // writeln(infoUtente);
        writeln("  </div>");
        writeln("  <div class=\"newLine\"></div>");
    }

    private void drawOpenMenu() throws JspException {

        writeln("  <!--Apri menu-->");
        writeln("  <div class=\"menuButtonClose\" >");
        writeln("  <button class=\"openClose\" id=\"menuLateraleApri\" title=\"Apri il menu\">");
        writeln("   <img src=\"" + getContextPath() + IMG_SHOW_MENU
                + "\" alt=\"Apri il menu laterale\" title=\"Apri il menu laterale\" id=\"menuLateraleImgApri\"  />");
        writeln("  </button>\n");
        writeln("  </div>");
    }

    private void drawMenu(MenuEntry menuEntry) throws JspException {
        writeln("    <li class=\"voce\">");

        if (menuEntry instanceof Menu) {
            // Menu
            Menu menu = (Menu) menuEntry;

            writeln("      <h2>" + JavaScript.stringToHTMLString(menu.getDescr()) + "</h2>");

            for (MenuEntry subMenu : menu) {
                String styleClass = "";
                if (!(subMenu instanceof Menu)) {
                    styleClass = subMenu.isSelected() ? " selezionato" : "";
                }
                writeln("    <ul class=\"menu" + styleClass + "\">");
                drawMenu(subMenu);
                writeln("    </ul>");
            }

        } else {
            // E' una Foglia
            Link link = (Link) menuEntry;
            String styleClass = menuEntry.isSelected() ? " class=\"selezionato\" " : "";

            write("      <a href=\"" + link.getUrl() + "\" " + styleClass + ">"
                    + JavaScript.stringToHTMLString(link.getDescr()) + "</a>");

        }

        writeln("   </li>");
    }

    public boolean isShowChangePasswordBtn() {
        return showChangePasswordBtn;
    }

    public void setShowChangePasswordBtn(boolean showChangePasswordBtn) {
        this.showChangePasswordBtn = showChangePasswordBtn;
    }

}
