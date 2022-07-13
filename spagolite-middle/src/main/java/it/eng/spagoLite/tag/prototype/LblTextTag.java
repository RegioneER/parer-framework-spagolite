package it.eng.spagoLite.tag.prototype;

import it.eng.spagoCore.util.JavaScript;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

public class LblTextTag extends AbstractLblTag {

    private static final long serialVersionUID = 1L;

    private String text;
    private String maxLength;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    protected void writeControl() throws JspException {
        String text = JavaScript.stringToHTMLString(getText());
        String controlWidth = getControlwidth();
        String maxLength = StringUtils.isNotBlank(getMaxLength()) ? " maxlength=\"" + getMaxLength() + "\" " : "";

        if (isEditable()) {
            writeln(" <input class=\"slText " + controlWidth + "\" type=\"text\" value=\"" + getText() + "\" "
                    + maxLength + " />");
        } else {
            writeln(" <div class=\"slText " + controlWidth + "\" >" + text + "</div>");
        }
    }
}
