package it.eng.spagoLite.tag.prototype;

import it.eng.spagoCore.util.JavaScript;

import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

public abstract class AbstractLblSelectTag extends AbstractLblTag {

    private static final long serialVersionUID = 1L;

    private String elements;

    public String getElements() {
        return elements;
    }

    public void setElements(String elements) {
        this.elements = elements;
    }

    public int doStartTag() throws JspException {
        return EVAL_BODY_AGAIN;
    }

    protected String getOption() {
        // Scelgo il pool di option da rappresentare
        String buffer = getElements();
        if (getElements() == null || "".equals(getElements().trim())) {
            buffer = getBodyContent().getString();
        }
        buffer = buffer.replace("\r", "").replace("\t", "").replace(",", "\n");
        buffer = StringUtils.strip(buffer, " ");

        // Scrivo le Option
        StringBuffer stringBuffer = new StringBuffer();
        StringTokenizer st = new StringTokenizer(buffer, "\n");
        while (st.hasMoreTokens()) {
            String valore = JavaScript.stringToHTMLString(st.nextToken().trim());
            stringBuffer.append("<option value=\"" + valore + "\">" + valore + "</option>");
        }

        return stringBuffer.toString();
    }

}
