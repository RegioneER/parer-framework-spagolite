package it.eng.spagoLite.tag;

import javax.servlet.jsp.JspException;

public class NewLineTag extends BaseSpagoLiteTag {

    private static final long serialVersionUID = 1L;

    private boolean skipLine;

    public int doStartTag() throws JspException {
        write(Factory.htmlNewLine(isSkipLine()));
        return SKIP_BODY;
    }

    public boolean isSkipLine() {
        return skipLine;
    }

    public void setSkipLine(boolean skipLine) {
        this.skipLine = skipLine;
    }

    public static class Factory {

        public static String htmlNewLine(boolean skipLine) {
            return "<div class=\"newLine " + (skipLine ? "skipLine" : "") + "\"></div>\n";
        }

        public static String htmlNewLine() {
            return htmlNewLine(false);
        }

    }
}
