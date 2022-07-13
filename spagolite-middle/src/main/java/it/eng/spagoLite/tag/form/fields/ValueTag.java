package it.eng.spagoLite.tag.form.fields;

import it.eng.spagoCore.util.JavaScript;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.tag.form.BaseFormTag;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringEscapeUtils;

public class ValueTag extends BaseFormTag<SingleValueField<?>> {
    private static final long serialVersionUID = 1L;

    private boolean escapeJavaScript = false;

    @Override
    public int doStartTag() throws JspException {
        String str = getComponent().getValue();
        if (escapeJavaScript) {
            str = StringEscapeUtils.escapeJava(str);
        }

        write(JavaScript.stringToHTMLString(str));
        return SKIP_BODY;
    }

    public boolean isEscapeJavaScript() {
        return escapeJavaScript;
    }

    public void setEscapeJavaScript(boolean escapeJavaScript) {
        this.escapeJavaScript = escapeJavaScript;
    }

}
