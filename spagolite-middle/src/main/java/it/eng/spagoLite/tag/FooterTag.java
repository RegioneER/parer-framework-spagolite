package it.eng.spagoLite.tag;

import it.eng.spagoCore.configuration.ConfigSingleton;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang3.StringUtils;

public class FooterTag extends BaseSpagoLiteTag {

    private static final long serialVersionUID = 1L;

    @Override
    public int doStartTag() throws JspException {
        writeln("<!--Footer-->");
        writeln("<div class=\"footer\">");
        writeln(renderFooterInfo(this.getContextPath()));
        return SKIP_BODY;

    }

    @Override
    public int doEndTag() throws JspException {
        writeln("</div>");
        return EVAL_PAGE;
    }

    private String renderFooterInfo(String contextPath) {
        StringBuilder footer = new StringBuilder();
        footer.append("<div class=\"left\">");
        if (StringUtils.isNotBlank(ConfigSingleton.getLogo2_url())) {
            footer.append("<a href=\"").append(ConfigSingleton.getLogo2_url()).append("\"  title=\"")
                    .append(ConfigSingleton.getLogo2_title()).append("\">");

            footer.append("<img src=\"").append(contextPath + ConfigSingleton.getLogo2_relativePath())
                    .append("\" alt=\"").append(ConfigSingleton.getLogo2_alt()).append("\"/>");

            footer.append("</a>");
        } else {
            footer.append("<img src=\"").append(contextPath + ConfigSingleton.getLogo2_relativePath())
                    .append("\" alt=\"").append(ConfigSingleton.getLogo2_alt()).append("\"/>");
        }
        footer.append("</div>\n");

        if (StringUtils.isNotBlank(ConfigSingleton.getLogo3_relativePath())) {
            footer.append("<div class=\"right\">");
            if (StringUtils.isNotBlank(ConfigSingleton.getLogo3_url())) {
                footer.append("<a href=\"").append(ConfigSingleton.getLogo3_url()).append("\"  title=\"")
                        .append(ConfigSingleton.getLogo3_title()).append("\">");

                footer.append("<img src=\"").append(contextPath + ConfigSingleton.getLogo3_relativePath())
                        .append("\" alt=\"").append(ConfigSingleton.getLogo3_alt()).append("\"/>");

                footer.append("</a>");
            } else {
                footer.append("<img src=\"").append(contextPath + ConfigSingleton.getLogo3_relativePath())
                        .append("\" alt=\"").append(ConfigSingleton.getLogo3_alt()).append("\"/>");
            }

            footer.append("</div>\n");
        }

        /*
         * footer.
         * append("<div class=\"center\">I cookie ci aiutano a fornire i nostri servizi. Utilizzando tali servizi, accetti l'utilizzo dei cookie da parte nostra. <a id=\"informativa_cookie\" alt=\"Informativa sui cookie\" href=\""
         * + this.getActionName() + "?operation=mostraInformativa"+ "\">Informazioni</a></div>");
         */

        // footer.append("<div class=\"right\"></div>");
        // if(getUser()!=null){
        // footer.append("<div class=\"center\">");
        // footer.append(" <a
        // href=\""+((HttpServletResponse)pageContext.getResponse()).encodeURL("SceltaStrutture.html")+"\">Cambio
        // struttura</a>");
        // footer.append(" <a
        // href=\""+((HttpServletResponse)pageContext.getResponse()).encodeURL("Home.html?operation=changePwd")+"\">Cambio
        // password</a>");
        // footer.append("</div>");
        // }
        return footer.toString();
    }
}