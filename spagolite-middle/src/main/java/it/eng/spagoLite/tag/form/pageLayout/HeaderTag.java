package it.eng.spagoLite.tag.form.pageLayout;

import static it.eng.spagoCore.configuration.ConfigProperties.StandardProperty.LOGO_1_ALT;
import static it.eng.spagoCore.configuration.ConfigProperties.StandardProperty.LOGO_1_TITLE;
import static it.eng.spagoCore.configuration.ConfigProperties.StandardProperty.LOGO_1_URL;
import static it.eng.spagoCore.configuration.ConfigProperties.StandardProperty.LOGO_APP_ALT;
import static it.eng.spagoCore.configuration.ConfigProperties.StandardProperty.LOGO_APP_TITLE;
import static it.eng.spagoCore.configuration.ConfigProperties.StandardProperty.LOGO_APP_URL;
import static it.eng.spagoCore.configuration.ConfigProperties.URIProperty.LOGO_1_RELATIVE;
import static it.eng.spagoCore.configuration.ConfigProperties.URIProperty.LOGO_APP_RELATIVE;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

import it.eng.spagoCore.configuration.ConfigSingleton;
import it.eng.spagoCore.util.JavaScript;
import it.eng.spagoLite.tag.NewLineTag;
import it.eng.spagoLite.tag.form.BaseTag;

public class HeaderTag extends BaseTag {

    private static final String IMG_HOME = "/img/base/home.png";
    private static final String IMG_LOGOUT = "/img/base/IconaLogout.png";
    private static final String IMG_CAMBIO_STRUT = "/img/base/IconaCambioStruttura.png";

    private static final long serialVersionUID = 1L;
    private static final String HOME_PAGE = "Home.html";
    private boolean showHomeBtn = true;
    private boolean showChangeOrganizationBtn = true;
    private String changeOrganizationBtnDescription = "Cambio organizzazione";
    private String description = "";

    @Override
    public int doStartTag() throws JspException {
        ConfigSingleton configSingleton = ConfigSingleton.getInstance();
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

        // Logo applicazione (in alto a sinistra)
        final String logoApplicazione = prepareImage(configSingleton.getStringValue(LOGO_APP_RELATIVE.name()),
                configSingleton.getStringValue(LOGO_APP_ALT.name()),
                configSingleton.getStringValue(LOGO_APP_URL.name()),
                configSingleton.getStringValue(LOGO_APP_TITLE.name()), contextPath, "floatLeft");
        writeln(logoApplicazione);

        // Logo 1 (in alto a destra)
        final String logo1 = prepareImage(configSingleton.getStringValue(LOGO_1_RELATIVE.name()),
                configSingleton.getStringValue(LOGO_1_ALT.name()), configSingleton.getStringValue(LOGO_1_URL.name()),
                configSingleton.getStringValue(LOGO_1_TITLE.name()), contextPath, "floatRight");
        writeln(logo1);

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
        }
        return SKIP_BODY;

    }

    private String prepareImage(String relativePath, String alt, String url, String title, String contextPath,
            String cssClass) {
        StringBuilder prepareImage = new StringBuilder();

        StringBuilder img = new StringBuilder();
        img.append("<img class=\"").append(cssClass).append("\" src=\"");
        img.append(contextPath).append(relativePath);
        img.append("\" alt=\"");
        img.append(alt);
        img.append("\"/>");

        if (StringUtils.isNotBlank(url)) {
            prepareImage.append("<a href=\"");
            prepareImage.append(url);
            prepareImage.append("\"  title=\"");
            prepareImage.append(title);
            prepareImage.append("\">");

            prepareImage.append(img.toString());

            prepareImage.append("</a>");
        } else {
            prepareImage.append(img.toString());
        }

        return prepareImage.toString();
    }

    private String getHomePage(String contextPath) {
        return contextPath + "/" + JavaScript.stringToHTMLString(HOME_PAGE);
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
