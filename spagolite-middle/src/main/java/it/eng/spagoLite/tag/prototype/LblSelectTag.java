package it.eng.spagoLite.tag.prototype;

import it.eng.spagoLite.tag.form.formLayout.ContainerTag;

import javax.servlet.jsp.JspException;

/**
 * Stampa una Combo statica
 */
public class LblSelectTag extends AbstractLblSelectTag {

    private static final long serialVersionUID = 1L;

    protected void writeControl() throws JspException {
        String className = ContainerTag.LEFT.equalsIgnoreCase(getLabelPosition()) ? "slText " + getControlwidth()
                : "slTextRight " + getControlwidth();

        if (isEditable()) {
            writeln(" <select id=\"" + getId() + "\" name=\"" + getId() + "\" class=\"" + className + "\">"
                    + getOption() + "</select>");
        } else {
            writeln(" <div class=\"" + className + "\" ></div>");
        }
    }
}