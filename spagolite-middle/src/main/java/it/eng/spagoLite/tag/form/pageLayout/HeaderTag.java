package it.eng.spagoLite.tag.form.pageLayout;

import it.eng.spagoCore.configuration.ConfigSingleton;
import it.eng.spagoCore.util.JavaScript;
import it.eng.spagoLite.tag.NewLineTag;
import it.eng.spagoLite.tag.form.BaseTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

public class HeaderTag extends BaseTag {

    private static final String IMG_HOME = "/img/base/home.png";
    private static final String IMG_LOGOUT = "/img/base/IconaLogout.png";
    private static final String IMG_CAMBIO_STRUT = "/img/base/IconaCambioStruttura.png";
    private static final String IMG_SACER = "/img/logo_sacer_small.png";

    private static final long serialVersionUID = 1L;
    private static final String HREF_BASE = "/menu/AdapterHTTP?";
    private static final String HREF_ACTION = HREF_BASE + "action_name=";
    private static final String HOME_PAGE = "Home.html";
    private boolean showHomeBtn = true;
    private boolean showChangeOrganizationBtn = true;
    private String changeOrganizationBtnDescription = "Cambio organizzazione";
    private String description = "";

    // private static final String HOME_PAGE =
    // "HOME_ACTION&CURRENT_TOP_MENU_ENTRY=SIPS_GESTIONE&VOID_SESSION=TRUE";

    @Override
    public int doStartTag() throws JspException {
        String description = this.description;
        HttpServletRequest httpRequest = (HttpServletRequest) pageContext.getRequest();
        if ("".equals(description) && getForm() != null) {
            description = JavaScript.stringToHTMLString(getForm().getDescription());
        }
        String contextPath = ((HttpServletRequest) pageContext.getRequest()).getContextPath();
        writeln("<!--Skip navigation-->");
        writeln("<div class=\"skipLink\">");
        writeln(" <ul>");
        writeln("   <li><a href=\"#content\" accesskey=\"k\" class=\"skipnav\" >Salta al contenuto</a></li>");
        writeln("   <li><a href=\"#home-page\" accesskey=\"h\" class=\"skipnav\" >Vai alla Pagina principale</a></li>");
        writeln("   <li><a href=\"#menu\" accesskey=\"n\" class=\"skipnav\" >Salta al menu</a></li>");
        writeln(" </ul>");
        writeln("</div>");
        writeln("<!--Header-->");
        writeln("<div class=\"header\">");
        writeln(" <img class=\"floatLeft\" src=\"" + contextPath + IMG_SACER + "\" alt=\"Logo\"/>");

        if (StringUtils.isNotBlank(ConfigSingleton.getLogo1_url())) {
            writeln("<a href=\"");
            writeln(ConfigSingleton.getLogo1_url());
            writeln("\"  title=\"");
            writeln(ConfigSingleton.getLogo1_title());
            writeln("\">");

            writeln("<img class=\"floatRight\" src=\"");
            writeln(contextPath + ConfigSingleton.getLogo1_relativePath());
            writeln("\" alt=\"");
            writeln(ConfigSingleton.getLogo1_alt());
            writeln("\"/>");

            writeln("</a>");
        } else {
            writeln("<img class=\"floatRight\" src=\"");
            writeln(contextPath + ConfigSingleton.getLogo1_relativePath());
            writeln("\" alt=\"");
            writeln(ConfigSingleton.getLogo1_alt());
            writeln("\"/>");
        }

        writeln("  <div class=\"newLine\"></div>");

        writeln("</div>");
        writeln(NewLineTag.Factory.htmlNewLine());

        // Nuova TOOLBAR
        if (getUser() != null && showHomeBtn) {
            writeln("  <div class=\"toolBar\">");
            writeln("    <a tabindex=\"0\" id=\"home-page\" class=\"floatLeft\" href=\""
                    + getHomePage(httpRequest.getContextPath()) + "\" title=\"Torna alla pagina principale\">");
            writeln("     <img style=\"margin: 5px 0px 0px 5px;\" src=\"" + contextPath + IMG_HOME
                    + "\" alt=\"Torna alla pagina principale\" title=\"Torna alla pagina principale\" />");
            writeln("    </a>");
            writeln(" <h2 class=\"floatLeft\">");
            // for(ExecutionHistory his : SessionManager.getExecutionHistory(httpRequest.getSession())){
            // if(his.getForm()!=null)
            // writeln( his.getForm().getDescription() +" >> ");
            // }

            writeln(description + "</h2>");
            writeln("<div class=\"right\">");
            if (showChangeOrganizationBtn) {
                writeln("<h2><a href=\""
                        + ((HttpServletResponse) pageContext.getResponse()).encodeURL("SceltaOrganizzazione.html")
                        + "\" title=\"" + changeOrganizationBtnDescription + "\">"
                        + "<img style=\"padding-right: 5px;\" src=\"" + contextPath + IMG_CAMBIO_STRUT + "\" alt=\""
                        + changeOrganizationBtnDescription + "\" title=\"" + changeOrganizationBtnDescription + "\" />"
                        + changeOrganizationBtnDescription + "</a></h2>" + " | ");
            }
            writeln("<h2><a href=\"" + ((HttpServletResponse) pageContext.getResponse()).encodeURL("Logout.html")
                    + "\" title=\"Logout\">" + "<img style=\"padding-right: 5px;\" src=\"" + contextPath + IMG_LOGOUT
                    + "\" alt=\"Logout\" title=\"Logout\" />" + "Logout</a></h2>");

            writeln("</div>");
            writeln("  </div>");

        } else if (!"".equals(this.description)) {
            writeln(" <h2 class=\"floatLeft\">" + description + "</h2>");
        }
        return SKIP_BODY;

    }

    private String getHomePage(String contextPath) {
        return contextPath + "/" +
        // HREF_ACTION +
                JavaScript.stringToHTMLString(HOME_PAGE);
    }

    public boolean isShowHomeBtn() {
        return showHomeBtn;
    }

    public void setShowHomeBtn(boolean showHomeBtn) {
        this.showHomeBtn = showHomeBtn;
    }

    @Override
    public int doEndTag() throws JspException {

        return EVAL_PAGE;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isShowChangeOrganizationBtn() {
        return showChangeOrganizationBtn;
    }

    public void setShowChangeOrganizationBtn(boolean showChangeOrganizationBtn) {
        this.showChangeOrganizationBtn = showChangeOrganizationBtn;
    }

    public String getChangeOrganizationBtnDescription() {
        return changeOrganizationBtnDescription;
    }

    public void setChangeOrganizationBtnDescription(String changeOrganizationBtnDescription) {
        this.changeOrganizationBtnDescription = changeOrganizationBtnDescription;
    }
}
