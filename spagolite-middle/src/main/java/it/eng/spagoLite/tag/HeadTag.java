/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package it.eng.spagoLite.tag;

import static it.eng.spagoCore.configuration.ConfigProperties.URIProperty.CSS_OVER_RELATIVE;
import static it.eng.spagoCore.configuration.ConfigProperties.URIProperty.FAV_ICON_RELATIVE;

import static it.eng.spagoCore.configuration.ConfigProperties.StandardProperty.TITOLO_APPLICATIVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

import it.eng.spagoCore.configuration.ConfigSingleton;
import it.eng.spagoCore.util.JavaScript;
import it.eng.spagoLite.security.CsrfHelper;

public class HeadTag extends BaseSpagoLiteTag {

    private static final long serialVersionUID = 1L;

    private final ConfigSingleton configSingleton = ConfigSingleton.getInstance();

    private String title;
    /* in caso di pagina inclusa in un'altra e non si vogliono ri-caricare le risorse js per evitare problemi */
    private boolean excludeStdJs = false;// default value
    /* creato con stessa logica dei js */
    private boolean excludeStdCss = false;// default value

    @Override
    public int doStartTag() throws JspException {
        String contextPath = this.getContextPath();
        writeln(" <head>");
        writeln(includeTitle(contextPath));
        if (!isExcludeStdCss()) {
            writeln(includeCss(contextPath));
        }
        if (!isExcludeStdJs()) {
            writeln(includeJs(contextPath));
        }

        return EVAL_BODY_INCLUDE;

    }

    @Override
    public int doEndTag() throws JspException {
        writeln(" </head>");
        return EVAL_PAGE;
    }

    protected String includeCss(String contextPath) {
        StringBuilder css = new StringBuilder();

        /**
         * Static
         */
        css.append("  <link href=\"" + contextPath + "/css/help.css\" rel=\"stylesheet\" type=\"text/css\" />\n");

        css.append("  <link href=\"" + contextPath + "/css/slForms.css\" rel=\"stylesheet\" type=\"text/css\" />\n");

        css.append("  <link href=\"" + contextPath + configSingleton.getStringValue(CSS_OVER_RELATIVE.name())
                + "\" rel=\"stylesheet\" type=\"text/css\" />\n");

        css.append("  <link href=\"" + contextPath
                + "/css/slScreen.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n");

        css.append("  <link href=\"" + contextPath
                + "/css/slPrint.css\" rel=\"stylesheet\" type=\"text/css\" media=\"print\" />\n");

        css.append("  <link href=\"" + contextPath
                + "/css/prototype/tags.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n");
        /**
         * Webjars
         */
        css.append("  <link href=\"" + contextPath
                + "/webjars/select2/4.0.13/css/select2.min.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n");

        css.append("  <link href=\"" + contextPath
                + "/webjars/chosen/1.8.7/chosen.min.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n");

        css.append("  <link href=\"" + contextPath
                + "/webjars/jstree/3.3.8/themes/default/style.min.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n");

        /*
         * Highlightjs
         * 
         * <pre><code> block </code></pre>
         * 
         */
        css.append("  <link href=\"" + contextPath
                + "/webjars/font-awesome/6.4.0/css/all.min.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n");

        css.append("  <link href=\"" + contextPath
                + "/webjars/highlightjs/11.5.0/styles/vs.min.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n");

        /**
         * Custom
         */
        css.append("  <link href=\"" + contextPath
                + "/css/custom/jquery-ui-custom/1.13.2/jquery-ui.min.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n");

        css.append("  <link href=\"" + contextPath
                + "/css/custom/chosen/1.8.7/chosen.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n");

        css.append("  <link href=\"" + contextPath
                + "/css/custom/highlightjs/11.5.0/highlightjs.custom.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n");

        css.append("  <link href=\"" + contextPath
                + "/css/custom/select2/4.0.13/select2.custom.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n");

        return css.toString();
    }

    protected String includeTitle(String contextPath) {
        String titleHtml = JavaScript.stringToHTMLString(getTitle());

        StringBuilder localTitle = new StringBuilder();
        if (StringUtils.isNotBlank(configSingleton.getStringValue(TITOLO_APPLICATIVO.name()))) {
            // se e' stato indicato un nome per l'applicazione nel file web.xml
            // lo racchiudo tra parentesi quadre e lo antepongo al titolo della
            // pagina definito nella JSP
            localTitle.append("  <title> [").append(configSingleton.getStringValue(TITOLO_APPLICATIVO.name()))
                    .append("] ").append(titleHtml).append("</title>\n");
        } else {
            localTitle.append("  <title>").append(titleHtml).append("</title>\n");
        }
        localTitle.append("  <meta http-equiv=\"Content-Language\" content=\"it\" />\n");
        localTitle.append("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
        localTitle.append("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        // csrf
        localTitle.append(includeCsrfToken());

        localTitle.append("  <link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"").append(contextPath)
                .append(configSingleton.getStringValue(FAV_ICON_RELATIVE.name())).append("\" />\n");

        return localTitle.toString();
    }

    protected String includeJs(String contextPath) {
        StringBuilder js = new StringBuilder();

        /**
         * Webjars
         */
        js.append("  <script type=\"text/javascript\" src=\"" + contextPath
                + "/webjars/jquery/3.6.4/jquery.min.js\"></script>\n");

        // Static (not updatable to webjar)
        js.append("  <script type=\"text/javascript\" src=\"" + contextPath
                + "/js/jQuery/jquery.base64-min.js\"></script>\n");

        js.append("  <script type=\"text/javascript\" src=\"" + contextPath
                + "/webjars/jquery-ui/1.13.2/jquery-ui.min.js\"></script>\n");

        // Static (not updatable to webjar)
        js.append("  <script type=\"text/javascript\" src=\"" + contextPath
                + "/js/jQuery/snowfall.min.jquery.js\"></script>\n");
        /**
         * Custom
         */
        js.append("  <script type=\"text/javascript\" src=\"" + contextPath + "/js/sips/classes.js\"></script>\n");

        js.append("  <script type=\"text/javascript\" src=\"" + contextPath + "/js/sips/main.js\"></script>\n");

        js.append("  <script type=\"text/javascript\" src=\"" + contextPath
                + "/webjars/chosen/1.8.7/chosen.jquery.min.js\"></script>\n");

        /**
         * Webjars
         */
        js.append(" <script type=\"text/javascript\" src=\"" + contextPath
                + "/webjars/jstree/3.3.8/jstree.min.js\"></script>\n");

        js.append("  <script type=\"text/javascript\" src=\"" + contextPath
                + "/webjars/select2/4.0.13/js/select2.full.min.js\"></script>\n");

        js.append("  <script type=\"text/javascript\" src=\"" + contextPath
                + "/webjars/select2/4.0.13/js/i18n/it.js\"></script>\n");

        /*
         * Highlightjs
         * 
         * <pre><code> block </code></pre>
         * 
         */
        js.append("  <script type=\"text/javascript\" src=\"" + contextPath
                + "/webjars/highlightjs/11.5.0/highlight.min.js\"></script>\n");

        js.append("  <script type=\"text/javascript\" src=\"" + contextPath
                + "/webjars/highlightjs-line-numbers.js/dist/highlightjs-line-numbers.min.js\"></script>\n");

        js.append("  <script type=\"text/javascript\" src=\"" + contextPath
                + "/webjars/highlightjs-badgejs/0.0.5/highlightjs-badgejs.min.js\"></script>\n");

        // Internal script
        js.append("  <script type=\"text/javascript\" src=\"" + contextPath + "/js/help/globalsetup.js\"></script>\n");

        js.append("  <script type=\"text/javascript\" src=\"" + contextPath + "/js/help/help.js\"></script>\n");

        return js.toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String includeCsrfToken() {
        return CsrfHelper.getCsrfMetaDataToken((HttpServletRequest) this.pageContext.getRequest());
    }

    public boolean isExcludeStdJs() {
        return excludeStdJs;
    }

    public void setExcludeStdJs(boolean excludeStdJs) {
        this.excludeStdJs = excludeStdJs;
    }

    public boolean isExcludeStdCss() {
        return excludeStdCss;
    }

    public void setExcludeStdCss(boolean excludeStdCss) {
        this.excludeStdCss = excludeStdCss;
    }

}
