package it.eng.spagoLite.tag;

import it.eng.spagoCore.configuration.ConfigSingleton;
import it.eng.spagoCore.util.JavaScript;
import it.eng.spagoLite.security.CsrfHelper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang3.StringUtils;

public class HeadTag extends BaseSpagoLiteTag {

    private static final String IMG_FAVICON = "/img/regione/favicon.ico";

    private static final long serialVersionUID = 1L;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int doStartTag() throws JspException {
        String contextPath = this.getContextPath();
        writeln(" <head>");
        writeln(includeTitle(contextPath));
        writeln(includeCss(contextPath));
        writeln(includeJs(contextPath));

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

        css.append(
                "  <link href=\"" + contextPath + "/css/slForms-over.css\" rel=\"stylesheet\" type=\"text/css\" />\n");

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
                + "/webjars/font-awesome/4.7.0/css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n");

        css.append("  <link href=\"" + contextPath
                + "/webjars/highlightjs/9.15.10/styles/magula.min.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n");

        /**
         * Custom
         */
        css.append("  <link href=\"" + contextPath
                + "/css/custom/jquery-ui-custom/1.12.1/jquery-ui.min.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n");

        css.append("  <link href=\"" + contextPath
                + "/css/custom/chosen/1.8.7/chosen.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n");

        css.append("  <link href=\"" + contextPath
                + "/css/custom/highlightjs/9.15.10/highlightjs.custom.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n");

        css.append("  <link href=\"" + contextPath
                + "/css/custom/select2/4.0.13/select2.custom.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n");

        return css.toString();
    }

    protected String includeTitle(String contextPath) {
        String titleHtml = JavaScript.stringToHTMLString(getTitle());

        StringBuilder title = new StringBuilder();
        if (StringUtils.isNotBlank(ConfigSingleton.getTitolo_applicativo())) {
            // se è stato indicato un nome per l'applicazione nel file web.xml
            // lo racchiudo tra parentesi quadre e lo antepongo al titolo della
            // pagina definito nella JSP
            title.append("  <title> [" + ConfigSingleton.getTitolo_applicativo() + "] " + titleHtml + "</title>\n");
        } else {
            title.append("  <title>" + titleHtml + "</title>\n");
        }
        title.append("  <meta http-equiv=\"Content-Language\" content=\"it\" />\n");
        title.append("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
        // csrf
        title.append(includeCsrfToken());

        title.append(
                "  <link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"" + contextPath + IMG_FAVICON + "\" />\n");

        return title.toString();
    }

    protected String includeJs(String contextPath) {
        StringBuilder js = new StringBuilder();

        /**
         * Webjars
         */
        js.append("  <script type=\"text/javascript\" src=\"" + contextPath
                + "/webjars/jquery/3.4.1/jquery.min.js\"></script>\n");

        // Static (not updatable to webjar)
        js.append("  <script type=\"text/javascript\" src=\"" + contextPath
                + "/js/jQuery/jquery.base64-min.js\"></script>\n");

        js.append("  <script type=\"text/javascript\" src=\"" + contextPath
                + "/webjars/jquery-ui/1.12.1/jquery-ui.min.js\"></script>\n");

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
                + "/webjars/highlightjs/9.15.10/highlight.min.js\"></script>\n");

        js.append("  <script type=\"text/javascript\" src=\"" + contextPath
                + "/webjars/highlightjs-line-numbers.js/dist/highlightjs-line-numbers.min.js\"></script>\n");

        js.append("  <script type=\"text/javascript\" src=\"" + contextPath
                + "/webjars/highlightjs-badgejs/0.0.5/highlightjs-badgejs.min.js\"></script>\n");

        // Internal script
        js.append("  <script type=\"text/javascript\" src=\"" + contextPath + "/js/help/globalsetup.js\"></script>\n");

        js.append("  <script type=\"text/javascript\" src=\"" + contextPath + "/js/help/help.js\"></script>\n");

        return js.toString();
    }

    private String includeCsrfToken() {
        return CsrfHelper.getCsrfMetaDataToken((HttpServletRequest) this.pageContext.getRequest());
    }

}
